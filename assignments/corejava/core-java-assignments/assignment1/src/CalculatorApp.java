
import java.util.Scanner;

public class CalculatorApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("CalculatorApp");
        System.out.println("Available operations: +, -, *, /");
        
        boolean continueCalculating = true;
        
        while (continueCalculating) {
            System.out.print("Enter first number: ");
            double num1 = scanner.nextDouble();
            
            System.out.print("Enter operator (+, -, *, /): ");
            char operator = scanner.next().charAt(0);
            
            System.out.print("Enter second number: ");
            double num2 = scanner.nextDouble();
            
            double result = calculate(num1, operator, num2);
            
            if (Double.isNaN(result)) {
                System.out.println("Error: Invalid operator or division by zero.");
            } else {
                System.out.println("Result: " + result);
            }
            
            System.out.print("Do you want to continue? (y/n): ");
            char choice = scanner.next().charAt(0);
            
            if (choice != 'y' && choice != 'Y') {
                continueCalculating = false;
            }
        }
        
        System.out.println("Calculator app closed.");
        scanner.close();
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
                    return Double.NaN; // Indicate division by zero
                }
                break;
            default:
                return Double.NaN; // Indicate invalid operator
        }
        
        return result;
    }
}