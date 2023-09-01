package com.assignment4.calculator;

public class CalculatorApp4 {

    public static double add(double num1, double num2) {
        return num1 + num2;
    }

    public static double subtract(double num1, double num2) {
        return num1 - num2;
    }

    public static double multiply(double num1, double num2) {
        return num1 * num2;
    }

    public static double divide(double num1, double num2) {
        if (num2 != 0) {
            return num1 / num2;
        } else {
            throw new ArithmeticException("Division by zero is not allowed.");
        }
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java CalculatorApp <number1> <number2> <operation>");
            return;
        }

        double num1 = Double.parseDouble(args[0]);
        double num2 = Double.parseDouble(args[1]);
        String operation = args[2];

        double result = 0.0;

        switch (operation) {
            case "add":
                result = add(num1, num2);
                break;
            case "sub":
                result = subtract(num1, num2);
                break;
            case "mul":
                result = multiply(num1, num2);
                break;
            case "div":
                try {
                    result = divide(num1, num2);
                } catch (ArithmeticException e) {
                    System.out.println("Error: " + e.getMessage());
                    return;
                }
                break;
            default:
                System.out.println("Invalid operation. Supported operations: add, sub, mul, div");
                return;
        }

        System.out.println("Result: " + result);
    }
}