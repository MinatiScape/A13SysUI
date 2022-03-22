package com.google.common.base;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
/* loaded from: classes.dex */
public final class Preconditions {
    public static String badPositionIndex(int i, int i2, String str) {
        if (i < 0) {
            return Strings.lenientFormat("%s (%s) must not be negative", str, Integer.valueOf(i));
        }
        if (i2 >= 0) {
            return Strings.lenientFormat("%s (%s) must not be greater than size (%s)", str, Integer.valueOf(i), Integer.valueOf(i2));
        }
        throw new IllegalArgumentException(VendorAtomValue$$ExternalSyntheticOutline0.m("negative size: ", i2));
    }

    public static void checkState(boolean z, String str) {
        if (!z) {
            throw new IllegalStateException(str);
        }
    }

    @CanIgnoreReturnValue
    public static int checkElementIndex(int i, int i2) {
        String str;
        if (i >= 0 && i < i2) {
            return i;
        }
        if (i < 0) {
            str = Strings.lenientFormat("%s (%s) must not be negative", "index", Integer.valueOf(i));
        } else if (i2 >= 0) {
            str = Strings.lenientFormat("%s (%s) must be less than size (%s)", "index", Integer.valueOf(i), Integer.valueOf(i2));
        } else {
            throw new IllegalArgumentException(VendorAtomValue$$ExternalSyntheticOutline0.m("negative size: ", i2));
        }
        throw new IndexOutOfBoundsException(str);
    }

    @CanIgnoreReturnValue
    public static int checkPositionIndex(int i, int i2) {
        if (i >= 0 && i <= i2) {
            return i;
        }
        throw new IndexOutOfBoundsException(badPositionIndex(i, i2, "index"));
    }

    public static void checkPositionIndexes(int i, int i2, int i3) {
        String str;
        if (i < 0 || i2 < i || i2 > i3) {
            if (i < 0 || i > i3) {
                str = badPositionIndex(i, i3, "start index");
            } else if (i2 < 0 || i2 > i3) {
                str = badPositionIndex(i2, i3, "end index");
            } else {
                str = Strings.lenientFormat("end index (%s) must not be less than start index (%s)", Integer.valueOf(i2), Integer.valueOf(i));
            }
            throw new IndexOutOfBoundsException(str);
        }
    }

    public static void checkState(boolean z, String str, Object obj) {
        if (!z) {
            throw new IllegalStateException(Strings.lenientFormat(str, obj));
        }
    }
}
