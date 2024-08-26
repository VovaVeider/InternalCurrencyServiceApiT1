package org.vladimir.t1.currency.service.api.exception.transaction;

import org.springframework.http.HttpStatus;
import org.vladimir.t1.currency.service.api.exception.ApiException;

public class TransactionException extends ApiException {
    public TransactionException(TransactionExceptionType transactionExceptionType, String reason) {
        super(TransactionException.class.getSimpleName(), transactionExceptionType.name(), reason);
    }

    public TransactionException(TransactionExceptionType transactionExceptionType, String reason, Throwable cause) {
        super(TransactionException.class.getSimpleName(), transactionExceptionType.name(), reason, cause);
    }

    public TransactionException(TransactionExceptionType transactionExceptionType, String reason, HttpStatus httpStatus) {
        super(TransactionException.class.getSimpleName(), transactionExceptionType.name(), reason, httpStatus);
    }

    public TransactionException(TransactionExceptionType transactionExceptionType, String reason, HttpStatus httpStatus, Throwable cause) {
        super(TransactionException.class.getSimpleName(), transactionExceptionType.name(), reason, httpStatus, cause);

    }
}
