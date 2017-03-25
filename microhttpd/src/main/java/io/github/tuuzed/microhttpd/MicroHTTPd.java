package io.github.tuuzed.microhttpd;


import io.github.tuuzed.microhttpd.exception.URIRegexException;
import io.github.tuuzed.microhttpd.handler.Handler;
import io.github.tuuzed.microhttpd.handler.StaticFileHandler;
import io.github.tuuzed.microhttpd.util.Logger;
import io.github.tuuzed.microhttpd.util.TextUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MicroHTTPd {
    private static final Logger sLogger = Logger.getLogger(MicroHTTPd.class);
    private RequestsDispatcher mDispatcher;
    private String address;
    private int port;
    private int timeout;

    private MicroHTTPd(Builder builder) {
        Logger.setDebug(builder.debug);
        this.address = builder.address;
        this.port = builder.port;
        this.timeout = builder.timeout;
        mDispatcher = new RequestsDispatcher(builder.threadNumber, builder.buffSize);
        if (!TextUtils.isEmpty(builder.staticPath)) {
            File file = new File(builder.staticPath);
            if (file.exists() && file.isDirectory()) {
                if (TextUtils.isEmpty(builder.staticUriRegex)) {
                    builder.staticUriRegex = "^/static/.*";
                }
                register(builder.staticUriRegex, new StaticFileHandler(builder.staticUriRegex, file));
            }
        }
    }

    /**
     * 开启服务器
     */
    public void listen() {
        new Thread(new Runnable() {
            ServerSocket serverSocket = null;

            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket();
                    serverSocket.bind(new InetSocketAddress(address, port));
                    for (; ; ) {
                        Socket socket = serverSocket.accept();
                        sLogger.d(String.format("客户端(%d)连入...", hashCode()));
                        socket.setSoTimeout(timeout);
                        mDispatcher.dispatch(socket);
                    }
                } catch (IOException e) {
                    sLogger.e(e);
                } finally {
                    if (serverSocket != null) {
                        try {
                            serverSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
        sLogger.d(String.format("服务器正在运行: http://%s:%d", address, port));
    }

    /**
     * 注册 Handler
     *
     * @param regex   :URI 正则表达式
     * @param handler :处理者
     */
    public void register(String regex, Handler handler) {
        if (regex.length() > 2 && "^/".equals(regex.substring(0, 2))) {
            mDispatcher.register(regex, handler);
        } else {
            throw new URIRegexException(regex);
        }
    }

    public static class Builder {
        private String address;
        private int port;
        private int threadNumber;
        private int buffSize;
        private int timeout;
        private boolean debug;
        private String staticPath;
        private String staticUriRegex;

        public Builder setBindAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setBindPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setFixedThreadNumber(int threadNumber) {
            this.threadNumber = threadNumber;
            return this;
        }

        public Builder setBuffSize(int buffSize) {
            this.buffSize = buffSize;
            return this;
        }

        public Builder setTimeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Builder setStaticPath(String staticPath) {
            this.staticPath = staticPath;
            return this;
        }

        public Builder setStaticUriRegex(String staticUriRegex) {
            this.staticUriRegex = staticUriRegex;
            return this;
        }

        public MicroHTTPd build() {
            // 默认绑定地址为127.0.0.1
            if (TextUtils.isEmpty(this.address)) {
                this.address = "127.0.0.1";
            }
            // 默认绑定端口号为5000
            if (this.port == 0) {
                this.port = 5000;
            }
            // 默认缓存大小为1kb
            if (this.buffSize == 0) {
                buffSize = 1024;
            }
            // 默认超时为30秒
            if (this.timeout == 0) {
                this.timeout = 1000 * 30;
            }
            return new MicroHTTPd(this);
        }
    }
}

