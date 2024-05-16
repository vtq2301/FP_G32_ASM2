package rmit.fp.g32_asm2.model.customer;

public class CustomerRelations {
    private String userId;
    private int roleId;
    private String policyHolderId;
    private String policyOwnerId;
    private double insuranceRate;

    public CustomerRelations() {}
    public CustomerRelations(String userId, int roleId, String policyHolderId, String policyOwnerId, double insuranceRate) {
        this.userId = userId;
        this.roleId = roleId;
        this.policyHolderId = policyHolderId;
        this.policyOwnerId = policyOwnerId;
        this.insuranceRate = insuranceRate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getPolicyHolderId() {
        return policyHolderId;
    }

    public void setPolicyHolderId(String policyHolderId) {
        this.policyHolderId = policyHolderId;
    }

    public String getPolicyOwnerId() {
        return policyOwnerId;
    }

    public void setPolicyOwnerId(String policyOwnerId) {
        this.policyOwnerId = policyOwnerId;
    }

    public double getInsuranceRate() {
        return insuranceRate;
    }

    public void setInsuranceRate(double insuranceRate) {
        this.insuranceRate = insuranceRate;
    }

    @Override
    public String toString() {
        return "CustomerRelations{" +
                "userId='" + userId + '\'' +
                ", roleId=" + roleId +
                ", policyHolderId='" + policyHolderId + '\'' +
                ", policyOwnerId='" + policyOwnerId + '\'' +
                ", insuranceRate=" + insuranceRate +
                '}';
    }
}
