package all.provider;

import java.util.List;

public class InsuranceSurveyor extends Provider {

    private String license; // Optional: Surveyor license number
    private String expertise; // Optional: Surveyor area of expertise

    public InsuranceSurveyor(String id, String license, String expertise) {
        super(id);
        this.license = license;
        this.expertise = expertise;
    }

    public Claim requireMoreInfo(Claim claim) {
        // Implement logic to request more information and potentially update the claim
        return claim; // Can return the updated claim object
    }

    public void proposeClaim(Claim claim) {
        // Implement logic to send claim proposal to manager
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
