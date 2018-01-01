package com.tuuzed.microhttpd.common.util;

public class CloseableUtils {
    public static void safeClose(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                // pass
            }
        }
    }
}
