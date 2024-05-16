package rmit.fp.g32_asm2.service;

import rmit.fp.g32_asm2.DAO.CustomerDAO;
import rmit.fp.g32_asm2.DAO.Order;
import rmit.fp.g32_asm2.model.User;
import rmit.fp.g32_asm2.model.customer.CustomerRelations;
import rmit.fp.g32_asm2.model.customer.CustomerType;

import java.util.*;

public class CustomerService {
    private final CustomerDAO customerDAO = new CustomerDAO();

    public boolean addCustomer(CustomerRelations customer) {
        return customerDAO.save(customer);
    }

    public boolean updateCustomer(CustomerRelations customer) {
        return customerDAO.update(Optional.ofNullable(customer));
    }

    public boolean deleteCustomer(String userId) {
        return customerDAO.delete(userId);
    }

    public CustomerRelations findOneByUserId(String userId) {
        return customerDAO.findOne("user_id", UUID.fromString(userId));
    }

    public List<String> findDependentsByPolicyHolderId(String policyHolderId) {
        Map<String, Object> params = new HashMap<>();
        params.put("policy_holder_id", UUID.fromString(policyHolderId));
        List<CustomerRelations> crList = customerDAO.findMany(params, "created_at", Order.ASC, 20, 0);
        return crList.stream().map(CustomerRelations::getUserId).toList();
    }

    public List<String> findPolicyHoldersByPolicyOwnerId(String policyOwnerId) {
        Map<String, Object> params = new HashMap<>();
        params.put("policy_owner_id", UUID.fromString(policyOwnerId));
        params.put("role_id", CustomerType.POLICY_HOLDER.getValue());
        List<CustomerRelations> crList = customerDAO.findMany(params, "created_at", Order.ASC, 20, 0);
        return crList.stream().map(CustomerRelations::getUserId).toList();
    }

    public double findInsuranceRateByUserId(String userId) {
        CustomerRelations cr = customerDAO.findOne("user_id", UUID.fromString(userId));
        if (cr == null) return 0.0;
        return cr.getInsuranceRate();
    }
}
