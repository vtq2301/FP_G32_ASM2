package rmit.fp.g32_asm2.auth;

import rmit.fp.g32_asm2.database.dbConnection;
import rmit.fp.g32_asm2.model.provider.InsuranceManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InsuranceManagerDatabase {
    private static final dbConnection dbConn = new dbConnection();
    public static List<InsuranceManager> getInsuranceManagerList() {
        List<InsuranceManager> insuranceManagerList = new ArrayList<>();
        String query = "SELECT * FROM insurance_managers";
        try(Connection conn = dbConn.connection_to_db("postgres", "postgres.orimpphhrfwkilebxiki", "RXj1sf5He5ORnrjS");
            PreparedStatement ps = conn.prepareStatement(query)){
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                insuranceManagerList.add(new InsuranceManager(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("email")));
            };
            return insuranceManagerList;
        } catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }
}
