package rmit.fp.g32_asm2.auth;

import rmit.fp.g32_asm2.controller.UniqueIDGenerator;
import rmit.fp.g32_asm2.database.dbConnection;
import rmit.fp.g32_asm2.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PolicyOwnerDatabase {
    private static final dbConnection dbConn = new dbConnection();
    public static List<User> getUserList() {
        List<User> policyHoldersList = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role  = 'PolicyOwner' ";
        try(Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
            PreparedStatement ps = conn.prepareStatement(query)){
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                policyHoldersList.add(new User(
                        rs.getString("id"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getString("role"),
                        rs.getString("full_name"),
                        rs.getString("address"),
                        rs.getString("phone_number"),
                        rs.getString("policy_holder_id")
                ));
            };
            return policyHoldersList;
        } catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }
    public void addPolicyOwners(User policyOwner){
        String query = "INSERT INTO users VALUES (?,?,?,?,?,?,?)";
        String id = UniqueIDGenerator.generateUniqueID(dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS"));
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1, id);
            ps.setString(2, policyOwner.getUsername());
            ps.setString(3, policyOwner.getPassword());
            ps.setString(4, "PolicyOwner");
            ps.setString(5, policyOwner.getFullName());
            ps.setString(6, policyOwner.getAddress());
            ps.setString(7, policyOwner.getPhoneNumber());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                policyOwner.setId(id);
            } else {
                throw new SQLException("Creating policy Owner failed, no rows affected.");
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updatePolicyOwner(User policyOwner) {
        String sql = "UPDATE users SET username = ?," +
                " password_hash = ?, role = ?," +
                " full_name = ?," +
                " address = ?," +
                " phone_number = ? WHERE id = ?";
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, policyOwner.getUsername());
            ps.setString(2, policyOwner.getPassword());
            ps.setString(3, policyOwner.getRole());
            ps.setString(4, policyOwner.getFullName());
            ps.setString(5, policyOwner.getAddress());
            ps.setString(6, policyOwner.getPhoneNumber());
            ps.setString(7, policyOwner.getId());
            if (ps.executeUpdate() == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deletePolicyOwner(String id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,id);
            if (ps.executeUpdate() == 0) {
                throw new SQLException("Deletion failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
