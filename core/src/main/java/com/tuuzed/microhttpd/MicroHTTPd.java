package com.tuuzed.microhttpd;


import com.tuuzed.microhttpd.handler.Handler;
import com.tuuzed.microhttpd.staticfile.StaticFileHandler;
import com.tuuzed.microhttpd.util.CloseableUtils;
import com.tuuzed.microhttpd.util.Logger;
import com.tuuzed.microhttpd.util.TextUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class MicroHTTPd {
    private static final Logger logger = Logger.getLogger(MicroHTTPd.class);
    private RequestsDispatcher mDispatcher;
    private int mPort;
    private int mTimeout;
    private ServerSocket mServerSocket;

    private MicroHTTPd(Builder builder) {
        Logger.setDebug(builder.debug);
        Logger.setStacktrace(builder.stacktrace);
        this.mPort = builder.port;
        this.mTimeout = builder.timeout;
        this.mDispatcher = new RequestsDispatcher(builder.threadNumber);
        if (!TextUtils.isEmpty(builder.prefix) && builder.prefix.startsWith("^/")) {
            register(builder.prefix,
                    new StaticFileHandler(builder.prefix, builder.path));
        }
    }

    /**
     * 启动
     */
    public void startup() throws IOException {
        mServerSocket = new ServerSocket();
        mServerSocket.bind(new InetSocketAddress(mPort));
        new Thread(new ServerListenRunnable(mServerSocket, mDispatcher, mTimeout)).start();
        logger.d("Server is running at http://localhost:" + mPort);
    }

    /**
     * 停止
     */
    public void stop() {
        CloseableUtils.safeClose(mServerSocket);
    }

    /**
     * 注册 Handler
     *
     * @param route   路由(支持正则匹配)
     * @param handler 处理者
     */
    public void register(String route, Handler handler) {
        if (!TextUtils.isEmpty(route) && route.startsWith("^/")) {
            mDispatcher.register(route, handler);
        } else {
            throw new RuntimeException(String.format("uriRegex '%s' Non conformity,UriRegex needs to start '^/'!", route));
        }
    }

    public static class Builder {
        private Integer port;
        private int threadNumber;
        private Integer timeout;
        private String prefix;
        private String path;
        // 调试
        boolean debug;
        boolean stacktrace;

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setThreadNumber(int threadNumber) {
            this.threadNumber = threadNumber;
            return this;
        }

        public Builder setTimeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder setPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public Builder setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Builder setStacktrace(boolean stacktrace) {
            this.stacktrace = stacktrace;
            return this;
        }

        public MicroHTTPd build() {
            // 默认绑定端口号为5000
            if (port == null) port = 5000;
            // 默认超时为3秒
            if (timeout == null) timeout = 3000;
            return new MicroHTTPd(this);
        }

    }
}

