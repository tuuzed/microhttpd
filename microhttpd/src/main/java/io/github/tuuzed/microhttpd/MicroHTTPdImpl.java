package io.github.tuuzed.microhttpd;


import io.github.tuuzed.microhttpd.exception.URIRegexException;
import io.github.tuuzed.microhttpd.handler.Handler;
import io.github.tuuzed.microhttpd.handler.StaticFileHandler;
import io.github.tuuzed.microhttpd.util.CloseableUtils;
import io.github.tuuzed.microhttpd.util.Logger;
import io.github.tuuzed.microhttpd.util.TextUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

class MicroHTTPdImpl implements MicroHTTPd {
    private static final Logger sLogger = Logger.getLogger(MicroHTTPdImpl.class);
    private RequestsDispatcher mDispatcher;
    private int mPort;
    private int mTimeout;
    private ServerSocket mServerSocket;

    MicroHTTPdImpl(MicroHTTPdBuilder builder) {
        Logger.setDebug(builder.debug);
        this.mPort = builder.port;
        this.mTimeout = builder.timeout;
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

    @Override
    public void listen() throws IOException {
        mServerSocket = new ServerSocket();
        mServerSocket.bind(new InetSocketAddress(mPort));
        new Thread(new ServerListenRunnable(mServerSocket, mDispatcher, mTimeout)).start();
        sLogger.d("Server is running");
    }

    @Override
    public void stop() {
        CloseableUtils.quietClose(mServerSocket);
    }

    @Override
    public void register(String regex, Handler handler) {
        if (regex.length() > 2 && "^/".equals(regex.substring(0, 2))) {
            mDispatcher.register(regex, handler);
        } else {
            throw new URIRegexException(regex);
        }
    }

}

