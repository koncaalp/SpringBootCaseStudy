package com.alpkonca.rowMatch.exception;

import java.util.List;

public class MissingFieldException extends RuntimeException {

    public MissingFieldException(String fieldName) {
        super("Missing required field: " + fieldName);
    }

}
