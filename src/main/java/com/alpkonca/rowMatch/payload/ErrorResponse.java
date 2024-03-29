package com.alpkonca.rowMatch.payload;

import java.util.Date;

// This class is used to create a proper response with necessary information when an error occurs

public class ErrorResponse {
    private Date timestamp;
    private String errorDetails;
    private String errorCode;
    private String message;


    public ErrorResponse(Date timestamp, String errorDetails, String errorCode, String message) {
        this.timestamp = timestamp;
        this.errorDetails = errorDetails;
        this.errorCode = errorCode;
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
