package com.tuuzed.microhttpd.http.request;

public interface HttpRequest {

    String getMethod();

    String getUrl();

    String getProtocol();

    String getHeader(String key);

    String getParams(String key);

    String getData(String key);
}
