package src.solution;

import src.EdgeInfo;
import src.Graph;

public class GreedySolution extends SupplySolution{

    public GreedySolution(Graph graph){super(graph);}

    public void solve() {
        for (int i = graph.vertexCt - 1; i >= 0; i--) {
            if (!plan.supplies[i] && !plan.covered[i]) {
                this.addSupplies(graph, i);
            }
            if (allTrue(plan.covered)) break;
        }
    }

    private void addSupplies(Graph graph, int newSupplyNode) {
        if (graph.vertexCt - 1 >= newSupplyNode) {
            plan.supplies[newSupplyNode] = true;
            plan.covered[newSupplyNode] = true;
            plan.supplyCt++;

            for (EdgeInfo edge : graph.nodes[newSupplyNode].succ) {
                plan.covered[edge.to] = true;
            }
            plan.updateCovered();
        }
    }

    private static boolean allTrue(boolean[] array){
        for(boolean b : array) if(!b) return false;
        return true;
    }
}
