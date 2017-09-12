package com.tuuzed.microhttpd;


import com.tuuzed.microhttpd.annotation.Route;
import com.tuuzed.microhttpd.common.util.CloseableUtils;
import com.tuuzed.microhttpd.common.util.Logger;
import com.tuuzed.microhttpd.common.util.StringUtils;
import com.tuuzed.microhttpd.view.View;
import com.tuuzed.microhttpd.view.file.FileView;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.Executors;

public class MicroHTTPd {
    private static final Logger logger = Logger.getLogger(MicroHTTPd.class);
    private RequestDispatcher mDispatcher;
    private int mPort;
    private int mTimeout;
    private ServerSocket mServerSocket;

    private MicroHTTPd(@NotNull Builder builder) {
        Logger.setDebug(builder.debug);
        Logger.setPrintStackTrace(builder.printStacktrace);
        this.mPort = builder.port;
        this.mTimeout = builder.timeout;
        this.mDispatcher = RequestDispatcher.create(builder.threads);
        if (!StringUtils.isEmpty(builder.prefix) && builder.prefix.startsWith("^/")) {
            register(builder.prefix, new FileView(builder.prefix, builder.path));
        }
    }

    /**
     * 启动
     */
    public void startup() throws IOException {
        mServerSocket = new ServerSocket();
        mServerSocket.bind(new InetSocketAddress(mPort));
        Executors.newSingleThreadExecutor()
                .execute(ServerListenHandler.create(mServerSocket, mDispatcher, mTimeout));
        logger.debug("Server is running at http://localhost:{}", mPort);
    }

    /**
     * 停止
     */
    public void stop() {
        CloseableUtils.safeClose(mServerSocket);
    }

    public void register(@NotNull View view) {
        Route route = view.getClass().getAnnotation(Route.class);
        if (route != null) {
            register(route.value(), view);
        } else {
            throw new RuntimeException("Not Route");
        }
    }

    /**
     * 注册 View
     *
     * @param route 路由(支持正则匹配)
     * @param view  处理者
     */
    public void register(@NotNull String route, @NotNull View view) {
        if (!StringUtils.isEmpty(route) && route.startsWith("^/")) {
            mDispatcher.register(route, view);
        } else {
            throw new RuntimeException(String.format("route '%s' Non conformity,route needs to start '^/'!", route));
        }
    }

    public static class Builder implements com.tuuzed.microhttpd.common.Builder<MicroHTTPd> {
        private Integer port;
        private int threads;
        private Integer timeout;
        private String charset;
        private String prefix;
        private String path;
        // 调试
        boolean debug;
        boolean printStacktrace;

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setThreads(int threads) {
            this.threads = threads;
            return this;
        }

        public Builder setTimeout(int timeout) {
            this.timeout = timeout;
            return this;
        }


        public Builder setCharset(String charset) {
            this.charset = charset;
            return this;
        }

        public Builder useFileView(String prefix, String path) {
            this.prefix = prefix;
            this.path = path;
            return this;
        }

        public Builder debug(boolean debug, boolean printStacktrace) {
            this.debug = debug;
            this.printStacktrace = printStacktrace;
            return this;
        }

        @Override
        public MicroHTTPd build() {
            // 默认绑定端口号为5000
            if (port == null) port = 5000;
            // 默认超时为30秒
            if (timeout == null) timeout = 30_000;
            if (charset == null) charset = "utf-8";
            return new MicroHTTPd(this);
        }

    }
}

