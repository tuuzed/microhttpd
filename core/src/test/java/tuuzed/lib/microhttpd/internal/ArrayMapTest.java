package tuuzed.lib.microhttpd.internal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ArrayMapTest {
    Map<String, String> arrayMap;

    @Before
    public void setup() {
        arrayMap = new ArrayMap<>();
        arrayMap.put("name", "tom");
        arrayMap.put("age", "18");
    }

    @Test
    public void size() throws Exception {
        Assert.assertEquals(arrayMap.size(), 2);
    }

    @Test
    public void isEmpty() throws Exception {
        Assert.assertEquals(arrayMap.isEmpty(), false);
        arrayMap.clear();
        Assert.assertEquals(arrayMap.isEmpty(), true);
    }

    @Test
    public void containsKey() throws Exception {
        Assert.assertEquals(arrayMap.containsKey("name"), true);
        arrayMap.clear();
        Assert.assertEquals(arrayMap.containsKey("name"), false);
    }

    @Test
    public void containsValue() throws Exception {
        Assert.assertEquals(arrayMap.containsValue("tom"), true);
        arrayMap.clear();
        Assert.assertEquals(arrayMap.containsValue("tom"), false);
    }

    @Test
    public void get() throws Exception {
        Assert.assertEquals(arrayMap.get("name"), "tom");
        arrayMap.remove("name");
        Assert.assertEquals(arrayMap.get("name"), null);
    }

    @Test
    public void put() throws Exception {
        arrayMap.put("sex", "man");
        Assert.assertEquals(arrayMap.size(), 3);
        Assert.assertEquals(arrayMap.get("sex"), "man");
    }

    @Test
    public void remove() throws Exception {
        Assert.assertEquals(arrayMap.remove("sex"), null);
        Assert.assertEquals(arrayMap.size(), 2);
        Assert.assertEquals(arrayMap.remove("name"), "tom");
        Assert.assertEquals(arrayMap.size(), 1);
    }

    @Test
    public void putAll() throws Exception {

    }

    @Test
    public void clear() throws Exception {
        arrayMap.clear();
        Assert.assertEquals(arrayMap.toString(), "{}");
    }

    @Test
    public void keySet() throws Exception {
        Set<String> keySet = arrayMap.keySet();
        System.out.println("keySet = " + keySet);
    }

    @Test
    public void values() throws Exception {
        Collection<String> values = arrayMap.values();
        System.out.println("values = " + values);
    }

    @Test
    public void entrySet() throws Exception {
        Set<Map.Entry<String, String>> entrySet = arrayMap.entrySet();
        System.out.println("entrySet = " + entrySet);
    }

    @Test
    public void testToString() throws Exception {
        String toString = arrayMap.toString();
        System.out.println("toString = " + toString);
    }
}