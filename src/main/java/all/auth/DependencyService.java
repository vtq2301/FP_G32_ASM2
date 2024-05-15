package all.auth;

import all.db.dbConnection;
import all.model.customer.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DependencyService {
    private final dbConnection dbConnectionManager = new dbConnection();

    /**
     * Fetch available dependents who are not currently associated with the policy holder.
     *
     * @param policyHolderId The unique ID of the policyholder.
     * @return List of available dependents.
     */
    public List<User> fetchAvailableDependents(String policyHolderId) {
        List<User> dependents = new ArrayList<>();
        String sql = "SELECT id, role, full_name, address, phone_number FROM users WHERE role = 'Dependent' AND (policy_holder_id IS NULL OR policy_holder_id != ?);";
        try (Connection connection = dbConnectionManager.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, policyHolderId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    dependents.add(new User(
                            resultSet.getString("id"),
                            resultSet.getString("role"),
                            resultSet.getString("full_name"),
                            resultSet.getString("address"),
                            resultSet.getString("phone_number")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching available dependents: " + e.getMessage());
            e.printStackTrace();
        }
        return dependents;
    }

    /**
     * Fetch the dependents currently associated with the policyholder.
     *
     * @param policyHolderId The unique ID of the policyholder.
     * @return List of selected dependents.
     */
    public List<User> fetchSelectedDependents(String policyHolderId) {
        List<User> dependents = new ArrayList<>();
        String sql = "SELECT id, role, full_name, address, phone_number FROM users WHERE role = 'Dependent' AND policy_holder_id = ?;";
        try (Connection connection = dbConnectionManager.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, policyHolderId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    dependents.add(new User(
                            resultSet.getString("id"),
                            resultSet.getString("role"),
                            resultSet.getString("full_name"),
                            resultSet.getString("address"),
                            resultSet.getString("phone_number")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching selected dependents: " + e.getMessage());
            e.printStackTrace();
        }
        return dependents;
    }

    /**
     * Save the dependents for the policyholder and remove those who should no longer be associated.
     *
     * @param dependentsToSave   The list of dependents to save under this policyholder.
     * @param dependentsToRemove The list of dependents to remove from this policyholder.
     * @param policyHolderId     The unique ID of the policyholder.
     * @return True if the operation is successful, otherwise False.
     */
    public boolean saveAndRemoveDependents(List<User> dependentsToSave, List<User> dependentsToRemove, String policyHolderId) {
        String updateSql = "UPDATE users SET policy_holder_id = ? WHERE id = ?;";
        String removeSql = "UPDATE users SET policy_holder_id = NULL WHERE id = ?;";
        try (Connection connection = dbConnectionManager.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS")) {
            connection.setAutoCommit(false);
            try (PreparedStatement updateStmt = connection.prepareStatement(updateSql);
                 PreparedStatement removeStmt = connection.prepareStatement(removeSql)) {

                for (User dependent : dependentsToSave) {
                    updateStmt.setString(1, policyHolderId);
                    updateStmt.setString(2, dependent.getId());
                    updateStmt.addBatch();
                }


                for (User dependent : dependentsToRemove) {
                    removeStmt.setString(1, dependent.getId());
                    removeStmt.addBatch();
                }

                updateStmt.executeBatch();
                removeStmt.executeBatch();
                connection.commit();
                return true;
            } catch (SQLException e) {
                System.err.println("Error saving and removing dependents: " + e.getMessage());
                connection.rollback();
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Transaction error: " + e.getMessage());
            return false;
        }
    }

    public void updateDependent(User user) {
        String sql = "UPDATE users SET full_name = ?, address = ?, phone_number = ? WHERE id = ?";
        try (Connection connection = dbConnectionManager.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getAddress());
            pstmt.setString(3, user.getPhoneNumber());
            pstmt.setString(4, user.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updatePolicyHolder(User user) {
        String sql = "UPDATE users SET full_name = ?, address = ?, phone_number = ? WHERE username = ?";
        try (Connection connection = dbConnectionManager.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement pstmt = connection.prepareStatement(sql)) {



            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getAddress());
            pstmt.setString(3, user.getPhoneNumber());
            pstmt.setString(4, user.getId());

            int affectedRows = pstmt.executeUpdate();



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean verifyOldPassword(String userId, String oldPassword) {
        String sql = "SELECT password_hash FROM users WHERE username = ? AND password_hash = ?";
        try (Connection connection = dbConnectionManager.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, oldPassword);

            try (ResultSet resultSet = pstmt.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updatePassword(String userId, String newPassword) {
        String sql = "UPDATE users SET password_hash = ? WHERE username = ?";
        try (Connection connection = dbConnectionManager.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, newPassword);
            pstmt.setString(2, userId);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}




