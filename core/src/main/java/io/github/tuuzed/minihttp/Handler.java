package io.github.tuuzed.minihttp;


import io.github.tuuzed.minihttp.request.Request;
import io.github.tuuzed.minihttp.response.Response;

public interface Handler {
    boolean serve(Request request, Response response);
}
