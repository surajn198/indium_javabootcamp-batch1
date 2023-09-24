package com.indium.bankingapp.service;

import com.indium.bankingapp.model.Account;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AccountServiceHashMapImpl implements AccountService<Account> {
    private Map<Integer, Account> accounts = new HashMap<>();
    private final Object lock = new Object();

    @Override
    public boolean createAccount(Account account) {
        synchronized (lock) {
            if (accounts.containsKey(account.getId())) {
                // Account with the same ID already exists
                return false;
            }
            accounts.put(account.getId(), account);
            return true;
        }
    }

    @Override
    public boolean updateAccount(int id, Account account) {
        synchronized (lock) {
            if (accounts.containsKey(id)) {
                accounts.put(id, account);
                return true;
            }
            return false; // Account not found
        }
    }

    @Override
    public boolean deleteAccount(int id) {
        synchronized (lock) {
            if (accounts.containsKey(id)) {
                accounts.remove(id);
                return true;
            }
            return false; // Account not found
        }
    }

    @Override
    public Account getAccount(int id) {
        synchronized (lock) {
            return accounts.get(id);
        }
    }

    @Override
    public Collection<Account> getAllAccounts() {
        synchronized (lock) {
            return accounts.values();
        }
    }

    @Override
    public boolean exportAccounts(String filePath) {
        synchronized (lock) {
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
                outputStream.writeObject(accounts);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    @Override
    public boolean importAccounts(String filePath) {
        synchronized (lock) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filePath))) {
                Map<Integer, Account> importedAccounts = (Map<Integer, Account>) inputStream.readObject();
                accounts.clear();
                accounts.putAll(importedAccounts);
                return true;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}