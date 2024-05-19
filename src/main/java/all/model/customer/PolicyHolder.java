package all.model.customer;

public class PolicyHolder extends Customer{
    public PolicyHolder(String id, String name, String phone, String address, String email) {
        super(id, name, phone, address, email);
    }
    public PolicyHolder() {
        super();
    }
    public PolicyHolder(String id){super();};
}
