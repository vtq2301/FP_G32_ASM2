package rmit.fp.g32_asm2.DAO;

import org.postgresql.util.PGobject;
import rmit.fp.g32_asm2.database.ConnectionPool;
import rmit.fp.g32_asm2.model.Claim.Claim;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ClaimDAO extends AbstractDAO<Claim> {
    private static final List<String> columns = List.of(
            "id", "insured_person_id", "amount",
            "documents", "exam_date", "claim_status"
    );

    public ClaimDAO() {
        super("claims", columns);
    }

    @Override
    public Claim createObjectFromResultSet(ResultSet rs) throws SQLException {
        Array documentsArr = rs.getArray("documents");
        String[] documentsArr2 = (String[]) documentsArr.getArray();

        return new Claim(
                rs.getString("id"),
                rs.getString("insured_person_id"),
                rs.getDate("exam_date"),
                Arrays.asList(documentsArr2),
                rs.getDouble("amount"),
                Claim.Status.valueOf(rs.getString("claim_status"))
        );
    }

    @Override
    public List<String> getColumns() {
        return columns;
    }

    @Override
    protected Object getId(Claim object) {
        return object.getId();
    }

    public Claim findById(String id) {
        return super.findOne("id", UUID.fromString(id));
    }


    @Override
    protected void setSaveStatementParameters(PreparedStatement ps, Claim object, List<String> columns) throws SQLException {
        int i = 1;
        if (columns.contains("id")) {
            ps.setString(i++, object.getId());
        }

        ps.setObject(i++, UUID.fromString(object.getInsuredPersonId()));
        ps.setDouble(i++, object.getAmount());
        ps.setArray(i++, ConnectionPool.getInstance().getConnection().createArrayOf("text", object.getDocuments().toArray()));
        ps.setDate(i++, new java.sql.Date(object.getExamDate().getTime()));

        PGobject statusObject = new PGobject();
        statusObject.setType("claim_status");
        statusObject.setValue(object.getStatus().name());
        ps.setObject(i, statusObject);
    }

    @Override
    protected void setUpdateStatementParameters(PreparedStatement ps, Claim object) throws SQLException {
        int i = 1;
        ps.setObject(i++, UUID.fromString(object.getInsuredPersonId()));
        ps.setDouble(i++, object.getAmount());
        ps.setArray(i++, ConnectionPool.getInstance().getConnection().createArrayOf("text", object.getDocuments().toArray()));
        ps.setDate(i++, new java.sql.Date(object.getExamDate().getTime()));
        PGobject statusObject = new PGobject();
        statusObject.setType("claim_status");
        statusObject.setValue(object.getStatus().name());
        ps.setObject(i++, statusObject);
        ps.setString(i, object.getId());
    }
}
