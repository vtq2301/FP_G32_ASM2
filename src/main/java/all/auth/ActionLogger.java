package all.auth;

import all.db.dbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ActionLogger {
    private final dbConnection dbConn = new dbConnection();

    // Check if the user ID exists (now String)
    private boolean isUserIdValid(String policyHolderId) {
        String sql = "SELECT username FROM users WHERE username = ?";  // Assuming username is the unique identifier
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, policyHolderId);
            return pstmt.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public void logAction(String policyHolderId, String actionType, String description, String claimId) {
        if (policyHolderId == null || policyHolderId.isEmpty()) {
            System.err.println("Error: policyHolderId is empty or null.");
            return;
        }

        if (!isUserIdValid(policyHolderId)) {
            System.err.println("Error: No such policy holder exists with ID: " + policyHolderId);
            return;
        }

        String sql = "INSERT INTO user_actions (policyHolderId, action_type, action_description, claim_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, policyHolderId);
            pstmt.setString(2, actionType);
            pstmt.setString(3, description);
            pstmt.setString(4, claimId);  // Include the claim ID in the INSERT statement; it can be null
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
