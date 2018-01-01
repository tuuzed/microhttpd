package com.tuuzed.microhttpd.common.log;

import org.jetbrains.annotations.NotNull;

public class SimpleLogger implements Logger {
    private volatile boolean debug;
    private volatile boolean printStackTrace;
    private String name;

    private SimpleLogger(Builder builder) {
        this.debug = builder.debug;
        this.printStackTrace = builder.printStackTrace;
        this.name = builder.name;
    }

    @Override
    public void debug(String message) {
        if (debug) {
            String threadName = Thread.currentThread().getName();
            System.out.printf("[DEBUG] [%s] %s => %s%n", threadName, name, message);
        }
    }

    @Override
    public void debug(String pattern, Object... args) {
        if (debug) {
            for (Object arg : args) {
                pattern = pattern.replace("{}", String.valueOf(arg));
            }
            String threadName = Thread.currentThread().getName();
            System.out.printf("[DEBUG] [%s] %s => %s%n", threadName, name, pattern);
        }
    }

    @Override
    public void debug(String pattern, Throwable throwable, Object... args) {
        if (printStackTrace) {
            throwable.printStackTrace();
        }
        debug(pattern, args);
    }

    @Override
    public void error(String message) {
        if (debug) {
            String threadName = Thread.currentThread().getName();
            System.err.printf("[ERROR] [%s] %s => %s%n", threadName, name, message);
        }

    }

    @Override
    public void error(String pattern, Object... args) {
        if (debug) {
            for (Object arg : args) {
                pattern = pattern.replace("{}", String.valueOf(arg));
            }
            String threadName = Thread.currentThread().getName();
            System.err.printf("[ERROR] [%s] %s => %s%n", threadName, name, pattern);
        }
    }

    @Override
    public void error(String pattern, Throwable throwable, Object... args) {
        if (printStackTrace) {
            throwable.printStackTrace();
        }
        debug(pattern, args);
    }


    public static class Builder implements com.tuuzed.microhttpd.common.Builder<Logger> {
        private boolean debug;
        private boolean printStackTrace;
        private String name;

        public Builder setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Builder setPrintStackTrace(boolean printStackTrace) {
            this.printStackTrace = printStackTrace;
            return this;
        }

        public Builder setName(@NotNull String name) {
            this.name = name;
            return this;
        }

        @Override
        public Logger build() {
            return new SimpleLogger(this);
        }
    }
}
