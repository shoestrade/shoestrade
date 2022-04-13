package com.study.shoestrade.exception.member;

public class MemberNotFoundException extends IllegalArgumentException {
    public MemberNotFoundException() {
    }

    public MemberNotFoundException(String s) {
        super(s);
    }

    public MemberNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberNotFoundException(Throwable cause) {
        super(cause);
    }
}
