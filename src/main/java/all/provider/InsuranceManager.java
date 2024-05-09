package all.provider;

import java.util.List;

public class InsuranceManager extends Provider {

    private List<InsuranceSurveyor> team; // Optional: List of managed surveyors

    public InsuranceManager(String id, List<InsuranceSurveyor> team) {
        super(id);
        this.team = team;
    }

    public void approveClaim(Claim claim) {
        // Implement logic to approve claim
    }

    public void rejectClaim(Claim claim, String reason) {
        // Implement logic to reject claim with a reason
    }

    public List<InsuranceSurveyor> getSurveyorsInfo() {
        return team;
    }

    public void updateInformation(ProviderInfo info) {
        // Implement logic to update manager information
    }

    @Override
    public List<Claim> retrieveClaims(ClaimFilter filter) {
        return null;
    }

    @Override
    public void logsActions(String message) {

    }

    // Override abstract methods from Provider (retrieveClaims, logsActions)
}