package rmit.fp.g32_asm2.DAO;

import rmit.fp.g32_asm2.model.log.MyLogRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

public class LoggerDAO extends AbstractDAO<MyLogRecord> {

    private static final List<String> logColumns = List.of("id", "user_id", "severity", "description", "created_at");

    public LoggerDAO() {
        super("logs", logColumns);
    }

    @Override
    public List<String> getColumns() {
        return logColumns;
    }

    @Override
    protected Object getId(MyLogRecord log) {
        return log.getId();
    }

    @Override
    public MyLogRecord createObjectFromResultSet(ResultSet rs) throws SQLException {
        return new MyLogRecord(
                rs.getString("id"),
                rs.getString("user_id"),
                rs.getString("description"),
                Level.parse(rs.getString("severity")),
                rs.getDate("created_at")
        );
    }

    @Override
    protected void setSaveStatementParameters(PreparedStatement ps, MyLogRecord log, List<String> columns) throws SQLException {
        int i = 1;
        if (columns.contains("id")){
            ps.setString(i++, log.getId());
        }
        ps.setString(i++, log.getUserId());
        ps.setString(i++, log.getLevel().toString());
        ps.setString(i++, log.getMessage());
        ps.setDate(i, new java.sql.Date(log.getCreatedAt().getTime()));
    }

    @Override
    protected void setUpdateStatementParameters(PreparedStatement ps, MyLogRecord log) throws SQLException {
        ps.setString(1, log.getUserId());
        ps.setString(2, log.getLevel().toString());
        ps.setString(3, log.getMessage());
        ps.setDate(4, new java.sql.Date(log.getCreatedAt().getTime()));
        ps.setString(5, log.getId());
    }
}
