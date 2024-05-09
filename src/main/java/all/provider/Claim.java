package all.provider;

import java.util.Date;

public class Claim {

    private String id;
    private String details;
    private String status;
    private Date submissionDate;
    private InsuranceSurveyor assignedSurveyor;
    private InsuranceManager manager;
    private String rejectionReason; // Optional

    // Optional attributes (documents, communicationLog)

    // Getters and setters for all attributes
}