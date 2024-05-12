package rmit.fp.g32_asm2.model.customer;

public class ClaimManagement {
    private String id;
    private String customerId;
    private String description;
    private String status;

    public ClaimManagement(String id, String customerId, String description, String status) {
        this.id = id;
        this.customerId = customerId;
        this.description = description;
        this.status = status;
    }

    public ClaimManagement() {
    }

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
