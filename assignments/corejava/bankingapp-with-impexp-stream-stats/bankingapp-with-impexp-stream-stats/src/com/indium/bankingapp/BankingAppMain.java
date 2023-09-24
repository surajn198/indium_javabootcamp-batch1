package com.indium.bankingapp;

import com.indium.bankingapp.model.Account;
import com.indium.bankingapp.service.AccountService;
import com.indium.bankingapp.service.AccountServiceHashMapImpl;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors; // Added import for Collectors

public class BankingAppMain {
    private static AccountService<Account> accountService = createAccountService();
    private static Scanner scanner = new Scanner(System.in);

    // Defining int variables to keep track of the counts
    private static int importCount = 0;
    private static int exportCount = 0;

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
        return new AccountServiceHashMapImpl();
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

    private static synchronized int getUserChoice() {
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    private static synchronized void captureAccountDetails() {
        try {
            System.out.print("Enter Account Type (savings/deposit/loan): ");
            String accountType = scanner.nextLine().toLowerCase();

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

    private static synchronized void handlePrintStatisticsMenu() {
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
        System.out.println("5] List account ids whose account name contains given name");
        System.out.println("6] Back to Main Menu");
        System.out.print("Enter your choice: ");
    }

    private static void printAccountsAbove100000() {
        List<Account> accounts = new ArrayList<>(accountService.getAllAccounts());
        long accountsWithHighBalance = accounts.stream().filter(account -> account.getBalance() > 100000).count();
        System.out.println("No. of accounts with balance more than 1 Lac: " + accountsWithHighBalance);
    }

    private static void printAccountTypeCounts() {
        Map<String, Long> accountTypeCounts = accountService.getAllAccounts().stream()
                .collect(Collectors.groupingBy(Account::getAccountType, Collectors.counting()));

        System.out.println("No of accounts by account type:");
        accountTypeCounts.forEach((accountType, count) -> {
            System.out.println(accountType + ": " + count);
        });
    }

    private static void printSortedAccountTypeCounts() {
        Map<String, Long> accountTypeCounts = accountService.getAllAccounts().stream()
                .collect(Collectors.groupingBy(Account::getAccountType, Collectors.counting()));

        List<Map.Entry<String, Long>> sortedAccountTypeCounts = accountTypeCounts.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .collect(Collectors.toList());

        System.out.println("No of accounts by account type (sorted in descending order):");
        sortedAccountTypeCounts.forEach(entry -> {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        });
    }

    private static void printAvgBalanceByAccountType() {
        Map<String, Double> avgBalanceByType = accountService.getAllAccounts().stream()
                .collect(Collectors.groupingBy(
                        Account::getAccountType,
                        Collectors.averagingDouble(Account::getBalance)
                ));

        System.out.println("Average balance by account type:");
        avgBalanceByType.forEach((accountType, avgBalance) -> {
            System.out.println(accountType + ": " + avgBalance);
        });
    }

    private static void listAccountIdsByName() {
        try {
            System.out.print("Enter a name to search for: ");
            String searchName = scanner.nextLine().toLowerCase();
            List<Account> accounts = new ArrayList<>(accountService.getAllAccounts());

            List<String> accountInfoWithName = new ArrayList<>(); // Change to store both ID and type

            for (Account account : accounts) {
                if (account.getName().toLowerCase().contains(searchName)) {
                    String accountInfo = "Account ID: " + account.getId() + ", Account Type: " + account.getAccountType();
                    accountInfoWithName.add(accountInfo);
                }
            }

            if (!accountInfoWithName.isEmpty()) {
                System.out.println("Account IDs and Types with names containing '" + searchName + "':");
                for (String accountInfo : accountInfoWithName) {
                    System.out.println(accountInfo);
                }
            } else {
                System.out.println("No accounts found with names containing '" + searchName + "'.");
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    private static synchronized void importAccounts() {
        try {
            System.out.print("Enter the path of the file to import from: ");
            String filePath = scanner.nextLine();

            // Created a thread to perform the import
            Thread importThread = new Thread(() -> {
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(filePath));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length == 5) {
                            int id = Integer.parseInt(parts[0].trim());
                            String name = parts[1].trim();
                            double balance = Double.parseDouble(parts[2].trim());
                            double roi = Double.parseDouble(parts[3].trim());
                            String type = parts[4].trim();

                            Account account = new Account(id, name, balance, roi, type);

                            if (accountService.createAccount(account)) {
                                importCount++; // Increment the count 
                            }
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error reading from the file: " + e.getMessage());
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing data from the file: " + e.getMessage());
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            // Start the import thread
            importThread.start();

            // Wait for the import thread to finish
            importThread.join();

            System.out.println(importCount + " accounts imported successfully.");
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    private static synchronized void exportAccounts() {
        try {
            System.out.print("Enter the path of the file to export to: ");
            String filePath = scanner.nextLine();

            // Create a thread to perform the export
            Thread exportThread = new Thread(() -> {
                BufferedWriter writer = null;
                Collection<Account> accounts = accountService.getAllAccounts();

                try {
                    writer = new BufferedWriter(new FileWriter(filePath));

                    for (Account account : accounts) {
                        String line = account.getId() + "," + account.getName() + "," +
                                account.getBalance() + "," + account.getRoi() + "," +
                                account.getAccountType();

                        writer.write(line);
                        writer.newLine();

                        exportCount++; // Increment the count 
                    }
                } catch (IOException e) {
                    System.err.println("Error writing to the file: " + e.getMessage());
                } finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            // Start the export thread
            exportThread.start();

            // Wait for the export thread to finish
            exportThread.join();

            System.out.println(exportCount + " accounts exported successfully.");
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}