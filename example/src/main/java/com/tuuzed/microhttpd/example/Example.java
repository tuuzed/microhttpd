package com.tuuzed.microhttpd.example;


import com.tuuzed.microhttpd.MicroHTTPd;

import java.io.IOException;

public class Example {
    public static void main(String[] args) {
        MicroHTTPd server = new MicroHTTPd.Builder()
                .setPort(5000)
                .setPrefix("^/static/.*")
                .setPath("D:\\")
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
