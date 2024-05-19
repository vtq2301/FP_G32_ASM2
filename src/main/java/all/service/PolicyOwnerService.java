package all.service;

import all.db.ConnectionPool;
import all.model.customer.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PolicyOwnerService {
    private final ConnectionPool connectionPool;

    // Default constructor using the singleton instance of ConnectionPool
    public PolicyOwnerService() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    // Constructor for dependency injection
    public PolicyOwnerService(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public int getRate(User user) {
        if (!Objects.equals(user.getRole(), "PolicyOwner")) {
            return 0;
        }

        String sql = "SELECT insurance_rate FROM insurance_rates WHERE policy_owner_id = ?";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("insurance_rate");
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return -1;
    }

    public List<User> findAllBeneficiaries(User user) {
        if (!Objects.equals(user.getRole(), "PolicyOwner")) {
            return List.of();
        }

        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE policy_owner_id = ?";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User foundUser = new User.Builder()
                            .setId(rs.getString("id"))
                            .setFullName(rs.getString("full_name"))
                            .setRole(rs.getString("role"))
                            .setPhoneNumber(rs.getString("phone_number"))
                            .setAddress(rs.getString("address"))
                            .setEmail(rs.getString("email"))
                            .setIsActive(rs.getBoolean("is_active"))
                            .build();
                    users.add(foundUser);
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return users;
    }

    public void addBeneficiary(User user, String policyOwnerId) {
        String sql = "INSERT INTO users (id, full_name, role, phone, address, email, is_active, policy_owner_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getId());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getRole());
            ps.setString(4, user.getPhoneNumber());
            ps.setString(5, user.getAddress());
            ps.setString(6, user.getEmail());
            ps.setBoolean(7, user.isActive());
            ps.setString(8, policyOwnerId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void updateBeneficiary(User user) {
        String sql = "UPDATE users SET full_name = ?, role = ?, phone = ?, address = ?, email = ?, is_active = ? WHERE id = ?";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getRole());
            ps.setString(3, user.getPhoneNumber());
            ps.setString(4, user.getAddress());
            ps.setString(5, user.getEmail());
            ps.setBoolean(6, user.isActive());
            ps.setString(7, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void removeBeneficiary(String userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
