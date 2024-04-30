package all.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class dbConnection {
    public Connection connection_to_db(String dbname, String user, String pass) {
        Connection conn = null;
        try {
            // Correct the URL format here and include SSL mode if necessary
            String url = "jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:5432/" + dbname + "?sslmode=require";
            conn = DriverManager.getConnection(url, user, pass);
            System.out.println("Connected to PostgreSQL database");
        } catch (Exception e) {
            System.out.println("Connection error: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }
}
