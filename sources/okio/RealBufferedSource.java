package okio;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import java.nio.ByteBuffer;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
import okio.internal.BufferKt;
/* compiled from: RealBufferedSource.kt */
/* loaded from: classes.dex */
public final class RealBufferedSource implements BufferedSource {
    public final Buffer bufferField = new Buffer();
    public boolean closed;
    public final Source source;

    @Override // okio.Source
    public final long read(Buffer buffer, long j) {
        if (!this.closed) {
            Buffer buffer2 = this.bufferField;
            Objects.requireNonNull(buffer2);
            if (buffer2.size == 0 && this.source.read(this.bufferField, 8192L) == -1) {
                return -1L;
            }
            Buffer buffer3 = this.bufferField;
            Objects.requireNonNull(buffer3);
            return this.bufferField.read(buffer, Math.min(8192L, buffer3.size));
        }
        throw new IllegalStateException("closed".toString());
    }

    @Override // okio.Source, java.io.Closeable, java.lang.AutoCloseable, java.nio.channels.Channel
    public final void close() {
        if (!this.closed) {
            this.closed = true;
            this.source.close();
            Buffer buffer = this.bufferField;
            Objects.requireNonNull(buffer);
            buffer.skip(buffer.size);
        }
    }

    @Override // okio.BufferedSource
    public final long indexOfElement(ByteString byteString) {
        if (!this.closed) {
            long j = 0;
            while (true) {
                long indexOfElement = this.bufferField.indexOfElement(byteString, j);
                if (indexOfElement != -1) {
                    return indexOfElement;
                }
                Buffer buffer = this.bufferField;
                Objects.requireNonNull(buffer);
                long j2 = buffer.size;
                if (this.source.read(this.bufferField, 8192L) == -1) {
                    return -1L;
                }
                j = Math.max(j, j2);
            }
        } else {
            throw new IllegalStateException("closed".toString());
        }
    }

    @Override // java.nio.channels.Channel
    public final boolean isOpen() {
        return !this.closed;
    }

    @Override // okio.BufferedSource
    public final boolean request(long j) {
        boolean z;
        if (j >= 0) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            throw new IllegalArgumentException(Intrinsics.stringPlus("byteCount < 0: ", Long.valueOf(j)).toString());
        } else if (!this.closed) {
            do {
                Buffer buffer = this.bufferField;
                Objects.requireNonNull(buffer);
                if (buffer.size >= j) {
                    return true;
                }
            } while (this.source.read(this.bufferField, 8192L) != -1);
            return false;
        } else {
            throw new IllegalStateException("closed".toString());
        }
    }

    @Override // okio.BufferedSource
    public final int select(Options options) {
        if (!this.closed) {
            while (true) {
                int selectPrefix = BufferKt.selectPrefix(this.bufferField, options, true);
                if (selectPrefix == -2) {
                    if (this.source.read(this.bufferField, 8192L) == -1) {
                        break;
                    }
                } else if (selectPrefix != -1) {
                    ByteString byteString = options.byteStrings[selectPrefix];
                    Objects.requireNonNull(byteString);
                    this.bufferField.skip(byteString.getSize$external__okio__android_common__okio_lib());
                    return selectPrefix;
                }
            }
            return -1;
        }
        throw new IllegalStateException("closed".toString());
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("buffer(");
        m.append(this.source);
        m.append(')');
        return m.toString();
    }

    public RealBufferedSource(InputStreamSource inputStreamSource) {
        this.source = inputStreamSource;
    }

    @Override // java.nio.channels.ReadableByteChannel
    public final int read(ByteBuffer byteBuffer) {
        Buffer buffer = this.bufferField;
        Objects.requireNonNull(buffer);
        if (buffer.size == 0 && this.source.read(this.bufferField, 8192L) == -1) {
            return -1;
        }
        return this.bufferField.read(byteBuffer);
    }
}
