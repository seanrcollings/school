package src;

import src.EdgeInfo;
import src.LinkedList;

import java.util.*;

public class GraphNode {
    public int nodeID;
    public int succCt;
    public String nodeName;
    public src.LinkedList<EdgeInfo> succ;
    public boolean holdsSupply;


    public GraphNode( ){
        this.nodeID = 0;
        this.succ = new src.LinkedList<EdgeInfo>();
        this.nodeName="";
        this.succCt=0;
        this.holdsSupply = false;
    }

    public GraphNode( int nodeID){
        this.nodeID = nodeID;
        this.succ = new LinkedList<EdgeInfo>();
        this.nodeName="noName";
        this.succCt=0;
        this.holdsSupply = false;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(nodeID).append(": ").append(nodeName);
        for (EdgeInfo edgeInfo : succ) {
            sb.append(edgeInfo.toString());
        }
        sb.append("\n");
        return sb.toString();
    }

    public void addEdge(int v1, int v2){
        succ.addFirst( new EdgeInfo(v1,v2) );
        succCt++;
    }
}
