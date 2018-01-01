package com.tuuzed.microhttpd_sample;

import com.tuuzed.microhttpd.annotation.Route;
import com.tuuzed.microhttpd.http.Request;
import com.tuuzed.microhttpd.http.Response;
import com.tuuzed.microhttpd.view.MethodView;

import java.io.IOException;

@Route("^/$")
public class IndexView extends MethodView {

    @Override
    public void doGet(Request req, Response resp) throws IOException {
        resp.renderText("hello\n" + req.toString());
    }

    @Override
    public void doPost(Request req, Response resp) throws IOException {
        resp.renderText("hello\n" + req.toString());
    }
}