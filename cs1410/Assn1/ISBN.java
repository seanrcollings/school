import java.util.Scanner;

/**
 * @author Sean Collings
 * */
public class ISBN {
    /**
     * Get all the digits of an ISBN seperatley and return an array of them
     * @param isbn - The user's inputted isbn number (9 digits long)
     * @return digits - Array of all the digits from the isbn**/
    public static int[] splitISBN(int isbn) {
        int isbnCopy = isbn;
        int digits[] = new int[9];
        for (int i=0 ; i < 9 ; i++) {
            int value = isbnCopy % 10;
            digits[i] = value;
            isbnCopy /= 10;
        }
        return digits;
    }

    /**
     * Calculates the checksum for a given ISBN number
     * @param digits - Array of 9 digits for an ISBN number
     * @return checksum - the checksum for an ISBN - 10
     * **/
    public static int getChecksum(int[] digits) {
        int checksum = 0;
        int multiplier = 1; // Used as the multiplier because the array is reversed
        for (int i = 8; i >=  0 ; i--) {
            checksum += (digits[i] * multiplier);
            multiplier++;
        }

        return checksum % 11;
    }

    public static void main(String[] args) {
        System.out.print("Enter ISBN: ");
        Scanner input = new Scanner(System.in);
        int isbn = input.nextInt();
        input.close();
        int digits[] = splitISBN(isbn);
        int checksum = getChecksum(digits);

        System.out.print("The ISBN-10 number is: " + String.format("%09d", isbn));
        System.out.println(checksum == 10 ? "X" : checksum);



    }
}



























