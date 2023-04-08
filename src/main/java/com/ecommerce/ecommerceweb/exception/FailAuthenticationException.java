package com.ecommerce.ecommerceweb.exception;

public class FailAuthenticationException extends IllegalArgumentException {
    public FailAuthenticationException(String msg){
        super(msg);
    }
}
