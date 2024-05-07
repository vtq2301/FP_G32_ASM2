package all.model.customer;

public class ClaimManagement {
    private String id;
    private String customerId;  // Remains updated from Integer to String
    private String description;
    private String status;

    public ClaimManagement(String id, String customerId, String description, String status) {
        this.id = id;
        this.customerId = customerId;
        this.description = description;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    // Updated to accept String as the parameter type
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
