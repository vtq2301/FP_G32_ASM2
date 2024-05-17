package rmit.fp.g32_asm2.model.Claim;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Claim {
    private String id;
    private String insuredPersonId;
    private List<String> documents;
    private int amount;
    private Status status;
    private Date claimDate;
    private Date examDate;

    public enum Status {
        FILED, PROCESSING, ACCEPTED, REJECTED, REQUIRED_MORE_INFO, DONE
    }

    public Claim(String id, String insuredPersonId, Date examDate, List<String> documents, int amount, Status status, Date claimDate) {
        this.id = id;
        this.insuredPersonId = insuredPersonId;
        this.examDate = examDate;
        this.documents = documents;
        this.amount = amount;
        this.status = status;
        this.claimDate = claimDate;
    }

    public boolean addDocument(String document) {
        if (documents.contains(document)) {
            return false;
        }
        return documents.add(document);
    }

    public boolean removeDocument(String document) {
        return documents.remove(document);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInsuredPersonId() {
        return insuredPersonId;
    }

    public void setInsuredPersonId(String insuredPersonId) {
        this.insuredPersonId = insuredPersonId;
    }

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    public List<String> getDocuments() {
        return documents;
    }

    public void setDocuments(List<String> documents) {
        this.documents = documents;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(Date claimDate) {
        this.claimDate = claimDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Claim claim = (Claim) o;
        return Double.compare(claim.amount, amount) == 0 &&
                Objects.equals(id, claim.id) &&
                Objects.equals(insuredPersonId, claim.insuredPersonId) &&
                Objects.equals(examDate, claim.examDate) &&
                Objects.equals(documents, claim.documents) &&
                Objects.equals(claimDate, claim.claimDate) &&
                status == claim.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, insuredPersonId, examDate, documents, amount, status, claimDate);
    }

    @Override
    public String toString() {
        return "Claim{" +
                "id='" + id + '\'' +
                ", insuredPersonId='" + insuredPersonId + '\'' +
                ", documents count=" + documents.size() +
                ", amount=" + amount +
                ", status=" + status +
                ", examDate=" + examDate +
                ", claimDate=" + claimDate +
                '}';
    }
}
