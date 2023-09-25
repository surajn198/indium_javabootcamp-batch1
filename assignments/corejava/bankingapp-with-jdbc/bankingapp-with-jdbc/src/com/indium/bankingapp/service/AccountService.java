package com.indium.bankingapp.service;

import com.indium.bankingapp.model.Account;

import java.util.List;

public interface AccountService<T extends Account> {
    boolean createAccount(T account);
    List<T> getAllAccounts();
    T getAccount(int id);
    boolean updateAccount(int id, T account);
    boolean deleteAccount(int id);
}
