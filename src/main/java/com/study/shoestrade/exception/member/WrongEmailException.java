package com.study.shoestrade.exception.member;

public class WrongEmailException extends RuntimeException{
    public WrongEmailException() {
        super();
    }

    public WrongEmailException(String message) {
        super(message);
    }

    public WrongEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongEmailException(Throwable cause) {
        super(cause);
    }

    protected WrongEmailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
