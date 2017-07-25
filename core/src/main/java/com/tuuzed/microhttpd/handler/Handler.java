package com.tuuzed.microhttpd.handler;


import com.tuuzed.microhttpd.http.request.HttpRequest;
import com.tuuzed.microhttpd.http.response.HttpResponse;

import java.io.IOException;

public interface Handler {
    void serve(HttpRequest request, HttpResponse response) throws IOException;
}
