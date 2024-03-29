package com.example;

import java.util.*;

public class GraphNode implements Comparable {
    static int INF = 9999;
    public int nodeID;
    public LinkedList<EdgeInfo> succ;
    public int prevNode;
    public boolean visited;
    public int distance;

    public GraphNode() {
        this.nodeID = 0;
        this.succ = new LinkedList<EdgeInfo>();
        this.prevNode = 0;
        this.visited = false;
        this.distance = INF;
     }

    public GraphNode(int nodeID) {
        this.nodeID = nodeID;
        this.succ = new LinkedList<EdgeInfo>();
        this.prevNode = 0;
        this.visited = false;
        this.distance = INF;
      }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(nodeID).append(": ");
        for (EdgeInfo edgeInfo : succ) {
            sb.append(edgeInfo.toString());
        }
        sb.append("\n");
        return sb.toString();
    }

    public void addEdge(int v1, int v2, int capacity,int cost) {
        //System.out.println("GraphNode.addEdge " + v1 + "->" + v2 + "(" + capacity + ")");
        succ.addFirst( new EdgeInfo(v1,v2,capacity,cost) );
    }

    public int getCapacity(int destination) {
        for (EdgeInfo e : succ) {
            if (e.to == destination)
                return e.capacity;
        }
       return 0;
    }

    public int getCost(int destination) {
        for (EdgeInfo e : succ) {
            if (e.to == destination)
                return e.cost;
        }
        return 0;
    }

    @Override
    public int compareTo(Object o) {
        GraphNode n = (GraphNode)o;
        return Integer.compare(this.nodeID, n.nodeID);
    }
}
