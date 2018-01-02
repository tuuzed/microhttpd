package tuuzed.lib.microhttpd;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tuuzed.lib.microhttpd.core.HttpConnectionProcessor;
import tuuzed.lib.microhttpd.core.StaticFileHttpRequestDispatcher;
import tuuzed.lib.microhttpd.internal.LogFormatter;
import tuuzed.lib.microhttpd.util.CloseUtils;
import tuuzed.lib.microhttpd.util.SleepUtils;
import tuuzed.lib.microhttpd.util.ThreadUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MicroHTTPD {
    private static final Logger sLogger = Logger.getLogger(MicroHTTPD.class.getSimpleName());
    public static Charset sDefaultCharset = Charset.forName("utf-8");

    @Nullable
    private String mListenHostname;
    private int mListenPort;
    private HttpConnectionProcessor mConnectionProcessor;
    @Nullable
    private ServerSocket mServerSocket;
    @Nullable
    private ExecutorService mThreadPool;
    private AtomicBoolean mStarted = new AtomicBoolean(false);

    private MicroHTTPD(@NotNull Builder builder) {
        this.mListenHostname = builder.listenHostname;
        this.mListenPort = builder.listenPort;
        this.mConnectionProcessor = new HttpConnectionProcessor(builder.nanoTimeout);
        this.mConnectionProcessor.addRequestDispatchers(builder.requestDispatchers);
        this.mConnectionProcessor.setStaticFileHttpRequestDispatcher(builder.staticFileHttpRequestDispatcher);
        if (builder.requestHandleThreads > 0) {
            this.mThreadPool = Executors.newFixedThreadPool(builder.requestHandleThreads);
        }
        sDefaultCharset = builder.charset;

    }

    public void start() {
        if (!mStarted.get()) {
            mStarted.set(true);
            ThreadUtils.runOnIoThread(new Runnable() {
                @Override
                public void run() {
                    startup();
                }
            });
            if (mListenHostname == null) {
                sLogger.log(Level.INFO, LogFormatter.format("Serving HTTP on 0.0.0.0 port {} (http://0.0.0.0:{}/)",
                        mListenPort, mListenPort));
            } else {
                sLogger.log(Level.INFO, LogFormatter.format("Serving HTTP on {} port {} (http://{}:{}/)",
                        mListenHostname, mListenPort, mListenHostname, mListenPort));
            }
        }
    }

    private void startup() {
        SocketAddress endpoint;
        if (mListenHostname == null) {
            endpoint = new InetSocketAddress(mListenPort);
        } else {
            endpoint = new InetSocketAddress(mListenHostname, mListenPort);
        }
        while (mStarted.get()) {
            try {
                mServerSocket = new ServerSocket();
                mServerSocket.bind(endpoint);
                for (; ; ) {
                    serv(mServerSocket.accept());
                }
            } catch (IOException e) {
                sLogger.log(Level.WARNING, "exception: " + e.toString());
            } finally {
                CloseUtils.close(mServerSocket);
            }
            SleepUtils.sleep(100);
        }
    }

    public void stop() {
        mStarted.set(false);
        CloseUtils.close(mServerSocket);
    }

    private void serv(@NotNull final Socket socket) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    mConnectionProcessor.process(socket);
                } catch (IOException | TimeoutException e) {
                    e.printStackTrace();
                } finally {
                    CloseUtils.close(socket);
                }
            }
        };
        if (mThreadPool != null) {
            mThreadPool.execute((runnable));
        } else {
            ThreadUtils.runOnIoThread(runnable);
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        @Nullable
        private String listenHostname;
        private int listenPort;
        private int requestHandleThreads;
        private HttpRequestDispatcher staticFileHttpRequestDispatcher;
        private List<HttpRequestDispatcher> requestDispatchers;
        private long nanoTimeout;
        private Charset charset;

        public MicroHTTPD build() {
            if (listenPort <= 0) {
                listenPort = 4000;
            }
            if (nanoTimeout <= 0) {
                nanoTimeout = TimeUnit.MILLISECONDS.toNanos(1500);
            }
            if (charset == null) {
                charset = Charset.forName("UTF-8");
            }
            return new MicroHTTPD(this);
        }

        public Builder() {
            requestDispatchers = new ArrayList<>();
        }

        public Builder setListenHostname(String listenHostname) {
            this.listenHostname = listenHostname;
            return this;
        }

        public Builder setListenPort(int listenPort) {
            this.listenPort = listenPort;
            return this;
        }

        public Builder setRequestHanldeThreads(int requestHandleThreads) {
            this.requestHandleThreads = requestHandleThreads;
            return this;
        }

        public Builder addHttpRequestDispatcher(HttpRequestDispatcher requestDispatcher) {
            this.requestDispatchers.add(requestDispatcher);
            return this;
        }

        public Builder useStaticFileHttpRequestDispatcher(String urlPrefix, File path) {
            return useStaticFileHttpRequestDispatcher(new StaticFileHttpRequestDispatcher(urlPrefix, path));
        }

        public Builder useStaticFileHttpRequestDispatcher(HttpRequestDispatcher staticFileHttpRequestDispatcher) {
            this.staticFileHttpRequestDispatcher = staticFileHttpRequestDispatcher;
            return this;
        }

        public Builder setTimeout(long timeout, @NotNull TimeUnit timeUnit) {
            this.nanoTimeout = timeUnit.toNanos(timeout);
            return this;
        }

        public Builder setCharset(Charset charset) {
            this.charset = charset;
            return this;
        }
    }

}
