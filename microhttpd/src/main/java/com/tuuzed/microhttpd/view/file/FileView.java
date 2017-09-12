package com.tuuzed.microhttpd.view.file;


import com.tuuzed.microhttpd.http.Request;
import com.tuuzed.microhttpd.http.Response;
import com.tuuzed.microhttpd.http.Status;
import com.tuuzed.microhttpd.common.util.Logger;
import com.tuuzed.microhttpd.view.View;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * 静态文件处理
 */
public class FileView implements View {
    private final static Logger logger = Logger.getLogger(FileView.class);

    private final static Pattern DEF_INDEX = Pattern.compile("^((index)|(default))\\.((htm)|(html))$");
    private String uriRegex;
    private String staticDir;

    public FileView(String uriRegex, String staticDir) {
        this(uriRegex, new File(staticDir));
    }

    public FileView(String uriRegex, File staticDir) {
        this.uriRegex = uriRegex;
        this.staticDir = staticDir.getAbsolutePath();
    }

    @Override
    public void serve(Request req, Response resp) throws IOException {
        logger.debug("Receive request...{}", req);
        String url = req.getUrl();
        // ^/fileview/.*  => /fileview/
        // ^/.*             => /
        String path = staticDir + File.separator + url.replaceFirst(uriRegex.substring(1, uriRegex.length() - 2), "");
        logger.debug("path = {}", path);
        File file = new File(path);
        if (!file.exists()) {
            // 文件不存在
            resp.renderError(Status.STATUS_404);
        } else if (file.isFile()) {
            // 存在且是一个文件
            resp.renderFile(file);
        } else if ("/".equals(url.substring(url.length() - 1)) && file.isDirectory()) {
            // URI定位的是一个文件夹且这个文件夹存在
            File[] files = file.listFiles();
            if (files == null) {
                // 空文件夹
                if (!file.getAbsolutePath().equals(staticDir)) {
                    // 返回上级目录
                    resp.renderHtml("<a href=\"../\">../</a><br/>\n");
                }
            } else {
                // 不是空文件夹
                // 查找默认首页
                for (File f : files) {
                    if (DEF_INDEX.matcher(f.getName()).find() && f.isFile()) {
                        logger.debug("Default home page: {}", f.getName());
                        if (file.length() > 0) {
                            resp.renderFile(file);
                        } else {
                            resp.renderHtml("");
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
                    resp.renderHtml(sb.toString());
                }
            }
        } else {
            resp.renderError(Status.STATUS_404);
        }
    }
}
