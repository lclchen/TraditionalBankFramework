package com.bank.server.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.Enumeration;
import java.util.Vector;

public class ConnectionPool {
    private String jdbcDriver = "";
    private String dbUrl = "";
    private String dbUsername = "";
    private String dbPassword = "";

    private int initialConnections = 10;
    private int incrementalConnections = 5;
    private int maxConnections = 20;
    private Vector connections = null;

    public ConnectionPool(String jdbcDriver, String dbUrl, String dbUsername,
                          String dbPassword) {
        this.jdbcDriver = jdbcDriver;
        this.dbUrl = dbUrl;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        try {
            createPool();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getInitialConnections() {
        return this.initialConnections;
    }
    public void setInitialConnections(int initialConnections) {
        this.initialConnections = initialConnections;
    }

    public int getIncrementalConnections() {
        return this.incrementalConnections;
    }

    public void setIncrementalConnections(int incrementalConnections) {
        this.incrementalConnections = incrementalConnections;
    }

    public int getMaxConnections() {
        return this.maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public synchronized void createPool() throws Exception {
        if (connections != null) {
            return;
        }

        Driver driver = (Driver) (Class.forName(this.jdbcDriver).newInstance());
        DriverManager.registerDriver(driver);

        connections = new Vector();

        createConnections(this.initialConnections);
    }

    private void createConnections(int numConnections) throws SQLException {

        for (int x = 0; x < numConnections; x++) {

            if (this.maxConnections > 0
                    && this.connections.size() >= this.maxConnections) {
                break;
            }

            try {
                connections.addElement(new PooledConnection(newConnection()));
            } catch (SQLException e) {
                throw new SQLException();
            }
        }
    }

    private Connection newConnection() throws SQLException {

        Connection conn = DriverManager.getConnection(dbUrl, dbUsername,
                dbPassword);

        if (connections.size() == 0) {
            DatabaseMetaData metaData = conn.getMetaData();
            int driverMaxConnections = metaData.getMaxConnections();

            if (driverMaxConnections > 0
                    && this.maxConnections > driverMaxConnections) {
                this.maxConnections = driverMaxConnections;
            }
        }
        return conn;
    }

    public synchronized Connection getConnection() throws SQLException {

        if (connections == null) {
            return null;
        }
        Connection conn = getFreeConnection();

        while (conn == null) {
            wait(250);
            conn = getFreeConnection();

        }
        return conn;
    }

    private Connection getFreeConnection() throws SQLException {

        Connection conn = findFreeConnection();
        if (conn == null) {

            createConnections(incrementalConnections);

            conn = findFreeConnection();
            if (conn == null) {

                return null;
            }
        }
        return conn;
    }

    private Connection findFreeConnection() throws SQLException {
        Connection conn = null;
        PooledConnection pConn = null;

        Enumeration enumerate = connections.elements();

        while (enumerate.hasMoreElements()) {
            pConn = (PooledConnection) enumerate.nextElement();
            if (!pConn.isBusy()) {

                conn = pConn.getConnection();
                pConn.setBusy(true);
                break;
            }
        }
        return conn;
    }

    public void returnConnection(Connection conn) {

        if (connections == null) {
            return;
        }
        PooledConnection pConn = null;
        Enumeration enumerate = connections.elements();

        while (enumerate.hasMoreElements()) {
            pConn = (PooledConnection) enumerate.nextElement();
            if (conn == pConn.getConnection()) {
                pConn.setBusy(false);
                break;
            }
        }
    }

    public synchronized void refreshConnections() throws SQLException {
        if (connections == null) {
            return;
        }
        PooledConnection pConn = null;
        Enumeration enumerate = connections.elements();
        while (enumerate.hasMoreElements()) {
            pConn = (PooledConnection) enumerate.nextElement();
            if (pConn.isBusy()) {
                wait(5000);
            }
            closeConnection(pConn.getConnection());
            pConn.setConnection(newConnection());
            pConn.setBusy(false);
        }
    }

    public synchronized void closeConnectionPool() throws SQLException {
        if (connections == null) {
            return;
        }
        PooledConnection pConn = null;
        Enumeration enumerate = connections.elements();
        while (enumerate.hasMoreElements()) {
            pConn = (PooledConnection) enumerate.nextElement();
            if (pConn.isBusy()) {
                wait(5000);
            }
            closeConnection(pConn.getConnection());
            connections.removeElement(pConn);
        }
        connections = null;
    }

    private void closeConnection(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void wait(int mSeconds) {
        try {
            Thread.sleep(mSeconds);
        } catch (InterruptedException e) {
        }
    }

    class PooledConnection {
        Connection connection = null;
        boolean busy = false;

        public PooledConnection(Connection connection) {
            this.connection = connection;
        }

        public Connection getConnection() {
            return connection;
        }

        public void setConnection(Connection connection) {
            this.connection = connection;
        }

        public boolean isBusy() {
            return busy;
        }

        public void setBusy(boolean busy) {
            this.busy = busy;
        }
    }
}