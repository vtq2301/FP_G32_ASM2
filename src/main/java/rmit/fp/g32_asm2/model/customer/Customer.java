package rmit.fp.g32_asm2.model.customer;

import rmit.fp.g32_asm2.model.Claim.Claim;
import rmit.fp.g32_asm2.model.User;

import java.util.ArrayList;
import java.util.List;

public abstract class Customer extends User {
    protected double rate;
    protected List<Claim> claims;

    public Customer(User user, double rate) {
        super(user);
        this.rate = rate;
        this.claims = new ArrayList<Claim>();
    }
    protected Customer(Builder<?> builder) {
        super(builder);
        this.rate = builder.rate;
        this.claims = builder.claims;
    }

    public static abstract class Builder<T extends Builder<T>> extends User.Builder {
        private double rate;
        private List<Claim> claims;

        public Builder(User user) {
            super(user);
            this.rate = 0.0;
            this.claims = new ArrayList<>();
        }

        public Builder() {
            super();
        }

        public T withRate(double rate) {
            this.rate = rate;
            return self();
        }

        public T withClaims(List<Claim> claims) {
            this.claims = claims;
            return self();
        }

        @Override
        public abstract Customer build();

        protected abstract T self();
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public List<Claim> getClaims() {
        return claims;
    }

    public void setClaims(List<Claim> claims) {
        this.claims = claims;
    }

    public void addClaim(Claim claim) {
        claims.add(claim);
    }

    public void removeClaim(Claim claim) {
        claims.remove(claim);
    }

    @Override
    public String toString() {
        return super.toString() +
                ", rate=" + rate +
                ", claims=" + claims.isEmpty() +
                '}';
    }
}
