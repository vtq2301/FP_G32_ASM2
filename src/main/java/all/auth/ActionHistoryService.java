    package all.auth;

    import all.db.ConnectionPool;
    import all.db.dbConnection;
    import all.model.UserAction;

    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.List;

    public class ActionHistoryService {
        private final dbConnection dbConn = new dbConnection();
        public List<UserAction> getUserActions(String userId) {
            List<UserAction> actions = new ArrayList<>();
            String sql = "SELECT action_type, action_description, timestamp FROM user_actions WHERE username = ? ORDER BY timestamp DESC";
            try (Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, userId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        String actionType = rs.getString("action_type");
                        String description = rs.getString("action_description");
                        LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();
                        actions.add(new UserAction(actionType, description, timestamp));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return actions;
        }
    }
