package rmit.fp.g32_asm2.DAO;

import rmit.fp.g32_asm2.database.ConnectionPool;
import rmit.fp.g32_asm2.model.Claim.Claim;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractDAO<T> {
    protected String table;
    protected List<String> columns;

    public AbstractDAO(String table, List<String> columns) {
        this.table = table;
        this.columns = columns;
    }

    public List<String> getColumns(){
        return columns;
    };

    public abstract T createObjectFromResultSet(ResultSet rs) throws SQLException;
    protected abstract Object getId(T object);
    protected abstract void setUpdateStatementParameters(PreparedStatement ps, T object) throws SQLException;
    protected abstract void setSaveStatementParameters(PreparedStatement ps, T object, List<String> columns) throws SQLException;

    public T findOne(String columnName, Object value) {
        if (!columns.contains(columnName) || value == null) {
            return null;
        }

        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();

            String sql = "SELECT * FROM " + table + " WHERE " + columnName + " = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setObject(1, value);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return createObjectFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if (conn != null) {
                ConnectionPool.getInstance().releaseConnection(conn);
            }
        }
        return null;
    }

    public List<T> findMany(Map<String, Object> params, String orderByColumnName, Order order, int limit, int offset) {
        if (params == null || params.isEmpty()) {
            throw new IllegalArgumentException("Parameters map is null or empty");
        }

        Connection conn = null;
        List<T> results = new ArrayList<>();

        try {
            conn = ConnectionPool.getInstance().getConnection();

            StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM " + table + " WHERE ");
            List<Object> values = new ArrayList<>();

            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String columnName = entry.getKey();
                Object value = entry.getValue();

                if (!columns.contains(columnName)) {
                    throw new IllegalArgumentException("Invalid column name: " + columnName);
                }

                sqlBuilder.append(columnName).append(" = ? AND ");
                values.add(value);
            }

            sqlBuilder.delete(sqlBuilder.length() - 5, sqlBuilder.length());
            sqlBuilder.append(" ORDER BY ").append(orderByColumnName).append(" ").append(order.toString());
            sqlBuilder.append(" LIMIT ? OFFSET ?");
            System.out.println(sqlBuilder.toString());

            try (PreparedStatement ps = conn.prepareStatement(sqlBuilder.toString())) {
                int i = 1;
                for (Object value : values) {
                    ps.setObject(i++, value);
                }
                ps.setInt(i++, limit);
                ps.setInt(i, offset);

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    results.add(createObjectFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if (conn != null) {
                ConnectionPool.getInstance().releaseConnection(conn);
            }
        }

        return results;
    }

    public List<T> findAll(String orderByColumnName, Order order, int limit, int offset) {
        Connection conn = null;
        List<T> results = new ArrayList<>();
        try {
            conn = ConnectionPool.getInstance().getConnection();
            String sql = "SELECT * FROM " + table + " ORDER BY " + orderByColumnName + " " + order + " LIMIT ? OFFSET ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, limit);
                ps.setInt(2, offset);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    results.add(createObjectFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if (conn != null) {
                ConnectionPool.getInstance().releaseConnection(conn);
            }
        }
        return results;
    }

    public boolean save(T object) {
        if (object == null) {
            return false; // or throw IllegalArgumentException
        }

        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ").append(table).append(" (");

            List<String> columnsToInsert = new ArrayList<>(columns);
            Object id = getId(object);

            if (id == null) {
                columnsToInsert.remove("id");
            }

            for (String column : columnsToInsert) {
                sqlBuilder.append(column).append(", ");
            }

            sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length());
            sqlBuilder.append(") VALUES (");

            for (int i = 0; i < columnsToInsert.size(); i++) {
                sqlBuilder.append("?, ");
            }

            sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length());
            sqlBuilder.append(")");
            System.out.println(sqlBuilder);

            try (PreparedStatement ps = conn.prepareStatement(sqlBuilder.toString())) {
                setSaveStatementParameters(ps, object, columnsToInsert);
                int rowsAffected = ps.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                ConnectionPool.getInstance().releaseConnection(conn);
            }
        }
    }

    public boolean update(Optional<T> optionalObject) {
        if (optionalObject.isEmpty()) {
            return false; // or throw IllegalArgumentException
        }

        T object = optionalObject.get();

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConnectionPool.getInstance().getConnection();

            StringBuilder sqlBuilder = new StringBuilder("UPDATE ").append(table).append(" SET ");

            for (String columnName : columns) {
                if (!columnName.equalsIgnoreCase("id")) {
                    sqlBuilder.append(columnName).append(" = ?, ");
                }
            }

            sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length());
            sqlBuilder.append(" WHERE id = ?");
            System.out.println(sqlBuilder);

            ps = conn.prepareStatement(sqlBuilder.toString());

            setUpdateStatementParameters(ps, object);

            System.out.println(ps);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    System.err.println("Error closing PreparedStatement: " + e.getMessage());
                }
            }
            if (conn != null) {
                ConnectionPool.getInstance().releaseConnection(conn);
            }
        }
    }

    public boolean delete(String id) {
        if (id == null || id.isEmpty()) return false;
        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            String sql = "DELETE FROM " + table + " WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setString(1, id);
                int rowsAffected = ps.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                ConnectionPool.getInstance().releaseConnection(conn);
            }
        }
    }

}
