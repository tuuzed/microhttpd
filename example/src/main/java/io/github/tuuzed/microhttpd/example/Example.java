package io.github.tuuzed.microhttpd.example;

import io.github.tuuzed.microhttpd.MicroHTTPd;
import io.github.tuuzed.microhttpd.MicroHTTPdBuilder;
import io.github.tuuzed.microhttpd.staticfile.StaticFileHandler;

import java.io.IOException;

public class Example {
    public static void main(String[] args) {
        MicroHTTPd server = new MicroHTTPdBuilder()
                .setBindPort(5000)
                .build();
        server.register("^/static/.*", new StaticFileHandler("^/static/.*", "D:\\"));
        server.register("^/index$", new IndexHttpHandler());
        try {
            server.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
