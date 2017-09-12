package com.tuuzed.microhttpd;

import com.tuuzed.microhttpd.common.util.CloseableUtils;
import com.tuuzed.microhttpd.common.util.Logger;
import com.tuuzed.microhttpd.http.Request;
import com.tuuzed.microhttpd.http.RequestImpl;
import com.tuuzed.microhttpd.http.Response;
import com.tuuzed.microhttpd.http.ResponseImpl;
import com.tuuzed.microhttpd.http.Status;
import com.tuuzed.microhttpd.view.View;

import java.io.InputStream;
import java.net.Socket;

class AcceptHandler implements Runnable {
    private static final Logger logger = Logger.getLogger(AcceptHandler.class);
    private Socket mSocket;
    private RequestDispatcher mDispatcher;

    AcceptHandler(RequestDispatcher dispatcher, Socket socket) {
        mDispatcher = dispatcher;
        mSocket = socket;
    }

    @Override
    public void run() {
        Response resp = ResponseImpl.create(mSocket);
        InputStream input = null;
        try {
            Request request = RequestImpl.create(input = mSocket.getInputStream());
            String url = request.getUrl();
            logger.debug("{}", request);
            View view = mDispatcher.getView(url);
            if (view == null) {
                resp.renderError(Status.STATUS_404);
            } else {
                view.serve(request, resp);
            }
        } catch (Exception e) {
            logger.debug("{}", e, e);
        } finally {
            CloseableUtils.safeClose(input);
            CloseableUtils.safeClose(resp);
            logger.debug("{} disconnect...", mDispatcher.hashCode());
        }
    }
}
