package com.robotca.ControlApp.Core.Dijkstra;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class Graph<T extends GraphNode>{

    private final Set<T> nodes;
    private final Map<String, Set<String>> connections;

    public Graph(Set<T> nodes, Map<String, Set<String>> connections){
        this.nodes = nodes;
        this.connections = connections;
    }

    public T getNode(String id){
        for (T node: nodes){
            if (id.equals(node.getId())){
                return node;
            }
        }
        throw new IllegalArgumentException("no node found with ID");
    }

    public Set<T> getConnections(T node) {
        Set<String> connectedNodeIds = new HashSet<>();
        Set<T> connectedNodes = new HashSet<>();
        for(T n: nodes){
            if(node == n){
                connectedNodeIds = connections.get(node.getId());
            }
        }
        for(String id: connectedNodeIds){
            connectedNodes.add(getNode(id));
        }
        return connectedNodes;
    }
}
