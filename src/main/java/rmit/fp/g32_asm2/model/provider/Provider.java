package rmit.fp.g32_asm2.model.provider;

import rmit.fp.g32_asm2.model.User;

public abstract class Provider extends User {

    public Provider(User user) {
        super(user);
    }
    protected Provider(Builder<?> builder){
        super(builder);
    }

    public static abstract class Builder<T extends Builder<T>> extends User.Builder{

        public Builder(User user) {
            super(user);
        }

        @Override
        public abstract Provider build();

        protected abstract T self();
    }
}
