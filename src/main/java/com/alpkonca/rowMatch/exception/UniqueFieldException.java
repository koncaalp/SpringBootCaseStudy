package com.alpkonca.rowMatch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UniqueFieldException extends RuntimeException{



    public UniqueFieldException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s with same %s already exists : '%s'", resourceName, fieldName, fieldValue));

    }

}
