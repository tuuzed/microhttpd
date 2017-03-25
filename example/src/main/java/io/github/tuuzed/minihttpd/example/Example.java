package io.github.tuuzed.minihttpd.example;

import io.github.tuuzed.minihttp.MiniHTTPd;

public class Example {
    public static void main(String[] args) {
        MiniHTTPd server = new MiniHTTPd.Builder()
                .setBuffSize(1024)
                .setAddress("127.0.0.1")
                .setPort(5000)
                .setThreadNumber(Runtime.getRuntime().availableProcessors())
                .setTimeout(1000 * 30)
                .setStaticPath("D:\\blog\\public")
                .setStaticUriRegex("^/.*")
                .setDebug(true)
                .build();
        server.register("^/index$", new IndexHttpHandler());
        server.listen();
    }
}
