package io.github.tuuzed.microhttpd.example;

import io.github.tuuzed.microhttpd.MicroHTTPd;

public class Example {
    public static void main(String[] args) {
        MicroHTTPd server = new MicroHTTPd.Builder()
                .setBuffSize(1024)
                .setBindAddress("127.0.0.1")
                .setBindPort(5000)
                .setFixedThreadNumber(Runtime.getRuntime().availableProcessors())
                .setTimeout(1000 * 30)
                .setStaticPath("D:\\blog\\public")
                .setStaticUriRegex("^/.*")
                .setDebug(true)
                .build();
        server.register("^/index$", new IndexHttpHandler());
        server.listen();
    }
}
