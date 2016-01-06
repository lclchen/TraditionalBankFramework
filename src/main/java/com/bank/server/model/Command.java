package com.bank.server.model;

public class Command {
    final String TYPE_TRANSFER_IN = "transfer_in";
    final String TYPE_TRANSFER_OUT = "transfer_out";
    final String TYPE_WITHDRAW = "withdraw";
    final String TYPE_DEPOSIT = "deposit";

    String commandId;
    String accountId;
    String currency;

    String type;
    String accountTransferIn;
    String accountTransferOut;
    String amount;

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccountTransferIn() {
        return accountTransferIn;
    }

    public void setAccountTransferIn(String accountTransferIn) {
        this.accountTransferIn = accountTransferIn;
    }

    public String getAccountTransferOut() {
        return accountTransferOut;
    }

    public void setAccountTransferOut(String accountTransferOut) {
        this.accountTransferOut = accountTransferOut;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}