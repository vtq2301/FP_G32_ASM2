package all.auth;

import all.db.dbConnection;
import all.model.customer.ClaimManagement;
import all.model.customer.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BeneficiaryService {
    private final dbConnection dbConnectionManager = new dbConnection();

    public List<User> fetchAvailableBeneficiaries(String policyOwnerId) {
        List<User> beneficiaries = new ArrayList<>();
        String sql = "SELECT id, username, role, full_name, address, phone_number FROM users WHERE role = 'PolicyHolder' AND (policy_owner_id != ? OR policy_owner_id IS NULL);";
        try (Connection connection = dbConnectionManager.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, policyOwnerId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    beneficiaries.add(new User(
                            resultSet.getString("id"),
                            resultSet.getString("username"),
                            resultSet.getString("role"),
                            resultSet.getString("full_name"),
                            resultSet.getString("address"),
                            resultSet.getString("phone_number")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching available beneficiaries: " + e.getMessage());
            e.printStackTrace();
        }
        return beneficiaries;
    }

    public List<User> fetchSelectedBeneficiaries(String policyOwnerId) {
        List<User> beneficiaries = new ArrayList<>();
        String sql = "SELECT id, username, role, full_name, address, phone_number FROM users WHERE role = 'PolicyHolder' AND policy_owner_id = ?;";
        try (Connection connection = dbConnectionManager.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, policyOwnerId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    beneficiaries.add(new User(
                            resultSet.getString("id"),
                            resultSet.getString("username"),
                            resultSet.getString("role"),
                            resultSet.getString("full_name"),
                            resultSet.getString("address"),
                            resultSet.getString("phone_number")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching selected beneficiaries: " + e.getMessage());
            e.printStackTrace();
        }
        return beneficiaries;
    }

    public boolean saveAndRemoveBeneficiaries(List<User> beneficiariesToSave, List<User> beneficiariesToRemove, String policyOwnerId) {
        String updateSql = "UPDATE users SET policy_owner_id = ? WHERE id = ?;";
        String removeSql = "UPDATE users SET policy_owner_id = NULL WHERE id = ?;";
        try (Connection connection = dbConnectionManager.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS")) {
            connection.setAutoCommit(false);
            try (PreparedStatement updateStmt = connection.prepareStatement(updateSql);
                 PreparedStatement removeStmt = connection.prepareStatement(removeSql)) {

                for (User beneficiary : beneficiariesToSave) {
                    updateStmt.setString(1, policyOwnerId);
                    updateStmt.setString(2, beneficiary.getId());
                    updateStmt.addBatch();
                }

                for (User beneficiary : beneficiariesToRemove) {
                    removeStmt.setString(1, beneficiary.getId());
                    removeStmt.addBatch();
                }

                updateStmt.executeBatch();
                removeStmt.executeBatch();
                connection.commit();
                return true;
            } catch (SQLException e) {
                System.err.println("Error saving and removing beneficiaries: " + e.getMessage());
                connection.rollback();
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Transaction error: " + e.getMessage());
            return false;
        }
    }
    public List<ClaimManagement> getClaimsForBeneficiaries(String policyOwnerId) {
        List<ClaimManagement> claims = new ArrayList<>();
        // Ensure this SQL correctly references the actual schema and columns
        String sql = "SELECT c.id, c.customer_id, c.claim_date, c.insured_person, c.exam_date, c.documents, c.claim_amount, c.receiver_banking_info, c.status " +
                "FROM claims c JOIN users u ON c.customer_id = u.id " +
                "WHERE u.policy_owner_id = ?";  // Check this condition refers to the correct column
        try (Connection conn = dbConnectionManager.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, policyOwnerId);
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



}
