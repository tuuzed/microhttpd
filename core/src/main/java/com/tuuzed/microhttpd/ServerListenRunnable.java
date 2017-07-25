package com.tuuzed.microhttpd;

import com.tuuzed.microhttpd.util.CloseableUtils;
import com.tuuzed.microhttpd.util.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 监听连接
 */
class ServerListenRunnable implements Runnable {
    private static final Logger logger = Logger.getLogger(ServerListenRunnable.class);
    private RequestsDispatcher mDispatcher;
    private ServerSocket mServerSocket;
    private int mTimeout;

    ServerListenRunnable(ServerSocket serverSocket, RequestsDispatcher dispatcher, int timeout) {
        mServerSocket = serverSocket;
        mDispatcher = dispatcher;
        mTimeout = timeout;
    }

    @Override
    public void run() {
        try {
            while (!mServerSocket.isClosed()) {
                logger.d("accepting...");
                Socket connect = mServerSocket.accept();
                logger.d(String.format("Client (%d) connected...", connect.hashCode()));
                connect.setSoTimeout(mTimeout);
                mDispatcher.dispatch(connect);
            }
        } catch (IOException e) {
            logger.e(e);
        } finally {
            CloseableUtils.safeClose(mServerSocket);
        }
    }
}
