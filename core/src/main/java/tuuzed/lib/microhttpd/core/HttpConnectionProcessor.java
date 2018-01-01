package tuuzed.lib.microhttpd.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tuuzed.lib.microhttpd.*;
import tuuzed.lib.microhttpd.internal.LogFormatter;
import tuuzed.lib.microhttpd.util.SleepUtils;
import tuuzed.lib.microhttpd.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpConnectionProcessor {
    private static final Logger sLogger = Logger.getLogger(HttpConnectionProcessor.class.getSimpleName());
    @Nullable
    private HttpRequestDispatcher staticFileHttpRequestDispatcher;
    private List<HttpRequestDispatcher> httpRequestDispatchers;
    private long nanoTimeout;

    public HttpConnectionProcessor(long nanoTimeout) {
        this.httpRequestDispatchers = new ArrayList<>();
        this.nanoTimeout = nanoTimeout;
    }

    public HttpConnectionProcessor setStaticFileHttpRequestDispatcher(HttpRequestDispatcher staticFileHttpRequestDispatcher) {
        this.staticFileHttpRequestDispatcher = staticFileHttpRequestDispatcher;
        return this;
    }

    public void addHttpRequestDispatcher(@NotNull HttpRequestDispatcher dispatcher) {
        this.httpRequestDispatchers.add(dispatcher);
    }

    public boolean removeHttpRequestDispatcher(@NotNull HttpRequestDispatcher dispatcher) {
        return this.httpRequestDispatchers.remove(dispatcher);
    }

    public void addRequestDispatchers(@NotNull List<HttpRequestDispatcher> requestDispatchers) {
        this.httpRequestDispatchers.addAll(requestDispatchers);
    }

    public void process(@NotNull Socket socket) throws TimeoutException, IOException {
        HttpRequest req = parseHttpRequest(socket, nanoTimeout);
        if (req != null) {
            if (staticFileHttpRequestDispatcher != null) {
                HttpResponse resp = staticFileHttpRequestDispatcher.dispatch(req);
                if (resp != null) {
                    resp.respond(socket.getOutputStream());
                    sLogger.log(Level.INFO, LogFormatter.format("{}, {} - - \"{}\" {} -",
                            Thread.currentThread().getName(),
                            ((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress().getHostAddress(),
                            req.requestLine().rawRequestLine(),
                            resp.responseLine().sc().code()));
                    return;
                }
            }
            for (HttpRequestDispatcher dispatcher : httpRequestDispatchers) {
                HttpResponse resp = dispatcher.dispatch(req);
                if (resp != null) {
                    resp.respond(socket.getOutputStream());
                    sLogger.log(Level.INFO, LogFormatter.format("{}, {} - - \"{}\" {} -",
                            Thread.currentThread().getName(),
                            ((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress().getHostAddress(),
                            req.requestLine().rawRequestLine(),
                            resp.responseLine().sc().code()));
                    return;
                }
            }
        }
    }

    @Nullable
    private static HttpRequest parseHttpRequest(@NotNull Socket socket, long nanoTimeout) throws TimeoutException, IOException {
        InputStream inputStream = socket.getInputStream();
        int available = inputStream.available();
        if (available == 0) {
            final long startNanoTime = System.nanoTime();
            for (; ; ) {
                SleepUtils.sleep(1);
                available = inputStream.available();
                if (available > 0) break;
                else if (System.nanoTime() - startNanoTime > nanoTimeout) throw new TimeoutException("timeout");
            }
        }
        // requestLine
        String firstLine = readLine(inputStream);
        RequestLine requestLine = RealRequestLine.of(firstLine);
        sLogger.log(Level.CONFIG, LogFormatter.format("requestLine = {}", requestLine));
        if (requestLine == null) return null;
        // headers
        Map<String, String> headersMap = new HashMap<>(10);
        String header;
        for (; ; ) {
            header = readLine(inputStream);
            if (StringUtils.isEmpty(header)) {
                break;
            }
            String[] headerArray = header.split(":");
            headersMap.put(StringUtils.trim(headerArray[0]), StringUtils.trim(headerArray[1]));
        }
        sLogger.log(Level.CONFIG, LogFormatter.format("headersMap = {}", headersMap));
        Headers headers = new RealHeaders(headersMap.size());
        headers.putAllOf(headersMap);
        return new RealHttpRequest(requestLine, headers, inputStream);
    }

    private static String readLine(@NotNull InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        int ch;
        for (; ; ) {
            if (inputStream.available() == 0) {
                break;
            }
            ch = inputStream.read();
            if (ch == -1) {
                break;
            }
            if (ch == '\n') {
                break;
            }
            sb.append((char) ch);
        }
        return sb.toString().trim();
    }
}
