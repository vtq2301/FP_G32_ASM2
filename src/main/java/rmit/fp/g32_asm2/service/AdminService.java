package rmit.fp.g32_asm2.service;

import rmit.fp.g32_asm2.DAO.Order;
import rmit.fp.g32_asm2.DAO.UserDAO;
import rmit.fp.g32_asm2.model.User;
import rmit.fp.g32_asm2.util.HashUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AdminService {
    private final UserDAO userDAO = new UserDAO();

    public boolean addUser(User user) {
        return userDAO.save(user);
    }

    public boolean updateUser(User user) {
        return userDAO.update(Optional.ofNullable(user));
    }

    public boolean deleteUser(String userId) {
        return userDAO.delete(userId);
    }

    public User getUserById(String userId) {
        return userDAO.findById(userId);
    }

    public List<User> getAllUsers() {
        return userDAO.findAll("created_at", Order.ASC, 20, 0);
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("hash_password", HashUtils.hashPassword(password));
        List<User> users = userDAO.findMany(params, "created_at", Order.ASC, 20, 0);
        if (users.size() == 1) {
            return users.getFirst();
        }
        return null;
    }

    public User getUserByEmailAndPassword(String email, String password) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        params.put("hash_password", HashUtils.hashPassword(password));
        List<User> users = userDAO.findMany(params, "created_at", Order.ASC, 20, 0);
        if (users.size() == 1) {
            return users.getFirst();
        }
        return null;
    }

}
