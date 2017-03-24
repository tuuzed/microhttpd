package io.github.tuuzed.minihttp.response;


import io.github.tuuzed.minihttp.util.Logger;
import io.github.tuuzed.minihttp.util.MimeType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Pattern;

public class FileResponse extends BaseResponse {
    private static final Logger sLogger = Logger.getLogger(FileResponse.class);

    public FileResponse(File file) {
        // 如果文件不存在
        if (!file.exists()) {
            setStatus(Status.STATUS_404);
            setBody(Status.STATUS_404.toString().getBytes());
            return;
        }
        // 是一个文件夹
        if (file.isDirectory()) {
            String[] list = file.list();
            if (list == null) {
                setStatus(Status.STATUS_404);
                setBody(Status.STATUS_404.toString().getBytes());
                return;
            }
            Pattern pattern = Pattern.compile("^((index)|(default))\\.((htm)|(html))$");
            for (String s : list) {
                if (pattern.matcher(s).find()) {
                    sLogger.d("获取到默认首页HTML文件:" + s);
                    file = new File(file, s);
                    break;
                }
            }
        }
        // 还是一个目录
        if (file.isDirectory()) {
            setStatus(Status.STATUS_404);
            setBody(Status.STATUS_404.toString().getBytes());
            return;
        }
        // 文件存在
        setHeader(file);
        setStatus(Status.STATUS_200);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] bytes = new byte[fis.available()];
            int read = fis.read(bytes);
            setBody(bytes);
        } catch (IOException e) {
            sLogger.e(e);
            setStatus(Status.STATUS_500);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void setHeader(File file) {
        addHeader("Content-Type", MimeType.getMimeType(file));
        addHeader("Content-Disposition", "filename=" + file.getName());
    }
}
