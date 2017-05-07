package io.github.tuuzed.microhttpd;

import io.github.tuuzed.microhttpd.handler.Handler;
import io.github.tuuzed.microhttpd.request.Request;
import io.github.tuuzed.microhttpd.request.RequestImpl;
import io.github.tuuzed.microhttpd.response.Response;
import io.github.tuuzed.microhttpd.response.ResponseImpl;
import io.github.tuuzed.microhttpd.response.Status;
import io.github.tuuzed.microhttpd.util.Logger;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

class ConnectionProcessor implements Runnable {
    private static final Logger sLogger = Logger.getLogger(MicroHTTPdImpl.class);
    private int mBufSize;
    private Socket mConnect;
    private RequestsDispatcher mDispatcher;

    ConnectionProcessor(RequestsDispatcher dispatcher, Socket connect, int bufSize) {
        mDispatcher = dispatcher;
        mConnect = connect;
        mBufSize = bufSize;
    }

    @Override
    public void run() {
        Response response = new ResponseImpl(mConnect);
        InputStream in = null;
        // 一个用于缓存输入流的输出流
        ByteArrayOutputStream buffInOut = null;
        try {
            in = mConnect.getInputStream();
            buffInOut = new ByteArrayOutputStream();
            byte[] buf = new byte[mBufSize];
            // 读取输入流，堵塞程序
            int read = in.read(buf);
            buffInOut.write(buf, 0, read);
            while (in.available() != 0) {
                read = in.read(buf);
                buffInOut.write(buf, 0, read);
            }
            Request request = RequestImpl.getRequest(buffInOut.toByteArray());
            if (request != null) {
                // 符合协议的请求
                Handler handler = mDispatcher.getHandler(request.getUrl());
                if (handler != null) {
                    handler.serve(request, response);
                } else {
                    response.renderError(Status.STATUS_404);
                }
            } else {
                // 不是HTTP请求
                response.close();
            }
        } catch (IOException e) {
            sLogger.e(e);
        } finally {
            safeClose(buffInOut);
            safeClose(in);
            safeClose(response);
            sLogger.d(String.format("Client (%d) disconnect...", mConnect.hashCode()));
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
