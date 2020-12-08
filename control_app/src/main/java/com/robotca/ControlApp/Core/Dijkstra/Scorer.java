package com.robotca.ControlApp.Core.Dijkstra;

public interface Scorer<T extends GraphNode> {
    double computeCost(T from, T to);
}
