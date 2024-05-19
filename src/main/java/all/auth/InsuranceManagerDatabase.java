package all.auth;

import all.db.dbConnection;
import all.model.customer.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InsuranceManagerDatabase {
    private static final dbConnection dbConn = new dbConnection();
    public static List<User> getInsuranceManagerList() {
        List<User> insuranceManagerList = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = 'InsuranceManager'";
        try(Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
            PreparedStatement ps = conn.prepareStatement(query)){
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                insuranceManagerList.add(new User(
                        rs.getString("id"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getString("role"),
                        rs.getString("full_name"),
                        rs.getString("address"),
                        rs.getString("phone_number"),
                        rs.getString("policy_holder_id")
                ));
            };
            return insuranceManagerList;
        } catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }
}
