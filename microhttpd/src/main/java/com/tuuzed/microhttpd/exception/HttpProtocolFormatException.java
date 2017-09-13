package com.tuuzed.microhttpd.exception;

/**
 * HTTP协议格式异常
 */
public class HttpProtocolFormatException extends MicroHttpdException {
    public HttpProtocolFormatException() {
    }

    public HttpProtocolFormatException(String message) {
        super(message);
    }

    public HttpProtocolFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpProtocolFormatException(Throwable cause) {
        super(cause);
    }

    public HttpProtocolFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
