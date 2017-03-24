package io.github.tuuzed.minihttp;


import io.github.tuuzed.minihttp.request.Request;
import io.github.tuuzed.minihttp.response.Response;
import io.github.tuuzed.minihttp.util.Logger;

import java.io.File;
import java.io.IOException;

/**
 * 静态文件处理
 */
public class StaticFileHandler implements Handler {
    private final static Logger sLogger = Logger.getLogger(StaticFileHandler.class);
    private String uri;
    private String staticDir;

    public StaticFileHandler(String uri, File file) {
        this.uri = uri;
        this.staticDir = file.getAbsolutePath();
    }

    @Override
    public void serve(Request request, Response response) throws IOException {
        sLogger.d("接收到请求..." + request.toString());
        String uri = request.getUri();
        String path = uri.replace("/static", staticDir);
        response.write(new File(path));
    }
}