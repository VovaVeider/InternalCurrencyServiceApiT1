package org.vladimir.t1.currency.service.api.exception.login;

import org.vladimir.t1.currency.service.api.exception.ApiException;
import org.vladimir.t1.currency.service.api.exception.registration.RegistrationException;
import org.vladimir.t1.currency.service.api.exception.registration.RegistrationExceptionType;

public class LoginException extends ApiException {
    public LoginException(LoginExceptionType loginExceptionType, String description) {
        super(LoginException.class.getSimpleName(), loginExceptionType.name(), description);
    }

    public LoginException(LoginExceptionType loginExceptionType, String description, Throwable cause) {
        super(LoginException.class.getSimpleName(), loginExceptionType.name(), description, cause);

    }
}
