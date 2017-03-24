package io.github.tuuzed.minihttp;

import io.github.tuuzed.minihttp.request.Request;
import io.github.tuuzed.minihttp.request.RequestImpl;
import io.github.tuuzed.minihttp.response.Response;
import io.github.tuuzed.minihttp.response.Status;
import io.github.tuuzed.minihttp.response.StringResponse;
import io.github.tuuzed.minihttp.util.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

class SocketRunnable implements Runnable {
    private static final Logger sLogger = Logger.getLogger(MiniHTTPd.class);
    private Socket socket;
    private int buffSize;
    private RequestsDispatcher dispatcher;

    SocketRunnable(RequestsDispatcher dispatcher, Socket socket, int buffSize) {
        this.dispatcher = dispatcher;
        this.socket = socket;
        this.buffSize = buffSize;
    }

    @Override
    public void run() {
        InputStream in = null;
        OutputStream out = null;
        try {
            // 输出流
            in = socket.getInputStream();
            // 请求
            byte[] bytes = new byte[buffSize];
            int read = in.read(bytes);
            String rawRequest = new String(bytes).trim();
            sLogger.d("rawRequest:\n" + rawRequest);
            Request request = RequestImpl.analysis(rawRequest);
            // 输出流
            out = socket.getOutputStream();
            // 响应
            Response response;
            if (request == null) {
                // 不符合协议的请求
                response = new StringResponse(Status.STATUS_400);
            } else {
                // 符合协议的请求
                Handler handler = dispatcher.getHandler(request.getUri());
                if (handler != null) {
                    response = handler.serve(request);
                    if (response == null) {
                        // 请求方法未被实现
                        response = new StringResponse(Status.STATUS_405);
                    }
                } else {
                    // 请求URI不存在
                    response = new StringResponse(Status.STATUS_404);
                }
            }
            out.write((String.format("HTTP/1.1 %s\r\n", response.getStatus().toString())).getBytes());
            out.write(response.getHeader().getBytes());
            out.write("\r\n".getBytes());
            out.write(response.getBody());
            out.flush();
        } catch (IOException e) {
            sLogger.e(e);
        } finally {
            close(out);
            close(in);
            close(socket);
            sLogger.d(String.format("客户端(%d)断开...", hashCode()));
        }
    }


    // 关闭可关闭的对象
    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
