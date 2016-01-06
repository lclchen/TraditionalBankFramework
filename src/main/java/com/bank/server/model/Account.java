package com.bank.server.model;

public class Account {
    String uuid;
    String username;
    String balance;
    String currency;

    Boolean activated; //use string to getter and setter
    int revision;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getActivated() {
        return activated.toString();
    }

    public void setActivated(String activated) {
        this.activated = new Boolean(activated);
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }
}
