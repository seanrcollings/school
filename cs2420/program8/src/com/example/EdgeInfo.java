package com.example;

public class EdgeInfo {
    int from;        // source of edge
    int to;          // destination of edge
    int currentFill;
    int capacity;
    int cost;
    boolean backEdge;

    public EdgeInfo(int from, int to, int capacity, int cost) {
        this.from = from;
        this.to = to;
        this.currentFill = 0;
        this.capacity = capacity;
        this.cost = cost;
    }

    public String toString(){
        return "Edge " + from + "->" + to + " ("+ capacity + ", " + cost + ") " ;
    }
}
