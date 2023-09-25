package com.indium.bankingapp.model;

public class Account {
    private int id;
    private String name;
    private double balance;
    private double roi;
    private String accountType;

    public Account(int id, String name, double balance, double roi, String accountType) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.roi = roi;
        this.accountType = accountType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getRoi() {
        return roi;
    }

    public void setRoi(double roi) {
        this.roi = roi;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Override
    public String toString() {
        return "Account ID: " + id +
                "\nName: " + name +
                "\nBalance: " + balance +
                "\nRate of Interest (ROI): " + roi +
                "\nAccount Type: " + accountType;
    }
}