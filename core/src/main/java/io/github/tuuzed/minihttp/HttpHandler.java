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
    public boolean serve(Request request, Response response) {
        sLogger.d("接收到请求..." + request.toString());
        if ("GET".equals(request.getMethod())) {
            return doGet(request, response);
        } else if ("POST".equals(request.getMethod())) {
            return doPost(request, response);
        } else if ("PUT".equals(request.getMethod())) {
            return doPut(request, response);
        } else if ("DELETE".equals(request.getMethod())) {
            return doDelete(request, response);
        } else if ("PATCH".equals(request.getMethod())) {
            return doPatch(request, response);
        } else if ("HEAD".equals(request.getMethod())) {
            return doHead(request, response);
        } else if ("CONNECT".equals(request.getMethod())) {
            return doConnect(request, response);
        } else if ("OPTIONS".equals(request.getMethod())) {
            return doOptions(request, response);
        } else if ("TRACE".equals(request.getMethod())) {
            return doTrace(request, response);
        }
        return false;
    }

    public abstract boolean doGet(Request request, Response response);

    public boolean doPost(Request request, Response response) {
        return false;
    }

    public boolean doPut(Request request, Response response) {
        return false;
    }

    public boolean doDelete(Request request, Response response) {
        return false;
    }

    public boolean doPatch(Request request, Response response) {
        return false;
    }

    public boolean doHead(Request request, Response response) {
        return false;
    }

    public boolean doConnect(Request request, Response response) {
        return false;
    }

    public boolean doOptions(Request request, Response response) {
        return false;
    }

    public boolean doTrace(Request request, Response response) {
        return false;
    }

}
