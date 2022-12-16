package src;

import java.io.File;
import java.util.Scanner;

public class Graph {
    public int vertexCt;  // Number of vertices in the graph.
    public GraphNode[] nodes;  // Adjacency list for graph.
    public String graphName;  //The file from which the graph was created.

    public Graph() {
        this.vertexCt = 0;
        this.graphName = "";
      }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("The src.Graph ").append(graphName).append(" \n");

        for (int i = 0; i < vertexCt; i++) {
            sb.append( nodes[i].toString() );
        }
        return sb.toString();
    }

    public boolean makeGraph(String filename) {
        try {
            graphName = filename;
            Scanner reader = new Scanner( new File( filename ) );
            System.out.println( "\n" + filename );
            vertexCt = Integer.parseInt(reader.nextLine());
            nodes = new GraphNode[vertexCt];
            for (int i = 0; i < vertexCt; i++) {
                nodes[i] = new GraphNode( i );
            }
            for (int i = 0; i < vertexCt; i++) {
                String[] values = reader.nextLine().split(" ");
                int v1 = Integer.parseInt(values[0]);
                nodes[v1].nodeName = values[1];
                for (int v = 2; v < values.length; v++){
                    int v2=Integer.parseInt(values[v]);
                    nodes[v1].addEdge(v1,v2);
                }
             }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void sort() {
        for (int i = 0; i < vertexCt - 1; i++) {
            for (int j = 1; j < (vertexCt ); j++) {
                GraphNode node = nodes[j];
                GraphNode last = nodes[j - 1];
                if (node.succCt < last.succCt) {
                    nodes[j] = last;
                    nodes[j - 1] = node;
                }
            }
        }
    }
}