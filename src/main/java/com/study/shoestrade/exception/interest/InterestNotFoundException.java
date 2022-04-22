package com.study.shoestrade.exception.interest;

public class InterestNotFoundException extends IllegalArgumentException{
    public InterestNotFoundException() {
        super();
    }

    public InterestNotFoundException(String s) {
        super(s);
    }

    public InterestNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public InterestNotFoundException(Throwable cause) {
        super(cause);
    }
}
