package rmit.fp.g32_asm2.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        String url = "jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:5432/postgres";
        String user = "postgres.jxsirtahqsbolnobcemf";
        String password = "thichthichoi!123";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to the PostgreSQL database.");

            String query = "SELECT * FROM testing_1";
            try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
                    while(rs.next()) {
                        System.out.println("Column 1: " + rs.getString("column 1"));;

                    }
            }
        } catch (Exception e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
    }
}