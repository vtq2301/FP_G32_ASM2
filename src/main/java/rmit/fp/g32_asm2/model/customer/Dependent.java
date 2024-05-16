package rmit.fp.g32_asm2.model.customer;

import rmit.fp.g32_asm2.model.User;

public class Dependent extends Customer {
    private final PolicyHolder policyHolder;

    private Dependent(Builder builder) {
        super(builder);
        this.policyHolder = builder.policyHolder;
    }

    public static class Builder extends Customer.Builder<Builder> {
        private PolicyHolder policyHolder;

        public Builder(User user) {
            super(user);
        }

        public Builder withPolicyHolder(PolicyHolder policyHolder) {
            this.policyHolder = policyHolder;
            return this;
        }

        @Override
        public Dependent build() {
            return new Dependent(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public PolicyHolder getPolicyHolder() {
        return policyHolder;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", policyHolder=" + policyHolder.getId() +
                '}';
    }
}
