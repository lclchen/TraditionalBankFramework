package com.bank.server.system;

import com.bank.server.model.Command;
import com.bank.server.service.ReportServiceImpl;
import com.bank.server.util.ConnectionPool;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerSystem {
    private String jdbcDriver;
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;

    public ConnectionPool pool;
    private int poolSize = 20;
    public ExecutorService threadPool;

    public void init(){
        pool = new ConnectionPool(jdbcDriver, dbUrl, dbUsername, dbPassword);
        threadPool = Executors.newFixedThreadPool(poolSize);
    }

    public class CommandHandler implements Runnable{
        ReportServiceImpl service;
        Command cmd;

        public CommandHandler(Connection conn, Command cmd){
            service = new ReportServiceImpl(conn);
            this.cmd = cmd;
        }

        @Override
        public void run() {
            service.handleCommand(cmd);
        }
    }

    public class AccountHandler implements Runnable {
        ReportServiceImpl service;

        UUID uuid;
        String username;
        BigDecimal balance;
        String currency;
        boolean activated;
        int revision;

        public AccountHandler(Connection conn, UUID uuid, String username, BigDecimal balance, String currency,
                              boolean activated, int revision){
            service = new ReportServiceImpl(conn);

            this.uuid = uuid;
            this.username = username;
            this.balance = balance;
            this.currency = currency;
            this.activated = activated;
            this.revision = revision;
        }

        @Override
        public void run() {
            service.insertAccount(uuid, username, balance, currency, activated, revision);
        }
    }

    public boolean handleCommand(Command cmd){
        try {
            threadPool.execute(new CommandHandler(pool.getConnection(), cmd));
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertAccount(UUID uuid, String username, BigDecimal balance, String currency, boolean activated,
                                 int revision){
        try {
            threadPool.execute(new AccountHandler(pool.getConnection(), uuid, username, balance, currency,
                                                    activated, revision));
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }
}
