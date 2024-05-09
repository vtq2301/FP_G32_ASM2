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
    private dbConnection dbConnectionManager = new dbConnection();

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

    public boolean saveDependents(List<User> dependents, String policyHolderId) {
        String sql = "UPDATE users SET policy_holder_id = ? WHERE id = ?;";
        try (Connection connection = dbConnectionManager.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS")) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                for (User dependent : dependents) {
                    preparedStatement.setString(1, policyHolderId);
                    preparedStatement.setString(2, dependent.getId());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
                connection.commit();
                return true;
            } catch (SQLException e) {
                System.err.println("Error saving dependents: " + e.getMessage());
                connection.rollback();
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Transaction error: " + e.getMessage());
            return false;
        }
    }
}
