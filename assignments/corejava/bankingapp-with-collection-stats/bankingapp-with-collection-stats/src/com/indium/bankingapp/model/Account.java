package com.indium.bankingapp.model;

public class Account implements Comparable<Account> {
    private int id;
    private String name;
    private double balance;
    private double roi; // Rate of Interest
    private String type;

    public Account(int id, String name, double balance, double roi, String type) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.roi = roi;
        this.type = type;
    }

    public String getAccountType() {
        return type;
    }

    public void setAccountType(String type) {
        this.type = type;
    }
    public void setBalance(double balance) {
        this.balance = balance;
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
    
    public double getRoi() {
        return roi;
    }

    public void setRoi(double roi) {
        this.roi = roi;
    }
    
    @Override
    public int compareTo(Account other) {
        return Integer.compare(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Account ID: " + id +
               "\nAccount Name: " + name +
               "\nBalance: " + balance +
               "\nRate of Interest: " + roi +
               "\nAccount Type: " + type;
    }
}