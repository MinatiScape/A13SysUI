package okio.internal;

import okio.SegmentedByteString;
/* compiled from: SegmentedByteString.kt */
/* loaded from: classes.dex */
public final class SegmentedByteStringKt {
    public static final int segment(SegmentedByteString segmentedByteString, int i) {
        int i2;
        int[] iArr = segmentedByteString.directory;
        int i3 = i + 1;
        int i4 = 0;
        int length = segmentedByteString.segments.length - 1;
        while (true) {
            if (i4 <= length) {
                i2 = (i4 + length) >>> 1;
                int i5 = iArr[i2];
                if (i5 >= i3) {
                    if (i5 <= i3) {
                        break;
                    }
                    length = i2 - 1;
                } else {
                    i4 = i2 + 1;
                }
            } else {
                i2 = (-i4) - 1;
                break;
            }
        }
        if (i2 >= 0) {
            return i2;
        }
        return ~i2;
    }
}
