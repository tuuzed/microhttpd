package com.tuuzed.microhttpd;

import com.tuuzed.microhttpd.common.log.Logger;
import com.tuuzed.microhttpd.common.log.LoggerFactory;
import com.tuuzed.microhttpd.common.util.CloseableUtils;
import com.tuuzed.microhttpd.http.*;
import com.tuuzed.microhttpd.route.Routes;
import com.tuuzed.microhttpd.view.View;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.net.Socket;

class AcceptHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(AcceptHandler.class);
    private Socket mSocket;
    private Routes mRoutes;

    static AcceptHandler create(@NotNull Routes routes,
                                @NotNull Socket socket) {
        return new AcceptHandler(routes, socket);
    }

    private AcceptHandler(@NotNull Routes routes,
                          @NotNull Socket socket) {
        mRoutes = routes;
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
            View view = mRoutes.getView(url);
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
            logger.debug("{} disconnect...", mSocket.hashCode());
        }
    }
}
