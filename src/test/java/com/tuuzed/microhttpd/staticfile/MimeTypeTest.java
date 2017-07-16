package com.tuuzed.microhttpd.staticfile;

import org.junit.Assert;
import org.junit.Test;


public class MimeTypeTest {

    @Test
    public void test() {
        MimeType mimeType = MimeType.getInstance();
        String s = mimeType.get("111.txt");
        Assert.assertEquals(s, "text/plain");
    }

}