package com.alpkonca.rowMatch.exception;


// This exception is thrown when the request is trying to create a resource with a field that is already taken - does not follow the unique constraint.
public class UniqueFieldException extends RuntimeException{
    public UniqueFieldException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s with same %s already exists : '%s'", resourceName, fieldName, fieldValue)); // Format the message with resourceName, fieldName and fieldValue to customize the message

    }

}
