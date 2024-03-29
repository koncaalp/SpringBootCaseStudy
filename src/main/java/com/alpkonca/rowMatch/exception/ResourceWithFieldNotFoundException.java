package com.alpkonca.rowMatch.exception;

// This exception is thrown when the resource with the given field value is not found
public class ResourceWithFieldNotFoundException extends RuntimeException{
    public ResourceWithFieldNotFoundException(String resourceName, String fieldName, int fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue)); // Format the message with resourceName, fieldName and fieldValue to customize the message
    }


}
