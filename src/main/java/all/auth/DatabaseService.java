package all.auth;

import all.db.dbConnection;
import all.model.Claim;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {
    private dbConnection dbConn = new dbConnection();

    public List<Claim> getAllClaims() {
        List<Claim> claims = new ArrayList<>();
        String sql = "SELECT id, customer_id, description, status FROM claims";
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int customerId = rs.getInt("customer_id");
                String description = rs.getString("description");
                String status = rs.getString("status");
                claims.add(new Claim(id, customerId, description, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return claims;
    }

    public void addClaim(Claim claim) {
        String sql = "INSERT INTO claims (customer_id, description, status) VALUES (?, ?, ?)";
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, claim.getCustomerId());
            pstmt.setString(2, claim.getDescription());
            pstmt.setString(3, claim.getStatus());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateClaim(Claim claim) {
        String sql = "UPDATE claims SET customer_id = ?, description = ?, status = ? WHERE id = ?";
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, claim.getCustomerId());
            pstmt.setString(2, claim.getDescription());
            pstmt.setString(3, claim.getStatus());
            pstmt.setInt(4, claim.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteClaim(int id) {
        String sql = "DELETE FROM claims WHERE id = ?";
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
