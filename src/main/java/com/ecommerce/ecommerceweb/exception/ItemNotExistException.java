package com.ecommerce.ecommerceweb.exception;

public class ItemNotExistException  extends IllegalArgumentException{
    public ItemNotExistException(String s) {
        super(s);
    }
}
