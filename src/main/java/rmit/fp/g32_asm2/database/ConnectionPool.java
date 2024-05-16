package rmit.fp.g32_asm2.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

public class ConnectionPool {
    private static final int INITIAL_POOL_SIZE = 5;
    private final Queue<Connection> connectionPool = new LinkedList<>();
    private static ConnectionPool instance;

    private ConnectionPool() {
        try {
            initialize();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        System.out.println("Pool size: " + instance.connectionPool.size());
        return instance;
    }

    private void initialize() throws SQLException {
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            connectionPool.add(createNewConnectionForPool());
        }
    }

    private Connection createNewConnectionForPool() throws SQLException {
        String dbUrl = System.getenv("DB_URL");
        String dbPort = System.getenv("DB_PORT_SESSION");
        String dbName = System.getenv("DB_NAME");
        String dbUser = System.getenv("DB_USER");
        String dbPass = System.getenv("DB_PASSWORD");

        String url = dbUrl + ":" + dbPort + "/" + dbName;
        return DriverManager.getConnection(url, dbUser, dbPass);
    }

    public synchronized Connection getConnection() {
        if (connectionPool.isEmpty()) {
            throw new RuntimeException("No available connection");
        }
        return connectionPool.poll();
    }

    public synchronized void releaseConnection(Connection connection) {
        connectionPool.add(connection);
    }

    public synchronized void shutdown() {
        while (!connectionPool.isEmpty()) {
            Connection conn = connectionPool.poll();
            try {
                if (!conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
