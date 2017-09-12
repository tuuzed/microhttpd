package com.tuuzed.microhttpd.fileview;

import com.tuuzed.microhttpd.view.file.MimeType;
import org.junit.Test;

public class MimeTypeTest {
    @Test
    public void get() throws Exception {
        String s = MimeType.getInstance().get("bootmgr.efi.mui");
        System.out.println(s);
    }

}