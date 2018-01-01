package tuuzed.lib.microhttpd.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tuuzed.lib.microhttpd.RequestBody;
import tuuzed.lib.microhttpd.internal.ByteBuf;
import tuuzed.lib.microhttpd.util.CloseUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.nio.file.FileAlreadyExistsException;

final class RealRequestBody implements RequestBody {
    private WeakReference<InputStream> inputStream;

    RealRequestBody(InputStream inputStream) {
        this.inputStream = new WeakReference<>(inputStream);
    }

    @Nullable
    public byte[] bytes() throws IOException {
        InputStream inputStream = this.inputStream.get();
        if (inputStream != null && inputStream.available() > 0) {
            ByteBuf byteBuf = ByteBuf.allocate(1024);
            byte[] buf = new byte[1024];
            int len;
            while (inputStream.available() > 0) {
                len = inputStream.read(buf);
                byteBuf.write(buf, 0, len);
            }
            return byteBuf.bytes();
        }
        return null;
    }

    @Override
    public boolean file(@NotNull File file) throws IOException {
        if (file.exists()) {
            throw new FileAlreadyExistsException(file.getName());
        }
        InputStream inputStream = this.inputStream.get();
        if (inputStream != null && inputStream.available() > 0) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int len;
                while (inputStream.available() > 0) {
                    len = inputStream.read(buf);
                    fos.write(buf, 0, len);
                }
                fos.flush();
                fos.close();
                return true;
            } finally {
                CloseUtils.close(fos);
            }
        }
        return false;
    }
}