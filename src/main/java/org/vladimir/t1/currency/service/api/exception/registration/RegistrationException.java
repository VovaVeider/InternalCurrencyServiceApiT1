package org.vladimir.t1.currency.service.api.exception.registration;

import org.springframework.http.HttpStatus;
import org.vladimir.t1.currency.service.api.exception.ApiException;

public class RegistrationException extends ApiException {
    public RegistrationException(RegistrationExceptionType registrationExceptionType, String description) {
        super(RegistrationException.class.getSimpleName(), registrationExceptionType.name(), description);
    }

    public RegistrationException(RegistrationExceptionType registrationExceptionType, String description, Throwable cause) {
        super(RegistrationException.class.getSimpleName(), registrationExceptionType.name(), description, cause);
    }

    public RegistrationException(RegistrationExceptionType registrationExceptionType, String description, HttpStatus httpStatus) {
        super(RegistrationException.class.getSimpleName(), registrationExceptionType.name(), description, httpStatus);
    }

    public RegistrationException(RegistrationExceptionType registrationExceptionType, String description, HttpStatus httpStatus, Throwable cause) {
        super(RegistrationException.class.getSimpleName(), registrationExceptionType.name(), description, httpStatus, cause);
    }
}

