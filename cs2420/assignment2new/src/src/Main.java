package src;

import src.solution.GreedySolution;
import src.solution.OptimalSolution;

public class Main {

    public static void main(String[] args) {
        String[] files  = {"Tester.txt", "Tester2.txt", "WesternUS.txt", "BritishIsles.txt",
                "NortheastUS.txt", "CentralEurope.txt", "IberianPeninsula.txt", "SouthernNigeria.txt",
                "SouthernSouthKorea.txt", "NortheastUS2.txt", "SouthernUS.txt"};

        Graph graph1;
        for (String file:files) {
            graph1 = new Graph();
            graph1.makeGraph("data/" + file);
            graph1.sort();
            GreedySolution gsol = new GreedySolution(graph1);
            gsol.solve();

            graph1 = new Graph();
            graph1.makeGraph("data/" + file);
            OptimalSolution osol = new OptimalSolution(graph1);
            osol.solve(gsol.getSupplyNodes());

            System.out.println("GREEDY");
            System.out.println(gsol.toString());

            System.out.println("OPTIMAL");
            System.out.println(osol.toString());
        }
    }
}
