package io.github.tuuzed.minihttpd.example;

import io.github.tuuzed.minihttp.HttpHandler;
import io.github.tuuzed.minihttp.request.Request;
import io.github.tuuzed.minihttp.response.Response;

import java.io.IOException;

public class IndexHttpHandler extends HttpHandler {

    @Override
    public void doGet(Request request, Response response) throws IOException {
        response.write("hello");
    }
}