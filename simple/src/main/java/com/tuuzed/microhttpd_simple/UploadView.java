package com.tuuzed.microhttpd_simple;

import com.tuuzed.microhttpd.annotation.Route;
import com.tuuzed.microhttpd.http.Request;
import com.tuuzed.microhttpd.http.Response;
import com.tuuzed.microhttpd.view.MethodView;

import java.io.IOException;

@Route("^/upload$")
public class UploadView extends MethodView {
    @Override
    public void doPost(Request req, Response resp) throws IOException {
        byte[] data = req.getData();
        resp.renderArrayByte(data);
    }
}
