package org.vladimir.t1.currency.service.api.exception;

import lombok.Getter;



public class ApiException extends RuntimeException {
    @Getter
    private final String type;
    @Getter
    private final String description;

    public ApiException(String type, String message, String description) {
        super(message);
        this.type = type;
        this.description = description;
    }

    protected ApiException(String type, String message, String description, Throwable cause) {
        super(message);
        this.type = type;
        this.description = description;

    }
}
