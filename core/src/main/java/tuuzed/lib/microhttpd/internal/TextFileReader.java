package tuuzed.lib.microhttpd.internal;

import java.io.*;

/**
 * 纯文本文件读取工具
 */
public class TextFileReader implements Closeable {
    private static final String DEF_ENCODE = "utf-8";
    private BufferedReader br;

    public TextFileReader(String filename) throws IOException {
        this(new File(filename), DEF_ENCODE);
    }

    public TextFileReader(File file) throws IOException {
        this(file, DEF_ENCODE);
    }

    public TextFileReader(String filename, String encode) throws IOException {
        this(new File(filename), encode);
    }

    public TextFileReader(File file, String encode) throws IOException {
        br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), encode));
    }

    public String readAll() throws IOException {
        int bufSize = 4096;
        char[] buffer = new char[bufSize];
        int read, fill = 0;
        while (true) {
            read = br.read(buffer, fill, buffer.length - fill);
            if (read == -1) {
                break;
            }
            fill += read;
            if (fill >= buffer.length) {
                char[] newBuffer = new char[bufSize + buffer.length];
                System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
                buffer = newBuffer;
            }
        }

        return new String(buffer, 0, fill);
    }

    public static String readAll(String filename) throws IOException {
        return readAll(filename, DEF_ENCODE);
    }

    public static String readAll(String filename, String encode) throws IOException {
        TextFileReader reader = new TextFileReader(filename, encode);
        String result = reader.readAll();
        reader.close();
        return result;
    }

    public String readLine() throws IOException {
        return br.readLine();
    }

    @Override
    public void close() throws IOException {
        br.close();
    }
}
