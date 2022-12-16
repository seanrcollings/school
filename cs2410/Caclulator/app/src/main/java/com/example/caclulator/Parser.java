package com.example.caclulator;

public class Parser {
    enum Mode {
        VALUE,
        OPERATOR
    }
    public static Double evaluate(String expression) {
        String[] tokens = expression.split(" ");
        switch (tokens.length) {
            case 0:
            case 2:
                return Double.NaN;
            case 1:
                try {
                    return Double.parseDouble(tokens[0]);
                } catch (Exception e) {
                    return Double.NaN;
                }
        }

        Mode mode = Mode.VALUE;
        double currentValue;
        try {
            currentValue = Double.parseDouble(tokens[0]);
        } catch (Exception e) {
            return Double.NaN;
        }

        String operator = tokens[1];

        for (int i = 2; i < tokens.length; i++) {
            if (mode == Mode.VALUE) {
                double foundValue;
                try {
                    foundValue = Double.parseDouble(tokens[i]);
                } catch (Exception e) {
                    return Double.NaN;
                }

                switch (operator) {
                    case "+":
                        currentValue = currentValue + foundValue;
                        break;
                    case "-":
                        currentValue = currentValue - foundValue;
                        break;
                    case "/":
                        currentValue = currentValue / foundValue;
                        break;
                    case "*":
                        currentValue = currentValue * foundValue;
                        break;
                }
                mode = Mode.OPERATOR;
            } else {
                operator = tokens[i];
                mode = Mode.VALUE;
            }
        }

        return currentValue;
    }
}
