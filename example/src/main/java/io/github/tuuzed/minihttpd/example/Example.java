package io.github.tuuzed.minihttpd.example;

import io.github.tuuzed.minihttp.MiniHTTPd;

public class Example {
    public static void main(String[] args) {
        MiniHTTPd server = new MiniHTTPd.Builder()
                .setBuffSize(1024 * 100)
                .setAddress("127.0.0.1")
                .setPort(5000)
                .setThreadNumber(Runtime.getRuntime().availableProcessors())
                .setTimeout(1000 * 30)
                .setStaticPath("E:\\")
                .setDebug(true)
                .build();
        server.register("^/$", new IndexHttpHandler());
        server.listen();
    }
}
