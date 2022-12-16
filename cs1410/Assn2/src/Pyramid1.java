import java.util.Scanner;

public class Pyramid1 {

    private static String getFormattedWhiteSpace(int whitespace) {
        if (whitespace != 0) {
            return String.format("%" + whitespace + "s", " ");
        } else {
            return "";
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the number of rows: ");
        final int ROWS = input.nextInt();
        final int COLUMN_WIDTH = Integer.toString(ROWS).length() + 1;
        final int TOTAL_ROW_WIDTH = ((ROWS * 2) - 1) * COLUMN_WIDTH;
        String formattedString = "%" + (COLUMN_WIDTH) + "d";

        for (int i = 1; i <= ROWS; i ++){
            int currentRowWidth = ((i * 2) - 1) * COLUMN_WIDTH;
            int whitespace = (TOTAL_ROW_WIDTH - currentRowWidth ) / 2;
            StringBuilder line = new StringBuilder();

            line.append(getFormattedWhiteSpace(whitespace));

            for (int j = i; j >= 1; j-- ){
                line.append(String.format(formattedString, j));
            }
            for (int j = 2; j <= i; j ++) {
                line.append(String.format(formattedString, j));
            }

            line.append(getFormattedWhiteSpace(whitespace));
            System.out.println(line);
        }
    }
}
