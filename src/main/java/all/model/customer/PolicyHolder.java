/**
 @author GROUP 32
 - s3870729 - Tran Vu Nhat Tin
 - s3929202 - Vu Pham Nguyen Vu
 - s3914412 - Nguyen Duong Truong Thinh
 - s3981278 - Vu Tien Quang
 */
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
