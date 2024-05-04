    package all.auth;

    import all.db.dbConnection;
    import all.model.customer.ClaimManagement;

    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.util.ArrayList;
    import java.util.List;

    public class ClaimDatabase {
        private dbConnection dbConn = new dbConnection();

        public List<ClaimManagement> getAllClaims() {
            List<ClaimManagement> claims = new ArrayList<>();
            String sql = "SELECT id, customer_id, description, status FROM claims";
            try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
                 PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    int id = rs.getInt("id");
                    int customerId = rs.getInt("customer_id");
                    String description = rs.getString("description");
                    String status = rs.getString("status");
                    claims.add(new ClaimManagement(id, customerId, description, status));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return claims;
        }

        public void addClaim(ClaimManagement claim) {
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

        public void updateClaim(ClaimManagement claim) {
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
