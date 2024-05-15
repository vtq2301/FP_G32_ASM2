package all.auth;

import all.controller.UniqueIDGenerator;
import all.db.dbConnection;
import all.model.customer.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {
    private dbConnection dbConn = new dbConnection();

    public boolean registerUser(String username, String password, String role, String fullName, String address, String phoneNumber) {
        String sql = "INSERT INTO users (id, username, password_hash, role, full_name, address, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?);";

        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS")) {
            String id = UniqueIDGenerator.generateUniqueID(conn);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.setString(4, role);
            pstmt.setString(5, fullName);
            pstmt.setString(6, address);
            pstmt.setString(7, phoneNumber);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User authenticateUser(String username, String password) {
        String sql = "SELECT id, username, password_hash, role, full_name, address, phone_number FROM users WHERE username = ?;";
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS")) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password_hash");
                if (storedPassword.equals(password)) {
                    return new User(
                            rs.getString("username"),
                            rs.getString("role"),
                            rs.getString("full_name"),
                            rs.getString("address"),
                            rs.getString("phone_number")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
