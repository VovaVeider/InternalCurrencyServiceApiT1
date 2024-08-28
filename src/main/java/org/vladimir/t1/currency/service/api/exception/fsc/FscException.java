package org.vladimir.t1.currency.service.api.exception.fsc;

import org.springframework.http.HttpStatus;
import org.vladimir.t1.currency.service.api.exception.ApiException;

public class FscException extends ApiException {
    public FscException(FscExceptionType fscExceptionType, String reason) {
        super(FscException.class.getSimpleName(), fscExceptionType.name(), reason);
    }

    public FscException(FscExceptionType fscExceptionType, String reason, Throwable cause) {
        super(FscException.class.getSimpleName(), fscExceptionType.name(), reason, cause);
    }

    public FscException(FscExceptionType fscExceptionType, String reason, HttpStatus httpStatus) {
        super(FscException.class.getSimpleName(), fscExceptionType.name(), reason, httpStatus);
    }

    public FscException(FscExceptionType fscExceptionType, String reason, HttpStatus httpStatus, Throwable cause) {
        super(FscException.class.getSimpleName(), fscExceptionType.name(), reason, httpStatus, cause);
    }
}
