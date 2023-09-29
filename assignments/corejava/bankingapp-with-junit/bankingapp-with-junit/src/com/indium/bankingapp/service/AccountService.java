package com.indium.bankingapp.service;
import java.util.Collection;

public interface AccountService<T> {
    boolean createAccount(T account);
    boolean updateAccount(int id, T account);
    boolean deleteAccount(int id);
    T getAccount(int id);
    Collection<T> getAllAccounts();
}