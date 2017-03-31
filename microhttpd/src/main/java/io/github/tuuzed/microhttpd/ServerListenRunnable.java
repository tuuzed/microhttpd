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
                Socket client = mServerSocket.accept();
                sLogger.d(String.format("Client (%d) connected...", client.hashCode()));
                client.setSoTimeout(mTimeout);
                mDispatcher.dispatch(client);
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
