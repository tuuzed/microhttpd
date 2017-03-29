package io.github.tuuzed.microhttpd;

import io.github.tuuzed.microhttpd.util.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class ServerListenRunnable implements Runnable {
    private static final Logger sLogger = Logger.getLogger(ServerListenRunnable.class);
    private RequestsDispatcher mDispatcher;
    private ServerSocket mServerSocket;
    private int timeout;

    ServerListenRunnable(ServerSocket serverSocket, RequestsDispatcher dispatcher, int timeout) {
        this.mServerSocket = serverSocket;
        this.mDispatcher = dispatcher;
        this.timeout = timeout;
    }

    @Override
    public void run() {
        try {
            while (!mServerSocket.isClosed()) {
                sLogger.d("accepting...");
                Socket client = mServerSocket.accept();
                sLogger.d(String.format("Client (%d) connected...", client.hashCode()));
                client.setSoTimeout(timeout);
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
