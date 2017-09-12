package com.tuuzed.microhttpd.common.util;

public class Logger {
    private static boolean debug;
    private static boolean printStackTrace;
    private String clazzName;

    public static void setDebug(boolean debug) {
        Logger.debug = debug;
    }

    public static void setPrintStackTrace(boolean printStackTrace) {
        Logger.printStackTrace = printStackTrace;
    }

    public Logger(String clazzName) {
        this.clazzName = clazzName;
    }

    public static Logger getLogger(Class clazz) {
        return new Logger(clazz.getName());
    }


    public void error(String message) {
        if (debug) {
            System.err.printf("[ERROR:]%s => %s%n", clazzName, message);
        }
    }

    public void error(String pattern, Object... args) {
        if (debug) {
            for (Object arg : args) {
                pattern = pattern.replace("{}", String.valueOf(arg));
            }
            System.err.printf("[ERROR:]%s => %s%n", clazzName, pattern);
        }
    }

    public void error(String pattern, Throwable throwable, Object... args) {
        if (printStackTrace) throwable.printStackTrace();
        error(pattern, args);
    }

    public void debug(String message) {
        if (debug) {
            System.out.printf("[DEBUG:]%s => %s%n", clazzName, message);
        }
    }

    public void debug(String pattern, Object... args) {
        if (debug) {
            for (Object arg : args) {
                pattern = pattern.replace("{}", String.valueOf(arg));
            }
            System.out.printf("[DEBUG:]%s => %s%n", clazzName, pattern);
        }
    }

    public void debug(String pattern, Throwable throwable, Object... args) {
        if (printStackTrace) throwable.printStackTrace();
        error(pattern, args);
    }
}
