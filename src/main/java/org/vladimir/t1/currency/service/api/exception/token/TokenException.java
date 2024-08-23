package org.vladimir.t1.currency.service.api.exception.token;

import org.vladimir.t1.currency.service.api.exception.ApiException;

public class TokenException extends ApiException {
    public TokenException(TokenExceptionType tokenExceptionType, String description) {
        super(TokenException.class.getSimpleName(), tokenExceptionType.name(), description);
    }

    public TokenException(TokenExceptionType tokenExceptionType, String description, Throwable cause) {
        super(TokenException.class.getSimpleName(), tokenExceptionType.name(), description, cause);

    }
}
