package com.africanb.africanb.utils.security;

public class JwtValidationException extends RuntimeException{

    public JwtValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
