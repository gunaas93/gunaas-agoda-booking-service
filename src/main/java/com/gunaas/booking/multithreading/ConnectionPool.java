package com.gunaas.booking.multithreading;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Sql connection pool for multi threading concept.
 * It keeps the pool of reusable connections.
 */
public class ConnectionPool {
    private String connString;
    private String user;
    private String pwd;

    private static final int INITIAL_CAPACITY = 100;
    private static final LinkedList<Connection> pool = new LinkedList<Connection>();

    public String getConnString() {
        return connString;
    }

    public String getPwd() {
        return pwd;
    }

    public String getUser() {
        return user;
    }

    public ConnectionPool(String connString, String user, String pwd) {
        this.connString = connString;
        this.user = user;
        this.pwd = pwd;
    }

    @PostConstruct
    public void initPool() throws SQLException {
        for (int i = 0; i < INITIAL_CAPACITY; i++) {
            pool.add(DriverManager.getConnection(connString, user, pwd));
        }
    }

    public synchronized Connection getConnection() throws SQLException {
        if (pool.isEmpty()) {
            pool.add(DriverManager.getConnection(connString, user, pwd));
        }
        return pool.pop();
    }

    public synchronized void returnConnection(Connection connection) {
        pool.push(connection);
    }
}
