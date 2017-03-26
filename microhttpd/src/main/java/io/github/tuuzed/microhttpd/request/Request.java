package io.github.tuuzed.microhttpd.request;

public interface Request {
    String GET = "GET";
    String POST = "POST";
    String PUT = "PUT";
    String DELETE = "DELETE";
    String PATCH = "PATCH";
    String HEAD = "HEAD";
    String CONNECT = "CONNECT";
    String OPTIONS = "OPTIONS";
    String TRACE = "TRACE";

    String getMethod();

    String getUri();

    String getProtocol();

    String getHeader(String key);

    String getParams(String key);

    String getData(String key);
}
