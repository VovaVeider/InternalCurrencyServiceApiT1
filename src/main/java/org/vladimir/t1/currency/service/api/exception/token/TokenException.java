package org.vladimir.t1.currency.service.api.exception.token;

import org.springframework.http.HttpStatus;
import org.vladimir.t1.currency.service.api.exception.ApiException;

public class TokenException extends ApiException {
    public TokenException(TokenExceptionType tokenExceptionType, String reason) {
        super(TokenException.class.getSimpleName(), tokenExceptionType.name(), reason);
    }

    public TokenException(TokenExceptionType tokenExceptionType, String reason, Throwable cause) {
        super(TokenException.class.getSimpleName(), tokenExceptionType.name(), reason, cause);
    }

    public TokenException(TokenExceptionType tokenExceptionType, String reason, HttpStatus httpStatus) {
        super(TokenException.class.getSimpleName(), tokenExceptionType.name(), reason, httpStatus);
    }

    public TokenException(TokenExceptionType tokenExceptionType, String reason, HttpStatus httpStatus, Throwable cause) {
        super(TokenException.class.getSimpleName(), tokenExceptionType.name(), reason, httpStatus, cause);

    }
}
