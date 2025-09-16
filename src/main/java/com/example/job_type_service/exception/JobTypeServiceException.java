package com.example.job_type_service.exception;

public class JobTypeServiceException extends RuntimeException {
    public JobTypeServiceException(String message) {
        super(message);
    }
    
    public JobTypeServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
