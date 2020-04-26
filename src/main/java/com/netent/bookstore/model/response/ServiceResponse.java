package com.netent.bookstore.model.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.netent.bookstore.exception.BookStoreException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ServiceResponse<T> extends ResponseEntity<T> {

    public ServiceResponse(T data) {
        super(data, HttpStatus.OK);
    }

    public ServiceResponse(HttpStatus status) {
        super(status);
    }

    public ServiceResponse(T data, HttpStatus status) {
        super(data, status);
    }

    public static ServiceResponse getSuccessResponse() {
        return new ServiceResponse<>(new StatusMessage(), HttpStatus.OK);
    }

    public static ServiceResponse getErrorResponse(BookStoreException e) {
        return new ServiceResponse<>(new StatusMessage(e), e.getHttpStatus());
    }

    public static ServiceResponse getBadRequestResponse(String message) {
        return new ServiceResponse<>(new StatusMessage(message), HttpStatus.BAD_REQUEST);
    }

}
