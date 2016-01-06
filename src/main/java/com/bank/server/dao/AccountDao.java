package com.bank.server.dao;

import com.bank.server.model.Account;

public interface AccountDao {
    public boolean createAccount(Account accout) throws Exception;

    public Account getAccountById(String uuid, boolean forUpdate) throws Exception;

    public boolean updateAccount(String uuid, Account newAccount) throws Exception;

    public boolean disableAccount(String uuid) throws Exception;
}
