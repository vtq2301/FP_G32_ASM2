package all.auth;

import all.controller.ClaimIDGenerator;
import all.db.dbConnection;
import all.model.customer.ClaimManagement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClaimDatabase {
    private final dbConnection dbConn = new dbConnection();


    public List<ClaimManagement> getClaimsForPolicyHolder(String policyHolderId) {
        List<ClaimManagement> claims = new ArrayList<>();
        String sql = "SELECT id, customer_id, description, status FROM claims WHERE customer_id = ?";
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, policyHolderId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                claims.add(new ClaimManagement(
                        rs.getString("id"),
                        rs.getString("customer_id"),
                        rs.getString("description"),
                        rs.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return claims;
    }


    // Add a new claim for a specific policyholder and return the new claim ID
    public String addClaim(ClaimManagement claim) {
        String sql = "INSERT INTO claims (id, customer_id, description, status) VALUES (?, ?, ?, ?)";
        String claimId = ClaimIDGenerator.generateUniqueClaimID(dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS"));
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, claimId);
            pstmt.setString(2, claim.getCustomerId());
            pstmt.setString(3, claim.getDescription());
            pstmt.setString(4, claim.getStatus());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                claim.setId(claimId); // Set the claim ID back on the object
                return claimId;
            } else {
                throw new SQLException("Creating claim failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void updateClaim(ClaimManagement claim) {
        String sql = "UPDATE claims SET description = ?, status = ? WHERE id = ?";
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, claim.getDescription());
            pstmt.setString(2, claim.getStatus());
            pstmt.setString(3, claim.getId());
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteClaim(String id) {
        String sql = "DELETE FROM claims WHERE id = ?";
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Deletion failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
