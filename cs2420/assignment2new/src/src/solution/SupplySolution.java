package src.solution;

import src.EdgeInfo;
import src.Graph;
import src.GraphNode;

abstract public class SupplySolution {
    protected DisasterPlan plan;
    protected Graph graph;

    public SupplySolution() {}

    public SupplySolution(Graph graph) {
        this.graph = graph;
        this.plan = new DisasterPlan(graph.vertexCt);
    }

    public String toString(){
        return plan.toString();
    }

    public int getSupplyNodes(){
        return plan.supplyCt;
    }

    abstract public void solve();

    private int willCover(Graph graph, int newSupplyNode) {
        int cover = 0;
        GraphNode node = graph.nodes[newSupplyNode];
        for (EdgeInfo edgeInfo : node.succ) {
            if (!graph.nodes[edgeInfo.to].holdsSupply) cover++;
        }
        return cover;
    }


}

