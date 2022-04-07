package com.study.shoestrade.exception.member;

public class LoginFailureException extends RuntimeException {
    public LoginFailureException() {
        super();
    }

    public LoginFailureException(String message) {
        super(message);
    }

    public LoginFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginFailureException(Throwable cause) {
        super(cause);
    }

    protected LoginFailureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
