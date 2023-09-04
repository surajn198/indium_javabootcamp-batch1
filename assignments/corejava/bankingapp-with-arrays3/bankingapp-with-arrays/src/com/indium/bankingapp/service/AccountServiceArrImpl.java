package com.indium.bankingapp.service;

import com.indium.bankingapp.model.Account;

public class AccountServiceArrImpl implements AccountService {
    private static final int MAX_ACCOUNTS = 10;
    private Account[] accounts = new Account[MAX_ACCOUNTS];
    private int totalAccounts = 0;

    @Override
    public boolean createAccount(Account account) {
        if (totalAccounts < MAX_ACCOUNTS) {
            accounts[totalAccounts++] = account;
            return true; // Account created successfully
        } else {
            System.out.println("Maximum number of accounts reached. Cannot create more accounts.");
            return false; // Failed to create an account
        }
    }

    @Override
    public boolean updateAccount(int id, Account updatedAccount) {
        for (int i = 0; i < totalAccounts; i++) {
            if (accounts[i].getId() == id) {
                accounts[i] = updatedAccount;
                return true; // Account updated successfully
            }
        }
        return false; // Account not found
    }

    @Override
    public boolean deleteAccount(int id) {
        for (int i = 0; i < totalAccounts; i++) {
            if (accounts[i].getId() == id) {
                // Move all accounts after the deleted account one position forward
                for (int j = i; j < totalAccounts - 1; j++) {
                    accounts[j] = accounts[j + 1];
                }
                totalAccounts--;
                return true; // Account deleted successfully
            }
        }
        return false; // Account not found
    }

    @Override
    public Account getAccount(int id) {
        for (int i = 0; i < totalAccounts; i++) {
            if (accounts[i].getId() == id) {
                return accounts[i]; // Return the account if found
            }
        }
        return null; // account not found
    }

    @Override
    public Account[] getAllAccounts() {
        Account[] allAccounts = new Account[totalAccounts];
        System.arraycopy(accounts, 0, allAccounts, 0, totalAccounts);
        return allAccounts;
    }
}