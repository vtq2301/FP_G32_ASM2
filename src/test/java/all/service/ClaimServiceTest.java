package all.service;

import all.db.ConnectionPool;
import all.model.customer.ClaimManagement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.sql.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ClaimServiceTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private ClaimService claimService;

    @BeforeEach
    public void setUp() throws SQLException {
        // Set Mockito to lenient mode

        ConnectionPool connectionPool = new ConnectionPool(Collections.singletonList(connection));
        claimService = new ClaimService(connectionPool);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
    }

    @Test
    public void testFindAllBeneficiaryClaims() throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("id")).thenReturn("1");
        when(resultSet.getString("customer_id")).thenReturn("123");
        when(resultSet.getDate("claim_date")).thenReturn(new java.sql.Date(0));
        when(resultSet.getString("insured_person")).thenReturn("John Doe");
        when(resultSet.getDate("exam_date")).thenReturn(new java.sql.Date(0));
        when(resultSet.getArray("documents")).thenReturn(new MockArray(new String[]{"doc1", "doc2"}));
        when(resultSet.getDouble("claim_amount")).thenReturn(1000.0);
        when(resultSet.getString("receiver_banking_info")).thenReturn("Bank Info");
        when(resultSet.getString("status")).thenReturn("Accepted");

        List<ClaimManagement> claims = claimService.findAllBeneficiaryClaims("policyOwnerId");
        assertEquals(1, claims.size());
        assertEquals("1", claims.get(0).getId());
    }

    @Test
    public void testAddClaim() throws SQLException {
        when(preparedStatement.executeUpdate()).thenReturn(1);

        ClaimManagement claim = new ClaimManagement(
                "1", "123", new Date(0), "John Doe", new Date(0),
                new String[]{"doc1", "doc2"}, 1000.0, "Bank Info", "Accepted"
        );

        claimService.addClaim(claim);

        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testUpdateClaim() throws SQLException {
        when(preparedStatement.executeUpdate()).thenReturn(1);

        ClaimManagement claim = new ClaimManagement(
                "1", "123", new Date(0), "John Doe", new Date(0),
                new String[]{"doc1", "doc2"}, 1000.0, "Bank Info", "Accepted"
        );

        claimService.updateClaim(claim);

        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testDeleteClaim() throws SQLException {
        when(preparedStatement.executeUpdate()).thenReturn(0);

        claimService.deleteClaim("1");

        verify(preparedStatement, times(1)).executeUpdate();
    }

    // Mock Array class to simulate the Array SQL type
    private static class MockArray implements Array {
        private final String[] array;

        MockArray(String[] array) {
            this.array = array;
        }

        @Override
        public String getBaseTypeName() {
            return "VARCHAR";
        }

        @Override
        public int getBaseType() {
            return Types.VARCHAR;
        }

        @Override
        public Object getArray() {
            return array;
        }

        @Override
        public Object getArray(Map<String, Class<?>> map) {
            return array;
        }

        @Override
        public Object getArray(long index, int count) {
            String[] result = new String[count];
            System.arraycopy(array, (int) index, result, 0, count);
            return result;
        }

        @Override
        public Object getArray(long index, int count, Map<String, Class<?>> map) {
            return getArray(index, count);
        }

        @Override
        public ResultSet getResultSet() {
            return null;
        }

        @Override
        public ResultSet getResultSet(Map<String, Class<?>> map) {
            return null;
        }

        @Override
        public ResultSet getResultSet(long index, int count) {
            return null;
        }

        @Override
        public ResultSet getResultSet(long index, int count, Map<String, Class<?>> map) {
            return null;
        }

        @Override
        public void free() {
        }
    }
}
