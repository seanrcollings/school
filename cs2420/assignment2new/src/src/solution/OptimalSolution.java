package src.solution;

import src.EdgeInfo;
import src.Graph;

public class OptimalSolution extends SupplySolution {

    public OptimalSolution(Graph graph){super(graph);}

    public void solve() {
        solve(99);
    }

    public void solve(int maxSupplyNodes) {
        this.plan = getSupply(0, maxSupplyNodes, this.plan);
    }


    private DisasterPlan getSupply(int nodeId, int maxSupplyNodes, DisasterPlan partialPlan){
        if (nodeId >= partialPlan.vertexCt) return partialPlan;
        if (partialPlan.supplyCt >= maxSupplyNodes) return partialPlan;

        DisasterPlan useit = getSupply(nodeId + 1,
                maxSupplyNodes, addSupply(partialPlan, nodeId));

        DisasterPlan dont = getSupply(nodeId + 1, maxSupplyNodes, partialPlan);

        return betterSolution(useit, dont);
    }

    private DisasterPlan addSupply(DisasterPlan partialPlan, int nodeId) {
        DisasterPlan newPlan = new DisasterPlan(partialPlan);
        newPlan.supplies[nodeId] = true;
        newPlan.covered[nodeId] = true;
        newPlan.supplyCt++;

        for (EdgeInfo edge : graph.nodes[nodeId].succ) {
            newPlan.covered[edge.to] = true;
        }
        newPlan.updateCovered();

        return newPlan;
    }

    private DisasterPlan betterSolution(DisasterPlan solution1, DisasterPlan solution2) {
        int sum1 = solution1.supplyCt + solution1.needToCover;
        int sum2 = solution2.supplyCt + solution2.needToCover;
        if (sum1 <= sum2) return solution1;
        return solution2;
    }
}
