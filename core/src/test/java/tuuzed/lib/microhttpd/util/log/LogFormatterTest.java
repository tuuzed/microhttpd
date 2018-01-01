package tuuzed.lib.microhttpd.util.log;

import org.junit.Test;
import tuuzed.lib.microhttpd.internal.LogFormatter;

public class LogFormatterTest {
    @Test
    public void format() throws Exception {
        String format = LogFormatter.format("{} - {} {} - ", Thread.currentThread().getName(), 'a', "B");
        System.out.println(format);
    }

}