package com.tuuzed.microhttpd.request;

import java.util.Map;

public interface HttpRequest {

    String getMethod();

    String getUrl();

    String getProtocol();

    String getHeader(String key);

    Map<String, String> getHeaders();

    String getParam(String key);

    Map<String, String> getParams();

    String getData(String key);

    Map<String, String> getAllData();
}
