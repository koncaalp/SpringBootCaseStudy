package com.alpkonca.rowMatch.exception;


public class ResourceWithIdNotFoundException extends RuntimeException{
    public ResourceWithIdNotFoundException(String resourceName, String fieldName, int fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue)); // Format the message with resourceName, fieldName and fieldValue to customize the message
    }


}
