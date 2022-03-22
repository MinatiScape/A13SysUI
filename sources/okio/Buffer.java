package okio;

import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline0;
import java.io.Closeable;
import java.io.EOFException;
import java.io.Flushable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import okio.internal.BufferKt;
/* compiled from: Buffer.kt */
/* loaded from: classes.dex */
public final class Buffer implements BufferedSource, Closeable, Flushable, WritableByteChannel, Cloneable, ByteChannel {
    public Segment head;
    public long size;

    @Override // okio.Source, java.io.Closeable, java.lang.AutoCloseable, java.nio.channels.Channel
    public final void close() {
    }

    @Override // java.io.Flushable
    public final void flush() {
    }

    @Override // okio.BufferedSource
    public final long indexOfElement(ByteString byteString) {
        return indexOfElement(byteString, 0L);
    }

    @Override // java.nio.channels.Channel
    public final boolean isOpen() {
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:72:0x0152, code lost:
        return r13;
     */
    @Override // okio.Source
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final long read(okio.Buffer r18, long r19) {
        /*
            Method dump skipped, instructions count: 372
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.Buffer.read(okio.Buffer, long):long");
    }

    @Override // okio.BufferedSource
    public final int select(Options options) {
        int selectPrefix = BufferKt.selectPrefix(this, options, false);
        if (selectPrefix == -1) {
            return -1;
        }
        ByteString byteString = options.byteStrings[selectPrefix];
        Objects.requireNonNull(byteString);
        skip(byteString.getSize$external__okio__android_common__okio_lib());
        return selectPrefix;
    }

    public final Buffer writeInt(int i) {
        Segment writableSegment$external__okio__android_common__okio_lib = writableSegment$external__okio__android_common__okio_lib(4);
        byte[] bArr = writableSegment$external__okio__android_common__okio_lib.data;
        int i2 = writableSegment$external__okio__android_common__okio_lib.limit;
        int i3 = i2 + 1;
        bArr[i2] = (byte) ((i >>> 24) & 255);
        int i4 = i3 + 1;
        bArr[i3] = (byte) ((i >>> 16) & 255);
        int i5 = i4 + 1;
        bArr[i4] = (byte) ((i >>> 8) & 255);
        bArr[i5] = (byte) (i & 255);
        writableSegment$external__okio__android_common__okio_lib.limit = i5 + 1;
        this.size += 4;
        return this;
    }

    public final Buffer writeUtf8(String str, int i, int i2) {
        boolean z;
        boolean z2;
        boolean z3;
        char charAt;
        char c;
        boolean z4;
        if (i >= 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            if (i2 >= i) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z2) {
                if (i2 <= str.length()) {
                    z3 = true;
                } else {
                    z3 = false;
                }
                if (z3) {
                    while (i < i2) {
                        char charAt2 = str.charAt(i);
                        if (charAt2 < 128) {
                            Segment writableSegment$external__okio__android_common__okio_lib = writableSegment$external__okio__android_common__okio_lib(1);
                            byte[] bArr = writableSegment$external__okio__android_common__okio_lib.data;
                            int i3 = writableSegment$external__okio__android_common__okio_lib.limit - i;
                            int min = Math.min(i2, 8192 - i3);
                            int i4 = i + 1;
                            bArr[i + i3] = (byte) charAt2;
                            while (true) {
                                i = i4;
                                if (i >= min || (charAt = str.charAt(i)) >= 128) {
                                    break;
                                }
                                i4 = i + 1;
                                bArr[i + i3] = (byte) charAt;
                            }
                            int i5 = writableSegment$external__okio__android_common__okio_lib.limit;
                            int i6 = (i3 + i) - i5;
                            writableSegment$external__okio__android_common__okio_lib.limit = i5 + i6;
                            this.size += i6;
                        } else {
                            if (charAt2 < 2048) {
                                Segment writableSegment$external__okio__android_common__okio_lib2 = writableSegment$external__okio__android_common__okio_lib(2);
                                byte[] bArr2 = writableSegment$external__okio__android_common__okio_lib2.data;
                                int i7 = writableSegment$external__okio__android_common__okio_lib2.limit;
                                bArr2[i7] = (byte) ((charAt2 >> 6) | 192);
                                bArr2[i7 + 1] = (byte) ((charAt2 & '?') | 128);
                                writableSegment$external__okio__android_common__okio_lib2.limit = i7 + 2;
                                this.size += 2;
                            } else if (charAt2 < 55296 || charAt2 > 57343) {
                                Segment writableSegment$external__okio__android_common__okio_lib3 = writableSegment$external__okio__android_common__okio_lib(3);
                                byte[] bArr3 = writableSegment$external__okio__android_common__okio_lib3.data;
                                int i8 = writableSegment$external__okio__android_common__okio_lib3.limit;
                                bArr3[i8] = (byte) ((charAt2 >> '\f') | 224);
                                bArr3[i8 + 1] = (byte) ((63 & (charAt2 >> 6)) | 128);
                                bArr3[i8 + 2] = (byte) ((charAt2 & '?') | 128);
                                writableSegment$external__okio__android_common__okio_lib3.limit = i8 + 3;
                                this.size += 3;
                            } else {
                                int i9 = i + 1;
                                if (i9 < i2) {
                                    c = str.charAt(i9);
                                } else {
                                    c = 0;
                                }
                                if (charAt2 <= 56319) {
                                    if (56320 > c || c >= 57344) {
                                        z4 = false;
                                    } else {
                                        z4 = true;
                                    }
                                    if (z4) {
                                        int i10 = (((charAt2 & 1023) << 10) | (c & 1023)) + 65536;
                                        Segment writableSegment$external__okio__android_common__okio_lib4 = writableSegment$external__okio__android_common__okio_lib(4);
                                        byte[] bArr4 = writableSegment$external__okio__android_common__okio_lib4.data;
                                        int i11 = writableSegment$external__okio__android_common__okio_lib4.limit;
                                        bArr4[i11] = (byte) ((i10 >> 18) | 240);
                                        bArr4[i11 + 1] = (byte) (((i10 >> 12) & 63) | 128);
                                        bArr4[i11 + 2] = (byte) (((i10 >> 6) & 63) | 128);
                                        bArr4[i11 + 3] = (byte) ((i10 & 63) | 128);
                                        writableSegment$external__okio__android_common__okio_lib4.limit = i11 + 4;
                                        this.size += 4;
                                        i += 2;
                                    }
                                }
                                Segment writableSegment$external__okio__android_common__okio_lib5 = writableSegment$external__okio__android_common__okio_lib(1);
                                byte[] bArr5 = writableSegment$external__okio__android_common__okio_lib5.data;
                                int i12 = writableSegment$external__okio__android_common__okio_lib5.limit;
                                writableSegment$external__okio__android_common__okio_lib5.limit = i12 + 1;
                                bArr5[i12] = (byte) 63;
                                this.size++;
                                i = i9;
                            }
                            i++;
                        }
                    }
                    return this;
                }
                StringBuilder m = ExifInterface$$ExternalSyntheticOutline0.m("endIndex > string.length: ", i2, " > ");
                m.append(str.length());
                throw new IllegalArgumentException(m.toString().toString());
            }
            throw new IllegalArgumentException(("endIndex < beginIndex: " + i2 + " < " + i).toString());
        }
        throw new IllegalArgumentException(Intrinsics.stringPlus("beginIndex < 0: ", Integer.valueOf(i)).toString());
    }

    public final Object clone() {
        Buffer buffer = new Buffer();
        if (this.size != 0) {
            Segment segment = this.head;
            Intrinsics.checkNotNull(segment);
            Segment sharedCopy = segment.sharedCopy();
            buffer.head = sharedCopy;
            sharedCopy.prev = sharedCopy;
            sharedCopy.next = sharedCopy;
            for (Segment segment2 = segment.next; segment2 != segment; segment2 = segment2.next) {
                Segment segment3 = sharedCopy.prev;
                Intrinsics.checkNotNull(segment3);
                Intrinsics.checkNotNull(segment2);
                segment3.push(segment2.sharedCopy());
            }
            buffer.size = this.size;
        }
        return buffer;
    }

    public final boolean equals(Object obj) {
        if (this != obj) {
            if (!(obj instanceof Buffer)) {
                return false;
            }
            long j = this.size;
            Buffer buffer = (Buffer) obj;
            Objects.requireNonNull(buffer);
            if (j != buffer.size) {
                return false;
            }
            if (this.size != 0) {
                Segment segment = this.head;
                Intrinsics.checkNotNull(segment);
                Segment segment2 = buffer.head;
                Intrinsics.checkNotNull(segment2);
                int i = segment.pos;
                int i2 = segment2.pos;
                long j2 = 0;
                while (j2 < this.size) {
                    long min = Math.min(segment.limit - i, segment2.limit - i2);
                    long j3 = 0;
                    while (j3 < min) {
                        j3++;
                        int i3 = i + 1;
                        int i4 = i2 + 1;
                        if (segment.data[i] != segment2.data[i2]) {
                            return false;
                        }
                        i = i3;
                        i2 = i4;
                    }
                    if (i == segment.limit) {
                        segment = segment.next;
                        Intrinsics.checkNotNull(segment);
                        i = segment.pos;
                    }
                    if (i2 == segment2.limit) {
                        segment2 = segment2.next;
                        Intrinsics.checkNotNull(segment2);
                        i2 = segment2.pos;
                    }
                    j2 += min;
                }
            }
        }
        return true;
    }

    public final byte getByte(long j) {
        Util.checkOffsetAndCount(this.size, j, 1L);
        Segment segment = this.head;
        if (segment != null) {
            long j2 = this.size;
            if (j2 - j < j) {
                while (j2 > j) {
                    segment = segment.prev;
                    Intrinsics.checkNotNull(segment);
                    j2 -= segment.limit - segment.pos;
                }
                return segment.data[(int) ((segment.pos + j) - j2)];
            }
            long j3 = 0;
            while (true) {
                int i = segment.limit;
                int i2 = segment.pos;
                long j4 = (i - i2) + j3;
                if (j4 > j) {
                    return segment.data[(int) ((i2 + j) - j3)];
                }
                segment = segment.next;
                Intrinsics.checkNotNull(segment);
                j3 = j4;
            }
        } else {
            Intrinsics.checkNotNull(null);
            throw null;
        }
    }

    public final int hashCode() {
        Segment segment = this.head;
        if (segment == null) {
            return 0;
        }
        int i = 1;
        do {
            int i2 = segment.limit;
            for (int i3 = segment.pos; i3 < i2; i3++) {
                i = (i * 31) + segment.data[i3];
            }
            segment = segment.next;
            Intrinsics.checkNotNull(segment);
        } while (segment != this.head);
        return i;
    }

    public final long indexOfElement(ByteString byteString, long j) {
        int i;
        int i2;
        int i3;
        int i4;
        long j2 = 0;
        if (j >= 0) {
            Segment segment = this.head;
            if (segment == null) {
                return -1L;
            }
            long j3 = this.size;
            if (j3 - j < j) {
                while (j3 > j) {
                    segment = segment.prev;
                    Intrinsics.checkNotNull(segment);
                    j3 -= segment.limit - segment.pos;
                }
                if (byteString.getSize$external__okio__android_common__okio_lib() == 2) {
                    byte internalGet$external__okio__android_common__okio_lib = byteString.internalGet$external__okio__android_common__okio_lib(0);
                    byte internalGet$external__okio__android_common__okio_lib2 = byteString.internalGet$external__okio__android_common__okio_lib(1);
                    while (j3 < this.size) {
                        byte[] bArr = segment.data;
                        i3 = (int) ((segment.pos + j) - j3);
                        int i5 = segment.limit;
                        while (i3 < i5) {
                            byte b = bArr[i3];
                            if (b == internalGet$external__okio__android_common__okio_lib || b == internalGet$external__okio__android_common__okio_lib2) {
                                i4 = segment.pos;
                            } else {
                                i3++;
                            }
                        }
                        j3 += segment.limit - segment.pos;
                        segment = segment.next;
                        Intrinsics.checkNotNull(segment);
                        j = j3;
                    }
                    return -1L;
                }
                byte[] internalArray$external__okio__android_common__okio_lib = byteString.internalArray$external__okio__android_common__okio_lib();
                while (j3 < this.size) {
                    byte[] bArr2 = segment.data;
                    i3 = (int) ((segment.pos + j) - j3);
                    int i6 = segment.limit;
                    while (i3 < i6) {
                        byte b2 = bArr2[i3];
                        int length = internalArray$external__okio__android_common__okio_lib.length;
                        int i7 = 0;
                        while (i7 < length) {
                            byte b3 = internalArray$external__okio__android_common__okio_lib[i7];
                            i7++;
                            if (b2 == b3) {
                                i4 = segment.pos;
                            }
                        }
                        i3++;
                    }
                    j3 += segment.limit - segment.pos;
                    segment = segment.next;
                    Intrinsics.checkNotNull(segment);
                    j = j3;
                }
                return -1L;
                return (i3 - i4) + j3;
            }
            while (true) {
                long j4 = (segment.limit - segment.pos) + j2;
                if (j4 > j) {
                    break;
                }
                segment = segment.next;
                Intrinsics.checkNotNull(segment);
                j2 = j4;
            }
            if (byteString.getSize$external__okio__android_common__okio_lib() == 2) {
                byte internalGet$external__okio__android_common__okio_lib3 = byteString.internalGet$external__okio__android_common__okio_lib(0);
                byte internalGet$external__okio__android_common__okio_lib4 = byteString.internalGet$external__okio__android_common__okio_lib(1);
                while (j2 < this.size) {
                    byte[] bArr3 = segment.data;
                    i = (int) ((segment.pos + j) - j2);
                    int i8 = segment.limit;
                    while (i < i8) {
                        byte b4 = bArr3[i];
                        if (b4 == internalGet$external__okio__android_common__okio_lib3 || b4 == internalGet$external__okio__android_common__okio_lib4) {
                            i2 = segment.pos;
                        } else {
                            i++;
                        }
                    }
                    j2 += segment.limit - segment.pos;
                    segment = segment.next;
                    Intrinsics.checkNotNull(segment);
                    j = j2;
                }
                return -1L;
            }
            byte[] internalArray$external__okio__android_common__okio_lib2 = byteString.internalArray$external__okio__android_common__okio_lib();
            while (j2 < this.size) {
                byte[] bArr4 = segment.data;
                i = (int) ((segment.pos + j) - j2);
                int i9 = segment.limit;
                while (i < i9) {
                    byte b5 = bArr4[i];
                    int length2 = internalArray$external__okio__android_common__okio_lib2.length;
                    int i10 = 0;
                    while (i10 < length2) {
                        byte b6 = internalArray$external__okio__android_common__okio_lib2[i10];
                        i10++;
                        if (b5 == b6) {
                            i2 = segment.pos;
                        }
                    }
                    i++;
                }
                j2 += segment.limit - segment.pos;
                segment = segment.next;
                Intrinsics.checkNotNull(segment);
                j = j2;
            }
            return -1L;
            return (i - i2) + j2;
        }
        throw new IllegalArgumentException(Intrinsics.stringPlus("fromIndex < 0: ", Long.valueOf(j)).toString());
    }

    public final byte readByte() throws EOFException {
        if (this.size != 0) {
            Segment segment = this.head;
            Intrinsics.checkNotNull(segment);
            int i = segment.pos;
            int i2 = segment.limit;
            int i3 = i + 1;
            byte b = segment.data[i];
            this.size--;
            if (i3 == i2) {
                this.head = segment.pop();
                SegmentPool.recycle(segment);
            } else {
                segment.pos = i3;
            }
            return b;
        }
        throw new EOFException();
    }

    public final byte[] readByteArray(long j) throws EOFException {
        boolean z;
        int i;
        int i2 = 0;
        if (j < 0 || j > 2147483647L) {
            z = false;
        } else {
            z = true;
        }
        if (!z) {
            throw new IllegalArgumentException(Intrinsics.stringPlus("byteCount: ", Long.valueOf(j)).toString());
        } else if (this.size >= j) {
            int i3 = (int) j;
            byte[] bArr = new byte[i3];
            while (i2 < i3) {
                int i4 = i3 - i2;
                Util.checkOffsetAndCount(i3, i2, i4);
                Segment segment = this.head;
                if (segment == null) {
                    i = -1;
                } else {
                    i = Math.min(i4, segment.limit - segment.pos);
                    byte[] bArr2 = segment.data;
                    int i5 = segment.pos;
                    System.arraycopy(bArr2, i5, bArr, i2, (i5 + i) - i5);
                    int i6 = segment.pos + i;
                    segment.pos = i6;
                    this.size -= i;
                    if (i6 == segment.limit) {
                        this.head = segment.pop();
                        SegmentPool.recycle(segment);
                    }
                }
                if (i != -1) {
                    i2 += i;
                } else {
                    throw new EOFException();
                }
            }
            return bArr;
        } else {
            throw new EOFException();
        }
    }

    public final ByteString readByteString() {
        boolean z;
        long j = this.size;
        if (j < 0 || j > 2147483647L) {
            z = false;
        } else {
            z = true;
        }
        if (!z) {
            throw new IllegalArgumentException(Intrinsics.stringPlus("byteCount: ", Long.valueOf(j)).toString());
        } else if (j < j) {
            throw new EOFException();
        } else if (j < 4096) {
            return new ByteString(readByteArray(j));
        } else {
            ByteString snapshot = snapshot((int) j);
            skip(j);
            return snapshot;
        }
    }

    public final String readString(long j, Charset charset) throws EOFException {
        boolean z;
        int i = (j > 0L ? 1 : (j == 0L ? 0 : -1));
        if (i < 0 || j > 2147483647L) {
            z = false;
        } else {
            z = true;
        }
        if (!z) {
            throw new IllegalArgumentException(Intrinsics.stringPlus("byteCount: ", Long.valueOf(j)).toString());
        } else if (this.size < j) {
            throw new EOFException();
        } else if (i == 0) {
            return "";
        } else {
            Segment segment = this.head;
            Intrinsics.checkNotNull(segment);
            int i2 = segment.pos;
            if (i2 + j > segment.limit) {
                return new String(readByteArray(j), charset);
            }
            int i3 = (int) j;
            String str = new String(segment.data, i2, i3, charset);
            int i4 = segment.pos + i3;
            segment.pos = i4;
            this.size -= j;
            if (i4 == segment.limit) {
                this.head = segment.pop();
                SegmentPool.recycle(segment);
            }
            return str;
        }
    }

    public final String readUtf8(long j) throws EOFException {
        return readString(j, Charsets.UTF_8);
    }

    @Override // okio.BufferedSource
    public final boolean request(long j) {
        if (this.size >= j) {
            return true;
        }
        return false;
    }

    public final void skip(long j) throws EOFException {
        while (j > 0) {
            Segment segment = this.head;
            if (segment != null) {
                int min = (int) Math.min(j, segment.limit - segment.pos);
                long j2 = min;
                this.size -= j2;
                j -= j2;
                int i = segment.pos + min;
                segment.pos = i;
                if (i == segment.limit) {
                    this.head = segment.pop();
                    SegmentPool.recycle(segment);
                }
            } else {
                throw new EOFException();
            }
        }
    }

    public final ByteString snapshot(int i) {
        if (i == 0) {
            return ByteString.EMPTY;
        }
        Util.checkOffsetAndCount(this.size, 0L, i);
        Segment segment = this.head;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (i3 < i) {
            Intrinsics.checkNotNull(segment);
            int i5 = segment.limit;
            int i6 = segment.pos;
            if (i5 != i6) {
                i3 += i5 - i6;
                i4++;
                segment = segment.next;
            } else {
                throw new AssertionError("s.limit == s.pos");
            }
        }
        byte[][] bArr = new byte[i4];
        int[] iArr = new int[i4 * 2];
        Segment segment2 = this.head;
        int i7 = 0;
        while (i2 < i) {
            Intrinsics.checkNotNull(segment2);
            bArr[i7] = segment2.data;
            i2 += segment2.limit - segment2.pos;
            iArr[i7] = Math.min(i2, i);
            iArr[i7 + i4] = segment2.pos;
            segment2.shared = true;
            i7++;
            segment2 = segment2.next;
        }
        return new SegmentedByteString(bArr, iArr);
    }

    public final String toString() {
        boolean z;
        long j = this.size;
        if (j <= 2147483647L) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            return snapshot((int) j).toString();
        }
        throw new IllegalStateException(Intrinsics.stringPlus("size > Int.MAX_VALUE: ", Long.valueOf(j)).toString());
    }

    public final Segment writableSegment$external__okio__android_common__okio_lib(int i) {
        boolean z = true;
        if (i < 1 || i > 8192) {
            z = false;
        }
        if (z) {
            Segment segment = this.head;
            if (segment == null) {
                Segment take = SegmentPool.take();
                this.head = take;
                take.prev = take;
                take.next = take;
                return take;
            }
            Intrinsics.checkNotNull(segment);
            Segment segment2 = segment.prev;
            Intrinsics.checkNotNull(segment2);
            if (segment2.limit + i <= 8192 && segment2.owner) {
                return segment2;
            }
            Segment take2 = SegmentPool.take();
            segment2.push(take2);
            return take2;
        }
        throw new IllegalArgumentException("unexpected capacity".toString());
    }

    @Override // java.nio.channels.WritableByteChannel
    public final int write(ByteBuffer byteBuffer) throws IOException {
        int remaining = byteBuffer.remaining();
        int i = remaining;
        while (i > 0) {
            Segment writableSegment$external__okio__android_common__okio_lib = writableSegment$external__okio__android_common__okio_lib(1);
            int min = Math.min(i, 8192 - writableSegment$external__okio__android_common__okio_lib.limit);
            byteBuffer.get(writableSegment$external__okio__android_common__okio_lib.data, writableSegment$external__okio__android_common__okio_lib.limit, min);
            i -= min;
            writableSegment$external__okio__android_common__okio_lib.limit += min;
        }
        this.size += remaining;
        return remaining;
    }

    @Override // java.nio.channels.ReadableByteChannel
    public final int read(ByteBuffer byteBuffer) throws IOException {
        Segment segment = this.head;
        if (segment == null) {
            return -1;
        }
        int min = Math.min(byteBuffer.remaining(), segment.limit - segment.pos);
        byteBuffer.put(segment.data, segment.pos, min);
        int i = segment.pos + min;
        segment.pos = i;
        this.size -= min;
        if (i == segment.limit) {
            this.head = segment.pop();
            SegmentPool.recycle(segment);
        }
        return min;
    }
}
