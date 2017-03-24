package io.github.tuuzed.minihttpd.example;

import io.github.tuuzed.minihttp.HttpHandler;
import io.github.tuuzed.minihttp.request.Request;
import io.github.tuuzed.minihttp.response.Response;

import java.io.IOException;

public class IndexHttpHandler extends HttpHandler {

    @Override
    public boolean doGet(Request request, Response response) {
        try {
            response.write("hello");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            response.finish();
        }
        return true;
    }
}