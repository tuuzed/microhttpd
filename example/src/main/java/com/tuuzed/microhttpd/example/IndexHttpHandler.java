package com.tuuzed.microhttpd.example;

import com.tuuzed.microhttpd.handler.HttpHandler;
import com.tuuzed.microhttpd.http.request.HttpRequest;
import com.tuuzed.microhttpd.http.response.HttpResponse;

import java.io.IOException;

public class IndexHttpHandler extends HttpHandler {

    @Override
    public void doGet(HttpRequest req, HttpResponse resp) throws IOException {
        resp.renderText("hello\n" + req.toString());
    }

    @Override
    public void doPost(HttpRequest req, HttpResponse resp) throws IOException {
        resp.renderText("hello\n" + req.toString());
    }
}