package com.study.shoestrade.exception.product;

public class ProductDuplicationException extends RuntimeException{

    public ProductDuplicationException() {
    }

    public ProductDuplicationException(String message) {
        super(message);
    }

    public ProductDuplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductDuplicationException(Throwable cause) {
        super(cause);
    }

    public ProductDuplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
