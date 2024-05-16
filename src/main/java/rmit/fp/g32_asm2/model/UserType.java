package rmit.fp.g32_asm2.model;

public enum UserType implements RolesEnum {
    CUSTOMER(1, "Customer"),
    PROVIDER(2, "Provider"),
    ADMIN(9999, "SystemAdmin");

    private final int value;
    private final String name;

    UserType(final int value, final String name) {
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
