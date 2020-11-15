package de.fel1x.teamcrimx.crimxapi.database.sql;

import de.fel1x.teamcrimx.crimxapi.CrimxAPI;

import java.sql.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;

public class MySQL {

    private static MySQL instance;
    private final String host;
    private final String database;
    private final String user;
    private final String password;
    private final int port;
    private final CrimxAPI plugin;
    public ExecutorService executor;
    private Connection conn;

    public MySQL(CrimxAPI plugin, String host, String database, String user, String password, int port) {
        this.host = host;
        this.database = database;
        this.user = user;
        this.password = password;
        this.port = port;
        this.plugin = plugin;

        this.executor = Executors.newCachedThreadPool();

        instance = this;
    }

    public static MySQL getInstance() {
        return instance;
    }

    public void update(PreparedStatement stat) {
        if (this.isConnected()) {
            this.executor.execute(() -> this.queryUpdate(stat));
        }
    }

    public void update(String stat) {
        if (this.isConnected()) {
            this.executor.execute(() -> this.queryUpdate(stat));
        }
    }

    public ResultSet asnycquery(PreparedStatement stmt) {
        if (this.isConnected()) {
            Future<ResultSet> future = this.executor.submit(() -> query(stmt));
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    public ResultSet asnycquery(String statment) {
        if (this.isConnected()) {
            Future<ResultSet> future = this.executor.submit(() -> query(statment));
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    public PreparedStatement prepare(String query) {
        if (this.isConnected()) {
            try {
                return this.getConnection().prepareStatement(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void queryUpdate(String query) {
        if (this.isConnected()) {
            try (PreparedStatement statment = this.conn.prepareStatement(query)) {
                this.queryUpdate(statment);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public ResultSet query(String query) {
        if (this.isConnected()) {
            try {
                return this.query(this.conn.prepareStatement(query));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void queryUpdate(PreparedStatement stmt) {
        if (this.isConnected()) {
            try {
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ResultSet query(PreparedStatement stat) {
        if (this.isConnected()) {
            try {
                return stat.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean isConnected() {
        try {
            if (this.conn == null || !this.conn.isValid(10) || this.conn.isClosed()) {
                return false;
            }
        } catch (SQLException e) {

            return false;
        }
        return true;
    }

    public void connect() {
        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?autoReconnect=true",
                    this.user, this.password);
            this.conn.createStatement().setQueryTimeout(Integer.MAX_VALUE);
            this.plugin.getLogger().log(Level.INFO, "Successfully connected to mysql");
        } catch (SQLException e) {
            this.plugin.getLogger().log(Level.WARNING, "An error appeared while connecting to mysql");

        }
    }

    public Connection getConnection() {
        if (this.isConnected()) {
            return this.conn;
        }
        return null;
    }

    public void closeConnection() {
        if (this.isConnected()) {
            try {
                this.conn.close();
            } catch (SQLException e) {
            } finally {
                this.conn = null;
            }
        }
    }

}
