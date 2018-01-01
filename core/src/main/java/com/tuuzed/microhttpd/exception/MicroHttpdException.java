package com.tuuzed.microhttpd.exception;

public class MicroHttpdException extends Exception {
    public MicroHttpdException() {
        super();
    }

    public MicroHttpdException(String message) {
        super(message);
    }

    public MicroHttpdException(String message, Throwable cause) {
        super(message, cause);
    }

    public MicroHttpdException(Throwable cause) {
        super(cause);
    }

    protected MicroHttpdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
