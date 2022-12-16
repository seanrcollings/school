package com.example;

import java.util.Scanner;


public class FlowGraph {
    int vertexCt;  // Number of vertices in the graph.
    GraphNode[] G;  // Adjacency list for graph.
    GraphNode source;
    GraphNode sink;
    String graphName;  //The file from which the graph was created.
    int maxFlowFromSource;
    int maxFlowIntoSink;


    public FlowGraph() {
        this.vertexCt = 0;
        this.graphName = "";
        this.maxFlowFromSource = 0;
        this.maxFlowIntoSink = 0;
    }

    /**
     * create a graph with vertexCt nodes
     * @param vertexCt
     */
    public FlowGraph(int vertexCt) {
        this.vertexCt = vertexCt;
        G = new GraphNode[vertexCt];
        for (int i = 0; i < vertexCt; i++) {
            G[i] = new GraphNode(i);
        }
        this.maxFlowFromSource = 0;
        this.maxFlowIntoSink = 0;
    }

    public int getVertexCt() {
        return vertexCt;
    }

    public int getMaxFlowFromSource() {
        return maxFlowFromSource;
    }

    public int getMaxFlowIntoSink() {
        return maxFlowIntoSink;
    }

    public int getResidual(EdgeInfo edge) {
        if (edge.to < 0) return -1;
        GraphNode node = this.G[edge.to];
        for (EdgeInfo edge2: node.succ) {
            if (edge2.to == edge.from) { // Found the corresponding back node
                return edge2.capacity;
            }
        }
        return -1; // didn't find anything
    }

    public GraphNode getSource() { return G[0]; }
    public GraphNode getSink() { return G[G.length - 1]; }

    /**
     * @param source
     * @param destination
     * @param cap         capacity of edge
     * @param cost        cost of edge
     * @return create an edge from source to destination with capacity
     */
    public boolean addEdge(int source, int destination, int cap, int cost) {
        System.out.println("addEdge " + source + "->" + destination + "(" + cap + ", " + cost + ")");
        if (source < 0 || source >= vertexCt) return false;
        if (destination < 0 || destination >= vertexCt) return false;
        //add edge
        G[source].addEdge(source, destination, cap, cost);
        return true;
    }

    /**
     * @param source
     * @param destination
     * @return return the capacity between source and destination
     */
    public int getCapacity(int source, int destination) {
        return G[source].getCapacity(destination);
    }

    /**
     * @param source
     * @param destination
     * @return the cost of the edge from source to destination
     */
    public int getCost(int source, int destination) {
        return G[source].getCost(destination);
    }

    /**
     * @return string representing the graph
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("The Graph ")
                .append(graphName)
                .append(" \n");

        sb.append("Total input ")
                .append(maxFlowIntoSink)
                .append(" :  Total output ")
                .append(maxFlowFromSource)
                .append("\n");

        for (int i = 0; i < vertexCt; i++) {
            sb.append(G[i].toString());
        }
        return sb.toString();
    }

    /**
     * Builds a graph from filename.  It automatically inserts backward edges needed for min-cost max flow.
     * @param filename
     */
    public static FlowGraph makeGraph(String filename) {
        Scanner reader = Util.getScanner(filename);
        int vertexCt = reader.nextInt();

        FlowGraph graph = new FlowGraph(vertexCt);
        graph.graphName = filename;

        while (reader.hasNextInt()) {
            int v1 = reader.nextInt();
            int v2 = reader.nextInt();
            int cap = reader.nextInt();
            int cost = reader.nextInt();
            graph.G[v1].addEdge(v1, v2, cap, cost);
            graph.G[v2].addEdge(v2, v1, 0, -cost);
            if (v1 == 0) graph.maxFlowFromSource += cap;
            if (v2 == vertexCt - 1) graph.maxFlowIntoSink += cap;
        }
        reader.close();
        return graph;
    }
}