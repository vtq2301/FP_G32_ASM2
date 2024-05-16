package rmit.fp.g32_asm2.model.customer;

import rmit.fp.g32_asm2.model.Claim.Claim;
import rmit.fp.g32_asm2.model.User;

import java.util.ArrayList;
import java.util.List;

public class PolicyHolder extends Customer {
    private final String policyOwnerId;
    private final List<String> dependentIds;

    private PolicyHolder(Builder builder) {
        super(builder);
        this.policyOwnerId = builder.policyOwnerId;
        this.dependentIds = builder.dependentIds;
    }

    public static class Builder extends Customer.Builder<Builder> {
        private String policyOwnerId;
        private List<String> dependentIds;

        public Builder(User user){
            super(user);
            this.dependentIds = new ArrayList<>();
        }

        public Builder withPolicyOwnerId(String policyOwnerId) {
            this.policyOwnerId = policyOwnerId;
            return this;
        }

        public Builder withDependentIds(List<String> dependentIds) {
            this.dependentIds = dependentIds;
            return this;
        }

        @Override
        public PolicyHolder build() {
            return new PolicyHolder(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public String getPolicyOwnerId() {
        return policyOwnerId;
    }

    public List<String> getDependentIds() {
        return dependentIds;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", policyOwnerId='" + policyOwnerId + '\'' +
                ", dependentIds=" + dependentIds.size() +
                '}';
    }
}
