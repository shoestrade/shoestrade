package com.study.shoestrade.exception;

public class ProductImageDuplicationException extends RuntimeException{

    public ProductImageDuplicationException() {
    }

    public ProductImageDuplicationException(String message) {
        super(message);
    }

    public ProductImageDuplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductImageDuplicationException(Throwable cause) {
        super(cause);
    }

    public ProductImageDuplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
