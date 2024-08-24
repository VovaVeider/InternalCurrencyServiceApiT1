package org.vladimir.t1.currency.service.api.exception.registration;

import org.springframework.http.HttpStatus;
import org.vladimir.t1.currency.service.api.exception.ApiException;

public class RegistrationException extends ApiException {
    public RegistrationException(RegistrationExceptionType registrationExceptionType, String reason) {
        super(RegistrationException.class.getSimpleName(), registrationExceptionType.name(), reason);
    }

    public RegistrationException(RegistrationExceptionType registrationExceptionType, String reason, Throwable cause) {
        super(RegistrationException.class.getSimpleName(), registrationExceptionType.name(), reason, cause);
    }

    public RegistrationException(RegistrationExceptionType registrationExceptionType, String reason, HttpStatus httpStatus) {
        super(RegistrationException.class.getSimpleName(), registrationExceptionType.name(), reason, httpStatus);
    }

    public RegistrationException(RegistrationExceptionType registrationExceptionType, String reason, HttpStatus httpStatus, Throwable cause) {
        super(RegistrationException.class.getSimpleName(), registrationExceptionType.name(), reason, httpStatus, cause);
    }
}

