package io.github.tuuzed.minihttpd.example;

import io.github.tuuzed.minihttp.HttpServer;

public class Example {
    public static void main(String[] args) {
        HttpServer server = new HttpServer.Builder()
                .setBuffSize(102400)
                .setAddress("127.0.0.1")
                .setPort(5000)
                .setThreadNumber(Runtime.getRuntime().availableProcessors())
                .setTimeout(1000 * 30)
                .setDebug(true)
                .build();
        server.register("/", new IndexHttpHandler());
        server.listen();
    }
}
