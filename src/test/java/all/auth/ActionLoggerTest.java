package all.auth;

import all.auth.ActionLogger;
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

class ActionLoggerTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    private ActionLogger actionLogger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        actionLogger = new ActionLogger(mockConnection);
    }

    @Test
    void testIsUsernameValid_ValidUsername_ReturnsTrue() throws SQLException {
        // Arrange
        String validUsername = "policy_holder_1";
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true); // Simulate a valid username

        // Act
        boolean result = actionLogger.isUsernameValid(validUsername);

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsUsernameValid_InvalidUsername_ReturnsFalse() throws SQLException {
        // Arrange
        String invalidUsername = "nonexistent_user";
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // Simulate an invalid username

        // Act
        boolean result = actionLogger.isUsernameValid(invalidUsername);

        // Assert
        assertFalse(result);
    }
}
