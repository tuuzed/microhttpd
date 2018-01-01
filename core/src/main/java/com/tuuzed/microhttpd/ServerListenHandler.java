package com.tuuzed.microhttpd;

import com.tuuzed.microhttpd.common.log.Logger;
import com.tuuzed.microhttpd.common.log.LoggerFactory;
import com.tuuzed.microhttpd.common.util.CloseableUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 监听连接
 */
class ServerListenHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ServerListenHandler.class);
    private RequestDispatcher mDispatcher;
    private ServerSocket mServerSocket;
    private int mTimeout;

    static ServerListenHandler create(@NotNull ServerSocket serverSocket,
                                      @NotNull RequestDispatcher dispatcher,
                                      int timeout) {
        return new ServerListenHandler(serverSocket, dispatcher, timeout);

    }

    private ServerListenHandler(@NotNull ServerSocket serverSocket,
                                @NotNull RequestDispatcher dispatcher,
                                int timeout) {
        mServerSocket = serverSocket;
        mDispatcher = dispatcher;
        mTimeout = timeout;
    }

    @Override
    public void run() {
        try {
            while (!mServerSocket.isClosed()) {
                logger.debug("accepting...");
                Socket accept = mServerSocket.accept();
                logger.debug("{} connected...", accept.hashCode());
                accept.setSoTimeout(mTimeout);
                mDispatcher.dispatch(accept);
            }
        } catch (IOException e) {
            logger.debug("{}", e, e);
        } finally {
            CloseableUtils.safeClose(mServerSocket);
        }
    }
}
