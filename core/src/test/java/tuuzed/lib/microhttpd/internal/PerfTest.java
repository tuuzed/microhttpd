package tuuzed.lib.microhttpd.internal;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PerfTest {

    @Test
    public void test() {
        test(100);
    }

    public void test(int size) {
        long start;
        // arraymap
        start = System.nanoTime();
        Map<String, String> arraymap = new ArrayMap<>();
        for (int i = 0; i < size; i++) {
            arraymap.put("key" + i, "value" + i);
        }
        System.out.println("arraymap = " + arraymap.toString());
        System.out.println("arraymap = " + (System.nanoTime() - start));
        System.out.println("arraymap = " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
        // hashmap
        start = System.nanoTime();
        Map<String, String> hashmap = new HashMap<>();
        for (int i = 0; i < size; i++) {
            hashmap.put("key" + i, "value" + i);
        }
        System.out.println("hashmap  = " + hashmap.toString());
        System.out.println("hashmap  = " + (System.nanoTime() - start));
        System.out.println("hashmap  = " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
    }
}
