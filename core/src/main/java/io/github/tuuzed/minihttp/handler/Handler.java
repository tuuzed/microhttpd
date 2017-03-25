package io.github.tuuzed.minihttp.handler;


import io.github.tuuzed.minihttp.request.Request;
import io.github.tuuzed.minihttp.response.Response;

import java.io.IOException;

public interface Handler {
    void serve(Request request, Response response) throws IOException;
}
