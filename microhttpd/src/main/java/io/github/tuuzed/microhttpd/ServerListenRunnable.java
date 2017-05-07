package io.github.tuuzed.microhttpd;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import io.github.tuuzed.microhttpd.util.Logger;

class ServerListenRunnable implements Runnable {
    private static final Logger sLogger = Logger.getLogger(ServerListenRunnable.class);
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
                sLogger.d("accepting...");
                Socket connect = mServerSocket.accept();
                sLogger.d(String.format("Client (%d) connected...", connect.hashCode()));
                connect.setSoTimeout(mTimeout);
                mDispatcher.dispatch(connect);
            }
        } catch (IOException e) {
            sLogger.e(e);
        } finally {
            if (mServerSocket != null) {
                try {
                    mServerSocket.close();
                } catch (IOException e) {
                    sLogger.e(e);
                }
            }
        }
    }
}
