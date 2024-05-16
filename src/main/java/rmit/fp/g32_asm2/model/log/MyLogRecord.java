package rmit.fp.g32_asm2.model.log;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class MyLogRecord extends LogRecord {
    private final String id;
    private final String userId;
    private final Date createdAt;

    public MyLogRecord(String id, String userId, String message, Level level, Date createdAt) {
        super(level, message);
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public MyLogRecord(String userId, String message, Level level) {
        super(level, message);
        this.id = null;
        this.userId = userId;
        this.createdAt = new Date();
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

}
