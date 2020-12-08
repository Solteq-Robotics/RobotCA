package com.robotca.ControlApp.Core;

import com.robotca.ControlApp.Fragments.MapFragment;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class Utils2 {

    public static boolean isPointContainedInObstacle(ArrayList<GeoPoint> obstacle , GeoPoint location) {
        int j, k;
        boolean result = false;
        for (j = 0, k = obstacle.size() - 1; j < obstacle.size(); k = j++) {
            if ((obstacle.get(j).getLongitude() > location.getLongitude()) != (obstacle.get(k).getLongitude() > location.getLongitude()) &&
                    (location.getLatitude() < (obstacle.get(k).getLatitude() - obstacle.get(j).getLatitude()) * (location.getLongitude() -
                            obstacle.get(j).getLongitude()) / (obstacle.get(k).getLongitude() - obstacle.get(j).getLongitude()) +
                            obstacle.get(j).getLatitude())) {
                result = !result;
            }
        }
        return result;
    }

    public static boolean doIntersect(GeoPoint p1, GeoPoint q1, GeoPoint p2, GeoPoint q2){
        // Find the four orientations needed for general and
        // special cases
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        // General case
        if (o1 != o2 && o3 != o4)
            return true;

        // Special Cases
        // p1, q1 and p2 are colinear and p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, p2, q1)) return true;

        // p1, q1 and q2 are colinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, q2, q1)) return true;

        // p2, q2 and p1 are colinear and p1 lies on segment p2q2
        if (o3 == 0 && onSegment(p2, p1, q2)) return true;

        // p2, q2 and q1 are colinear and q1 lies on segment p2q2
        if (o4 == 0 && onSegment(p2, q1, q2)) return true;

        return false; // Doesn't fall in any of the above cases
    }

    public static int orientation(GeoPoint p, GeoPoint q, GeoPoint r){
        double val = (q.getLongitude() - p.getLongitude()) * (r.getLatitude() - q.getLatitude()) -
                (q.getLatitude() - p.getLatitude()) * (r.getLongitude() - q.getLongitude());

        if (val == 0) return 0; // colinear

        return (val > 0)? 1: 2; // clock or counterclock wise
    }

    public static boolean onSegment(GeoPoint p, GeoPoint q, GeoPoint r){
        if (q.getLatitude() <= Math.max(p.getLatitude(), r.getLatitude()) && q.getLatitude() >= Math.min(p.getLatitude(), r.getLatitude()) &&
                q.getLongitude() <= Math.max(p.getLongitude(), r.getLongitude()) && q.getLongitude() >= Math.min(p.getLongitude(), r.getLongitude()))
            return true;

        return false;
    }

    //calculate positive bearing in radians
    public static double getPositiveBearing(double bearing){
        double b = bearing;
        if (bearing < 0){
            b = 2 * Math.PI + bearing;
        }
        return b;
    }

    public static GeoPoint calculatePointOutsideObstacle(GeoPoint point, ArrayList<GeoPoint> obstacle){
        GeoPoint[] nn = getNeighboursInObstacle(point, obstacle);
        GeoPoint n1 = nn[0];
        GeoPoint n2 = nn[1];

        double b1 = getPositiveBearing(Math.toRadians(MapFragment.computeBearingBetweenTwoPoints(point, n1)));
        double b2 = getPositiveBearing(Math.toRadians(MapFragment.computeBearingBetweenTwoPoints(point, n2)));
        double b3 = ((b1 + b2)/2 + Math.PI)%(2*Math.PI);

        GeoPoint p = MapFragment.inverseHaversine(point, b3, 0.5);
        if (isPointContainedInObstacle(obstacle, p)){
            return MapFragment.inverseHaversine(point, (b3 + Math.PI)%(2*Math.PI), 0.5);
        } else{
            return p;
        }
    }

    public static GeoPoint[] getNeighboursInObstacle(GeoPoint point, ArrayList<GeoPoint> obstacle){
        GeoPoint[] neighbours = new GeoPoint[2];
        int indexOfPoint = obstacle.indexOf(point);
        if (indexOfPoint == 0){
            neighbours[0] = obstacle.get(obstacle.size()-2);
        } else {
            neighbours[0] = obstacle.get(indexOfPoint-1);
        }
        if (indexOfPoint == obstacle.size()-2){
            neighbours[1] = obstacle.get(0);
        } else{
            neighbours[1] = obstacle.get(indexOfPoint + 1);
        }
        return neighbours;
    }

    public static boolean doesLineSegmentIntersectWithObstacle(GeoPoint start, GeoPoint goal, ArrayList<GeoPoint> obstacle) {
        for (int k = 0; k < obstacle.size()-1; k++){
            GeoPoint p2 = obstacle.get(k);
            GeoPoint q2;
            if (k == obstacle.size()-2){
                q2 = obstacle.get(0);
            } else{
                q2 = obstacle.get(k+1);
            }
            if (doIntersect(start, goal, p2, q2)){
                return true;
            }
        }
        return false;
    }
}
