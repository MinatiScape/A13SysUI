package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import okio.internal.ByteStringKt;
/* compiled from: ByteString.kt */
/* loaded from: classes.dex */
public class ByteString implements Serializable, Comparable<ByteString> {
    public static final ByteString EMPTY = new ByteString(new byte[0]);
    private static final long serialVersionUID = 1;
    private final byte[] data;
    public transient int hashCode;
    public transient String utf8;

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ByteString) {
            ByteString byteString = (ByteString) obj;
            Objects.requireNonNull(byteString);
            int size$external__okio__android_common__okio_lib = byteString.getSize$external__okio__android_common__okio_lib();
            byte[] bArr = this.data;
            if (size$external__okio__android_common__okio_lib == bArr.length && byteString.rangeEquals(0, bArr, 0, bArr.length)) {
                return true;
            }
        }
        return false;
    }

    public boolean rangeEquals(int i, byte[] bArr, int i2, int i3) {
        boolean z;
        if (i < 0) {
            return false;
        }
        byte[] bArr2 = this.data;
        if (i > bArr2.length - i3 || i2 < 0 || i2 > bArr.length - i3) {
            return false;
        }
        int i4 = 0;
        while (true) {
            if (i4 >= i3) {
                z = true;
                break;
            }
            i4++;
            if (bArr2[i4 + i] != bArr[i4 + i2]) {
                z = false;
                break;
            }
        }
        return z;
    }

    public static final ByteString encodeUtf8(String str) {
        ByteString byteString = new ByteString(str.getBytes(Charsets.UTF_8));
        byteString.utf8 = str;
        return byteString;
    }

    private final void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeInt(this.data.length);
        objectOutputStream.write(this.data);
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x002a, code lost:
        if (r0 < r1) goto L_0x002c;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x002e, code lost:
        return 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:?, code lost:
        return -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0024, code lost:
        if (r6 < r7) goto L_0x002c;
     */
    @Override // java.lang.Comparable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final int compareTo(okio.ByteString r9) {
        /*
            r8 = this;
            okio.ByteString r9 = (okio.ByteString) r9
            int r0 = r8.getSize$external__okio__android_common__okio_lib()
            int r1 = r9.getSize$external__okio__android_common__okio_lib()
            int r2 = java.lang.Math.min(r0, r1)
            r3 = 0
            r4 = r3
        L_0x0010:
            r5 = -1
            if (r4 >= r2) goto L_0x0027
            byte r6 = r8.internalGet$external__okio__android_common__okio_lib(r4)
            r6 = r6 & 255(0xff, float:3.57E-43)
            byte r7 = r9.internalGet$external__okio__android_common__okio_lib(r4)
            r7 = r7 & 255(0xff, float:3.57E-43)
            if (r6 != r7) goto L_0x0024
            int r4 = r4 + 1
            goto L_0x0010
        L_0x0024:
            if (r6 >= r7) goto L_0x002e
            goto L_0x002c
        L_0x0027:
            if (r0 != r1) goto L_0x002a
            goto L_0x002f
        L_0x002a:
            if (r0 >= r1) goto L_0x002e
        L_0x002c:
            r3 = r5
            goto L_0x002f
        L_0x002e:
            r3 = 1
        L_0x002f:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.ByteString.compareTo(java.lang.Object):int");
    }

    public int getSize$external__okio__android_common__okio_lib() {
        return this.data.length;
    }

    public int hashCode() {
        int i = this.hashCode;
        if (i != 0) {
            return i;
        }
        int hashCode = Arrays.hashCode(this.data);
        this.hashCode = hashCode;
        return hashCode;
    }

    public String hex() {
        byte[] bArr = this.data;
        char[] cArr = new char[bArr.length * 2];
        int length = bArr.length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            byte b = bArr[i];
            i++;
            int i3 = i2 + 1;
            char[] cArr2 = ByteStringKt.HEX_DIGIT_CHARS;
            cArr[i2] = cArr2[(b >> 4) & 15];
            i2 = i3 + 1;
            cArr[i3] = cArr2[b & 15];
        }
        return new String(cArr);
    }

    public byte internalGet$external__okio__android_common__okio_lib(int i) {
        return this.data[i];
    }

    /* JADX WARN: Code restructure failed: missing block: B:103:0x0106, code lost:
        if (r4 == 64) goto L_0x022f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x0119, code lost:
        if (r4 == 64) goto L_0x022f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:115:0x0128, code lost:
        if (r4 == 64) goto L_0x022f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:118:0x013a, code lost:
        if (r4 == 64) goto L_0x022f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:124:0x0147, code lost:
        if (r4 == 64) goto L_0x022f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:156:0x018f, code lost:
        if (r4 == 64) goto L_0x022f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:162:0x01a2, code lost:
        if (r4 == 64) goto L_0x022f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x01b3, code lost:
        if (r4 == 64) goto L_0x022f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:174:0x01c2, code lost:
        if (r4 == 64) goto L_0x022f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:177:0x01d8, code lost:
        if (r4 == 64) goto L_0x022f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:183:0x01e5, code lost:
        if (r4 == 64) goto L_0x022f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:186:0x01ec, code lost:
        if (r4 == 64) goto L_0x022f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:215:0x022b, code lost:
        if (r4 == 64) goto L_0x022f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:216:0x022e, code lost:
        r5 = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x009e, code lost:
        if (r4 == 64) goto L_0x022f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x00af, code lost:
        if (r4 == 64) goto L_0x022f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x00ba, code lost:
        if (r4 == 64) goto L_0x022f;
     */
    /* JADX WARN: Removed duplicated region for block: B:245:0x022e A[EDGE_INSN: B:245:0x022e->B:216:0x022e ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:250:0x022e A[EDGE_INSN: B:250:0x022e->B:216:0x022e ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:256:0x022e A[EDGE_INSN: B:256:0x022e->B:216:0x022e ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:269:0x022e A[EDGE_INSN: B:269:0x022e->B:216:0x022e ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:274:0x022e A[EDGE_INSN: B:274:0x022e->B:216:0x022e ?: BREAK  , SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.String toString() {
        /*
            Method dump skipped, instructions count: 816
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.ByteString.toString():java.lang.String");
    }

    public ByteString(byte[] bArr) {
        this.data = bArr;
    }

    private final void readObject(ObjectInputStream objectInputStream) throws IOException {
        boolean z;
        int readInt = objectInputStream.readInt();
        int i = 0;
        if (readInt >= 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            byte[] bArr = new byte[readInt];
            while (i < readInt) {
                int read = objectInputStream.read(bArr, i, readInt - i);
                if (read != -1) {
                    i += read;
                } else {
                    throw new EOFException();
                }
            }
            ByteString byteString = new ByteString(bArr);
            Field declaredField = ByteString.class.getDeclaredField("data");
            declaredField.setAccessible(true);
            declaredField.set(this, byteString.data);
            return;
        }
        throw new IllegalArgumentException(Intrinsics.stringPlus("byteCount < 0: ", Integer.valueOf(readInt)).toString());
    }

    public boolean rangeEquals(ByteString byteString, int i) {
        return byteString.rangeEquals(0, this.data, 0, i);
    }

    public final byte[] getData$external__okio__android_common__okio_lib() {
        return this.data;
    }

    public byte[] internalArray$external__okio__android_common__okio_lib() {
        return this.data;
    }
}
