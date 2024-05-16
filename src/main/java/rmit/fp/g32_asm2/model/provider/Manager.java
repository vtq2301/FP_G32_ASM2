package rmit.fp.g32_asm2.model.provider;

import rmit.fp.g32_asm2.model.User;

public class Manager extends Provider{

    public Manager(User user) {
        super(user);
    }
    private Manager(Builder builder) {
        super(builder);
    }

    public static class Builder extends Provider.Builder<Builder>{


        public Builder(User user) {
            super(user);
        }

        @Override
        public Manager build() {
            return new Manager(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
