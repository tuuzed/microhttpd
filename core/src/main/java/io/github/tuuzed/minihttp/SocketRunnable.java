package io.github.tuuzed.minihttp;

import io.github.tuuzed.minihttp.request.Request;
import io.github.tuuzed.minihttp.request.RequestImpl;
import io.github.tuuzed.minihttp.response.Response;
import io.github.tuuzed.minihttp.response.StringResponse;
import io.github.tuuzed.minihttp.util.LogUtils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

class SocketRunnable implements Runnable {
    private static final String TAG = "SocketRunnable";
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
            String raw = new String(bytes).trim();
            LogUtils.d(TAG, "raw request:\n" + raw);
            Request request = new RequestImpl(raw);
            // 输出流
            out = socket.getOutputStream();
            // 响应
            Response response;
            if (request.isNormal()) {
                Handler handler = dispatcher.getHandler(request.getUri());
                if (handler != null) {
                    response = handler.serve(request);
                } else {
                    response = new StringResponse(StringResponse.STATUS_404);
                }
            } else {
                response = new StringResponse(StringResponse.STATUS_400);
            }
            if (response == null) {
                response = new StringResponse(StringResponse.STATUS_405);
            }
            out.write((String.format("HTTP/1.1 %s\r\n", response.getStatus())).getBytes());
            out.write(response.getHeader().getBytes());
            out.write("\r\n".getBytes());
            out.write(response.getBody());
            out.flush();
        } catch (IOException e) {
            LogUtils.e(TAG, e);
        } finally {
            close(out);
            close(in);
            close(socket);
            LogUtils.d(TAG, String.format("客户端(%d)断开...", hashCode()));
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
