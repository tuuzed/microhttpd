package io.github.tuuzed.microhttpd.example;

import io.github.tuuzed.microhttpd.MicroHTTPd;
import io.github.tuuzed.microhttpd.MicroHTTPdBuilder;

import java.io.IOException;

public class Example {
    public static void main(String[] args) {
        MicroHTTPd server = new MicroHTTPdBuilder()
                .setBindPort(5000)
                .useStaticFileHandler("^/static/.*", "D:\\")
                .setDebug(true)
                .build();
        server.register("^/$", new IndexHttpHandler());
        try {
            server.startup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
