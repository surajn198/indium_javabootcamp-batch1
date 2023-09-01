package com.bankingapp.arrays;

import java.util.Scanner;

class BankAccount {
    int accountNumber;
    String accountHolder;
    double balance;

    public BankAccount(int accountNumber, String accountHolder, double balance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = balance;
    }
}

public class BankingAppArrays {

    static final int MAX_ACCOUNTS = 100;
    static BankAccount[] accounts = new BankAccount[MAX_ACCOUNTS];
    static int totalAccounts = 0;

    public static void addAccount(int accountNumber, String accountHolder, double balance) {
        if (totalAccounts < MAX_ACCOUNTS) {
            accounts[totalAccounts++] = new BankAccount(accountNumber, accountHolder, balance);
            System.out.println("Account added successfully.");
        } else {
            System.out.println("Maximum number of accounts reached.");
        }
    }

    public static void viewAllAccounts() {
        System.out.println("All Accounts:");
        for (int i = 0; i < totalAccounts; i++) {
            System.out.println("Account Number: " + accounts[i].accountNumber);
            System.out.println("Account Holder: " + accounts[i].accountHolder);
            System.out.println("Balance: " + accounts[i].balance);
            System.out.println("--------------");
        }
    }

    public static void viewAccount(int accountNumber) {
        for (int i = 0; i < totalAccounts; i++) {
            if (accounts[i].accountNumber == accountNumber) {
                System.out.println("Account Number: " + accounts[i].accountNumber);
                System.out.println("Account Holder: " + accounts[i].accountHolder);
                System.out.println("Balance: " + accounts[i].balance);
                return;
            }
        }
        System.out.println("Account not found.");
    }

    public static void updateAccount(int accountNumber, double newBalance) {
        for (int i = 0; i < totalAccounts; i++) {
            if (accounts[i].accountNumber == accountNumber) {
                accounts[i].balance = newBalance;
                System.out.println("Account updated successfully.");
                return;
            }
        }
        System.out.println("Account not found.");
    }

    public static void deleteAccount(int accountNumber) {
        for (int i = 0; i < totalAccounts; i++) {
            if (accounts[i].accountNumber == accountNumber) {
                for (int j = i; j < totalAccounts - 1; j++) {
                    accounts[j] = accounts[j + 1];
                }
                totalAccounts--;
                System.out.println("Account deleted successfully.");
                return;
            }
        }
        System.out.println("Account not found.");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Banking App Menu:");
            System.out.println("1] Add Account");
            System.out.println("2] View All Accounts");
            System.out.println("3] View Account");
            System.out.println("4] Update Account");
            System.out.println("5] Delete Account");
            System.out.println("6] Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter Account Number: ");
                    int accNumber = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character
                    System.out.print("Enter Account Holder: ");
                    String accHolder = scanner.nextLine();
                    System.out.print("Enter Balance: ");
                    double accBalance = scanner.nextDouble();
                    addAccount(accNumber, accHolder, accBalance);
                    break;
                case 2:
                    viewAllAccounts();
                    break;
                case 3:
                    System.out.print("Enter Account Number: ");
                    int viewAccNumber = scanner.nextInt();
                    viewAccount(viewAccNumber);
                    break;
                case 4:
                    System.out.print("Enter Account Number: ");
                    int updateAccNumber = scanner.nextInt();
                    System.out.print("Enter New Balance: ");
                    double newBalance = scanner.nextDouble();
                    updateAccount(updateAccNumber, newBalance);
                    break;
                case 5:
                    System.out.print("Enter Account Number: ");
                    int deleteAccNumber = scanner.nextInt();
                    deleteAccount(deleteAccNumber);
                    break;
                case 6:
                    System.out.println("Exiting the Banking App.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }
}