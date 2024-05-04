package all.model.customer;

public class ClaimManagement {
    private Integer id;
    private Integer customerId;
    private String description;
    private String status;

    public ClaimManagement(Integer id, Integer customerId, String description, String status) {
        this.id = id;
        this.customerId = customerId;
        this.description = description;
        this.status = status;
    }


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
