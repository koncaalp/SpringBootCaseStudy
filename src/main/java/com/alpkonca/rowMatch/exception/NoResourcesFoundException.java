package com.alpkonca.rowMatch.exception;


// This exception is thrown when the queried table is empty.
public class NoResourcesFoundException extends RuntimeException{
    public NoResourcesFoundException(String resourceName) {
        super(String.format("There are no %s", resourceName)); // Format the message with the resource name to customize the message
    }

}
