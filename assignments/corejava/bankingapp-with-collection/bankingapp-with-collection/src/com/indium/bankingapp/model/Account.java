package com.indium.bankingapp.model;

public class Account implements Comparable<Account> {
    private int id;
    private String name;
    private double balance;
    private double roi;
    private String type;

    public Account(int id, String name, double balance, String type) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.type = type;
    }

    public String getAccountType() {
        return type;
    }

    public void setAccountType(String type) {
        this.type = type;
    }

    @Override
    public int compareTo(Account other) {
        return Integer.compare(this.id, other.id);
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
}