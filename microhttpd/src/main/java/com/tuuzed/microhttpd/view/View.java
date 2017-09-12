package com.tuuzed.microhttpd.view;


import com.tuuzed.microhttpd.http.Request;
import com.tuuzed.microhttpd.http.Response;

import java.io.IOException;

public interface View {
    void serve(Request req, Response resp) throws IOException;
}
