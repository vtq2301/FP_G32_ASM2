/**
 @author GROUP 32
 - s3870729 - Tran Vu Nhat Tin
 - s3929202 - Vu Pham Nguyen Vu
 - s3914412 - Nguyen Duong Truong Thinh
 - s3981278 - Vu Tien Quang
 */
package all.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class dbConnection {
    public Connection connection_to_db(String dbname, String user, String pass) {
        Connection conn = null;
        try {
            String url = "jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:5432/" + dbname + "?sslmode=require";
            conn = DriverManager.getConnection(url, user, pass);

        } catch (Exception e) {
            System.out.println("Connection error: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }
}
