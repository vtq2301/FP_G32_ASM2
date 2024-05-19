/**
 @author GROUP 32
 - s3870729 - Tran Vu Nhat Tin
 - s3929202 - Vu Pham Nguyen Vu
 - s3914412 - Nguyen Duong Truong Thinh
 - s3981278 - Vu Tien Quang
 */
package all.auth;

import all.db.dbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ActionLogger {
    private final dbConnection dbConn = new dbConnection();

    public ActionLogger() {
    }
    public ActionLogger(Connection mockConnection){}
    boolean isUsernameValid(String username) {
        String sql = "SELECT username FROM users WHERE username = ?";
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            return pstmt.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void logAction(String username, String actionType, String description, String claimId) {
        if (username == null || username.isEmpty()) {
            System.err.println("Error: username is empty or null.");
            return;
        }

        if (!isUsernameValid(username)) {
            System.err.println("Error: No such user exists with username: " + username);
            return;
        }

        String sql = "INSERT INTO user_actions (username, action_type, action_description, claim_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, actionType);
            pstmt.setString(3, description);
            pstmt.setString(4, claimId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                System.err.println("Insertion failed, no rows affected.");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
