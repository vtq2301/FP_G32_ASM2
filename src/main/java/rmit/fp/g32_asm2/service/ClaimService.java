package rmit.fp.g32_asm2.service;

import rmit.fp.g32_asm2.DAO.ClaimDAO;
import rmit.fp.g32_asm2.DAO.Order;
import rmit.fp.g32_asm2.model.Claim.Claim;

import java.util.*;

public class ClaimService {
    private final ClaimDAO claimDAO = new ClaimDAO();

    public boolean addClaim(Claim claim) {
        return claimDAO.save(claim);
    }

    public boolean updateClaim(Claim claim) {
        return claimDAO.update(Optional.ofNullable(claim));
    }

    public boolean deleteClaim(String claimId) {
        return claimDAO.delete(claimId);
    }

    public Claim findClaimById(String claimId) {
        return claimDAO.findById(claimId);
    }

    public List<Claim> findAllClaims() {
        return claimDAO.findAll("created_at", Order.ASC, 10, 0);
    }

    public List<Claim> findClaimsByUserId(String userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("insured_person_id", UUID.fromString(userId));
        return claimDAO.findMany(params, "created_at", Order.ASC, 20, 0);
    }

    public List<Claim> getBeneficiaryClaims(String policyOwnerId) {
        List<Claim> claims = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        return List.of();
    }
}
