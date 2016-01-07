package com.bank.server.model;

public class Event {
    static public final String TYPE_TRANSFER_IN = "transfer_in";
    static public final String TYPE_TRANSFER_OUT = "transfer_out";
    static public final String TYPE_WITHDRAW = "withdraw";
    static public final String TYPE_DEPOSIT = "deposit";

    String eventId;
    String commandId;
    String committedTime;
    String accountId;
    String currency;
    int revision;

    String type;
    String accountTransferIn;
    String accountTransferOut;
    String amount;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public String getCommittedTime() {
        return committedTime;
    }

    public void setCommittedTime(String committedTime) {
        this.committedTime = committedTime;
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

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
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

    public String getAccountTRansferOut() {
        return accountTransferOut;
    }

    public void setAccountTRansferOut(String accountTransferOut) {
        this.accountTransferOut = accountTransferOut;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
