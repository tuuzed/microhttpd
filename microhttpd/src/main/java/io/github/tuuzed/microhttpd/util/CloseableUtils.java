package io.github.tuuzed.microhttpd.util;


import java.io.Closeable;
import java.io.IOException;


public class CloseableUtils {
    private static final Logger sLogger = Logger.getLogger(CloseableUtils.class);

    /**
     * 静默关闭可关闭的对象
     *
     * @param closeable:可关闭的对象
     */
    public static void quietClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                sLogger.e(e);
            }
        }
    }
}
