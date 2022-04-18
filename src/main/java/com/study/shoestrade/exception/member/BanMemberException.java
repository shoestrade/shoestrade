package com.study.shoestrade.exception.member;

public class BanMemberException extends RuntimeException{
    public BanMemberException() {
        super();
    }

    public BanMemberException(String message) {
        super(message);
    }

    public BanMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public BanMemberException(Throwable cause) {
        super(cause);
    }

    protected BanMemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
