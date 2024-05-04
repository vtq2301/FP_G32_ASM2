package all.model.customer;

public class User {
    private String username;
    private String role;
    private String fullName;
    private String address;
    private String phoneNumber;

    public User(String username, String role, String fullName, String address, String phoneNumber) {
        this.username = username;
        this.role = role;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    // Getters
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public String getFullName() { return fullName; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
}
