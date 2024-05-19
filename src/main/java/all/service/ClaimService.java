/**
 @author GROUP 32
 - s3870729 - Tran Vu Nhat Tin
 - s3929202 - Vu Pham Nguyen Vu
 - s3914412 - Nguyen Duong Truong Thinh
 - s3981278 - Vu Tien Quang
 */
package all.service;

import all.db.ConnectionPool;
import all.model.customer.ClaimManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClaimService {

    private final ConnectionPool connectionPool;

    public ClaimService() {
        this.connectionPool = ConnectionPool.getInstance();
    }
    public ClaimService(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public List<ClaimManagement> findAllBeneficiaryClaims(String policyOwnerId) {
        List<ClaimManagement> claims = new ArrayList<>();
        String sql = "SELECT c.* FROM claims c, users u WHERE c.insured_person = u.id AND u.policy_owner_id = ?";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, policyOwnerId);
                System.out.println(ps);
                ResultSet rs = ps.executeQuery();
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
                            rs.getString("status")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return claims;
    }

    public void addClaim(ClaimManagement claim) {
        String sql = "INSERT INTO claims (id, customer_id, claim_date, insured_person, exam_date, documents, claim_amount, receiver_banking_info, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, claim.getId());
                ps.setString(2, claim.getCustomerId());
                ps.setDate(3, new java.sql.Date(claim.getClaimDate().getTime()));
                ps.setString(4, claim.getInsuredPerson());
                ps.setDate(5, new java.sql.Date(claim.getExamDate().getTime()));
                ps.setArray(6, conn.createArrayOf("VARCHAR", claim.getDocuments()));
                ps.setDouble(7, claim.getClaimAmount());
                ps.setString(8, claim.getReceiverBankingInfo());
                ps.setString(9, claim.getStatus());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    public void updateClaim(ClaimManagement claim) {
        String sql = "UPDATE claims SET insured_person = ?, claim_amount = ?, status = ?, claim_date = ?, exam_date = ?, documents = ?, receiver_banking_info = ? WHERE id = ?";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, claim.getInsuredPerson());
                ps.setDouble(2, claim.getClaimAmount());
                ps.setString(3, claim.getStatus());
                ps.setDate(4, new java.sql.Date(claim.getClaimDate().getTime()));
                ps.setDate(5, new java.sql.Date(claim.getExamDate().getTime()));
                ps.setArray(6, conn.createArrayOf("VARCHAR", claim.getDocuments()));
                ps.setString(7, claim.getReceiverBankingInfo());
                ps.setString(8, claim.getId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    public void deleteClaim(String claimId) {
        String sql = "DELETE FROM claims WHERE id = ?";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, claimId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }
}
