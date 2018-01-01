package tuuzed.lib.microhttpd.internal;

public final class ByteBuf {
    private byte[] buf;
    private int size = 0;
    private int capacity;

    private ByteBuf(int capacity) {
        this.capacity = capacity;
        buf = new byte[capacity];
    }

    public static ByteBuf allocate(int capacity) {
        if (capacity < 0)
            throw new IllegalArgumentException();
        return new ByteBuf(capacity);
    }

    public void write(byte[] bytes, int offset, int length) {
        if (buf.length < size + length) {
            // 扩容
            byte[] old = buf;
            buf = new byte[old.length * 2];
            System.arraycopy(old, 0, buf, 0, old.length);
        }
        System.arraycopy(bytes, offset, buf, size, length);
        size += length;
    }

    public byte[] bytes() {
        byte[] bytes = new byte[size];
        System.arraycopy(buf, 0, bytes, 0, size);
        return bytes;
    }

    public void clear() {
        buf = new byte[capacity];
    }

    public int size() {
        return size;
    }
}
