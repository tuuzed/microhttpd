package io.github.tuuzed.microhttpd.util;

public class Logger {
    private static boolean debug;
    private static boolean stacktrace;
    private String clazzName;

    public static void setDebug(boolean debug) {
        Logger.debug = debug;
    }

    public static void setStacktrace(boolean stacktrace) {
        Logger.stacktrace = stacktrace;
    }

    public Logger(String clazzName) {
        this.clazzName = clazzName;
    }

    public static Logger getLogger(Class clazz) {
        return new Logger(clazz.getName());
    }

    public void e(String msg) {
        if (debug) {
            System.err.printf("[ERROR:]%s => %s%n", clazzName, msg);
        }
    }

    public void e(Throwable throwable) {
        if (stacktrace) {
            throwable.printStackTrace();
        }
        System.err.printf("[ERROR:] %s => %s%n", clazzName, throwable.toString());
    }

    public void d(String msg) {
        if (debug) {
            System.out.printf("[DEBUG:] %s => %s%n", clazzName, msg);
        }
    }

    public void d(Throwable throwable) {
        if (stacktrace) {
            throwable.printStackTrace();
        }
        System.out.printf("[DEBUG:] %s => %s%n", clazzName, throwable.toString());
    }
}
