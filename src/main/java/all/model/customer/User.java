package all.model.customer;

public class User {
    private String id;
    private String role;
    private String fullName;
    private String address;
    private String phoneNumber;


    public User() {
    }


    public User(String id, String role, String fullName, String address, String phoneNumber) {
        this.id = id;
        this.role = role;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getId() { return id; }
    public String getRole() { return role; }
    public String getFullName() { return fullName; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return fullName + " (" + id + ")";
    }
}
