package com.robotca.ControlApp.Core.Plans;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.robotca.ControlApp.ControlApp;
import com.robotca.ControlApp.Core.ControlMode;
import com.robotca.ControlApp.Core.RobotController;
import com.robotca.ControlApp.Fragments.MapFragment;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Random;

public class AreaPlan extends RobotPlan {

    private static final String TAG = "AreaPlan";

    private final Random random;
    private ControlApp controlApp;
    private ArrayList<Double> areaDistances;
    private ArrayList<Double> obstacleDistances;
    private ArrayList<Double> areaAngles;
    private ArrayList<Double> obstacleAngles;
    private String distanceStrategy;

    /**
     * Creates an AreaPlan with the specified area.
     */
    public AreaPlan(ControlApp controlApp) {
        this.controlApp = controlApp;
        random = new Random();
        areaDistances = new ArrayList<>();
        obstacleDistances = new ArrayList<>();
        areaAngles = new ArrayList<>();
        obstacleAngles = new ArrayList<>();
    }

    /**
     * @return The ControlMode for this RobotPlan
     */
    @Override
    public ControlMode getControlMode() {
        return ControlMode.Area;
    }

    @Override
    public void start(final RobotController controller) throws Exception {

        Log.d(TAG, "Started");

        double areaResult, obstacleResult = 300;

        while (!isInterrupted()) {

            Log.d(TAG, "Begin loop");

            // Wait for enough area points
            while (controlApp.getAreaPoints() == null || controlApp.getAreaPoints().size() < 5) {
                waitFor(1000L);
            }

            // Get area and obstacle points, and location of robot
            ArrayList<GeoPoint> areaPoints = controlApp.getAreaPoints();
            ArrayList<ArrayList<GeoPoint>> allObstaclePoints = controlApp.getObstaclePoints();
            Location location = controlApp.getRobotController().LOCATION_PROVIDER.getLastKnownLocation();

            // If robot is outside area or inside obstacle, stop robot.
            while (!robotInArea(areaPoints, location) || robotInObstacle(allObstaclePoints, location)) {
                waitFor(1000L);
            }

            //calculateDistFromRobotToAreaLine(areaPoints, location);
            distanceStrategy = "area";
            calculateDistFromRobotToLine(areaPoints, location);
            angleOf(areaPoints);

            if (controlApp.getObstaclePoints().get(0).size() != 0) {
                distanceStrategy = "obstacle";
                for (ArrayList<GeoPoint> obstaclePoints: allObstaclePoints) {
                    calculateDistFromRobotToLine(obstaclePoints, location);
                    angleOf(obstaclePoints);
                }
            }

            areaResult = areaDistances.get(0);

            for (int i = 0; i < areaDistances.size(); i++) {
                if (areaResult > areaDistances.get(i)) {
                    areaResult = areaDistances.get(i);
                }
            }

            if (controlApp.getObstaclePoints().get(0).size() != 0) {
                obstacleResult = obstacleDistances.get(0);

                for (int i = 0; i < obstacleDistances.size(); i++) {
                    if (obstacleResult > obstacleDistances.get(i)) {
                        obstacleResult = obstacleDistances.get(i);
                    }
                }
            }

            double heading = headingToNavigateFrom();

            // If robot gets close to a line, rotate. Otherwise continue same direction
            if (areaResult < 150) {
                int indexOf = areaDistances.indexOf(areaResult);

                double maxAngle, minAngle;

                // Get angles at which robot should be between, above or under.
                maxAngle = areaAngles.get(indexOf);

                if (maxAngle < 180)
                    minAngle = maxAngle + 180d;
                else
                    minAngle = maxAngle - 180d;

                if (maxAngle < minAngle) {
                    double tempAngle = maxAngle;
                    maxAngle = minAngle;
                    minAngle = tempAngle;
                }

                // Check heading of robot, and rotate to drive opposite
                if (heading <= 90) {
                    while (!(heading > minAngle && heading < maxAngle)) {
                        rotateRobot(controller);
                        heading = headingToNavigateFrom();
                    }
                } else if (heading > 90 && heading <= 180d) {
                    while (!(heading < minAngle || heading > maxAngle)) {
                        rotateRobot(controller);
                        heading = headingToNavigateFrom();
                    }
                } else if (heading > 180 && heading < 270d) {
                    while (!(heading < minAngle || heading > maxAngle)) {
                        rotateRobot(controller);
                        heading = headingToNavigateFrom();
                    }
                } else if (heading > 270d) {
                    while (!(heading > minAngle && heading < maxAngle)) {
                        rotateRobot(controller);
                        heading = headingToNavigateFrom();
                    }
                }
                controller.publishVelocity(0.5, 0, 0);
                // For debugging
                waitFor(3000);
                controller.publishVelocity(0, 0, 0);
            } else if (obstacleResult < 150) {
                int number = 0;
                int indexOf = obstacleDistances.indexOf(obstacleResult);
                double maxAngle, minAngle;
                if (obstacleAngles.get(indexOf) > 355d) {
                    number = 1;
                    maxAngle = 5 + obstacleAngles.get(indexOf) - 360;
                    minAngle = obstacleAngles.get(indexOf) - 5;
                } else if (obstacleAngles.get(indexOf) < 5d) {
                    number = 2;
                    maxAngle = 5 + obstacleAngles.get(indexOf);
                    minAngle = 360 + obstacleAngles.get(indexOf) - 5;
                } else {
                    maxAngle = obstacleAngles.get(indexOf) + 5;
                    minAngle = obstacleAngles.get(indexOf) - 5;
                }

                if (number == 1) {
                    while (!(heading < maxAngle || heading > minAngle)) {
                        controller.publishVelocity(0, 0, 0.5);
                        heading = headingToNavigateFrom();
                    }
                } else if (number == 2) {
                    while (!(heading > maxAngle || heading < minAngle)) {
                        controller.publishVelocity(0, 0, 0.5);
                        heading = headingToNavigateFrom();
                    }
                } else {
                    while (!(heading < maxAngle && heading > minAngle)) {
                        controller.publishVelocity(0, 0, 0.5);
                        heading = headingToNavigateFrom();
                    }
                }
                controller.publishVelocity(0, 0, 0);
                waitFor(1000);
                controller.publishVelocity(0.5, 0, 0);
                // For debugging
                waitFor(3000);
                controller.publishVelocity(0, 0, 0);
            } else {
                controller.publishVelocity(0.5, 0, 0);
                // For debugging
                waitFor(1000);
                controller.publishVelocity(0, 0, 0);
            }
            areaDistances.clear();
            obstacleDistances.clear();
            areaAngles.clear();
            obstacleAngles.clear();
        }
    }

    /**
     * Calculate distance from robot to area lines.
     * @param areaPoints used to make area
     * @param location of the robot
     */
    private void calculateDistFromRobotToAreaLine(@NonNull ArrayList<GeoPoint> areaPoints, Location location) {
        ArrayList<Double> distBetweenAreaPoints = new ArrayList<>();
        ArrayList<Double> distBetweenAreaPointAndRobot = new ArrayList<>();

        for (int i = 0; i < areaPoints.size() - 1; i++) {
            double startLon = areaPoints.get(i).getLongitude();
            double startLat = areaPoints.get(i).getLatitude();
            double endLon = areaPoints.get(i+1).getLongitude();
            double endLat = areaPoints.get(i+1).getLatitude();

            double dPoints = MapFragment.computeDistanceToKilometers(startLat, startLon, endLat, endLon);
            double dRobot = MapFragment.computeDistanceToKilometers(startLat, startLon, location.getLatitude(), location.getLongitude());

            distBetweenAreaPoints.add(dPoints);
            distBetweenAreaPointAndRobot.add(dRobot);
            if (i == areaPoints.size() - 2) {
                distBetweenAreaPointAndRobot.add(distBetweenAreaPointAndRobot.get(0));
            }
        }

        for (int i = 0; i < areaPoints.size() - 1; i++) {
            double perimeter = (distBetweenAreaPoints.get(i) + distBetweenAreaPointAndRobot.get(i) + distBetweenAreaPointAndRobot.get(i+1)) / 2;
            double area = Math.sqrt(perimeter * (perimeter - distBetweenAreaPoints.get(i)) * (perimeter - distBetweenAreaPointAndRobot.get(i)) * (perimeter - distBetweenAreaPointAndRobot.get(i+1)));
            double d = ((2 * area) / distBetweenAreaPoints.get(i)) * 100000;

            areaDistances.add(d);
        }
    }

    /**
     * Calculate distance from robot to all lines, inspiration from https://stackoverflow.com/questions/849211/shortest-distance-between-a-point-and-a-line-segment
     * @param points used to make a line
     * @param location of the robot
     */
    private void calculateDistFromRobotToLine(@NonNull ArrayList<GeoPoint> points, Location location) {
        for (int i = 0; i < points.size() - 1; i++) {
            double x = location.getLatitude();
            double x1 = points.get(i).getLatitude();
            double x2 = points.get(i+1).getLatitude();
            double y = location.getLongitude();
            double y1 = points.get(i).getLongitude();
            double y2 = points.get(i+1).getLongitude();

            // Vectors
            double A = x - x1;
            double B = y - y1;
            double C = x2 - x1;
            double D = y2 - y1;

            double dot = A * C + B * D;
            double len_sq = C * C + D * D;
            double param = -1;

            if (len_sq != 0)
                param = dot / len_sq;

            double xx, yy;

            // If lower or equal 0, closest to first point.
            // If above 0 and under 1, closest to line segment.
            // If higher or equal 1, closest to last point.
            if (param <= 0) {
                xx = x1;
                yy = y1;
            } else if (param >= 1) {
                xx = x2;
                yy = y2;
            } else {
                xx = x1 + param * C;
                yy = y1 + param * D;
            }

            // Calculate distance from robot to xx and yy. Distance is in cm.
            double dx = MapFragment.computeDistanceToKilometers(x, 0, xx, 0) * 100000;
            double dy = MapFragment.computeDistanceToKilometers(y, 0, yy, 0) * 100000;

            // Calculate length of vector and add to ArrayList
            if (distanceStrategy.equals("area")) {
                areaDistances.add((Math.sqrt(dx * dx + dy * dy)));
            } else if (distanceStrategy.equals("obstacle")) {
                obstacleDistances.add(Math.sqrt(dx * dx + dy * dy));
            }
        }
    }

    /**
     * Get heading of robot and turn it to degrees
     * @return heading in degrees
     */
    private double headingToNavigateFrom() {
        double heading = RobotController.getHeading();

        if (heading < 0) {
            double h = Math.PI + heading;
            heading = Math.PI + h;
        }

        heading = Math.toDegrees(heading);

        return heading;
    }

    /**
     * Get angle of line two points are forming
     * @param geoPoints to get angle from
     */
    private void angleOf(@NonNull ArrayList<GeoPoint> geoPoints) {
        for (int i = 0; i < geoPoints.size() - 1; i++) {
            final double deltaY = (geoPoints.get(i).getLongitude() - geoPoints.get(i+1).getLongitude());
            final double deltaX = (geoPoints.get(i).getLatitude() - geoPoints.get(i+1).getLatitude());
            final double result = (int) Math.toDegrees(Math.atan2(deltaY, deltaX));

            if (distanceStrategy.equals("area")) {
                areaAngles.add((result < 0) ? (360 + result) : result);
            } else if (distanceStrategy.equals("obstacle")) {
                obstacleAngles.add((result < 0) ? (360 + result) : result);
            }
        }
    }

    private void rotateRobot(@NonNull RobotController controller) throws Exception {
        controller.publishVelocity(0, 0, 0);
        waitFor(1000);
        long delay = (long) (2000 * (1 + random.nextFloat()));
        controller.publishVelocity(0, 0, 0.5);
        waitFor(delay);
        controller.publishVelocity(0, 0, 0);
    }

    /**
     * Check if robot is inside the marked area
     * @param areaPoints used to mark area
     * @param location of robot
     * @return true or false depending on if robot is inside or outside
     */
    private boolean robotInArea(ArrayList<GeoPoint> areaPoints, Location location) {
        int i, j;
        boolean result = false;

        for (i = 0, j = areaPoints.size() - 1; i < areaPoints.size(); j = i++) {
            if ((areaPoints.get(i).getLongitude() > location.getLongitude()) != (areaPoints.get(j).getLongitude() > location.getLongitude()) &&
                    (location.getLatitude() < (areaPoints.get(j).getLatitude() - areaPoints.get(i).getLatitude()) * (location.getLongitude() - areaPoints.get(i).getLongitude())
                    / (areaPoints.get(j).getLongitude() - areaPoints.get(i).getLongitude()) + areaPoints.get(i).getLatitude())) {
                result = !result;
            }
        }
        return result;
    }

    /**
     * Check if robot is inside the marked obstacle
     * @param obstaclePoints used to mark obstacles
     * @param location of robot
     * @return true or false depending on if robot is inside or outside
     */
    private boolean robotInObstacle(@NonNull ArrayList<ArrayList<GeoPoint>> obstaclePoints, Location location) {
        int i, j, k;
        boolean result = false;

        for (i = 0; i < obstaclePoints.size(); i++) {
            for (j = 0, k = obstaclePoints.get(i).size() - 1; j < obstaclePoints.get(i).size(); k = j++) {
                if ((obstaclePoints.get(i).get(j).getLongitude() > location.getLongitude()) != (obstaclePoints.get(i).get(k).getLongitude() > location.getLongitude()) &&
                        (location.getLatitude() < (obstaclePoints.get(i).get(k).getLatitude() - obstaclePoints.get(i).get(j).getLatitude()) * (location.getLongitude() -
                                obstaclePoints.get(i).get(j).getLongitude()) / (obstaclePoints.get(i).get(k).getLongitude() - obstaclePoints.get(i).get(j).getLongitude()) +
                                obstaclePoints.get(i).get(j).getLatitude())) {
                    result = !result;
                }
            }
        }
        return result;
    }
}
