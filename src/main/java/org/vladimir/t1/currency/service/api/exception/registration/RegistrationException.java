package org.vladimir.t1.currency.service.api.exception.registration;

import org.vladimir.t1.currency.service.api.exception.ApiException;

public class RegistrationException extends ApiException {
    public RegistrationException(RegistrationExceptionType registrationExceptionType, String description) {
        super(RegistrationException.class.getSimpleName(), registrationExceptionType.name(), description);
    }

    public RegistrationException(RegistrationExceptionType registrationExceptionType, String description, Throwable cause) {
        super(RegistrationException.class.getSimpleName(), registrationExceptionType.name(), description, cause);

    }
}
