package okio;

import java.util.ArrayList;
import java.util.Objects;
import java.util.RandomAccess;
import kotlin.collections.AbstractList;
/* compiled from: Options.kt */
/* loaded from: classes.dex */
public final class Options extends AbstractList<ByteString> implements RandomAccess {
    public final ByteString[] byteStrings;
    public final int[] trie;

    /* compiled from: Options.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public static void buildTrieRecursive(long j, Buffer buffer, int i, ArrayList arrayList, int i2, int i3, ArrayList arrayList2) {
            boolean z;
            int i4;
            int i5;
            boolean z2;
            int i6;
            long j2;
            long j3;
            Buffer buffer2;
            boolean z3;
            int i7 = i;
            if (i2 < i3) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                int i8 = i2;
                while (i8 < i3) {
                    int i9 = i8 + 1;
                    ByteString byteString = (ByteString) arrayList.get(i8);
                    Objects.requireNonNull(byteString);
                    if (byteString.getSize$external__okio__android_common__okio_lib() >= i7) {
                        z3 = true;
                    } else {
                        z3 = false;
                    }
                    if (z3) {
                        i8 = i9;
                    } else {
                        throw new IllegalArgumentException("Failed requirement.".toString());
                    }
                }
                ByteString byteString2 = (ByteString) arrayList.get(i2);
                ByteString byteString3 = (ByteString) arrayList.get(i3 - 1);
                Objects.requireNonNull(byteString2);
                if (i7 == byteString2.getSize$external__okio__android_common__okio_lib()) {
                    int intValue = ((Number) arrayList2.get(i2)).intValue();
                    int i10 = i2 + 1;
                    ByteString byteString4 = (ByteString) arrayList.get(i10);
                    i4 = i10;
                    i5 = intValue;
                    byteString2 = byteString4;
                } else {
                    i4 = i2;
                    i5 = -1;
                }
                Objects.requireNonNull(byteString2);
                byte internalGet$external__okio__android_common__okio_lib = byteString2.internalGet$external__okio__android_common__okio_lib(i7);
                Objects.requireNonNull(byteString3);
                if (internalGet$external__okio__android_common__okio_lib != byteString3.internalGet$external__okio__android_common__okio_lib(i7)) {
                    int i11 = i4 + 1;
                    int i12 = 1;
                    while (i11 < i3) {
                        int i13 = i11 + 1;
                        ByteString byteString5 = (ByteString) arrayList.get(i11 - 1);
                        Objects.requireNonNull(byteString5);
                        byte internalGet$external__okio__android_common__okio_lib2 = byteString5.internalGet$external__okio__android_common__okio_lib(i7);
                        ByteString byteString6 = (ByteString) arrayList.get(i11);
                        Objects.requireNonNull(byteString6);
                        if (internalGet$external__okio__android_common__okio_lib2 != byteString6.internalGet$external__okio__android_common__okio_lib(i7)) {
                            i12++;
                        }
                        i11 = i13;
                    }
                    long j4 = 4;
                    long j5 = (i12 * 2) + (buffer.size / j4) + j + 2;
                    buffer.writeInt(i12);
                    buffer.writeInt(i5);
                    int i14 = i4;
                    while (i14 < i3) {
                        int i15 = i14 + 1;
                        ByteString byteString7 = (ByteString) arrayList.get(i14);
                        Objects.requireNonNull(byteString7);
                        byte internalGet$external__okio__android_common__okio_lib3 = byteString7.internalGet$external__okio__android_common__okio_lib(i7);
                        if (i14 != i4) {
                            ByteString byteString8 = (ByteString) arrayList.get(i14 - 1);
                            Objects.requireNonNull(byteString8);
                            if (internalGet$external__okio__android_common__okio_lib3 == byteString8.internalGet$external__okio__android_common__okio_lib(i7)) {
                                i14 = i15;
                            }
                        }
                        buffer.writeInt(internalGet$external__okio__android_common__okio_lib3 & 255);
                        i14 = i15;
                    }
                    Buffer buffer3 = new Buffer();
                    while (i4 < i3) {
                        ByteString byteString9 = (ByteString) arrayList.get(i4);
                        Objects.requireNonNull(byteString9);
                        byte internalGet$external__okio__android_common__okio_lib4 = byteString9.internalGet$external__okio__android_common__okio_lib(i7);
                        int i16 = i4 + 1;
                        int i17 = i16;
                        while (true) {
                            if (i17 >= i3) {
                                i6 = i3;
                                break;
                            }
                            int i18 = i17 + 1;
                            ByteString byteString10 = (ByteString) arrayList.get(i17);
                            Objects.requireNonNull(byteString10);
                            if (internalGet$external__okio__android_common__okio_lib4 != byteString10.internalGet$external__okio__android_common__okio_lib(i7)) {
                                i6 = i17;
                                break;
                            }
                            i17 = i18;
                        }
                        if (i16 == i6) {
                            int i19 = i7 + 1;
                            ByteString byteString11 = (ByteString) arrayList.get(i4);
                            Objects.requireNonNull(byteString11);
                            if (i19 == byteString11.getSize$external__okio__android_common__okio_lib()) {
                                buffer.writeInt(((Number) arrayList2.get(i4)).intValue());
                                j3 = j4;
                                j2 = j5;
                                buffer2 = buffer3;
                                buffer3 = buffer2;
                                i4 = i6;
                                j4 = j3;
                                j5 = j2;
                            }
                        }
                        buffer.writeInt(((int) ((buffer3.size / j4) + j5)) * (-1));
                        j2 = j5;
                        buffer2 = buffer3;
                        j3 = j4;
                        buildTrieRecursive(j5, buffer3, i7 + 1, arrayList, i4, i6, arrayList2);
                        buffer3 = buffer2;
                        i4 = i6;
                        j4 = j3;
                        j5 = j2;
                    }
                    do {
                    } while (buffer3.read(buffer, 8192L) != -1);
                    return;
                }
                int min = Math.min(byteString2.getSize$external__okio__android_common__okio_lib(), byteString3.getSize$external__okio__android_common__okio_lib());
                int i20 = i7;
                int i21 = 0;
                while (i20 < min) {
                    int i22 = i20 + 1;
                    if (byteString2.internalGet$external__okio__android_common__okio_lib(i20) != byteString3.internalGet$external__okio__android_common__okio_lib(i20)) {
                        break;
                    }
                    i21++;
                    i20 = i22;
                }
                long j6 = 4;
                long j7 = 1 + (buffer.size / j6) + j + 2 + i21;
                buffer.writeInt(-i21);
                buffer.writeInt(i5);
                int i23 = i7 + i21;
                while (i7 < i23) {
                    buffer.writeInt(byteString2.internalGet$external__okio__android_common__okio_lib(i7) & 255);
                    i7++;
                }
                if (i4 + 1 == i3) {
                    ByteString byteString12 = (ByteString) arrayList.get(i4);
                    Objects.requireNonNull(byteString12);
                    if (i23 == byteString12.getSize$external__okio__android_common__okio_lib()) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    if (z2) {
                        buffer.writeInt(((Number) arrayList2.get(i4)).intValue());
                        return;
                    }
                    throw new IllegalStateException("Check failed.".toString());
                }
                Buffer buffer4 = new Buffer();
                buffer.writeInt(((int) ((buffer4.size / j6) + j7)) * (-1));
                buildTrieRecursive(j7, buffer4, i23, arrayList, i4, i3, arrayList2);
                do {
                } while (buffer4.read(buffer, 8192L) != -1);
                return;
            }
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:99:0x016e, code lost:
        continue;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final okio.Options of(okio.ByteString... r14) {
        /*
            Method dump skipped, instructions count: 566
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.Options.of(okio.ByteString[]):okio.Options");
    }

    @Override // kotlin.collections.AbstractCollection, java.util.Collection
    public final /* bridge */ boolean contains(Object obj) {
        if (!(obj instanceof ByteString)) {
            return false;
        }
        return super.contains((ByteString) obj);
    }

    @Override // kotlin.collections.AbstractList, java.util.List
    public final Object get(int i) {
        return this.byteStrings[i];
    }

    @Override // kotlin.collections.AbstractCollection
    public final int getSize() {
        return this.byteStrings.length;
    }

    @Override // kotlin.collections.AbstractList, java.util.List
    public final /* bridge */ int indexOf(Object obj) {
        if (!(obj instanceof ByteString)) {
            return -1;
        }
        return super.indexOf((ByteString) obj);
    }

    @Override // kotlin.collections.AbstractList, java.util.List
    public final /* bridge */ int lastIndexOf(Object obj) {
        if (!(obj instanceof ByteString)) {
            return -1;
        }
        return super.lastIndexOf((ByteString) obj);
    }

    public Options(ByteString[] byteStringArr, int[] iArr) {
        this.byteStrings = byteStringArr;
        this.trie = iArr;
    }
}
