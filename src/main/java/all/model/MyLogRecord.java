/**
 @author GROUP 32
 - s3870729 - Tran Vu Nhat Tin
 - s3929202 - Vu Pham Nguyen Vu
 - s3914412 - Nguyen Duong Truong Thinh
 - s3981278 - Vu Tien Quang
 */
package all.model;

import java.util.Date;

public class MyLogRecord {
    private String id;
    private String actionType;
    private String actionDescription;
    private Date timestamps;
    private String username;
    private String claimId;

    public MyLogRecord(String id, String actionType, String actionDescription, Date timestamps, String username, String claimId) {
        this.id = id;
        this.actionType = actionType;
        this.actionDescription = actionDescription;
        this.timestamps = timestamps;
        this.username = username;
        this.claimId = claimId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
    }

    public Date getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(Date timestamps) {
        this.timestamps = timestamps;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClaimId() {
        return claimId;
    }

    public void setClaimId(String claimId) {
        this.claimId = claimId;
    }
}
