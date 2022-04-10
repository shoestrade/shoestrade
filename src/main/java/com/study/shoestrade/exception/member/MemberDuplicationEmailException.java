package com.study.shoestrade.exception.member;

public class MemberDuplicationEmailException extends IllegalArgumentException{
    public MemberDuplicationEmailException() {
        super();
    }

    public MemberDuplicationEmailException(String s) {
        super(s);
    }

    public MemberDuplicationEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberDuplicationEmailException(Throwable cause) {
        super(cause);
    }
}
