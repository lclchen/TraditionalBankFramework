package com.bank.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

import com.bank.server.model.Account;
import com.bank.server.util.Logger;

public class AccountDaoImpl implements AccountDao{
    public Connection conn;

    public AccountDaoImpl(Connection connection){
        this.conn = connection;
    }

    @Override
    public boolean createAccount(Account account) throws Exception{
        String sql = "insert into account (account_id, username, currency, balance, activated, revision) values(\"" +
                account.getUuid() + "\", \"" +
                account.getUsername() + "\", \"" +
                account.getCurrency() + "\", \"" +
                account.getBalance() + "\", " +
                account.getActivated() + ", " +
                account.getRevision() + ");";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.executeUpdate();

        return true;
    }

    @Override
    public Account getAccountById(String uuid, boolean forUpdate) throws Exception{
        String sql = "select * from account where account_id=\"" + uuid;
        if(forUpdate)
            sql += "\" for update;";
        else
            sql += "\";";
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        List<Map> list = convertList(rs);
        if(list == null || list.isEmpty())
            return null;
        else
            return convertAccount(list.get(0));
    }

    @Override
    public boolean updateAccount(String uuid, Account newAccount) throws Exception{
        String sql = "update account set username=\"" + newAccount.getUsername() +
                    "\", currency=\"" + newAccount.getCurrency() +
                    "\", balance=\"" + newAccount.getBalance() +
                    "\", activated=" + newAccount.getActivated() +
                    ", revision=" + newAccount.getRevision() +
                    " where account_id=\"" + uuid + "\";";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.executeUpdate();
        return true;
    }

    @Override
    public boolean disableAccount(String uuid) throws Exception{
        String sql = "update account set activated=False where account_id=\"" + uuid + "\";";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.executeUpdate();
        return true;
    }

    private static List<Map> convertList(ResultSet rs) throws Exception {
        List list = new ArrayList();
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount(); //Map rowData;

        while (rs.next()) { //rowData = new HashMap(columnCount);
            Map rowData = new HashMap();
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(rowData);
        }
        return list;
    }

    public static Account convertAccount(Map map) throws Exception {
        Account account = new Account();
        account.setUuid((String) map.get("account_id"));
        account.setUsername((String) map.get("username"));
        account.setCurrency((String) map.get("currency"));
        account.setBalance((String) map.get("balance"));
        account.setActivated(((Boolean) map.get("activated")).toString());
        account.setRevision((Integer) map.get("revision"));
        return account;
    }
}
