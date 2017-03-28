package io.github.tuuzed.microhttpd.example;

import io.github.tuuzed.microhttpd.MicroHTTPd;
import io.github.tuuzed.microhttpd.MicroHTTPdBuilder;

import java.io.IOException;

public class Example {
    public static void main(String[] args) {
        MicroHTTPd server = new MicroHTTPdBuilder()
                .setBuffSize(1024)
                .setBindPort(5000)
                .setTimeout(1000 * 3)
                .setStaticPath("D:\\")
                .setStaticUriRegex("^/.*")
                .setDebug(true)
                .build();
        server.register("^/index$", new IndexHttpHandler());
        try {
            server.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
