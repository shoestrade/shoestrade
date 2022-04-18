package com.study.shoestrade.exception.product;

public class ProductSizeNoSuchElementException extends RuntimeException{

    public ProductSizeNoSuchElementException() {
    }

    public ProductSizeNoSuchElementException(String message) {
        super(message);
    }

    public ProductSizeNoSuchElementException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductSizeNoSuchElementException(Throwable cause) {
        super(cause);
    }

    public ProductSizeNoSuchElementException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
