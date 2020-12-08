package com.robotca.ControlApp.Core.Plans;

import android.util.Log;

import com.robotca.ControlApp.ControlApp;
import com.robotca.ControlApp.Core.ControlMode;
import com.robotca.ControlApp.Core.RobotController;
import com.robotca.ControlApp.Core.Utils;
import com.robotca.ControlApp.Fragments.MapFragment;

import org.osmdroid.util.GeoPoint;
import org.ros.rosjava_geometry.Vector3;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Vector;

import sensor_msgs.LaserScan;

/**
 * Rudimentary waypoint plan for testing. No collision detection, just moves towards the next waypoint.
 *
 * Created by Nathaniel Stone on 3/4/16.
 *
 */
public class RoutePlan extends RobotPlan {

    private static final double MINIMUM_DISTANCE = 0.5;
    private final ControlApp controlApp;

    private static final String TAG = "RoutePlan";

    private static final double MAX_SPEED = 1.00;


    private final static double GAMMA = 2;
    private final static double KAPPA = 0.4;


    private Vector3 currentPosition;
    private double currentHeading;
    private Vector3 goalPosition;
    private Vector3 lastPosition;
    private double angularVelocity;
    private double linearVelocity;

    /**
     * Creates a RoutePlan for the specified ControlApp.
     * @param controlApp The ControlApp
     */
    public RoutePlan(ControlApp controlApp) {
        this.controlApp = controlApp;
        stop();
    }

    /**
     * @return The ControlMode for this RobotPlan
     */
    @Override
    public ControlMode getControlMode() {
        return ControlMode.Routing;
    }

    @Override
    protected void start(RobotController controller) throws Exception {

        Log.d(TAG, "Started");

        GeoPoint next;
        double dir, dist, spd;

        while (!isInterrupted()) {

            Log.d(TAG, "begin loop");

            // Wait for the next point to become available
            while (controlApp.getNextPointInRoute() == null)
                waitFor(1000L);

            next = controlApp.getNextPointInRoute();
            Log.d(TAG, "Found next point: (" + next.getLatitude() + ", " + next.getLongitude() + ")");

            spd = 0.0;
            int counter = 0;
            do {
                // Increment speed
                spd += MAX_SPEED / 15.0;
                if (spd > MAX_SPEED)
                    spd = MAX_SPEED;

                GeoPoint point = RobotController.getCurrentGPSLocation();

                float[] res = new float[3];
                MapFragment.computeDistanceAndBearing(point.getLatitude(), point.getLongitude(), next.getLatitude(), next.getLongitude(), res);

                double bearing = Math.toRadians(res[2]);
                double heading = RobotController.getHeading();
                // Check angle to target - bearing is negative to accomodate the standard of angles in ros
                dir = Utils.angleDifference(heading, -bearing);
                dist = res[0];
                /*
                //initialize route with correct angle
                if (counter == 0){
                    while(!(dir < 0.2 && dir > -0.2)) {
                        controller.publishVelocity(0.0, 0.0, dir);
                        double tempHeading = RobotController.getHeading();
                        dir = Utils.angleDifference(tempHeading, -bearing);
                    }
                }
                */

                controller.publishVelocity(spd * Math.cos(dir), 0.0, spd* Math.sin(dir));

                counter = counter + 1;
                // Check distance to target
            } while (!isInterrupted() && dist > MINIMUM_DISTANCE && next.equals(controlApp.getNextPointInRoute()));
            // Stop
            final int N = 15;
            for (int i = N - 1; i >= 0 && !isInterrupted(); --i) {

                GeoPoint point = RobotController.getCurrentGPSLocation();

                float[] res = new float[3];
                MapFragment.computeDistanceAndBearing(point.getLatitude(), point.getLongitude(), next.getLatitude(), next.getLongitude(), res);

                // Check angle to target
                dir = Utils.angleDifference(RobotController.getHeading(), -Math.toRadians(res[2])) / 2.0;

                // Slow down
                controller.publishVelocity(spd * ((double)i / N) * Math.cos(dir), 0.0, spd * ((double)i / N) * Math.sin(dir));
                waitFor(N);
            }

            // Remove the way point
            if (!isInterrupted() && next.equals(controlApp.getNextPointInRoute()))
                controlApp.pollNextPointInRoute();
        }
    }

    //another shot at making the routing precise - possibly extendable for obstacle avoidance
    protected void start2(RobotController controller) throws Exception {
        GeoPoint initialPoint = RobotController.getCurrentGPSLocation();

        while (!isInterrupted()) {
            LaserScan scan = controller.getLaserScan();

            while (controlApp.getNextPointInRoute() == null) {
                controller.publishVelocity(0, 0, 0);
                waitFor(1000L);
            }


            currentPosition = MapFragment.createVectorFromGeoPoint(RobotController.getCurrentGPSLocation(), initialPoint);
            currentHeading = RobotController.getHeading();
            goalPosition = MapFragment.createVectorFromGeoPoint(controlApp.getNextPointInRoute(), initialPoint);

            if (goalPosition != null) {
                Vector3 netforce = calculateForces();
                applyForce(controller, netforce);

                double dist = Utils.distance(RobotController.getX(), RobotController.getY(),
                        goalPosition.getX(), goalPosition.getY());

                if (dist < MINIMUM_DISTANCE)
                    controlApp.pollNextPointInRoute();
            } else {
                controller.publishVelocity(0, 0, 0);
            }

            lastPosition = currentPosition;
            waitFor(100L);
        }
    }

    private Vector3 calculateForces() {
        Vector3 netForce = new Vector3(0, 0, 0);
        Vector3 attractiveForce = goalPosition.subtract(currentPosition);
        attractiveForce = attractiveForce.scale(GAMMA * attractiveForce.getMagnitude()); // f_a = gamma * ||x_g - x_r||^2 = |x_g - x_r|| * gamma * |||x_g - x_r||
        netForce = netForce.add(attractiveForce);
        return netForce;
    }

    private void applyForce(RobotController controller, Vector3 netForce) throws InterruptedException {
        double forceAngle = 0;
        if (netForce.getY() != 0 || netForce.getX() != 0)
            forceAngle = Math.atan2(netForce.getY(), netForce.getX());

        if (forceAngle < 0) {
            forceAngle += 2 * Math.PI;
        }

        if (currentHeading < 0) {
            currentHeading += 2 * Math.PI;
        }


        Log.d("ControlApp", String.format("Force Angle: %f", Math.toDegrees(forceAngle)));
        Log.d("ControlApp", String.format("Heading:     %f", Math.toDegrees(currentHeading)));

        //compute angular vel
        double angle1 = forceAngle - currentHeading;
        double angle2 = 2 * Math.PI - Math.abs(angle1);
        double angle;

        if (Math.abs(angle1) > Math.abs(angle2)) {
            angle = -angle2;
        } else {
            angle = angle1;
        }

        Log.d("ControlApp", String.format("Turn Angle:  %f", angle));

        angularVelocity = -KAPPA * angle;

        //compute linear vel
        double linVel = (netForce.getMagnitude() * Math.cos(angle1));
        if (linVel < 0) {
            linearVelocity = 0;
        } else
            linearVelocity = linVel;

        if (linearVelocity > MAX_SPEED)
            linearVelocity = MAX_SPEED;

        controller.publishVelocity(this.linearVelocity, 0, angularVelocity);
    }

}
