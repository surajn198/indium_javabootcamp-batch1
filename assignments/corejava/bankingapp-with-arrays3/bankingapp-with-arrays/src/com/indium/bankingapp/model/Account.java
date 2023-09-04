package com.indium.bankingapp.model;

public class Account {
    private int id;
    private String name;
    private double balance;
    private String accountType; 
    public Account(int id, String name, double balance, String accountType) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.accountType = accountType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountType() {
        return accountType;
    }
}