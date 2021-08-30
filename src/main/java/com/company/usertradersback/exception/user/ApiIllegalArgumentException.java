package com.company.usertradersback.exception.user;

//광범위한 RuntimeException중 IllegalStateException
public class ApiIllegalArgumentException extends IllegalArgumentException {
    public ApiIllegalArgumentException(final String msg) {
        super(msg);
    }
}
