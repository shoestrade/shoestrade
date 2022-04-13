package com.study.shoestrade.exception.address;

public class AddressNotFoundException extends IllegalArgumentException{
    public AddressNotFoundException() {
        super();
    }

    public AddressNotFoundException(String s) {
        super(s);
    }

    public AddressNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AddressNotFoundException(Throwable cause) {
        super(cause);
    }
}
