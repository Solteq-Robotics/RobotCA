package com.robotca.ControlApp.Core.Dijkstra;

import org.osmdroid.util.GeoPoint;

public class GeoPointNode implements GraphNode {
    private final String id;
    private final GeoPoint geoPoint;

    public GeoPointNode(String id, GeoPoint geoPoint){
        this.id = id;
        this.geoPoint = geoPoint;
    }

    @Override
    public String getId() {
        return id;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }
}
