package rmit.fp.g32_asm2.model;

import rmit.fp.g32_asm2.model.customer.CustomerType;
import rmit.fp.g32_asm2.model.provider.ProviderType;

import java.util.ArrayList;
import java.util.List;

public interface RolesEnum {
    static List<RolesEnum> values() {
        List<RolesEnum> list = new ArrayList<RolesEnum>();
        list.addAll(List.of(CustomerType.values()));
        list.addAll(List.of(ProviderType.values()));
        return list;
    }

    int getValue();
    String getName();
    static RolesEnum fromValue(int value) {
        for (UserType userType : UserType.values()) {
            if (userType.getValue() == value) {
                return userType;
            }
        }
        for (ProviderType providerType : ProviderType.values()) {
            if (providerType.getValue() == value) {
                return providerType;
            }
        }
        for (CustomerType customerType : CustomerType.values()) {
            if (customerType.getValue() == value) {
                return customerType;
            }
        }
        throw new IllegalArgumentException("Unknown role value: " + value);
    }
}
