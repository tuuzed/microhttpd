package io.github.tuuzed.minihttp.handler;


import io.github.tuuzed.minihttp.request.Request;
import io.github.tuuzed.minihttp.response.Response;
import io.github.tuuzed.minihttp.response.Status;
import io.github.tuuzed.minihttp.util.Logger;
import io.github.tuuzed.minihttp.util.MimeType;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * 静态文件处理
 */
public class StaticFileHandler implements Handler {
    private static final Pattern sDefIndex = Pattern.compile("^((index)|(default))\\.((htm)|(html))$");
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
        File file = new File(path);
        if (!file.exists()) {
            // 文件不存在
            response.setStatus(Status.STATUS_404);
            response.write(Status.STATUS_404.toString());
        } else if (file.isDirectory()) {
            // 是一个文件夹
            String[] list = file.list();
            if (list == null) {
                // 空文件夹
                response.setStatus(Status.STATUS_404);
                response.write(Status.STATUS_404.toString());
            } else {
                // 不是空文件夹
                for (String s : list) {
                    if (sDefIndex.matcher(s).find()) {
                        sLogger.d("获取到默认首页HTML文件:" + s);
                        file = new File(file, s);
                        response.addHeader("Content-Type", MimeType.getMimeType(file));
                        response.addHeader("Content-Disposition", "filename=" + file.getName());
                        response.setStatus(Status.STATUS_200);
                        if (file.length() > 0) {
                            response.write(file);
                        } else {
                            response.write("");
                        }
                        return;
                    }
                }
            }
        } else {
            // 存在且是一个文件
            response.write(file);
        }
    }
}
