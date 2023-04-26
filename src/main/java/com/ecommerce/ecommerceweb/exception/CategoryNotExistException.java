package com.ecommerce.ecommerceweb.exception;

public class CategoryNotExistException extends IllegalArgumentException{

    public CategoryNotExistException(String s) {
        super(s);
    }
}
