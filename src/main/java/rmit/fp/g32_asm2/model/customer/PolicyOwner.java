package rmit.fp.g32_asm2.model.customer;

import rmit.fp.g32_asm2.model.User;

import java.util.ArrayList;
import java.util.List;

public class PolicyOwner extends Customer {
    private final List<String> policyHolderIds;

    private PolicyOwner(Builder builder) {
        super(builder);
        this.policyHolderIds = builder.policyHolderIds;
    }

    public static class Builder extends Customer.Builder<Builder> {
        private List<String> policyHolderIds;

        public Builder(User user) {
            super(user);
            this.policyHolderIds = new ArrayList<>();

        }

        public Builder() {
            super();
            this.policyHolderIds = new ArrayList<>();
        }


        public Builder withPolicyHolderIds(List<String> policyHolderIds) {
            this.policyHolderIds = policyHolderIds;
            return this;
        }

        @Override
        public PolicyOwner build() {
            return new PolicyOwner(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public List<String> getPolicyHolderIds() {
        return policyHolderIds;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", policyHolderIds=" + policyHolderIds.size() +
                '}';
    }
}
