package org.vladimir.t1.currency.service.api.exception.transaction;

public enum TransactionExceptionType {
    FROM_ACCOUNT_NOT_FOUND,
    TO_ACCOUNT_NOT_FOUND,
    FROM_ACCOUNT_BLOCKED,
    TO_ACCOUNT_BLOCKED,
    NOT_ENOUGH_FUNDS,
    TRANSACTION_VALIDATION_FAILED,
    SENDER_AND_RECEIVER_EQUALS,
    PAYMENT_PURPOSE_NOT_SUPPORTED,

}
