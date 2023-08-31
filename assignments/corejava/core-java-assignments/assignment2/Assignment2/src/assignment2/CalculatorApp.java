package assignment2;

public class CalculatorApp {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: CalculatorApp <num1> <num2> <operator>");
            System.exit(1);
        }
        
        double num1 = Double.parseDouble(args[0]);
        double num2 = Double.parseDouble(args[1]);
        String operator = args[2].toLowerCase();

        char operatorSign = getOperatorSign(operator);
        if (operatorSign == '\0') {
            System.out.println("Error: Invalid operator");
            System.exit(1);
        }

        double result = calculate(num1, operatorSign, num2);

        if (Double.isNaN(result)) {
            System.out.println("Error: Invalid operator or division by zero.");
        } else {
            System.out.println("Result: " + result);
        }
    }

    public static char getOperatorSign(String operator) {
        switch (operator) {
            case "add":
                return '+';
            case "subtract":
                return '-';
            case "multiply":
                return '*';
            case "divide":
                return '/';
            default:
                return '\0'; 
        }
    }

    public static double calculate(double num1, char operator, double num2) {
        double result = 0.0;

        switch (operator) {
            case '+':
                result = num1 + num2;
                break;
            case '-':
                result = num1 - num2;
                break;
            case '*':
                result = num1 * num2;
                break;
            case '/':
                if (num2 != 0) {
                    result = num1 / num2;
                } else {
                    return Double.NaN; // division by zero
                }
                break;
            default:
                return Double.NaN; //  invalid operator
        }

        return result;
    }
}