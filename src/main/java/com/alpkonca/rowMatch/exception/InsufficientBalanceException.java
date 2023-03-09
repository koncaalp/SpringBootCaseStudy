package com.alpkonca.rowMatch.exception;

// This exception is thrown when the user tries to perform an action that requires more balance than the user has.
public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String action, String resourceName) {
        super(String.format("Insufficient balance to %s %s", action, resourceName)); // Format the message with the action and the resource name to customize the message
    }
}
