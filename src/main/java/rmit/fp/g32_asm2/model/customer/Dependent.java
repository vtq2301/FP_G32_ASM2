package rmit.fp.g32_asm2.model.customer;

import rmit.fp.g32_asm2.model.User;

public class Dependent extends Customer {
    private final String policyHolderId;

    private Dependent(Builder builder) {
        super(builder);
        this.policyHolderId = builder.policyHolderId;
    }

    public static class Builder extends Customer.Builder<Builder> {
        private String policyHolderId;

        public Builder(User user) {
            super(user);
        }
        public Builder() {
            super();
        }

        public Builder withPolicyHolderId(String policyHolderId) {
            this.policyHolderId = policyHolderId;
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

    public String getPolicyHolderId() {
        return policyHolderId;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", policyHolderId=" + policyHolderId +
                '}';
    }
}
