package rmit.fp.g32_asm2.model;

public class User {
    private String id;
    private String password;
    private String role;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String username;
    private String policyHolderId;

    public User() {}

    public User(String id, String username,String password, String role, String fullName, String address, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
    public User(String id, String username, String role, String fullName, String address, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
    public User(String id,String username, String password, String role, String fullName, String address, String phoneNumber,  String policyHolderId) {
        this.id = id;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.policyHolderId = policyHolderId;
    }

    public String getId() { return id; }
    public String getRole() { return role; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPolicyHolderId(String policyHolderId) {
        this.policyHolderId = policyHolderId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public String getPolicyHolderId() {
        return policyHolderId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setUsername(String username) { this.username = username; }

    @Override
    public String toString() {
        return fullName + " (" + id + ")";
    }
}