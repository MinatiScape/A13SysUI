package okio;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: Segment.kt */
/* loaded from: classes.dex */
public final class Segment {
    public final byte[] data;
    public int limit;
    public Segment next;
    public boolean owner;
    public int pos;
    public Segment prev;
    public boolean shared;

    public Segment() {
        this.data = new byte[8192];
        this.owner = true;
        this.shared = false;
    }

    public final Segment sharedCopy() {
        this.shared = true;
        return new Segment(this.data, this.pos, this.limit, true);
    }

    public final Segment pop() {
        Segment segment = this.next;
        if (segment == this) {
            segment = null;
        }
        Segment segment2 = this.prev;
        Intrinsics.checkNotNull(segment2);
        segment2.next = this.next;
        Segment segment3 = this.next;
        Intrinsics.checkNotNull(segment3);
        segment3.prev = this.prev;
        this.next = null;
        this.prev = null;
        return segment;
    }

    public final Segment push(Segment segment) {
        segment.prev = this;
        segment.next = this.next;
        Segment segment2 = this.next;
        Intrinsics.checkNotNull(segment2);
        segment2.prev = segment;
        this.next = segment;
        return segment;
    }

    public final void writeTo(Segment segment, int i) {
        if (segment.owner) {
            int i2 = segment.limit;
            int i3 = i2 + i;
            if (i3 > 8192) {
                if (!segment.shared) {
                    int i4 = segment.pos;
                    if (i3 - i4 <= 8192) {
                        byte[] bArr = segment.data;
                        System.arraycopy(bArr, i4, bArr, 0, i2 - i4);
                        segment.limit -= segment.pos;
                        segment.pos = 0;
                    } else {
                        throw new IllegalArgumentException();
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            }
            byte[] bArr2 = this.data;
            byte[] bArr3 = segment.data;
            int i5 = segment.limit;
            int i6 = this.pos;
            System.arraycopy(bArr2, i6, bArr3, i5, (i6 + i) - i6);
            segment.limit += i;
            this.pos += i;
            return;
        }
        throw new IllegalStateException("only owner can write".toString());
    }

    public Segment(byte[] bArr, int i, int i2, boolean z) {
        this.data = bArr;
        this.pos = i;
        this.limit = i2;
        this.shared = z;
        this.owner = false;
    }
}
