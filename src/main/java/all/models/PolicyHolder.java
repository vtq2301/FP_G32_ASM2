package all.models;


public class PolicyHolder {
    private String id;
    private String fullName;
    private String phone;
    private String address;
    private String email;
    private String password;


    public PolicyHolder(String id, String fullName, String phone, String address, String email, String password) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.password = password;
    }

    // Getters and setters
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
