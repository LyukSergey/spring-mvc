package com.lss.resource;

public class Transaction {

    private final String accountId;
    private final double amount;
    private boolean authenticated;
    private boolean fraudFree;
    private double balance;

    public Transaction(String accountId, double amount, double balance) {
        this.accountId = accountId;
        this.amount = amount;
        this.balance = balance;
        this.authenticated = false;
        this.fraudFree = true;
    }

    public String getAccountId() {
        return accountId;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double b) {
        this.balance = b;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean a) {
        this.authenticated = a;
    }

    public boolean isFraudFree() {
        return fraudFree;
    }

    public void setFraudFree(boolean f) {
        this.fraudFree = f;
    }
}
