package rmit.fp.g32_asm2.model;

import rmit.fp.g32_asm2.service.UserService;
import rmit.fp.g32_asm2.service.ClaimService;
import rmit.fp.g32_asm2.service.CustomerService;
import rmit.fp.g32_asm2.service.ProviderService;
import rmit.fp.g32_asm2.model.Claim.Claim;
import rmit.fp.g32_asm2.model.admin.SystemAdmin;
import rmit.fp.g32_asm2.model.customer.*;
import rmit.fp.g32_asm2.model.provider.Manager;
import rmit.fp.g32_asm2.model.provider.ProviderType;
import rmit.fp.g32_asm2.model.provider.Surveyor;

import java.util.List;

public class UserFactory {

    private static final UserService userService = new UserService();
    private static final CustomerService customerService = new CustomerService();
    private static final ClaimService claimService = new ClaimService();
    private static final ProviderService providerService = new ProviderService();

    public static User createUser(User user) {
        RolesEnum role = RolesEnum.fromValue(user.getRoleId());
        return switch (role) {
            case CustomerType.POLICY_HOLDER -> createPolicyHolder(user);
            case CustomerType.DEPENDENT -> createDependent(user);
            case CustomerType.POLICY_OWNER -> createPolicyOwner(user);
            case ProviderType.MANAGER -> createManager(user);
            case ProviderType.SURVEYOR -> createSurveyor(user);
            case UserType.ADMIN -> createAdmin(user);
            default -> throw new IllegalStateException("Unexpected value: " + role);
        };
    }

    private static SystemAdmin createAdmin(User user) {
        return new SystemAdmin(user);
    }

    private static Manager createManager(User user) {
        return new Manager(user);
    }

    private static Surveyor createSurveyor(User user) {
        return new Surveyor(user);
    }

    private static PolicyHolder createPolicyHolder(User user) {
        CustomerRelations cr = customerService.findOneByUserId(user.getId());
        String policyOwnerId = cr.getPolicyOwnerId();
        double insuranceRate = cr.getInsuranceRate();

        List<String> dependentIds = customerService.findDependentsByPolicyHolderId(user.getId());
        List<Claim> claims = claimService.findClaimsByUserId(user.getId());
        return new PolicyHolder.Builder(user)
                .withPolicyOwnerId(policyOwnerId)
                .withDependentIds(dependentIds)
                .withClaims(claims)
                .withRate(insuranceRate)
                .build();
    }

    private static Dependent createDependent(User user) {
        CustomerRelations cr = customerService.findOneByUserId(user.getId());
        String policyHolderId = cr.getPolicyHolderId();
        User policyHolderUser = userService.getUserById(policyHolderId);
        PolicyHolder policyHolder = createPolicyHolder(policyHolderUser);

        List<Claim> claims = claimService.findClaimsByUserId(user.getId());
        return new Dependent.Builder(user)
                .withPolicyHolder(policyHolder)
                .withClaims(claims)
                .build();
    }

    private static PolicyOwner createPolicyOwner(User user) {
        List<String> policyHolderIds = customerService.findPolicyHoldersByPolicyOwnerId(user.getId());
        double insuranceRate = customerService.findInsuranceRateByUserId(user.getId());

        return new PolicyOwner.Builder(user)
                .withPolicyHolderIds(policyHolderIds)
                .withRate(insuranceRate)
                .build();
    }
}
