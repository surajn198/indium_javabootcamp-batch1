package com.indium.bankingapp.service;

import com.indium.bankingapp.model.Account;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AccountServiceHashMapImpl implements AccountService<Account> {
    private Map<Integer, Account> accounts = new HashMap<>();

    @Override
    public boolean createAccount(Account account) {
        if (accounts.containsKey(account.getId())) {
            // Account with the same ID already exists
            return false;
        }
        accounts.put(account.getId(), account);
        return true;
    }

    @Override
    public boolean updateAccount(int id, Account account) {
        if (accounts.containsKey(id)) {
            accounts.put(id, account);
            return true;
        }
        return false; // Account not found
    }

    @Override
    public boolean deleteAccount(int id) {
        if (accounts.containsKey(id)) {
            accounts.remove(id);
            return true;
        }
        return false; // Account not found
    }

    @Override
    public Account getAccount(int id) {
        return accounts.get(id);
    }

    @Override
    public Collection<Account> getAllAccounts() {
        return accounts.values();
    }
}