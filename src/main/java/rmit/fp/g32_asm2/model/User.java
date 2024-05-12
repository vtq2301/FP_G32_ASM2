package rmit.fp.g32_asm2.model;

public class User {
    private String id; // Changed from username to id for clarity
    private String role;
    private String fullName;
    private String address;
    private String phoneNumber;

    public User(String id, String role, String fullName, String address, String phoneNumber) {
        this.id = id;
        this.role = role;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    // Getters
    public String getId() { return id; }
    public String getRole() { return role; }
    public String getFullName() { return fullName; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }

    @Override
    public String toString() {
        return fullName + " (" + id + ")"; // Display the full name and id
    }
}
