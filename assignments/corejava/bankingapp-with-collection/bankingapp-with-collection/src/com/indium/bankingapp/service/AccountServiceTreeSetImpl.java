package com.indium.bankingapp.service;

import com.indium.bankingapp.model.Account;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class AccountServiceTreeSetImpl implements AccountService {
    private Set<Account> accounts = new TreeSet<>();

    @Override
    public boolean createAccount(Account account) {
        return accounts.add(account);
    }

    @Override
    public boolean updateAccount(int id, Account account) {
        for (Account existingAccount : accounts) {
            if (existingAccount.getId() == id) {
                accounts.remove(existingAccount);
                accounts.add(account);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteAccount(int id) {
        for (Account existingAccount : accounts) {
            if (existingAccount.getId() == id) {
                accounts.remove(existingAccount);
                return true;
            }
        }
        return false;
    }

    @Override
    public Account getAccount(int id) {
        for (Account account : accounts) {
            if (account.getId() == id) {
                return account;
            }
        }
        return null;
    }

    @Override
    public Collection<Account> getAllAccounts() {
        return accounts;
    }
}