/**
 @author GROUP 32
 - s3870729 - Tran Vu Nhat Tin
 - s3929202 - Vu Pham Nguyen Vu
 - s3914412 - Nguyen Duong Truong Thinh
 - s3981278 - Vu Tien Quang
 */
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

    public UserAction() {
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
