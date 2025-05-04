package com.shan.sb.db.dbjpa.exception;

public class InsufficientStockException extends  RuntimeException{
    public InsufficientStockException(String message) {
        super(message);
    }
}
