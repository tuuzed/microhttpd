package io.github.tuuzed.microhttpd.staticfile;


import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import io.github.tuuzed.microhttpd.handler.Handler;
import io.github.tuuzed.microhttpd.request.Request;
import io.github.tuuzed.microhttpd.response.Response;
import io.github.tuuzed.microhttpd.response.Status;
import io.github.tuuzed.microhttpd.util.Logger;

/**
 * 静态文件处理
 */
public class StaticFileHandler implements Handler {
    private static final Pattern sDefIndex = Pattern.compile("^((index)|(default))\\.((htm)|(html))$");
    private final static Logger sLogger = Logger.getLogger(StaticFileHandler.class);
    private String uriRegex;
    private String staticDir;

    public StaticFileHandler(String uriRegex, String staticDir) {
        this(uriRegex, new File(staticDir));
    }

    public StaticFileHandler(String uriRegex, File staticDir) {
        this.uriRegex = uriRegex;
        this.staticDir = staticDir.getAbsolutePath();
    }

    @Override
    public void serve(Request request, Response response) throws IOException {
        sLogger.d("Receive request..." + request.toString());
        String url = request.getUrl();
        // ^/static/.*  => /static/
        // ^/.*         => /
        String path = staticDir + File.separator + url.replaceFirst(uriRegex.substring(1, uriRegex.length() - 2), "");
        sLogger.d("path =" + path);
        File file = new File(path);
        if (!file.exists()) {
            // 文件不存在
            response.setStatus(Status.STATUS_404);
            response.write(Status.STATUS_404.toString());
        } else if (file.isFile()) {
            // 存在且是一个文件
            response.setStatus(Status.STATUS_200);
            response.addHeader("Content-Length", String.valueOf(file.length()));
            response.addHeader("Content-Disposition", "inline; filename=" + file.getName());
            response.write(file);
        } else if ("/".equals(url.substring(url.length() - 1)) && file.isDirectory()) {
            // URI定位的是一个文件夹且这个文件夹存在
            File[] files = file.listFiles();
            if (files == null) {
                // 空文件夹
                response.setStatus(Status.STATUS_200);
                response.setContentType("text/html; charset=utf-8");
                if (!file.getAbsolutePath().equals(staticDir)) {
                    // 返回上级目录
                    response.write("<a href=\"../\">../</a><br/>");
                }
            } else {
                // 不是空文件夹
                // 查找默认首页
                for (File f : files) {
                    if (sDefIndex.matcher(f.getName()).find() && f.isFile()) {
                        sLogger.d("Default home page:" + f.getName());
                        file = f;
                        response.addHeader("Content-Type", MimeType.getMimeType(file));
                        response.addHeader("Content-Disposition", "inline; filename=" + file.getName());
                        response.setStatus(Status.STATUS_200);
                        if (file.length() > 0) {
                            response.addHeader("Content-Length", String.valueOf(file.length()));
                            response.write(file);
                        } else {
                            response.write("");
                        }
                        break;
                    }
                }
                // 没有找到默认首页文件
                if (file.isDirectory()) {
                    response.setStatus(Status.STATUS_200);
                    response.setContentType("text/html; charset=utf-8");
                    if (!file.getAbsolutePath().equals(staticDir)) {
                        // 返回上级目录
                        response.write("<a href=\"../\">../</a><br/>");
                    }
                    for (File f : files) {
                        if (f.isDirectory()) {
                            response.write(String.format("<a href=\"./%s/\">%s/</a><br/>",
                                    f.getName(), f.getName()));
                        } else {
                            response.write(String.format("<a href=\"./%s\">%s</a><br/>",
                                    f.getName(), f.getName()));
                        }
                    }
                }
            }
        } else {
            response.setStatus(Status.STATUS_404);
            response.write(Status.STATUS_404.toString());
        }
    }
}
