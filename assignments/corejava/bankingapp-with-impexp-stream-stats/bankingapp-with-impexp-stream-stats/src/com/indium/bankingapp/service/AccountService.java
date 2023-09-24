package com.indium.bankingapp.service;

import com.indium.bankingapp.model.Account;

import java.util.Collection;

public interface AccountService<T> {
    boolean createAccount(T account);
    boolean updateAccount(int id, T account);
    boolean deleteAccount(int id);
    T getAccount(int id);
    Collection<T> getAllAccounts();
    
    
    // method for importing accounts
    boolean importAccounts(String filePath);

    // method for exporting accounts
    boolean exportAccounts(String filePath);

 }
