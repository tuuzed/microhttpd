package io.github.tuuzed.minihttpd.example;

import io.github.tuuzed.minihttp.handler.HttpHandler;
import io.github.tuuzed.minihttp.request.Request;
import io.github.tuuzed.minihttp.response.Response;

import java.io.IOException;
import java.util.Date;

public class IndexHttpHandler extends HttpHandler {

    @Override
    public void doGet(Request request, Response response) throws IOException {
        response.setContentType("text/plain");
        response.addHeader("Date", new Date().toString());
        response.write("hello\n" + request.toString());
    }

    @Override
    public void doPost(Request request, Response response) throws IOException {
        response.setContentType("text/plain");
        response.addHeader("Date", new Date().toString());
        response.write("hello\n" + request.toString());
    }
}