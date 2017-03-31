package io.github.tuuzed.microhttpd.request;

public interface Request {

    String getMethod();

    String getUrl();

    String getProtocol();

    String getHeader(String key);

    String getParams(String key);

    String getData(String key);
}
