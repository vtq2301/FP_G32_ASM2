package all.auth;

import all.db.dbConnection;
import all.model.customer.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PolicyOwnerDatabaseTest {

    @Mock
    private dbConnection dbConn;

    @Mock
    private Connection conn;

    @Mock
    private PreparedStatement ps;

    @Mock
    private ResultSet rs;

    @InjectMocks
    private PolicyOwnerDatabase policyOwnerDatabase;

    @BeforeEach
    public void setup() throws SQLException {
        when(dbConn.connection_to_db(anyString(), anyString(), anyString())).thenReturn(conn);
        when(conn.prepareStatement(anyString())).thenReturn(ps);
    }

    @Test
    public void testGetUserList() throws SQLException {
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getString("id")).thenReturn("1");
        when(rs.getString("username")).thenReturn("testUser");
        when(rs.getString("password_hash")).thenReturn("password");
        when(rs.getString("role")).thenReturn("PolicyOwner");
        when(rs.getString("full_name")).thenReturn("Test User");
        when(rs.getString("address")).thenReturn("Test Address");
        when(rs.getString("phone_number")).thenReturn("123456789");
        when(rs.getString("policy_holder_id")).thenReturn(null);

        List<User> users = policyOwnerDatabase.getUserList();
        assertEquals(1, users.size());
        assertEquals("testUser", users.get(0).getUsername());
    }

    @Test
    public void testAddPolicyOwner() throws SQLException {
        User user = new User(null, "testUser", "password", "PolicyOwner", "Test User", "Test Address", "123456789", null);
        when(ps.executeUpdate()).thenReturn(1);

        policyOwnerDatabase.addPolicyOwners(user);

        verify(ps, times(1)).executeUpdate();
        assertNotNull(user.getId());
    }

    @Test
    public void testUpdatePolicyOwner() throws SQLException {
        User user = new User("1", "testUser", "password", "PolicyOwner", "Test User", "Test Address", "123456789", null);
        when(ps.executeUpdate()).thenReturn(1);

        policyOwnerDatabase.updatePolicyOwner(user);

        verify(ps, times(1)).executeUpdate();
    }

    @Test
    public void testDeletePolicyOwner() throws SQLException {
        when(ps.executeUpdate()).thenReturn(1);

        policyOwnerDatabase.deletePolicyOwner("1");

        verify(ps, times(1)).executeUpdate();
    }
}
