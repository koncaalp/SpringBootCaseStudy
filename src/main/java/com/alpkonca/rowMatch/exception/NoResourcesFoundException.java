package com.alpkonca.rowMatch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoResourcesFoundException extends RuntimeException{
    private String resourceName;

    public NoResourcesFoundException(String resourceName) {
        super(String.format("There are no %s", resourceName));
        this.resourceName = resourceName;
    }

    public String getResourceName() {
        return resourceName;
    }
}
