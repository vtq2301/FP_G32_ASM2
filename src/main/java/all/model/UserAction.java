package all.model;

import java.time.LocalDateTime;

public class UserAction {
    private String actionType;
    private String actionDescription;
    private LocalDateTime timestamp;

    public UserAction(String actionType, String actionDescription, LocalDateTime timestamp) {
        this.actionType = actionType;
        this.actionDescription = actionDescription;
        this.timestamp = timestamp;
    }

    // Getters
    public String getActionType() {
        return actionType;
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
