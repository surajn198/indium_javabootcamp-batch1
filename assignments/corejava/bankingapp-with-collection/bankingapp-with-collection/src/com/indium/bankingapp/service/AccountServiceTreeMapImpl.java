package com.indium.bankingapp.service;

import com.indium.bankingapp.model.Account;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class AccountServiceTreeMapImpl implements AccountService {
    private TreeMap<Integer, Account> accountsMap = new TreeMap<>();

    @Override
    public boolean createAccount(Account account) {
        if (!accountsMap.containsKey(account.getId())) {
            accountsMap.put(account.getId(), account);
            return true; // Account created successfully
        }
        return false; // Account with the same ID already exists
    }

    @Override
    public boolean updateAccount(int id, Account updatedAccount) {
        if (accountsMap.containsKey(id)) {
            accountsMap.put(id, updatedAccount);
            return true; // Account updated successfully
        }
        return false; // Account not found
    }

    @Override
    public boolean deleteAccount(int id) {
        if (accountsMap.containsKey(id)) {
            accountsMap.remove(id);
            return true; // Account deleted successfully
        }
        return false; // Account not found
    }

    @Override
    public Account getAccount(int id) {
        return accountsMap.get(id);
    }

    @Override
    public Collection<Account> getAllAccounts() {
        return accountsMap.values();
    }
}