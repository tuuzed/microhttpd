package io.github.tuuzed.microhttpd.example;

import io.github.tuuzed.microhttpd.MicroHTTPd;
import io.github.tuuzed.microhttpd.MicroHTTPdBuilder;

import java.io.IOException;

public class Example {
    public static void main(String[] args) {
        MicroHTTPd server = new MicroHTTPdBuilder()
                .setBuffSize(1024)
                .setBindAddress("127.0.0.1")
                .setBindPort(5000)
                .setTimeout(1000 * 3)
                .setStaticPath("D:\\")
                .setStaticUriRegex("^/.*")
                .setDebug(false)
                .build();
        server.register("^/index$", new IndexHttpHandler());
        try {
            server.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        server.stop();
    }
}
