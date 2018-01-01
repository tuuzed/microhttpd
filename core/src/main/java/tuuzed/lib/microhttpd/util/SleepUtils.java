package tuuzed.lib.microhttpd.util;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class SleepUtils {

    public static void sleep(long timeout) {
        sleep(timeout, TimeUnit.MILLISECONDS);
    }

    public static void sleep(long timeout, @NotNull TimeUnit timeUnit) {
        try {
            timeUnit.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
