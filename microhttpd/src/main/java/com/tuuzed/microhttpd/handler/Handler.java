package com.tuuzed.microhttpd.handler;


import com.tuuzed.microhttpd.request.HttpRequest;
import com.tuuzed.microhttpd.response.HttpResponse;

import java.io.IOException;

public interface Handler {
    void serve(HttpRequest request, HttpResponse response) throws IOException;
}
