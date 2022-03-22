package androidx.emoji2.text;

import androidx.emoji2.text.flatbuffer.MetadataList;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
/* loaded from: classes.dex */
public final class MetadataListReader {

    /* loaded from: classes.dex */
    public static class ByteBufferReader {
        public final ByteBuffer mByteBuffer;

        public final long readUnsignedInt() throws IOException {
            return this.mByteBuffer.getInt() & 4294967295L;
        }

        public final void skip(int i) throws IOException {
            ByteBuffer byteBuffer = this.mByteBuffer;
            byteBuffer.position(byteBuffer.position() + i);
        }

        public ByteBufferReader(ByteBuffer byteBuffer) {
            this.mByteBuffer = byteBuffer;
            byteBuffer.order(ByteOrder.BIG_ENDIAN);
        }
    }

    public static MetadataList read(MappedByteBuffer mappedByteBuffer) throws IOException {
        long j;
        ByteBuffer duplicate = mappedByteBuffer.duplicate();
        ByteBufferReader byteBufferReader = new ByteBufferReader(duplicate);
        byteBufferReader.skip(4);
        int i = duplicate.getShort() & 65535;
        if (i <= 100) {
            byteBufferReader.skip(6);
            int i2 = 0;
            while (true) {
                if (i2 >= i) {
                    j = -1;
                    break;
                }
                int i3 = byteBufferReader.mByteBuffer.getInt();
                byteBufferReader.skip(4);
                j = byteBufferReader.readUnsignedInt();
                byteBufferReader.skip(4);
                if (1835365473 == i3) {
                    break;
                }
                i2++;
            }
            if (j != -1) {
                byteBufferReader.skip((int) (j - byteBufferReader.mByteBuffer.position()));
                byteBufferReader.skip(12);
                long readUnsignedInt = byteBufferReader.readUnsignedInt();
                for (int i4 = 0; i4 < readUnsignedInt; i4++) {
                    int i5 = byteBufferReader.mByteBuffer.getInt();
                    long readUnsignedInt2 = byteBufferReader.readUnsignedInt();
                    byteBufferReader.readUnsignedInt();
                    if (1164798569 == i5 || 1701669481 == i5) {
                        duplicate.position((int) (readUnsignedInt2 + j));
                        MetadataList metadataList = new MetadataList();
                        duplicate.order(ByteOrder.LITTLE_ENDIAN);
                        metadataList.__reset(duplicate.position() + duplicate.getInt(duplicate.position()), duplicate);
                        return metadataList;
                    }
                }
            }
            throw new IOException("Cannot read metadata.");
        }
        throw new IOException("Cannot read metadata.");
    }
}
