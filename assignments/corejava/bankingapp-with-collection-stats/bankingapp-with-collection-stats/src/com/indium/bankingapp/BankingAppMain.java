package com.indium.bankingapp;

import com.indium.bankingapp.model.Account;
import com.indium.bankingapp.service.AccountService;
import com.indium.bankingapp.service.AccountServiceHashMapImpl;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class BankingAppMain {
    private static AccountService<Account> accountService = createAccountService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            displayMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    captureAccountDetails();
                    break;
                case 2:
                    updateAccountDetails();
                    break;
                case 3:
                    viewAccountDetails();
                    break;
                case 4:
                    viewAllAccounts();
                    break;
                case 5:
                    deleteAccount();
                    break;
                case 6:
                    handlePrintStatisticsMenu();
                    break;
                case 7:
                    System.out.println("Exiting the Banking App.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static AccountService<Account> createAccountService() {
        return new AccountServiceHashMapImpl();
    }

    private static void displayMenu() {
        System.out.println("\nBanking App Menu:");
        System.out.println("1] Create Account");
        System.out.println("2] Update Account");
        System.out.println("3] View Account");
        System.out.println("4] View All Accounts");
        System.out.println("5] Delete Account");
        System.out.println("6] Print Statistics");
        System.out.println("7] Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getUserChoice() {
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    private static void captureAccountDetails() {
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


    private static void calculateInterestAndUpdateBalance(Account account) {
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

    private static void updateAccountDetails() {
        System.out.print("Enter Account ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Account existingAccount = accountService.getAccount(id);

        if (existingAccount != null) {
            System.out.print("Enter New Account Name: ");
            String newName = scanner.nextLine();
            System.out.print("Enter New Account Balance: ");
            double newBalance = scanner.nextDouble();

            Account updatedAccount = new Account(id, newName, newBalance, existingAccount.getRoi(), existingAccount.getAccountType());

            if (accountService.updateAccount(id, updatedAccount)) {
                System.out.println("Account updated successfully.");
            } else {
                System.out.println("Failed to update the account.");
            }
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void viewAccountDetails() {
        System.out.print("Enter Account ID to view: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Account account = accountService.getAccount(id);

        if (account != null) {
            System.out.println("Account Details:");
            System.out.println("ID: " + account.getId());
            System.out.println("Name: " + account.getName());
            System.out.println("Balance: " + account.getBalance());
            System.out.println("Account Type: " + account.getAccountType());
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void viewAllAccounts() {
        List<Account> accounts = new ArrayList<>(accountService.getAllAccounts());

        if (!accounts.isEmpty()) {
            System.out.println("All Accounts:");
            for (Account account : accounts) {
                System.out.println("ID: " + account.getId());
                System.out.println("Name: " + account.getName());
                System.out.println("Balance: " + account.getBalance());
                System.out.println("Account Type: " + account.getAccountType());
                System.out.println("--------------");
            }
        } else {
            System.out.println("No accounts found.");
        }
    }

    private static void deleteAccount() {
        System.out.print("Enter Account ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        if (accountService.deleteAccount(id)) {
            System.out.println("Account deleted successfully.");
        } else {
            System.out.println("Failed to delete the account.");
        }
    }

    private static void handlePrintStatisticsMenu() {
        while (true) {
            displayStatisticsMenu();
            int choice = getUserChoice();

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
                    listAccountIdsAndTypesByName();
                    break;
                case 6:
                    return; // Return to the main menu
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void displayStatisticsMenu() {
        System.out.println("\nStatistics Menu:");
        System.out.println("1] No.of accounts which have balance more than 1 Lac");
        System.out.println("2] Show no of accounts by account type");
        System.out.println("3] Show no of accounts by account type with sorting");
        System.out.println("4] Show avg balance by account type");
        System.out.println("5] List account IDs and account types whose account name contains given name");
        System.out.println("6] Back to Main Menu");
        System.out.print("Enter your choice: ");
    }

    private static void printAccountsAbove100000() {
        try {
            List<Account> accounts = new ArrayList<>(accountService.getAllAccounts());
            long accountsWithHighBalance = accounts.stream()
                    .filter(account -> account.getBalance() > 100000)
                    .count();

            System.out.println("a] No. of accounts with balance more than 1 Lac: " + accountsWithHighBalance);
        } catch (Exception e) {
            System.err.println("An error occurred while printing accounts with high balance: " + e.getMessage());
        }
    }

    private static void printAccountTypeCounts() {
        try {
            List<Account> accounts = new ArrayList<>(accountService.getAllAccounts());
            long savingsCount = accounts.stream().filter(account -> account.getAccountType().equals("savings")).count();
            long depositCount = accounts.stream().filter(account -> account.getAccountType().equals("deposit")).count();
            long loanCount = accounts.stream().filter(account -> account.getAccountType().equals("loan")).count();

            System.out.println("b] No of accounts by account type:");
            System.out.println("Savings: " + savingsCount);
            System.out.println("Deposit: " + depositCount);
            System.out.println("Loan: " + loanCount);
        } catch (Exception e) {
            System.err.println("An error occurred while printing account type counts: " + e.getMessage());
        }
    }

    private static void printSortedAccountTypeCounts() {
        try {
            List<Account> accounts = new ArrayList<>(accountService.getAllAccounts());
            long savingsCount = accounts.stream().filter(account -> account.getAccountType().equals("savings")).count();
            long depositCount = accounts.stream().filter(account -> account.getAccountType().equals("deposit")).count();
            long loanCount = accounts.stream().filter(account -> account.getAccountType().equals("loan")).count();

            System.out.println("c] No of accounts by account type (sorted):");
            System.out.println("Savings: " + savingsCount);
            System.out.println("Deposit: " + depositCount);
            System.out.println("Loan: " + loanCount);
        } catch (Exception e) {
            System.err.println("An error occurred while printing sorted account type counts: " + e.getMessage());
        }
    }

    private static void printAvgBalanceByAccountType() {
        try {
            List<Account> accounts = new ArrayList<>(accountService.getAllAccounts());
            double totalSavingsBalance = accounts.stream()
                    .filter(account -> account.getAccountType().equals("savings"))
                    .mapToDouble(Account::getBalance)
                    .sum();
            long savingsCount = accounts.stream().filter(account -> account.getAccountType().equals("savings")).count();

            double totalDepositBalance = accounts.stream()
                    .filter(account -> account.getAccountType().equals("deposit"))
                    .mapToDouble(Account::getBalance)
                    .sum();
            long depositCount = accounts.stream().filter(account -> account.getAccountType().equals("deposit")).count();

            double totalLoanBalance = accounts.stream()
                    .filter(account -> account.getAccountType().equals("loan"))
                    .mapToDouble(Account::getBalance)
                    .sum();
            long loanCount = accounts.stream().filter(account -> account.getAccountType().equals("loan")).count();

            System.out.println("d] Average balance by account type:");
            System.out.println("Savings: " + (totalSavingsBalance / savingsCount));
            System.out.println("Deposit: " + (totalDepositBalance / depositCount));
            System.out.println("Loan: " + (totalLoanBalance / loanCount));
        } catch (Exception e) {
            System.err.println("An error occurred while printing average balance by account type: " + e.getMessage());
        }
    }

    private static void listAccountIdsAndTypesByName() {
        try {
            System.out.print("e] Enter a name to search for: ");
            String searchName = scanner.nextLine().toLowerCase();
            List<Account> accounts = new ArrayList<>(accountService.getAllAccounts());

            List<String> accountInfoWithName = new ArrayList<>();

            for (Account account : accounts) {
                if (account.getName().toLowerCase().contains(searchName)) {
                    accountInfoWithName.add("Account ID: " + account.getId() + ", Account Type: " + account.getAccountType());
                }
            }

            if (!accountInfoWithName.isEmpty()) {
                System.out.println("e] Account IDs and Types with names containing '" + searchName + "':");
                for (String accountInfo : accountInfoWithName) {
                    System.out.println(accountInfo);
                }
            } else {
                System.out.println("e] No accounts found with names containing '" + searchName + "'.");
            }
        } catch (Exception e) {
            System.err.println("An error occurred while listing account IDs and types by name: " + e.getMessage());
        }
    }
}