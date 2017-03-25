package io.github.tuuzed.minihttp.exception;

public class URIRegexException extends RuntimeException {
    public URIRegexException(String uriRegex) {
        super("uriRegex '" + uriRegex + "'不符合规范，uriRegex需要以'^/'开头 !");
    }
}