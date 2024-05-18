package all.controller.customer;

import all.auth.AuthService;
import all.controller.UserSession;
import all.model.customer.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;

import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PolicyOwnerTest extends ApplicationTest {

    private PolicyOwner controller;
    private static User mockUser;

    @BeforeAll
    public static void setUp() {
        mockUser = new User.Builder()
                .setId("1")
                .setUsername("policyOwner")
                .setFullName("John Doe")
                .setEmail("john.doe@example.com")
                .setPhoneNumber("1234567890")
                .setRole("PolicyOwner")
                .build();
        UserSession.login(mockUser);
    }

    @Start
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PolicyOwnerScreen.fxml"));
        Parent mainNode = loader.load();
        controller = loader.getController();
        controller.loadData(mockUser);
        Scene scene = new Scene(mainNode);
        stage.setScene(scene);
        stage.show();
    }



    @Test
    public void testLoadData(FxRobot robot) {
        TextField fullNameField = robot.lookup("#fullNameField").queryAs(TextField.class);
        TextField emailField = robot.lookup("#emailField").queryAs(TextField.class);
        TextField contactNumberField = robot.lookup("#contactNumberField").queryAs(TextField.class);
        TextField policyNumberField = robot.lookup("#policyNumberField").queryAs(TextField.class);

        robot.interact(() -> controller.loadData(mockUser));

        assert fullNameField.getText().equals(mockUser.getFullName());
        assert emailField.getText().equals(mockUser.getEmail());
        assert contactNumberField.getText().equals(mockUser.getPhoneNumber());
        assert policyNumberField.getText().equals("POLICY123456");
    }

    @Test
    public void testShowClaims(FxRobot robot) {
        VBox mainContent = robot.lookup("#mainContent").queryAs(VBox.class);
        Button claimsButton = robot.lookup("#claimsButton").queryAs(Button.class);

        robot.clickOn(claimsButton);

        assert !mainContent.getChildren().isEmpty();
        assert mainContent.getChildren().get(0).getId().equals("claimsView");
    }

    @Test
    public void testShowBeneficiaries(FxRobot robot) {
        VBox mainContent = robot.lookup("#mainContent").queryAs(VBox.class);
        Button beneficiariesButton = robot.lookup("#beneficiariesButton").queryAs(Button.class);

        robot.clickOn(beneficiariesButton);

        assert !mainContent.getChildren().isEmpty();
        assert mainContent.getChildren().getFirst().getId().equals("beneficiariesView");
    }

    @Test
    public void testShowLogs(FxRobot robot) {
        VBox mainContent = robot.lookup("#mainContent").queryAs(VBox.class);
        Button logsButton = robot.lookup("#logsButton").queryAs(Button.class);

        robot.clickOn(logsButton);

        assert !mainContent.getChildren().isEmpty();
        assert mainContent.getChildren().get(0).getId().equals("logsView");
    }

}
