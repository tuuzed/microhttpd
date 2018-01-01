package tuuzed.lib.microhttpd.core;

import org.jetbrains.annotations.NotNull;
import tuuzed.lib.microhttpd.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

final class RealResponseBody implements ResponseBody {
    private ResponseBody responseBody;

    RealResponseBody(@NotNull byte[] data, int offset, int length) {
        this.responseBody = new BytesResponseBody(data, offset, length);
    }

    RealResponseBody(@NotNull File file) {
        this.responseBody = new FileResponseBody(file);
    }

    @Override
    public void writeTo(@NotNull OutputStream outputStream) throws IOException {
        responseBody.writeTo(outputStream);
    }

    private static class BytesResponseBody implements ResponseBody {
        private byte[] data;
        private int offset;
        private int length;

        BytesResponseBody(@NotNull byte[] data, int offset, int length) {
            this.data = data;
            this.offset = offset;
            this.length = length;
        }

        @Override
        public void writeTo(@NotNull OutputStream outputStream) throws IOException {
            outputStream.write(data, offset, length);
        }
    }

    private static class FileResponseBody implements ResponseBody {
        private File file;

        FileResponseBody(@NotNull File file) {
            this.file = file;
        }

        @Override
        public void writeTo(@NotNull OutputStream outputStream) throws IOException {
            FileInputStream fis = new FileInputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = fis.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            fis.close();
        }
    }

}
