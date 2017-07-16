package com.tuuzed.microhttpd.util;


import java.io.Closeable;
import java.io.IOException;

public class CloseableUtils {
    private static final Logger logger = Logger.getLogger(CloseableUtils.class);

    public static void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                logger.e(e);
            }
        }
    }
}
