package com.tuuzed.microhttpd.staticfile;


import com.tuuzed.microhttpd.handler.Handler;
import com.tuuzed.microhttpd.http.request.HttpRequest;
import com.tuuzed.microhttpd.http.response.HttpResponse;
import com.tuuzed.microhttpd.http.response.Status;
import com.tuuzed.microhttpd.util.Logger;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * 静态文件处理
 */
public class StaticFileHandler implements Handler {
    private final static Logger logger = Logger.getLogger(StaticFileHandler.class);
    private final static Pattern DEF_INDEX = Pattern.compile("^((index)|(default))\\.((htm)|(html))$");
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
    public void serve(HttpRequest request, HttpResponse response) throws IOException {
        logger.d("Receive request..." + request.toString());
        String url = request.getUrl();
        // ^/staticfile/.*  => /staticfile/
        // ^/.*             => /
        String path = staticDir + File.separator + url.replaceFirst(uriRegex.substring(1, uriRegex.length() - 2), "");
        logger.d("path =" + path);
        File file = new File(path);
        if (!file.exists()) {
            // 文件不存在
            response.renderError(Status.STATUS_404);
        } else if (file.isFile()) {
            // 存在且是一个文件
            response.renderFile(file);
        } else if ("/".equals(url.substring(url.length() - 1)) && file.isDirectory()) {
            // URI定位的是一个文件夹且这个文件夹存在
            File[] files = file.listFiles();
            if (files == null) {
                // 空文件夹
                if (!file.getAbsolutePath().equals(staticDir)) {
                    // 返回上级目录
                    response.renderHtml("<a href=\"../\">../</a><br/>\n");
                }
            } else {
                // 不是空文件夹
                // 查找默认首页
                for (File f : files) {
                    if (DEF_INDEX.matcher(f.getName()).find() && f.isFile()) {
                        logger.d("Default home page:" + f.getName());
                        if (file.length() > 0) {
                            response.renderFile(file);
                        } else {
                            response.renderHtml("");
                        }
                        break;
                    }
                }
                // 没有找到默认首页文件
                if (file.isDirectory()) {
                    StringBuilder sb = new StringBuilder();
                    if (!file.getAbsolutePath().equals(staticDir)) {
                        // 返回上级目录
                        sb.append("<a href=\"../\">../</a><br/>\n");
                    }
                    for (File f : files) {
                        if (f.isDirectory()) {
                            sb.append(String.format("<a href=\"./%s/\">%s/</a><br/>\n",
                                    f.getName(), f.getName()));
                        } else {
                            sb.append(String.format("<a href=\"./%s\">%s</a><br/>\n",
                                    f.getName(), f.getName()));
                        }
                    }
                    response.renderHtml(sb.toString());
                }
            }
        } else {
            response.renderError(Status.STATUS_404);
        }
    }
}
