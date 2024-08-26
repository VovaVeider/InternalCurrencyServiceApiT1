package org.vladimir.t1.currency.service.api.entity;

import lombok.Getter;

public enum AccountType {
    USER_ACCOUNT(1),
    MASTER_FSC_ACCOUNT(2),
    TEAM_CFO_ACCOUNT(3),
    STORE_CFO_ACCOUNT(4);
    @Getter
    private final int code;

    private AccountType(int code) {
        this.code = code;
    }

    public static AccountType fromCode(int code){
        return switch (code) {
            case 1 -> USER_ACCOUNT;
            case 2 -> MASTER_FSC_ACCOUNT;
            case 3 -> TEAM_CFO_ACCOUNT;
            case 4 -> STORE_CFO_ACCOUNT;
            default -> throw new IllegalArgumentException("Invalid account type code: " + code);
        };
    }
}
