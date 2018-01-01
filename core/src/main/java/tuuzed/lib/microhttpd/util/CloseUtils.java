package tuuzed.lib.microhttpd.util;

import java.io.Closeable;
import java.lang.reflect.Method;

public class CloseUtils {


    public static void close(AutoCloseable... closeables) {
        if (closeables != null) {
            for (AutoCloseable it : closeables) {
                if (it != null) {
                    try {
                        it.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void close(Closeable... closeables) {
        if (closeables != null) {
            for (Closeable it : closeables) {
                if (it != null) {
                    try {
                        it.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void close(Object... objects) {
        if (objects != null) {
            for (Object it : objects) {
                if (it != null) {
                    try {
                        Method close = it.getClass().getMethod("close");
                        close.invoke(it);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
