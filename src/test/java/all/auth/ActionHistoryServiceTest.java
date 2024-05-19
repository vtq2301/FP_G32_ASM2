package all.auth;

import all.model.UserAction;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ActionHistoryServiceTest {
    private ActionHistoryService actionHistoryService;

    @Before
    public void setUp() {
        actionHistoryService = new ActionHistoryService();
    }

    @Test
    public void testGetUserActions() {

        String username = "policy_holder_1";

        // Call the method to retrieve user actions
        List<UserAction> actions = actionHistoryService.getUserActions(username);

        // Add your assertions here based on expected behavior
        assertEquals(11, actions.size());
        // Verify other properties of UserAction objects if needed

        UserAction firstAction = actions.get(0);
        assertEquals("Login", firstAction.getActionType());
        assertEquals("User logged in", firstAction.getActionDescription());
    }
}
