package org.vladimir.t1.currency.service.api.entity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountUtils {


    public static AccountType getAccountType(final String accountNumber) {
        return AccountType.fromCode(Integer.parseInt(accountNumber.substring(0, 3)));
    }

    public static String getAccountNumber(final AccountType accountType, final long accountId) {
        if (accountId < 0)
            throw new IllegalArgumentException("Invalid account ID. Must be a positive integer");
        if (accountType == null)
            throw new IllegalArgumentException("Invalid account type. Given null");
        if (accountId > 1_000_000_000L)
            throw new IllegalArgumentException("Invalid account ID. Must be less than 1_000_000_000");
        return "%03d%09d".formatted(accountType.getCode(), accountId);
    }

    public static boolean isFscAccount(final String accountNumber) {
        var accountType = getAccountType(accountNumber);
        return (accountType == AccountType.MASTER_FSC_ACCOUNT) ||
                (accountType == AccountType.TEAM_FSC_ACCOUNT) ||
                (accountType == AccountType.STORE_FSC_ACCOUNT);

    }
    public static boolean isUserAccount(final String accountNumber) {
        return ! isFscAccount(accountNumber);
    }
    public static AccountType getAccountType(final FscType fscType){
        return switch (fscType) {
            case MASTER -> AccountType.MASTER_FSC_ACCOUNT;
            case TEAM -> AccountType.TEAM_FSC_ACCOUNT;
            case STORE -> AccountType.STORE_FSC_ACCOUNT;
            default -> {
                log.error("Invalid fsc account type: {}", fscType);
                throw new IllegalArgumentException("Invalid fsc type");
            }
        };
    }

}
