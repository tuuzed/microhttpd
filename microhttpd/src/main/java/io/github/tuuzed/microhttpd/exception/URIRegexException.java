package io.github.tuuzed.microhttpd.exception;

/**
 * URI格式错误异常
 */
public class URIRegexException extends RuntimeException {
    public URIRegexException(String uriRegex) {
        super("uriRegex '" + uriRegex + "' Non conformity,UriRegex needs to start '^/'!");
    }
}
