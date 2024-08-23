package org.vladimir.t1.currency.service.api.controller;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.vladimir.t1.currency.service.api.dto.ErrorResponse;
import org.vladimir.t1.currency.service.api.exception.ApiException;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException e) {
        return  new ResponseEntity<ErrorResponse>(new ErrorResponse(e.getType(), e.getMessage(),e.getDescription()), HttpStatusCode.valueOf(400));
    }
}
