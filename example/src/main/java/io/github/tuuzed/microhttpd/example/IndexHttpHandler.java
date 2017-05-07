package io.github.tuuzed.microhttpd.example;

import io.github.tuuzed.microhttpd.handler.HttpHandler;
import io.github.tuuzed.microhttpd.request.Request;
import io.github.tuuzed.microhttpd.response.Response;

import java.io.IOException;
import java.util.Date;

public class IndexHttpHandler extends HttpHandler {

    @Override
    public void doGet(Request request, Response response) throws IOException {
        response.renderText("hello\n" + request.toString());
    }

    @Override
    public void doPost(Request request, Response response) throws IOException {
        response.renderText("hello\n" + request.toString());
    }
}