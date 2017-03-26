package io.github.tuuzed.microhttpd;

import io.github.tuuzed.microhttpd.util.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

class ServerListenRunnable implements Runnable {
    private static final Logger sLogger = Logger.getLogger(ServerListenRunnable.class);
    private RequestsDispatcher dispatcher;
    private String address;
    private int port;
    private int timeout;

    ServerListenRunnable(RequestsDispatcher dispatcher, String address, int port, int timeout) {
        this.dispatcher = dispatcher;
        this.address = address;
        this.port = port;
        this.timeout = timeout;
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(address, port));
            while (!serverSocket.isClosed()) {
                Socket client = serverSocket.accept();
                sLogger.d(String.format("Client (%d) connected...", client.hashCode()));
                client.setSoTimeout(timeout);
                dispatcher.dispatch(client);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
}
