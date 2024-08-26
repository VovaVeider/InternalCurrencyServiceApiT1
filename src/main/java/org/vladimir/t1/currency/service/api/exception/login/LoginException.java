package org.vladimir.t1.currency.service.api.exception.login;

import org.springframework.http.HttpStatus;
import org.vladimir.t1.currency.service.api.exception.ApiException;

public class LoginException extends ApiException {
    public LoginException(LoginExceptionType loginExceptionType, String reason) {
        super(LoginException.class.getSimpleName(), loginExceptionType.name(), reason);
    }

    public LoginException(LoginExceptionType loginExceptionType, String reason, Throwable cause) {
        super(LoginException.class.getSimpleName(), loginExceptionType.name(), reason, cause);
    }

    public LoginException(LoginExceptionType loginExceptionType, String reason, HttpStatus httpStatus) {
        super(LoginException.class.getSimpleName(), loginExceptionType.name(), reason, httpStatus);
    }

    public LoginException(LoginExceptionType loginExceptionType, String reason, HttpStatus httpStatus, Throwable cause) {
        super(LoginException.class.getSimpleName(), loginExceptionType.name(), reason, httpStatus, cause);
    }
}

