package io.github.tuuzed.microhttpd;


import io.github.tuuzed.microhttpd.handler.Handler;
import io.github.tuuzed.microhttpd.util.Logger;

import java.io.Closeable;
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
        Logger.setStacktrace(builder.stacktrace);
        this.mPort = builder.port;
        this.mTimeout = builder.timeout;
        mDispatcher = new RequestsDispatcher(builder.threadNumber, builder.bufSize);
    }

    @Override
    public void startup() throws IOException {
        mServerSocket = new ServerSocket();
        mServerSocket.bind(new InetSocketAddress(mPort));
        new Thread(new ServerListenRunnable(mServerSocket, mDispatcher, mTimeout)).start();
        sLogger.d("Server is running at http://localhost:" + mPort);
    }

    @Override
    public void stop() {
        safeClose(mServerSocket);
    }

    @Override
    public void register(String route, Handler handler) {
        if (route.length() > 2 && "^/".equals(route.substring(0, 2))) {
            mDispatcher.register(route, handler);
        } else {
            throw new RuntimeException(String.format("uriRegex '%s' Non conformity,UriRegex needs to start '^/'!", route));
        }
    }

    private void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                sLogger.e(e);
            }
        }
    }

}

