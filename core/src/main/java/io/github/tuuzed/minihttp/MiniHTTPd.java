package io.github.tuuzed.minihttp;


import io.github.tuuzed.minihttp.util.LogUtils;
import io.github.tuuzed.minihttp.util.TextUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    private static final String TAG = "HttpServer";
    private RequestsDispatcher mDispatcher;
    private String address;
    private int port;
    private int timeout;

    private HttpServer(Builder builder) {
        this.address = builder.address;
        this.port = builder.port;
        this.timeout = builder.timeout;
        LogUtils.setDebug(builder.debug);
        mDispatcher = new RequestsDispatcher(builder.threadNumber, builder.buffSize);
    }

    /**
     * 开启服务器
     */
    public void listen() {
        run(address, port);
    }

    /**
     * 注册 Handler
     *
     * @param uri     :uri
     * @param handler :处理者
     */
    public void register(String uri, Handler handler) {
        mDispatcher.register(uri, handler);
    }

    private void run(final String address, final int port) {
        new Thread(new Runnable() {
            ServerSocket serverSocket = null;

            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket();
                    serverSocket.bind(new InetSocketAddress(address, port));
                    while (true) {
                        Socket socket = serverSocket.accept();
                        LogUtils.d(TAG, String.format("客户端(%d)连入...", hashCode()));
                        socket.setSoTimeout(timeout);
                        mDispatcher.dispatch(socket);
                    }
                } catch (IOException e) {
                    LogUtils.e(TAG, e);
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
        LogUtils.d(TAG, String.format("Server is running http://%s:%d", address, port));
    }

    public static class Builder {
        private String address;
        private int port;
        private int threadNumber;
        private int buffSize;
        private int timeout;
        private boolean debug;

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setThreadNumber(int threadNumber) {
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

        public HttpServer build() {
            if (TextUtils.isEmpty(this.address)) {
                this.address = "127.0.0.1";
            }
            if (this.port == 0) {
                this.port = 5000;
            }
            if (this.threadNumber == 0) {
                this.threadNumber = Runtime.getRuntime().availableProcessors();
            }
            if (this.buffSize == 0) {
                // 100kb
                buffSize = 1024 * 100;
            }
            if (this.timeout == 0) {
                this.timeout = Integer.MAX_VALUE;
            }
            return new HttpServer(this);
        }
    }
}

