package org.vladimir.t1.currency.service.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class ApiException extends RuntimeException {
    private final String type;
    private final String reason;
    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    private final String description;

    public ApiException(String type, String reason, String description) {
        super("ApiException:( type %s reason %s description %s httpStatusCode 400)".formatted(type, reason, description));
        this.reason = reason;
        this.type = type;
        this.description = description;
    }

    public ApiException(String type, String reason, String description, Throwable cause) {
        super("ApiException:( type %s reason %s description %s httpStatusCode 400)".formatted(type, reason, description), cause);
        this.reason = reason;
        this.type = type;
        this.description = description;

    }

    public ApiException(String type, String reason, String description, HttpStatus httpStatus) {
        super("ApiException:( type %s reason %s description %s httpStatusCode %s)"
                .formatted(type, reason, description, httpStatus.value()));
        this.reason = reason;
        this.type = type;
        this.description = description;
        this.httpStatus = httpStatus;
    }

    public ApiException(String type, String reason, String description, HttpStatus httpStatus, Throwable cause) {
        super("ApiException:( type %s reason %s description %s httpStatusCode %s)"
                .formatted(type, reason, description, httpStatus.value()), cause);
        this.reason = reason;
        this.type = type;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
