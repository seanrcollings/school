/**
 * Assignment 1
 *
 * @author Sean Collings #A02278024
 *
 * Overall, I think this implementation is decent.
 * There's a bit of repeated information (i.e. ranges of each operation),
 * But for a small assignment like this, I don't think it's nessecary to improve
 */

import java.math.BigInteger;
import java.math.BigDecimal;
import java.math.RoundingMode;

class Assign1 {
	/**
	* Calculates the fibonnaci number at the provided value
	* @param value the value to calculate the fibonnaci sequence to
	*/
	private static int fibonacci(int value) {
		int fib1 = 1;
		int fib2 = 1;
		int fib3 = 0;

		for (int i = 0; i <= value - 2; i++) {
			fib3 = fib1 + fib2;
			fib1 = fib2;
			fib2 = fib3;
		}

		return fib3;
	}


	/**
	* Calculates the factorial at the provided value
	* @param value the value to calculate the factorial of
	*/
	private static BigInteger factorial(int value) {
		BigInteger fac = BigInteger.valueOf(1);

		if (value == 0) {
			return fac;
		}

		for (int i = value; i > 0; i--) {
			fac = fac.multiply(BigInteger.valueOf(i));
		}
		return fac;
	}
	/**
	* Calculates `e` using a given number of iterations
	* This implementation uses the Taylor expansion of the
	* sum Σ(1/n!) of the equation e^x. Because x is one in
	* this instance, it does factor into our calculation of the sum
	* @param value the number of iterations to use
	*/
	private static BigDecimal approxE(int value) {

        BigDecimal one = new BigDecimal(1);
        BigDecimal e = new BigDecimal(1.0);
        BigDecimal divisor = new BigDecimal(1.0);

        for (int i = 1; i <= value; i++) {
            divisor = divisor.multiply(new BigDecimal(i));
            e = e.add(one.divide(divisor, 20, RoundingMode.HALF_EVEN));
		}

		return e;
	}

	/**
	 * Outputs help information
	 */
	private static void help() {
		System.out.println(
			"--- Assign 1 Help ---\n"
			.concat("  -fib [n] : Compute the Fibonacci of [n]; valid range [0, 40]\n")
			.concat("  -fac [n] : Compute the factorial of [n]; valid range, [0, 2147483647]\n")
			.concat("  -e   [n] : Compute the value of 'e' using [n] iterations; valid range [1, 2147483647]")
		);
	}

	/**
	 * Asserts that a given integer is between two others.
	 * Is inclusive on both end
	 * @param between the integer to check if it's in the range
	 * @param start of the range
	 * @param end of the range
	 */
	private static boolean inRange(int between, int start, int end) {
		return start <= between && between <= end;
	}

	private static int parseArg(String arg) {
		try {
			return Integer.parseInt(arg);
		} catch(NumberFormatException e) {
			return -1;
		}
	}

	public static void main(String[] args) {
		// We can insure a correct number of arguments
		// By checking first that there are arguments,
		// and then checking if they are even, because
		// they always come in pairs
		if (args.length == 0 || args.length % 2 != 0) {
			help();
			return;
		}

		int currIdx = 0;

		while (currIdx < args.length) {
			String param = args[currIdx++];
			int arg = parseArg(args[currIdx++]);

			switch (param) {
				case "-fib":
					if(inRange(arg, 0, 40)) {
						System.out.println("Fibonacci of " + arg + " is " + fibonacci(arg));
					} else {
						System.out.println("Fibonacci valid range is [0, 40]");
					}
					break;
				case "-fac":
					if(inRange(arg, 0, 2147483647)) {
						System.out.println("Factorial of " + arg + " is " + factorial(arg));
					} else {
						System.out.println("Factorial valid range is [0, 2147483647]");
					}
					break;
				case "-e":
					if (inRange(arg, 1, 2147483647)) {
						System.out.println("Approximation of e using " + arg + " iterations is " + approxE(arg));
					} else {
						System.out.println("Approximation of e valid range is [1, 2147483647]");
					}
					break;
				default:
					System.out.println("Unkown comamnd line argument: " + param);
			}
		}
	}
}
