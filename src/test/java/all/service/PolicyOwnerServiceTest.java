package all.service;

import all.db.ConnectionPool;
import all.model.customer.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PolicyOwnerServiceTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private PolicyOwnerService policyOwnerService;

    @BeforeEach
    public void setUp() throws SQLException {
        ConnectionPool connectionPool = new ConnectionPool(Collections.singletonList(connection));
        policyOwnerService = new PolicyOwnerService(connectionPool);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
    }

    @Test
    public void testGetRate() throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("insurance_rate")).thenReturn(100);

        User user = new User.Builder()
                .setUsername("policyOwner")
                .setRole("PolicyOwner")
                .build();

        int rate = policyOwnerService.getRate(user);
        assertEquals(100, rate);
    }

    @Test
    public void testFindAllBeneficiaries() throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("id")).thenReturn("1");
        when(resultSet.getString("full_name")).thenReturn("John Doe");
        when(resultSet.getString("role")).thenReturn("Dependent");
        when(resultSet.getString("phone_number")).thenReturn("1234567890");
        when(resultSet.getString("address")).thenReturn("123 Street");
        when(resultSet.getString("email")).thenReturn("john@example.com");
        when(resultSet.getBoolean("is_active")).thenReturn(true);

        User user = new User.Builder()
                .setUsername("policyOwner")
                .setRole("PolicyOwner")
                .build();

        List<User> beneficiaries = policyOwnerService.findAllBeneficiaries(user);
        assertEquals(1, beneficiaries.size());
        assertEquals("1", beneficiaries.get(0).getId());
    }

    @Test
    public void testAddBeneficiary() throws SQLException {
        when(preparedStatement.executeUpdate()).thenReturn(1);

        User user = new User.Builder()
                .setId("1")
                .setFullName("John Doe")
                .setRole("Dependent")
                .setPhoneNumber("1234567890")
                .setAddress("123 Street")
                .setEmail("john@example.com")
                .setIsActive(true)
                .build();

        policyOwnerService.addBeneficiary(user, "policyOwner");

        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testUpdateBeneficiary() throws SQLException {
        when(preparedStatement.executeUpdate()).thenReturn(1);

        User user = new User.Builder()
                .setId("1")
                .setFullName("John Doe")
                .setRole("Dependent")
                .setPhoneNumber("1234567890")
                .setAddress("123 Street")
                .setEmail("john@example.com")
                .setIsActive(true)
                .build();

        policyOwnerService.updateBeneficiary(user);

        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testRemoveBeneficiary() throws SQLException {
        when(preparedStatement.executeUpdate()).thenReturn(1);

        policyOwnerService.removeBeneficiary("1");

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
