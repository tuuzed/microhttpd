package io.github.tuuzed.minihttpd.example;

import io.github.tuuzed.minihttp.HttpHandler;
import io.github.tuuzed.minihttp.request.Request;
import io.github.tuuzed.minihttp.response.Response;
import io.github.tuuzed.minihttp.response.StringResponse;

public class IndexHttpHandler extends HttpHandler {

    @Override
    public Response doGet(Request request) {
        return new StringResponse("hello");
    }
}