package rmit.fp.g32_asm2.DAO;

import rmit.fp.g32_asm2.model.User;
import rmit.fp.g32_asm2.model.UserFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class UserDAO extends AbstractDAO<User> {

    private static final List<String> userColumns = List.of("id", "username", "hash_password", "name", "phone", "email", "address", "role_id", "is_active");

    public UserDAO() {
        super("users", userColumns);
    }

    @Override
    public List<String> getColumns() {
        return userColumns;
    }

    @Override
    public User createObjectFromResultSet(ResultSet rs) throws SQLException {
        User user = new User.Builder()
                .withId(rs.getString("id"))
                .withUsername(rs.getString("username"))
                .withRoleId(rs.getInt("role_id"))
                .withHashPassword(rs.getString("hash_password"))
                .withName(rs.getString("name"))
                .withPhoneNumber(rs.getString("phone"))
                .withEmail(rs.getString("email"))
                .withAddress(rs.getString("address"))
                .withIsActive(rs.getBoolean("is_active"))
                .build();

        return UserFactory.createUser(user);
    }

    @Override
    protected Object getId(User user) {
        return user.getId();
    }

    @Override
    protected void setSaveStatementParameters(PreparedStatement ps, User user, List<String> columns) throws SQLException {
        int i = 1;
        if (columns.contains("id")) {
            ps.setString(i++, user.getId());
        }
        ps.setString(i++, user.getUsername());
        ps.setString(i++, user.getHashPassword());
        ps.setString(i++, user.getName());
        ps.setString(i++, user.getPhoneNumber());
        ps.setString(i++, user.getEmail());
        ps.setString(i++, user.getAddress());
        ps.setInt(i++, user.getRoleId());
        ps.setBoolean(i, user.isActive());
    }

    @Override
    protected void setUpdateStatementParameters(PreparedStatement ps, User user) throws SQLException {
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getHashPassword());
        ps.setString(3, user.getName());
        ps.setString(4, user.getPhoneNumber());
        ps.setString(5, user.getEmail());
        ps.setString(6, user.getAddress());
        ps.setInt(7, user.getRoleId());
        ps.setString(8, user.getId());
    }

    public User findById(String id) {
        return findOne("id", UUID.fromString(id));
    }
}
