package rmit.fp.g32_asm2.model.provider;

import rmit.fp.g32_asm2.model.User;

public class Surveyor extends Provider{

    public Surveyor(User user){
        super(user);
    }
    private Surveyor(Builder builder) {
        super(builder);
    }

    public static class Builder extends Provider.Builder<Builder>{

        public Builder(User user) {
            super(user);
        }

        @Override
        public Surveyor build() {
            return new Surveyor(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }}
