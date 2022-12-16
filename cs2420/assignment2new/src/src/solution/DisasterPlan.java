package src.solution;

public class DisasterPlan {
    int supplyCt;  // Number of nodes which are supply nodes
    int vertexCt;  // Total number of nodes in graph
    int needToCover;  // Nodes which are not covered.
    boolean[] supplies;  // supplies[i] is true if node i holds supplies
    boolean[] covered;   // covered[i] is true if node i is covered

    public DisasterPlan(int vertexCt){
        this.vertexCt = vertexCt;
        supplies = new boolean[vertexCt];
        covered = new boolean[vertexCt];
        supplyCt = 0;
        needToCover = vertexCt;
    }

    public DisasterPlan(DisasterPlan plan){
        this.supplies = plan.supplies.clone();
        this.covered = plan.covered.clone();
        this.vertexCt = plan.vertexCt;
        supplyCt = plan.supplyCt;
        needToCover = plan.needToCover;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("SupplyCt ").append(this.supplyCt).append(" Need to cover ").append(this.needToCover).append("\n SUPPLIES:");
        for (int i = 0; i < this.vertexCt; i++)
            sb.append(this.supplies[i] ? "1" : "0");
        sb.append("\n COVERED :");
        for (int i = 0; i < this.vertexCt; i++)
            sb.append(this.covered[i] ? "1" : "0");
        sb.append("\n ");
        return sb.toString();
    }


    public void updateCovered() {
        int newCovered = 0;
        for(boolean bool : covered) {
            if (bool) newCovered++;
        }
        this.needToCover = vertexCt - newCovered;
    }
}
