package all.model.customer;

public class User {
    private String id;
    private String password;
    private String role;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String username;
    private String policyHolderId;
    private String email;
    private boolean isActive;


    public User() {}


    public User(String id, String username, String password, String role, String fullName, String address, String phoneNumber) {
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
    
    // Private constructor to enforce usage of the Builder
    private User(Builder builder) {
        this.id = builder.id;
        this.password = builder.password;
        this.role = builder.role;
        this.fullName = builder.fullName;
        this.address = builder.address;
        this.phoneNumber = builder.phoneNumber;
        this.username = builder.username;
        this.policyHolderId = builder.policyHolderId;
        this.email = builder.email;
        this.isActive = builder.isActive;
    }

    // Builder class
    public static class Builder {
        private String username;
        private String password;
        private String role;
        private String fullName;
        private String address;
        private String phoneNumber;
        private String id;
        private String policyHolderId;
        private String email;
        private boolean isActive;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setRole(String role) {
            this.role = role;
            return this;
        }

        public Builder setFullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPolicyHolderId(String policyHolderId) {
            this.policyHolderId = policyHolderId;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setIsActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public User build() {
            return new User(this);
        }


    }
}