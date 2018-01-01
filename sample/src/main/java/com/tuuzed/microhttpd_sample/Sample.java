package com.tuuzed.microhttpd_sample;


import com.tuuzed.microhttpd.MicroHTTPd;

import java.io.IOException;

public class Simple {
    public static void main(String[] args) {
        MicroHTTPd server = new MicroHTTPd.Builder()
                .setPort(5000)
                .useFileView("^/static/.*", "C:\\")
                .debug(true, true)
                .build();
        server.registerView(new IndexView());
        server.registerView(new UploadView());
        try {
            server.startup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
