package rmit.fp.g32_asm2.DAO;

import rmit.fp.g32_asm2.model.customer.CustomerRelations;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class CustomerDAO extends AbstractDAO<CustomerRelations> {

    private static final List<String> customerColumns = List.of(
            "user_id", "role_id", "policy_holder_id", "policy_owner_id", "insurance_rate"
    );

    public CustomerDAO() {
        super("customers", customerColumns);
    }

    @Override
    public List<String> getColumns() {
        return customerColumns;
    }

    @Override
    protected Object getId(CustomerRelations object) {
        return object.getUserId();
    }

    @Override
    public CustomerRelations createObjectFromResultSet(ResultSet rs) throws SQLException {
        return new CustomerRelations(
                rs.getString("user_id"),
                rs.getInt("role_id"),
                rs.getString("policy_holder_id"),
                rs.getString("policy_owner_id"),
                rs.getDouble("insurance_rate")
        );
    }

    @Override
    protected void setSaveStatementParameters(PreparedStatement ps, CustomerRelations object, List<String> columns) throws SQLException {
        int i = 1;
        if (columns.contains("user_id")){
            if (object.getUserId() != null) {
                ps.setObject(i++, UUID.fromString(object.getUserId()));
            } else {
                ps.setObject(i++, null);
            }
        }
        ps.setInt(i++, object.getRoleId());
        if (columns.contains("policy_holder_id")){
            if (object.getPolicyHolderId() != null) {
                ps.setObject(i++, UUID.fromString(object.getPolicyHolderId()));
            } else {
                ps.setObject(i++, null);
            }
        }
        if (columns.contains("policy_owner_id")){
            if (object.getPolicyOwnerId() != null) {
                ps.setObject(i++, UUID.fromString(object.getPolicyOwnerId()));
            } else {
                ps.setObject(i++, null);
            }
        }
        ps.setDouble(i, object.getInsuranceRate());
        System.out.println(ps);
    }


    @Override
    protected void setUpdateStatementParameters(PreparedStatement ps, CustomerRelations object) throws SQLException {
        ps.setInt(1, object.getRoleId());
        ps.setString(2, object.getPolicyHolderId());
        ps.setString(3, object.getPolicyOwnerId());
        ps.setDouble(4, object.getInsuranceRate());
        ps.setString(5, object.getUserId());
    }
}
