package io.github.tuuzed.minihttp.handler;


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
    private String uriRegex;
    private String staticDir;

    public StaticFileHandler(String uriRegex, File file) {
        this.uriRegex = uriRegex;
        this.staticDir = file.getAbsolutePath();
    }

    @Override
    public void serve(Request request, Response response) throws IOException {
        sLogger.d("接收到请求..." + request.toString());
        String uri = request.getUri();
        // ^/static/.*  => /static/
        // ^/.*         => /
        String path = staticDir + File.separator + uri.replaceFirst(uriRegex.substring(1, uriRegex.length() - 2), "");
        sLogger.d("path =" + path);
        response.write(new File(path));
    }
}
