package com.tuuzed.microhttpd.io;

import org.junit.Test;

import java.net.URL;
import java.util.Arrays;

import static org.junit.Assert.*;


public class TextFileReaderTest {
    @Test
    public void readAll() throws Exception {
        URL url = TextFileReader.class.getResource("/mime-type.txt");
        TextFileReader reader = new TextFileReader(url.getFile());
        String all = reader.readAll();
        System.out.println(all);
    }

    @Test
    public void readLine() throws Exception {
        URL url = TextFileReader.class.getResource("/mime-type.txt");
        TextFileReader reader = new TextFileReader(url.getFile());
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(Arrays.toString(line.split(" ")));
        }
    }

}