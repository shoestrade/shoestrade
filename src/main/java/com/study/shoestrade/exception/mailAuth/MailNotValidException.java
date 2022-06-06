package com.study.shoestrade.exception.mailAuth;

public class MailNotValidException extends RuntimeException{
    public MailNotValidException() {
        super();
    }

    public MailNotValidException(String message) {
        super(message);
    }

    public MailNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public MailNotValidException(Throwable cause) {
        super(cause);
    }

    protected MailNotValidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
