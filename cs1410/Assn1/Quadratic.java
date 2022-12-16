import java.util.Scanner;
import java.util.ArrayList;

/**
* @author Sean Collings
* */

public class Quadratic {
    /**
     * Calculates roots using the quadratic equation
     * @param a, b, c - 3 values needed to use the quadratic equation ax^2 + bx + c
     * @return ArrayList<Double> of how many roots it found, can be 2, 1 or 0
    * **/
    private static ArrayList<Double> CalculateRoots(double a, double b, double c) {
        // Calculates the roots
        double discriminate = Math.pow(b, 2) - (4 * a * c);
        ArrayList<Double> roots = new ArrayList<Double>();
        double root1 = (-b + Math.sqrt(discriminate)) / (2 * a);
        double root2 = (-b - Math.sqrt(discriminate)) / (2 * a);
        if (!Double.isNaN(root1)) {
            roots.add(root1);
        }
        if (!Double.isNaN(root2) && root2 != root1) {
            roots.add(root2);
        }
        return roots;
    }

    public static void main(String[] args) {
        System.out.print("Enter in a, b, and c: ");
        Scanner input = new Scanner(System.in);
        double a = input.nextDouble();
        double b = input.nextDouble();
        double c = input.nextDouble();
        input.close();
        ArrayList<Double> roots = CalculateRoots(a, b, c);
        // Checks how many roots were found
        switch(roots.size()) {
            case 0:
                System.out.println("There are no real roots");
                break;
            case 1:
                System.out.println("There is one real root");
                break;
            case 2:
                System.out.println("There are two real roots");
                break;
        }
        // Displays all roots found
        for (int i = 0; i < roots.size(); i++){
            System.out.println("r" + (i + 1) +": " + roots.get(i));
        }
    }
}