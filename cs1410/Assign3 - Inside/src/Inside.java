/**
 * Assignment 3 for CS 1410
 * This program determines if points are contained within circles or rectangles.
 */
public class Inside {
    /**
     * This is the primary driver code to test the "inside" capabilities of the
     * various functions.
     */
    public static void main(String[] args) {
        double[] ptX = { 1, 2, 3, 4 };
        double[] ptY = { 1, 2, 3, 4 };
        double[] circleX = { 0, 5 };
        double[] circleY = { 0, 5 };
        double[] circleRadius = { 3, 3 };
        double[] rectLeft = { -2.5, -2.5 };
        double[] rectTop = { 2.5, 5.0 };
        double[] rectWidth = { 6.0, 5.0 };
        double[] rectHeight = { 5.0, 2.5 };

        System.out.println("----- Report of Points and Circles -----");
        for (int j = 0; j < circleX.length; j++) {
            for (int i = 0; i < ptX.length; i++) {
                reportPoint(ptX[i], ptY[i]);
                System.out.printf("is %s", isPointInsideCircle(ptX[i], ptY[i], circleX[j], circleY[j], circleRadius[j]) ? "inside " : "outside ");
                reportCircle(circleX[j], circleY[j], circleRadius[j]);
                System.out.println("");
            }
        }

        System.out.println("\n\n\n----- Report of Points and Rectangles -----");
        for (int j = 0; j < rectLeft.length; j++) {
            for (int i = 0; i < ptX.length; i++) {
                reportPoint(ptX[i], ptY[i]);
                System.out.printf("is %s", isPointInsideRectangle(ptX[i], ptY[i], rectLeft[j], rectTop[j], rectWidth[j], rectHeight[j]) ? "inside " : "outside ");
                reportRectangle(rectLeft[j], rectTop[j], rectWidth[j], rectHeight[j]);
                System.out.println("");
            }
        }


    }

    static void reportPoint(double x, double y) {
        System.out.printf("Point(%f, %f) ", x, y);
    }

    static void reportCircle(double x, double y, double r) {
        System.out.printf("Circle(%f, %f) Radius: %f ", x, y, r);
    }

    static void reportRectangle(double left, double top, double width, double height) {
        double bottom = top - height;
        double right = left + width;
        System.out.printf("Rectangle(%f, %f, %f, %f) ", left, top, right, bottom);
    }

    static boolean isPointInsideCircle(double ptX, double ptY, double circleX, double circleY, double circleRadius) {
        double distance = Math.sqrt(Math.pow(ptX - circleX, 2) + Math.pow(ptY - circleY, 2));
        return distance <= circleRadius;
    }

    static boolean isPointInsideRectangle(double ptX, double ptY, double rLeft, double rTop, double rWidth, double rHeight) {
        boolean betweenX = ptX >= rLeft && ptX <= rLeft + rWidth;
        boolean betweenY = ptY <= rTop && ptX >= rTop - rHeight;
        return betweenX && betweenY;
    }

}
