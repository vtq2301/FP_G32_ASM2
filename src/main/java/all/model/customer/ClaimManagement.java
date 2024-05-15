package all.model.customer;

import java.util.Date;

public class ClaimManagement {
    private String id;
    private String customerId;
    private Date claimDate;
    private String insuredPerson;
    private Date examDate;
    private String[] documents;
    private double claimAmount;
    private String receiverBankingInfo;
    private String status;

    public ClaimManagement(String id, String customerId, Date claimDate, String insuredPerson, Date examDate, String[] documents, double claimAmount, String receiverBankingInfo, String status) {
        this.id = id;
        this.customerId = customerId;
        this.claimDate = claimDate;
        this.insuredPerson = insuredPerson;
        this.examDate = examDate;
        this.documents = documents;
        this.claimAmount = claimAmount;
        this.receiverBankingInfo = receiverBankingInfo;
        this.status = status;
    }

    // Getters and setters...

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

    public Date getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(Date claimDate) {
        this.claimDate = claimDate;
    }

    public String getInsuredPerson() {
        return insuredPerson;
    }

    public void setInsuredPerson(String insuredPerson) {
        this.insuredPerson = insuredPerson;
    }

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    public String[] getDocuments() {
        return documents;
    }

    public void setDocuments(String[] documents) {
        this.documents = documents;
    }

    public double getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(double claimAmount) {
        this.claimAmount = claimAmount;
    }

    public String getReceiverBankingInfo() {
        return receiverBankingInfo;
    }

    public void setReceiverBankingInfo(String receiverBankingInfo) {
        this.receiverBankingInfo = receiverBankingInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ClaimManagement{" +
                "id='" + id + '\'' +
                ", customerId='" + customerId + '\'' +
                ", claimDate=" + claimDate +
                ", insuredPerson='" + insuredPerson + '\'' +
                ", examDate=" + examDate +
                ", documents=" + String.join(", ", documents) +
                ", claimAmount=" + claimAmount +
                ", receiverBankingInfo='" + receiverBankingInfo + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
