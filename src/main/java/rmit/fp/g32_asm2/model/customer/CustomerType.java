package rmit.fp.g32_asm2.model.customer;

import rmit.fp.g32_asm2.model.RolesEnum;

public enum CustomerType implements RolesEnum {
    POLICY_HOLDER(11, "PolicyHolder"),
    DEPENDENT(12, "Dependent"),
    POLICY_OWNER(13, "PolicyOwner");

    private final int value;
    private final String name;

    CustomerType(final int value, final String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name;
    }

}
