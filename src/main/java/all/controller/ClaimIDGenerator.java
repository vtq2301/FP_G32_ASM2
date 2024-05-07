package all.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

// Generates unique string-based claim ID
public class ClaimIDGenerator {
    public static String generateUniqueClaimID(Connection connection) {
        String generatedID;
        Random random = new Random();
        do {
            long randomNumber = Math.abs(random.nextLong()) % 1_000_000_0000L; // Generate random 10-digit number
            generatedID = "c-" + String.format("%010d", randomNumber); // Formatted string-based ID
        } while (isClaimIDExists(generatedID, connection));
        return generatedID;
    }

    private static boolean isClaimIDExists(String claimID, Connection connection) {
        String sql = "SELECT id FROM claims WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, claimID);  // String-based
            return pstmt.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
