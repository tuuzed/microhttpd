package io.github.tuuzed.minihttp.util;

public class LogUtils {
    private static boolean debug;

    public static void setDebug(boolean debug) {
        LogUtils.debug = debug;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void e(String tag, String msg) {
        if (debug) {
            System.err.println(tag + " => " + msg);
        }
    }

    public static void e(String tag, Throwable throwable) {
        if (debug) {
            throwable.printStackTrace();
            System.err.println(tag + " => " + throwable.toString());
        }
    }

    public static void d(String tag, String msg) {
        if (debug) {
            System.out.println(tag + " => " + msg);
        }
    }

    public static void d(String tag, Throwable throwable) {
        if (debug) {
            throwable.printStackTrace();
            System.out.println(tag + " => " + throwable.toString());
        }
    }
}
