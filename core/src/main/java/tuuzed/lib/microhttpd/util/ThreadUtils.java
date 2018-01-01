package tuuzed.lib.microhttpd.util;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadUtils {

    private static class IoThreadPoolHolder {
        private static final ExecutorService instance = Executors.newCachedThreadPool(DefaultThreadFactory.create("io"));
    }

    private static class ComputeThreadPoolHolder {
        private static final ExecutorService instance = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() + 1
                , DefaultThreadFactory.create("compute"));
    }

    public static void runOnIoThread(@NotNull Runnable runnable) {
        IoThreadPoolHolder.instance.execute(runnable);
    }

    public static void runOnComputeThread(@NotNull Runnable runnable) {
        ComputeThreadPoolHolder.instance.execute(runnable);
    }


    private static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        static ThreadFactory create(String name) {
            return new DefaultThreadFactory(name);
        }

        private DefaultThreadFactory(String name) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = name + "-pool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
        }

        public Thread newThread(@NotNull Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}
