package com.robotca.ControlApp.Core.Dijkstra;

public class RouteNode<T extends GraphNode> implements Comparable<RouteNode> {

    private final T current;
    private T previous;
    private double routeScore;
    private double estimatedScore;

    public RouteNode(T current){
        this.current = current;
        previous = null;
        routeScore = Double.POSITIVE_INFINITY;
        estimatedScore = Double.POSITIVE_INFINITY;
    }

    public RouteNode(T current, T previous, double routeScore, double estimatedScore){
        this.current = current;
        this.previous = previous;
        this.routeScore = routeScore;
        this.estimatedScore = estimatedScore;
    }

    @Override
    public int compareTo(RouteNode o) {
        if(this.estimatedScore > o.estimatedScore){
            return 1;
        } else if(this.estimatedScore < o.estimatedScore){
            return -1;
        } else {
            return 0;
        }
    }


    public T getCurrent() {
        return current;
    }

    public T getPrevious() {
        return previous;
    }

    public void setPrevious(T previous) {
        this.previous = previous;
    }

    public double getRouteScore() {
        return routeScore;
    }

    public void setRouteScore(double routeScore) {
        this.routeScore = routeScore;
    }

    public double getEstimatedScore() {
        return estimatedScore;
    }

    public void setEstimatedScore(double estimatedScore){
        this.estimatedScore = estimatedScore;
    }
}
