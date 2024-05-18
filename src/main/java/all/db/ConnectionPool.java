package all.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {
    private static final int INITIAL_POOL_SIZE = 10;
    private static final List<Connection> connectionPool = new ArrayList<>(INITIAL_POOL_SIZE);
    private static final List<Connection> usedConnections = new ArrayList<>();
    private static final String URL = "jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:5432/postgres?sslmode=require";
    private static final String USER = "postgres.orimpphhrfwkilebxiki";
    private static final String PASSWORD = "RXj1sf5He5ORnrjS";

    private static ConnectionPool instance;

    private ConnectionPool() {
        try {
            for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
                connectionPool.add(createConnection());
            }
        } catch (SQLException e) {
            throw new ExceptionInInitializerError("Error creating initial connections: " + e.getMessage());
        }
    }

    public static ConnectionPool getInstance() {
        if (instance == null) {
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connectionPool.isEmpty()) {
            throw new SQLException("All connections are in use!");
        }

        Connection connection = connectionPool.remove(connectionPool.size() - 1);
        usedConnections.add(connection);
        System.out.println("Available Connections: " + getPoolSize());
        return connection;
    }

    public boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);
        return usedConnections.remove(connection);
    }

    public int getPoolSize() {
        return connectionPool.size();
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void shutdown() throws SQLException {
        for (Connection connection : usedConnections) {
            connection.close();
        }
        for (Connection connection : connectionPool) {
            connection.close();
        }
        connectionPool.clear();
        usedConnections.clear();
    }
}
