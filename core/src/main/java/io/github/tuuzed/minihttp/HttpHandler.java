package io.github.tuuzed.minihttp;

import io.github.tuuzed.minihttp.request.Request;
import io.github.tuuzed.minihttp.response.Response;
import io.github.tuuzed.minihttp.util.Logger;

/**
 * Http请求处理
 */
public abstract class HttpHandler implements Handler {
    private final static Logger sLogger = Logger.getLogger(StaticFileHandler.class);

    @Override
    public Response serve(Request request) {
        sLogger.d("接收到请求..." + request.toString());
        if ("GET".equals(request.getMethod())) {
            return doGet(request);
        } else if ("POST".equals(request.getMethod())) {
            return doPost(request);
        } else if ("PUT".equals(request.getMethod())) {
            return doPut(request);
        } else if ("DELETE".equals(request.getMethod())) {
            return doDelete(request);
        } else if ("PATCH".equals(request.getMethod())) {
            return doPatch(request);
        } else if ("HEAD".equals(request.getMethod())) {
            return doHead(request);
        } else if ("CONNECT".equals(request.getMethod())) {
            return doConnect(request);
        } else if ("OPTIONS".equals(request.getMethod())) {
            return doOptions(request);
        } else if ("TRACE".equals(request.getMethod())) {
            return doTrace(request);
        }
        return null;
    }

    public abstract Response doGet(Request request);

    public Response doPost(Request request) {
        return null;
    }

    public Response doPut(Request request) {
        return null;
    }

    public Response doDelete(Request request) {
        return null;
    }

    public Response doPatch(Request request) {
        return null;
    }

    public Response doHead(Request request) {
        return null;
    }

    public Response doConnect(Request request) {
        return null;
    }

    public Response doOptions(Request request) {
        return null;
    }

    public Response doTrace(Request request) {
        return null;
    }

}
