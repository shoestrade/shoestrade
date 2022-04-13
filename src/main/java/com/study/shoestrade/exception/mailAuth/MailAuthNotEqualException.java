package com.study.shoestrade.exception.mailAuth;

public class MailAuthNotEqualException extends IllegalArgumentException{
    public MailAuthNotEqualException() {
        super();
    }

    public MailAuthNotEqualException(String s) {
        super(s);
    }

    public MailAuthNotEqualException(String message, Throwable cause) {
        super(message, cause);
    }

    public MailAuthNotEqualException(Throwable cause) {
        super(cause);
    }
}
