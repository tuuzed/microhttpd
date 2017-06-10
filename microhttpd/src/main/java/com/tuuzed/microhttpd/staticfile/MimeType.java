package com.tuuzed.microhttpd.staticfile;

import com.tuuzed.microhttpd.io.TextFileReader;
import com.tuuzed.microhttpd.util.CloseableSupport;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class MimeType {
    private Map<String, String> mimeType = new HashMap<>();

    private static final String DEFAULT_TYPE = "application/octet-stream";

    private static MimeType instance;

    private MimeType() {
        URL url = MimeType.class.getResource("/mime-type.txt");
        TextFileReader reader = null;
        try {
            reader = new TextFileReader(url.getFile());
            String line;
            while ((line = reader.readLine()) != null) {
                String[] array = line.split(" ");
                mimeType.put(array[0], array[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseableSupport.safeClose(reader);
        }
    }

    public static MimeType getInstance() {
        if (instance == null) {
            synchronized (MimeType.class) {
                if (instance == null) {
                    instance = new MimeType();
                }
            }
        }
        return instance;
    }

    public String get(String filename) {
        int lastIndexOf = filename.lastIndexOf(".");
        if (lastIndexOf > -1) {
            String type = mimeType.get(filename.substring(lastIndexOf));
            if (type != null) {
                return type;
            }
        }
        return DEFAULT_TYPE;
    }

    public String get(File file) {
        return get(file.getName());
    }
}
