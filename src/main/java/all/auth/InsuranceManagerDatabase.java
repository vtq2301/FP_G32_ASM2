/**
 @author GROUP 32
 - s3870729 - Tran Vu Nhat Tin
 - s3929202 - Vu Pham Nguyen Vu
 - s3914412 - Nguyen Duong Truong Thinh
 - s3981278 - Vu Tien Quang
 */
package all.auth;

import all.controller.UniqueIDGenerator;
import all.db.dbConnection;
import all.model.customer.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InsuranceManagerDatabase {
    private static final dbConnection dbConn = new dbConnection();
    public static List<User> getInsuranceManagerList() {
        List<User> insuranceManagerList = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = 'InsuranceManager'";
        try(Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
            PreparedStatement ps = conn.prepareStatement(query)){
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                insuranceManagerList.add(new User(
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
            return insuranceManagerList;
        } catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }
    public void addInsuranceManagers(User insuranceManager) {
        String query = "INSERT INTO users VALUES (?,?,?,?,?,?,?)";
        String id = UniqueIDGenerator.generateUniqueID(dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS"));
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1, id);
            ps.setString(2,insuranceManager.getUsername());
            ps.setString(3, insuranceManager.getPassword());
            ps.setString(4, "InsuranceManager");
            ps.setString(5, insuranceManager.getFullName());
            ps.setString(6, insuranceManager.getAddress());
            ps.setString(7, insuranceManager.getPhoneNumber());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                insuranceManager.setId(id);
            } else {
                throw new SQLException("Creating InsuranceManager failed, no rows affected.");
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateInsuranceManagers(User insuranceManager) {
        String sql = "UPDATE users SET username = ?," +
                " password_hash = ?, role = ?," +
                " full_name = ?," +
                " address = ?," +
                " phone_number = ? WHERE id = ?";
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, insuranceManager.getUsername());
            ps.setString(2, insuranceManager.getPassword());
            ps.setString(3, insuranceManager.getRole());
            ps.setString(4, insuranceManager.getFullName());
            ps.setString(5, insuranceManager.getAddress());
            ps.setString(6, insuranceManager.getPhoneNumber());
            ps.setString(7, insuranceManager.getId());
            if (ps.executeUpdate() == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteInsuranceManager(String id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            if (ps.executeUpdate() == 0) {
                throw new SQLException("Deletion failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
