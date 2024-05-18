package all.auth;

import all.controller.ClaimIDGenerator;
import all.db.dbConnection;
import all.model.customer.ClaimManagement;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClaimDatabase {
    private static final dbConnection dbConn = new dbConnection();

    public List<ClaimManagement> getClaimsForPolicyHolder(String policyHolderId) {
        List<ClaimManagement> claims = new ArrayList<>();
        String sql = "SELECT id, customer_id, claim_date, insured_person, exam_date, documents, claim_amount, receiver_banking_info, status FROM claims WHERE customer_id = ?";
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, policyHolderId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                claims.add(new ClaimManagement(
                        rs.getString("id"),
                        rs.getString("customer_id"),
                        rs.getDate("claim_date"),
                        rs.getString("insured_person"),
                        rs.getDate("exam_date"),
                        (String[]) rs.getArray("documents").getArray(),
                        rs.getDouble("claim_amount"),
                        rs.getString("receiver_banking_info"),
                        rs.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return claims;
    }

    public List<ClaimManagement> getClaimsForDependent(String dependentId) {
        List<ClaimManagement> claims = new ArrayList<>();
        String policyHolderQuery = "SELECT policy_holder_id FROM users WHERE username = ?";
        String claimsQuery = "SELECT id, customer_id, claim_date, insured_person, exam_date, documents, claim_amount, receiver_banking_info, status FROM claims WHERE customer_id = ?";

        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS")) {
            String policyHolderId = null;
            try (PreparedStatement policyHolderStmt = conn.prepareStatement(policyHolderQuery)) {
                policyHolderStmt.setString(1, dependentId);
                try (ResultSet rs = policyHolderStmt.executeQuery()) {
                    if (rs.next()) {
                        policyHolderId = rs.getString("policy_holder_id");
                    }
                }
            }

            if (policyHolderId != null) {
                try (PreparedStatement claimsStmt = conn.prepareStatement(claimsQuery)) {
                    claimsStmt.setString(1, policyHolderId);
                    try (ResultSet rs = claimsStmt.executeQuery()) {
                        while (rs.next()) {
                            claims.add(new ClaimManagement(
                                    rs.getString("id"),
                                    rs.getString("customer_id"),
                                    rs.getDate("claim_date"),
                                    rs.getString("insured_person"),
                                    rs.getDate("exam_date"),
                                    (String[]) rs.getArray("documents").getArray(),
                                    rs.getDouble("claim_amount"),
                                    rs.getString("receiver_banking_info"),
                                    rs.getString("status")));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return claims;
    }

    public static List<ClaimManagement> getAllClaims() {
        List<ClaimManagement> claims = new ArrayList<>();
        String sql = "SELECT id, customer_id, claim_date, insured_person, exam_date, documents, claim_amount, receiver_banking_info, status FROM claims";
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                claims.add(new ClaimManagement(
                        rs.getString("id"),
                        rs.getString("customer_id"),
                        rs.getDate("claim_date"),
                        rs.getString("insured_person"),
                        rs.getDate("exam_date"),
                        (String[]) rs.getArray("documents").getArray(),
                        rs.getDouble("claim_amount"),
                        rs.getString("receiver_banking_info"),
                        rs.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return claims;
    }

    public List<ClaimManagement> getClaimsByStatus(String status) {
        List<ClaimManagement> claims = new ArrayList<>();
        String sql = "SELECT id, customer_id, claim_date, insured_person, exam_date, documents, claim_amount, receiver_banking_info, status FROM claims WHERE status = ?";
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                claims.add(new ClaimManagement(
                        rs.getString("id"),
                        rs.getString("customer_id"),
                        rs.getDate("claim_date"),
                        rs.getString("insured_person"),
                        rs.getDate("exam_date"),
                        (String[]) rs.getArray("documents").getArray(),
                        rs.getDouble("claim_amount"),
                        rs.getString("receiver_banking_info"),
                        rs.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return claims;
    }

    public String addClaim(ClaimManagement claim) {
        String sql = "INSERT INTO claims (id, customer_id, insured_person, claim_date, exam_date, documents, claim_amount, receiver_banking_info, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String claimId = ClaimIDGenerator.generateUniqueClaimID(dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS"));
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, claimId);
            pstmt.setString(2, claim.getCustomerId());
            pstmt.setString(3, claim.getInsuredPerson());
            pstmt.setDate(4, new java.sql.Date(claim.getClaimDate().getTime()));
            pstmt.setDate(5, new java.sql.Date(claim.getExamDate().getTime()));
            pstmt.setArray(6, conn.createArrayOf("VARCHAR", claim.getDocuments()));
            pstmt.setDouble(7, claim.getClaimAmount());
            pstmt.setString(8, claim.getReceiverBankingInfo());
            pstmt.setString(9, claim.getStatus());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                claim.setId(claimId);
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
        String sql = "UPDATE claims SET insured_person = ?, claim_date = ?, exam_date = ?, documents = ?, claim_amount = ?, receiver_banking_info = ?, status = ? WHERE id = ?";
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, claim.getInsuredPerson());
            pstmt.setDate(2, new java.sql.Date(claim.getClaimDate().getTime()));
            pstmt.setDate(3, new java.sql.Date(claim.getExamDate().getTime()));
            pstmt.setArray(4, conn.createArrayOf("VARCHAR", claim.getDocuments()));
            pstmt.setDouble(5, claim.getClaimAmount());
            pstmt.setString(6, claim.getReceiverBankingInfo());
            pstmt.setString(7, claim.getStatus());
            pstmt.setString(8, claim.getId());
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
