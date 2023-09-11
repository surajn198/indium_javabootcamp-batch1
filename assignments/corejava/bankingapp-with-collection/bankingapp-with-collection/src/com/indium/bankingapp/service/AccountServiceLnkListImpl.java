package com.indium.bankingapp.service;

import com.indium.bankingapp.model.Account;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class AccountServiceLnkListImpl implements AccountService {
    private List<Account> accounts = new LinkedList<>();

    @Override
    public boolean createAccount(Account account) {
        return accounts.add(account);
    }

    @Override
    public boolean updateAccount(int id, Account account) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getId() == id) {
                accounts.set(i, account);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteAccount(int id) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getId() == id) {
                accounts.remove(i);
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
        return new LinkedList<>(accounts);
    }
}