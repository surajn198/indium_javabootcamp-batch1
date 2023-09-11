package com.indium.bankingapp;

import com.indium.bankingapp.model.Account;
import com.indium.bankingapp.service.AccountService;
import com.indium.bankingapp.service.AccountServiceArrListImpl;
import com.indium.bankingapp.service.AccountServiceLnkListImpl;
import com.indium.bankingapp.service.AccountServiceHashSetImpl;
import com.indium.bankingapp.service.AccountServiceTreeSetImpl;
import com.indium.bankingapp.service.AccountServiceHashMapImpl;
import com.indium.bankingapp.service.AccountServiceTreeMapImpl;

import java.util.Collection;
import java.util.Scanner;

public class BankingAppMain {
    public static void main(String[] args) {
        
        AccountService accountService = createAccountService();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Banking App Menu:");
            System.out.println("1] Create Account");
            System.out.println("2] Update Account");
            System.out.println("3] View Account");
            System.out.println("4] View All Accounts");
            System.out.println("5] Delete Account");
            System.out.println("6] Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    captureAccountDetails(accountService, scanner);
                    break;
                case 2:
                    updateAccountDetails(accountService, scanner);
                    break;
                case 3:
                    viewAccountDetails(accountService, scanner);
                    break;
                case 4:
                    viewAllAccounts(accountService);
                    break;
                case 5:
                    deleteAccount(accountService, scanner);
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

    private static AccountService createAccountService() {
        // Example for ArrayList implementation
        // return new AccountServiceArrListImpl();
    	
        // Example for LinkedList implementation
        // return new AccountServiceLnkListImpl();
    	
        // Example for HashSet implementation
        // return new AccountServiceHashSetImpl();
    	
        // Example for TreeSet implementation
         return new AccountServiceTreeSetImpl();
    	
        // Example for HashMap implementation
        // return new AccountServiceHashMapImpl();
         
        // Example for TreeMap implementation
        // return new AccountServiceTreeMapImpl();
         
        // return null; // Update with desired implementation
    }

    private static boolean isValidAccountType(String accountType) {
        return accountType.equals("savings") || accountType.equals("deposit") || accountType.equals("loan");
    }

    private static void captureAccountDetails(AccountService accountService, Scanner scanner) {
        System.out.print("Enter Account Type (savings/deposit/loan): ");
        String accountType = scanner.nextLine().toLowerCase();

        if (!isValidAccountType(accountType)) {
            System.out.println("Invalid account type. Please enter 'savings', 'deposit', or 'loan'.");
            return;
        }
        System.out.print("Enter Account ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Account Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Initial Balance: ");
        double balance = scanner.nextDouble();
        scanner.nextLine();

        Account account = new Account(id, name, balance, accountType);

        if (accountService.createAccount(account)) {
            System.out.println("Account created successfully.");
        } else {
            System.out.println("Failed to create an account.");
        }
    }

    private static void updateAccountDetails(AccountService accountService, Scanner scanner) {
        System.out.print("Enter Account ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Account existingAccount = accountService.getAccount(id);

        if (existingAccount != null) {
            System.out.print("Enter New Account Name: ");
            String newName = scanner.nextLine();
            System.out.print("Enter New Account Balance: ");
            double newBalance = scanner.nextDouble();

            Account updatedAccount = new Account(id, newName, newBalance, existingAccount.getAccountType());

            if (accountService.updateAccount(id, updatedAccount)) {
                System.out.println("Account updated successfully.");
            } else {
                System.out.println("Failed to update the account.");
            }
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void viewAccountDetails(AccountService accountService, Scanner scanner) {
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

    private static void viewAllAccounts(AccountService accountService) {
        Collection<Account> accounts = accountService.getAllAccounts();

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

    private static void deleteAccount(AccountService accountService, Scanner scanner) {
        System.out.print("Enter Account ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine();     
    }
}