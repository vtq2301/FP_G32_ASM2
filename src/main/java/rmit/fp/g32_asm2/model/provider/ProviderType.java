package rmit.fp.g32_asm2.model.provider;

import rmit.fp.g32_asm2.model.RolesEnum;

public enum ProviderType implements RolesEnum {
    MANAGER(3, "Manager"),
    SURVEYOR(4, "Surveyor");

    private final int value;
    private final String name;

    ProviderType(final int value, final String name) {
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
