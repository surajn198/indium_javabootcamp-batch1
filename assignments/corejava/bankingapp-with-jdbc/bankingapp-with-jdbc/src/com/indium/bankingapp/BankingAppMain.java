package com.indium.bankingapp;

import com.indium.bankingapp.model.Account;
import com.indium.bankingapp.service.AccountService;
import com.indium.bankingapp.service.AccountServiceMySQLImpl;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

public class BankingAppMain {
    private static AccountService<Account> accountService = createAccountService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            displayMenu();
            int choice = getUserChoice();

            try {
                switch (choice) {
                    case 1:
                        captureAccountDetails();
                        break;
                    case 2:
                        viewAllAccounts();
                        break;
                    case 3:
                        viewAccountDetails();
                        break;
                    case 4:
                        updateAccountDetails();
                        break;
                    case 5:
                        deleteAccount();
                        break;
                    case 6:
                        handlePrintStatisticsMenu();
                        break;
                    case 7:
                        importAccounts();
                        break;
                    case 8:
                        exportAccounts();
                        break;
                    case 9:
                        System.out.println("Exiting the Banking App.");
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid choice.");
                scanner.nextLine(); // Consume the invalid input
            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private static AccountService<Account> createAccountService() {
        return new AccountServiceMySQLImpl(); // AccountServiceMySQLImpl for database integration
    }

    private static void displayMenu() {
        System.out.println("\nBanking App Menu:");
        System.out.println("1] Add Account");
        System.out.println("2] View All Accounts");
        System.out.println("3] View Account");
        System.out.println("4] Update Account");
        System.out.println("5] Delete Account");
        System.out.println("6] Print Statistics");
        System.out.println("7] Import");
        System.out.println("8] Export");
        System.out.println("9] Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getUserChoice() {
        int choice;
        while (true) {
            try {
                choice = scanner.nextInt();
                break; // Break out of the loop if the input is a valid integer
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid choice.");
                scanner.nextLine(); // Consume the invalid input
            }
        }
        return choice;
    }

    private static synchronized void captureAccountDetails() {
        try {
            System.out.print("Enter Account Type (savings/deposit/loan): ");
            String accountType = scanner.next().toLowerCase();

            if (!isValidAccountType(accountType)) {
                System.out.println("Invalid account type. Please enter 'savings', 'deposit', or 'loan'.");
                return;
            }

            System.out.print("Enter Account ID: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            // Validate that ID is positive
            if (id <= 0) {
                System.out.println("Invalid account ID. Please enter a positive integer.");
                return;
            }

            System.out.print("Enter Account Name: ");
            String name = scanner.nextLine();

            // Validate that the name is not empty
            if (name.isEmpty()) {
                System.out.println("Account name cannot be empty.");
                return;
            }

            System.out.print("Enter Initial Balance: ");
            double balance = scanner.nextDouble();
            scanner.nextLine();

            // Validate that the balance is non-negative
            if (balance < 0) {
                System.out.println("Initial balance cannot be negative.");
                return;
            }

            System.out.print("Enter Rate of Interest (ROI): ");
            double roi = scanner.nextDouble();
            scanner.nextLine();

            // Validate that the ROI is non-negative
            if (roi < 0) {
                System.out.println("Rate of Interest (ROI) cannot be negative.");
                return;
            }

            Account account = new Account(id, name, balance, roi, accountType);

            // Calculate and update the balance with interest
            calculateInterestAndUpdateBalance(account);

            if (accountService.createAccount(account)) {
                System.out.println("Account created successfully.");
            } else {
                System.out.println("Failed to create an account.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter valid data.");
            scanner.nextLine(); // Consume the invalid input
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    private static synchronized void calculateInterestAndUpdateBalance(Account account) {
        double balance = account.getBalance();
        double roi = account.getRoi();
        double interest = balance * roi;

        // Add interest to the balance
        double newBalance = balance + interest;
        account.setBalance(newBalance); // Update the balance attribute directly
    }

    private static boolean isValidAccountType(String accountType) {
        return accountType.equals("savings") || accountType.equals("deposit") || accountType.equals("loan");
    }

    private static synchronized void viewAllAccounts() {
        List<Account> accounts = new ArrayList<>(accountService.getAllAccounts());

        if (!accounts.isEmpty()) {
            System.out.println("All Accounts:");
            for (Account account : accounts) {
                System.out.println(account.toString());
                System.out.println("--------------");
            }
        } else {
            System.out.println("No accounts found.");
        }
    }

    private static synchronized void viewAccountDetails() {
        try {
            System.out.print("Enter Account ID to view: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            Account account = accountService.getAccount(id);

            if (account != null) {
                System.out.println("Account Details:");
                System.out.println(account.toString());
            } else {
                System.out.println("Account not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid ID.");
            scanner.nextLine(); // Consume the invalid input
        }
    }

    private static synchronized void updateAccountDetails() {
        try {
            System.out.print("Enter Account ID to update: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            Account existingAccount = accountService.getAccount(id);

            if (existingAccount != null) {
                System.out.print("Enter New Account Name: ");
                String newName = scanner.nextLine();
                System.out.print("Enter New Account Balance: ");
                double newBalance = scanner.nextDouble();
                scanner.nextLine();

                Account updatedAccount = new Account(id, newName, newBalance, existingAccount.getRoi(), existingAccount.getAccountType());

                if (accountService.updateAccount(id, updatedAccount)) {
                    System.out.println("Account updated successfully.");
                } else {
                    System.out.println("Failed to update the account.");
                }
            } else {
                System.out.println("Account not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter valid data.");
            scanner.nextLine(); // Consume the invalid input
        }
    }

    private static synchronized void deleteAccount() {
        try {
            System.out.print("Enter Account ID to delete: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            if (accountService.deleteAccount(id)) {
                System.out.println("Account deleted successfully.");
            } else {
                System.out.println("Failed to delete the account.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid ID.");
            scanner.nextLine(); // Consume the invalid input
        }
    }

    private static void handlePrintStatisticsMenu() {
        while (true) {
            displayStatisticsMenu();
            int choice = getUserChoice();

            try {
                switch (choice) {
                    case 1:
                        printAccountsAbove100000();
                        break;
                    case 2:
                        printAccountTypeCounts();
                        break;
                    case 3:
                        printSortedAccountTypeCounts();
                        break;
                    case 4:
                        printAvgBalanceByAccountType();
                        break;
                    case 5:
                        listAccountIdsByName();
                        break;
                    case 6:
                        return; // Return to the main menu
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid choice.");
                scanner.nextLine(); // Consume the invalid input
            }
        }
    }

    private static void displayStatisticsMenu() {
        System.out.println("\nStatistics Menu:");
        System.out.println("1] No of accounts which have balance more than 1 Lac");
        System.out.println("2] Show no of account by account type");
        System.out.println("3] Show no of accounts by account type with sorting");
        System.out.println("4] Show avg balance by account type");
        System.out.println("5] List account IDs whose account name contains a given name");
        System.out.println("6] Back to Main Menu");
        System.out.print("Enter your choice: ");
    }

    private static void printAccountsAbove100000() {
        List<Account> accounts = accountService.getAllAccounts();
        long count = accounts.stream()
                .filter(account -> account.getBalance() > 100000)
                .count();
        System.out.println("Number of accounts with balance more than 1 Lac: " + count);
    }

    private static void printAccountTypeCounts() {
        List<Account> accounts = accountService.getAllAccounts();
        Map<String, Long> countByAccountType = accounts.stream()
                .collect(Collectors.groupingBy(Account::getAccountType, Collectors.counting()));

        for (Map.Entry<String, Long> entry : countByAccountType.entrySet()) {
            System.out.println("Account Type: " + entry.getKey() + ", Count: " + entry.getValue());
        }
    }

    private static void printSortedAccountTypeCounts() {
        List<Account> accounts = accountService.getAllAccounts();
        Map<String, Long> countByAccountType = accounts.stream()
                .collect(Collectors.groupingBy(Account::getAccountType, Collectors.counting()));

        countByAccountType.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> {
                    System.out.println("Account Type: " + entry.getKey() + ", Count: " + entry.getValue());
                });
    }

    private static void printAvgBalanceByAccountType() {
        List<Account> accounts = accountService.getAllAccounts();
        Map<String, Double> avgBalanceByAccountType = accounts.stream()
                .collect(Collectors.groupingBy(Account::getAccountType, Collectors.averagingDouble(Account::getBalance)));

        for (Map.Entry<String, Double> entry : avgBalanceByAccountType.entrySet()) {
            System.out.println("Account Type: " + entry.getKey() + ", Avg Balance: " + entry.getValue());
        }
    }

    private static void listAccountIdsByName() {
        System.out.print("Enter a name to search for in account names: ");
        scanner.nextLine(); // Consume the newline character from the previous input
        String searchName = scanner.nextLine().trim().toLowerCase();

        if (searchName.isEmpty()) {
            System.out.println("Search name cannot be empty.");
            return;
        }

        List<Account> accounts = accountService.getAllAccounts();
        List<Integer> accountIds = new ArrayList<>();

        for (Account account : accounts) {
            String accountName = account.getName().trim().toLowerCase();
            if (accountName.contains(searchName)) {
                accountIds.add(account.getId());
            }
        }

        if (!accountIds.isEmpty()) {
            System.out.println("Account IDs with names containing '" + searchName + "': " + accountIds);
        } else {
            System.out.println("No accounts found with names containing '" + searchName + "'.");
        }
    }

    private static void importAccounts() {
        try {
            System.out.print("Enter the path of the file to import from: ");
            String filePath = scanner.next();
            scanner.nextLine(); // Consume the newline character

            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            int importCount = 0;

            while ((line = reader.readLine()) != null) {
                // Parse the line and create an Account object
                Account account = parseAccountFromTXT(line);

                if (account != null) {
                    // Check if an account with the same ID already exists
                    if (accountService.getAccount(account.getId()) != null) {
                        System.out.println("Account with ID " + account.getId() + " already exists. Skipping import.");
                    } else {
                        if (accountService.createAccount(account)) {
                            importCount++; // Increment the count of successfully imported accounts
                        } else {
                            System.out.println("Failed to import account with ID " + account.getId());
                        }
                    }
                }
            }

            reader.close();
            System.out.println(importCount + " accounts imported successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred during import: " + e.getMessage());
        }
    }
 private static void exportAccounts() {
        try {
            System.out.print("Enter the path of the file to export to: ");
            String filePath = scanner.next();
            scanner.nextLine(); // Consume the newline character

            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            Collection<Account> accounts = accountService.getAllAccounts();

            // Write account data to the txt file
            for (Account account : accounts) {
                String line = createTXTLineFromAccount(account);
                writer.write(line);
                writer.newLine();
            }

            writer.close();
            System.out.println(accounts.size() + " accounts exported successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred during export: " + e.getMessage());
        }
    }

    private static Account parseAccountFromTXT(String line) {
        String[] parts = line.split(";"); // delimiter
        if (parts.length == 5) {
            int id = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();
            double balance = Double.parseDouble(parts[2].trim());
            double roi = Double.parseDouble(parts[3].trim());
            String type = parts[4].trim();
            return new Account(id, name, balance, roi, type);
        }
        return null;
    }

    private static String createTXTLineFromAccount(Account account) {
        return account.getId() + ";" + account.getName() + ";" +
                account.getBalance() + ";" + account.getRoi() + ";" +
                account.getAccountType();
    }
}