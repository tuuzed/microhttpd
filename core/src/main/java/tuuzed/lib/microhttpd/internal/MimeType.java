package tuuzed.lib.microhttpd.internal;


import org.jetbrains.annotations.NotNull;
import tuuzed.lib.microhttpd.util.CloseUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MimeType {
    private Map<String, String> mineType;
    private static final String DEFAULT_TYPE = "application/octet-stream";

    private MimeType() {
        mineType = new HashMap<>(327);
        URL url = getClass().getResource("/mimetypes.txt");
        TextFileReader reader = null;
        try {
            reader = new TextFileReader(url.getFile());
            String line;
            while ((line = reader.readLine()) != null) {
                String[] arrLocal = line.split(" ");
                mineType.put(arrLocal[0], arrLocal[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtils.close(reader);
        }
    }

    private static class InstanceHolder {
        private final static MimeType instance = new MimeType();
    }

    public static MimeType getInstance() {
        return InstanceHolder.instance;
    }

    public String get(@NotNull String filename) {
        int lastIndexOf = filename.lastIndexOf(".");
        if (lastIndexOf != -1) {
            String type = mineType.get(filename.substring(lastIndexOf));
            if (type != null) {
                return type;
            }
        }
        return DEFAULT_TYPE;
    }

    public String get(@NotNull File file) {
        return get(file.getName());
    }
}
