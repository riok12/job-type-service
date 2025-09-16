package com.example.job_type_service.exception;

public class JobTypeNotFoundException extends RuntimeException {
    public JobTypeNotFoundException(String message) {
        super(message);
    }
    
    public JobTypeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
