package io.github.tuuzed.microhttpd.handler;


import io.github.tuuzed.microhttpd.request.Request;
import io.github.tuuzed.microhttpd.response.Response;

import java.io.IOException;

public interface Handler {
    void serve(Request request, Response response) throws IOException;
}
