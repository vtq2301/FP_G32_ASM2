package rmit.fp.g32_asm2.model;

import rmit.fp.g32_asm2.model.customer.CustomerType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    private final String id;
    private final String username;
    private final int roleId;
    private String hashPassword;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;
    private boolean isActive;
    private List<String> actionLogIds;

    public User(User user) {
        this.id = user.id;
        this.username = user.username;
        this.roleId = user.roleId;
        this.hashPassword = user.hashPassword;
        this.name = user.name;
        this.phoneNumber = user.phoneNumber;
        this.email = user.email;
        this.address = user.address;
        this.isActive = user.isActive;
        this.actionLogIds = user.actionLogIds;
    }

    protected User(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.hashPassword = builder.hashPassword;
        this.name = builder.name;
        this.phoneNumber = builder.phoneNumber;
        this.email = builder.email;
        this.address = builder.address;
        this.roleId = builder.roleId;
        this.isActive = builder.isActive;
        this.actionLogIds = builder.actionLogIds;
    }

    public static class Builder {
        private String id;
        private String username;
        private int roleId;
        private String hashPassword;
        private String name;
        private String phoneNumber;
        private String email;
        private String address;
        private boolean isActive;
        private List<String> actionLogIds;

        public Builder(User user) {
            this.id = user.id;
            this.username = user.username;
            this.roleId = user.roleId;
            this.hashPassword = user.hashPassword;
            this.name = user.name;
            this.phoneNumber = user.phoneNumber;
            this.email = user.email;
            this.address = user.address;
            this.isActive = user.isActive;
            this.actionLogIds = user.actionLogIds;
        }

        public Builder(){
            this.actionLogIds = new ArrayList<>();
        }

        public Builder withUsername(String username){
            this.username = username;
            return this;
        }

        public Builder withRoleId(int roleId){
            this.roleId = roleId;
            return this;
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withHashPassword(String hashPassword) {
            this.hashPassword = hashPassword;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder withIsActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder withActionLogIds(List<String> actionLogIds) {
            this.actionLogIds = actionLogIds;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    // Getters and Setters

    public int getRoleId() {
        return roleId;
    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getActionLogIds() {
        return actionLogIds;
    }

    public void setActionLogIds(List<String> actionLogIds) {
        this.actionLogIds = actionLogIds;
    }

    public void addActionLogId(String actionLogId) {
        this.actionLogIds.add(actionLogId);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId()) && Objects.equals(getRoleId(), user.getRoleId()) && Objects.equals(getName(), user.getName()) && Objects.equals(getPhoneNumber(), user.getPhoneNumber()) && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getAddress(), user.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRoleId(), getName(), getPhoneNumber(), getEmail(), getAddress());
    }

    @Override
    public String toString() {
        String clazz = this.getClass().getSimpleName();
        return clazz +"{" +
                "id='" + id + '\'' +
                ", role='" + RolesEnum.fromValue(roleId) + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", isActive=" + isActive +
                ", actionLogIds=" + actionLogIds.isEmpty() +
                '}';
    }
}
