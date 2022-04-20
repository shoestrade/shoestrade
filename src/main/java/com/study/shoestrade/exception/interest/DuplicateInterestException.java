package com.study.shoestrade.exception.interest;

public class DuplicateInterestException extends IllegalArgumentException{
    public DuplicateInterestException() {
        super();
    }

    public DuplicateInterestException(String s) {
        super(s);
    }

    public DuplicateInterestException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateInterestException(Throwable cause) {
        super(cause);
    }
}
