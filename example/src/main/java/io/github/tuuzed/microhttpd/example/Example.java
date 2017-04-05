package io.github.tuuzed.microhttpd.example;

import java.io.IOException;

import io.github.tuuzed.microhttpd.MicroHTTPd;
import io.github.tuuzed.microhttpd.MicroHTTPdBuilder;
import io.github.tuuzed.microhttpd.staticfile.StaticFileHandler;

public class Example {
    public static void main(String[] args) {
        MicroHTTPd server = new MicroHTTPdBuilder()
                .setBindPort(5000)
                .setDebug(true)
                .build();
        server.register("^/static/.*", new StaticFileHandler("^/static/.*", "C:\\"));
        server.register("^/index$", new IndexHttpHandler());
        try {
            server.startup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
