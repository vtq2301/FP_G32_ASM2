package rmit.fp.g32_asm2.database;

import java.sql.Connection;
import java.sql.DriverManager;
public class dbConnection {
    public Connection connection_to_db(String dbname, String user, String pass) {
        Connection conn = null;
        try {
            String url = "jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:5432/" + dbname;
            conn = DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            System.out.println("Connection error: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }
}
