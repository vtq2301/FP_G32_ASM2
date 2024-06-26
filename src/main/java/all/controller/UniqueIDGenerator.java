/**
 @author GROUP 32
 - s3870729 - Tran Vu Nhat Tin
 - s3929202 - Vu Pham Nguyen Vu
 - s3914412 - Nguyen Duong Truong Thinh
 - s3981278 - Vu Tien Quang
 */
package all.controller;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class UniqueIDGenerator {
    private static final SecureRandom random = new SecureRandom();


    public static String generateUniqueID(Connection connection) {
        String generatedID;
        do {
            long randomNumber = Math.abs(new Random().nextLong()) % 10_000_000L;
            generatedID = "c-" + String.format("%07d", randomNumber);
        } while (isIDExists(generatedID, connection));
        return generatedID;
    }

    private static boolean isIDExists(String id, Connection connection) {
        String query = "SELECT 1 FROM users WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
