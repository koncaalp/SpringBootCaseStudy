package com.alpkonca.rowMatch.exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String action, String resourceName) {
        super(String.format("Insufficient balance to %s %s", action, resourceName));
    }
}
