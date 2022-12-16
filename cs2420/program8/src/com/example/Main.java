package com.example;

import java.util.LinkedList;
import java.util.PriorityQueue;


public class Main {
    /** Implementation of the Bellman-Ford algorithm for finding shortest path
     * @param graph to find the shortest path on
     * @return whether or not it found a shortest path
     */
    public static boolean findShortestPath(FlowGraph graph) {
        PriorityQueue<GraphNode> q = new PriorityQueue<>();
        for (GraphNode n: graph.G) {
            n.distance = GraphNode.INF;
            n.prevNode = -1;
        }
        GraphNode source = graph.getSource();
        GraphNode sink = graph.getSink();

        source.distance = 0;
        q.add(source);

        while (!q.isEmpty()) {
            GraphNode node = q.remove();
            node.visited = true;

            for (EdgeInfo edge: node.succ) {
                GraphNode successor = graph.G[edge.to];
                if (edge.capacity > 0 && successor.distance > node.distance + edge.cost) {
                    successor.distance = node.distance + edge.cost;
                    successor.prevNode = node.nodeID;
                    q.add(successor);
                }
            }
        }

        return sink.prevNode >= 0;
    }

    public static void displayFlow(LinkedList<Integer> path, int maxFlow) {
        StringBuilder sb = new StringBuilder();
        sb.append("Found flow ").append(maxFlow).append(" : ");
        for (int nodeId : path) {
            sb.append(nodeId).append(" ");
        }
        System.out.println(sb);
    }


    public static LinkedList<Integer> getCurrentPath(FlowGraph graph) {
        LinkedList<Integer> path = new LinkedList<>();

        GraphNode node = graph.getSink();
        path.addFirst(node.nodeID);

        while (node.prevNode >= 0) {
            path.addFirst(node.prevNode);
            node = graph.G[node.prevNode];
        }

        return path;
    }

    /**
     * Finds the bottleneck of the currently found path
     * @param graph
     * @return the maximum flow that can go through that path
     */
    public static int findMinimumFlow(FlowGraph graph) {
        int min = 99999999;

        GraphNode curr = graph.getSink();
        int sourceId = graph.getSource().nodeID;
        while (curr.nodeID != sourceId) {
            GraphNode prev = curr;
            curr = graph.G[prev.prevNode];
            for (EdgeInfo edge : curr.succ) {
                if (edge.to == prev.nodeID) {
                    min = Math.min(edge.capacity, min);
                }
            }
        }

        return min;
    }

    /**
     * Applies the flow to each edge of the path set
     * @param graph graph to apply flow to
     * @param flow minimum possible flow over the current path
     * @return cost of adding that flow
     */
    public static int applyFlow(FlowGraph graph, int flow) {
        GraphNode curr = graph.getSink();
        int sourceId = graph.getSource().nodeID;
        int cost = 0;

        while (curr.nodeID != sourceId) {
            GraphNode prev = curr;
            curr = graph.G[prev.prevNode];

            // Find the forward edges and subtract capacity
            for (EdgeInfo edge : curr.succ) {
                if (edge.to == prev.nodeID) {
                    cost += edge.cost;
                    edge.capacity -= flow;
                }
            }

            // Find the back edges and add capacity to them
            for (EdgeInfo edge: prev.succ) {
                if (edge.to == curr.nodeID) {
                    edge.capacity += flow;
                }
            }
        }

        return cost;
    }

    public static void findFlows(FlowGraph graph) {
        int totalCost = 0;
        int assigned = 0;
        System.out.println(graph.graphName);
        while(findShortestPath(graph)){
            LinkedList<Integer> path = getCurrentPath(graph);
            int flow = findMinimumFlow(graph);
            assigned += flow;
            totalCost += applyFlow(graph, flow);
            displayFlow(path, flow);
        }

        System.out.println("Max Flow " + assigned + " / " + graph.maxFlowIntoSink + " assigned");
        System.out.println("Total Cost: " + totalCost);
        System.out.println("----------------------------------");
    }


    public static void main(String[] args) {
        String[] files = new String[] { "group0.txt", "group1.txt", "group4.txt",
                "group5.txt", "group6.txt", "group7.txt", "group8.txt",};

        for (String file : files) {
            FlowGraph graph1 = FlowGraph.makeGraph("data/" + file);
            findFlows(graph1);
        }
    }
}

