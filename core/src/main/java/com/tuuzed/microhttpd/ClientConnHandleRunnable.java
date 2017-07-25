package com.tuuzed.microhttpd;

import com.tuuzed.microhttpd.handler.Handler;
import com.tuuzed.microhttpd.http.request.HttpRequest;
import com.tuuzed.microhttpd.http.request.HttpRequestImpl;
import com.tuuzed.microhttpd.http.response.HttpResponse;
import com.tuuzed.microhttpd.http.response.HttpResponseImpl;
import com.tuuzed.microhttpd.http.response.Status;
import com.tuuzed.microhttpd.util.CloseableUtils;
import com.tuuzed.microhttpd.util.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

class ClientConnHandleRunnable implements Runnable {
    private static final Logger logger = Logger.getLogger(ClientConnHandleRunnable.class);
    private Socket mConnect;
    private RequestsDispatcher mDispatcher;

    ClientConnHandleRunnable(RequestsDispatcher dispatcher, Socket connect) {
        mDispatcher = dispatcher;
        mConnect = connect;
    }

    @Override
    public void run() {
        HttpResponse response = new HttpResponseImpl(mConnect);
        InputStream in = null;
        try {
            in = mConnect.getInputStream();
            HttpRequest request = HttpRequestImpl.getRequest(in);
            // 不是HTTP请求
            if (request == null) {
                CloseableUtils.safeClose(response);
                return;
            }
            // 符合协议的请求
            Handler handler = mDispatcher.getHandler(request.getUrl());
            if (handler != null) {
                handler.serve(request, response);
            } else {
                response.renderError(Status.STATUS_404);
            }
        } catch (IOException e) {
            logger.e(e);
        } finally {
            CloseableUtils.safeClose(in);
            CloseableUtils.safeClose(response);
            logger.d(String.format("Client (%d) disconnect...", mConnect.hashCode()));
        }
    }
}
