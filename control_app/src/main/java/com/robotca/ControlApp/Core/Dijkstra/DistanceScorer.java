package com.robotca.ControlApp.Core.Dijkstra;

import com.robotca.ControlApp.Fragments.MapFragment;

public class DistanceScorer implements Scorer<GeoPointNode> {

    public DistanceScorer(){}

    @Override
    public double computeCost(GeoPointNode from, GeoPointNode to) {
        return MapFragment.computeDistanceBetweenTwoPoints(from.getGeoPoint(), to.getGeoPoint());
    }
}
