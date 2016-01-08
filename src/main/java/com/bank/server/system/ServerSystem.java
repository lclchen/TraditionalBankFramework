package com.bank.server.system;

import com.bank.server.model.Command;
import com.bank.server.service.ReportServiceImpl;
import com.bank.server.util.ConnectionPool;
import com.bank.server.util.Logger;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerSystem extends UnicastRemoteObject implements IServerSystem{
    private String jdbcDriver;
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;

    public ConnectionPool pool;
    private int poolSize = 20;
    public ExecutorService threadPool;

    private int remotePort = 9999;
    private String remoteUrl = "rmi://localhost:9999/RemoteServerSystem";
    private String remoteName = "RemoteServerSystem";

    public ServerSystem() throws RemoteException {}

    public void init(){
        pool = new ConnectionPool(jdbcDriver, dbUrl, dbUsername, dbPassword);
        threadPool = Executors.newFixedThreadPool(poolSize);
    }

    public boolean handleCommand(Command cmd) throws RemoteException{
        try {
            threadPool.execute(new CommandHandler(pool.getConnection(), cmd));
            Logger.logger.info("ServerSystem handleCommand() success, command_id:" + cmd.getCommandId());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertAccount(UUID uuid, String username, BigDecimal balance, String currency, boolean activated,
                                 int revision) throws RemoteException{
        try {
            threadPool.execute(new AccountHandler(pool.getConnection(), uuid, username, balance, currency,
                    activated, revision));
            Logger.logger.info("ServerSystem inserAccount() success, account_id:" + uuid.toString());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    // Runnable
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

    // Runnable
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

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public String getRemoteName() {
        return remoteName;
    }

    public void setRemoteName(String remoteName) {
        this.remoteName = remoteName;
    }
}
