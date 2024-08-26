package org.vladimir.t1.currency.service.api.entity;

import lombok.experimental.UtilityClass;

@UtilityClass
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
                (accountType == AccountType.TEAM_CFO_ACCOUNT) ||
                (accountType == AccountType.STORE_CFO_ACCOUNT);

    }
    public static boolean isUserAccount(final String accountNumber) {
        return ! isFscAccount(accountNumber);
    }

}
