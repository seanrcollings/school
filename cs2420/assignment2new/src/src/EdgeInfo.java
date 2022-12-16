package src;

public class EdgeInfo  {
    public int from;        // source of edge
    public int to;          // destination of edge

    public EdgeInfo(int from, int to){
        this.from = from;
        this.to = to;

    }
    public String toString(){
        return " " + to ;
    }
  }
