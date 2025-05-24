package com.github.julionaponucena.financedesktop.modules.registers.controllers.enums;

import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

@Getter
@ToString
public enum RegisterTypeEnum {
    DEBIT("Débito"),
    CREDIT("Crédito");

    private final String value;

    RegisterTypeEnum(String value) {
        this.value = value;
    }

    public static Optional<RegisterTypeEnum> fromValue(String value) {
        for (RegisterTypeEnum registerTypeEnum : RegisterTypeEnum.values()) {
            if (registerTypeEnum.value.equals(value)) {
                return Optional.of(registerTypeEnum);
            }
        }

        return Optional.empty();
    }
}
