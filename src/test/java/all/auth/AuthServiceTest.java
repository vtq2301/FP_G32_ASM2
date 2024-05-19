package all.auth;

import all.model.customer.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthService(mockConnection);
    }

    @Test
    void testRegisterUser_ValidUser_ReturnsTrue() throws SQLException {
        // Arrange
        String username = "test";
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // Simulate successful insertion

        // Act
        boolean result = authService.registerUser(username, "123", "PolicyHolder", "Policy Holder 1", "Address 1", "0900000001");

        // Assert
        assertTrue(result);
    }

    @Test
    void testAuthenticateUser_ValidCredentials_ReturnsUser() throws SQLException {
        // Arrange
        String username = "test";
        String password = "123";
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true); // Simulate user exists
        when(mockResultSet.getString("password_hash")).thenReturn("hashed_password");

        // Act
        User user = authService.authenticateUser(username, password);
        System.out.println(user.getUsername());

        // Assert
        assertNotNull(user);
        assertEquals(username, user.getUsername());
    }
}
