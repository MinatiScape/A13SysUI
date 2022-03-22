package com.google.android.systemui.assist.uihints;
/* loaded from: classes.dex */
public final class StringUtils {
    public static StringStabilityInfo getRightMostStabilityInfoLeaf(String str, int i, int i2, int i3, int i4, int[][] iArr) {
        int i5;
        int i6 = -1;
        int i7 = 0;
        int i8 = -1;
        while (i < i2) {
            for (int i9 = i3; i9 < i4; i9++) {
                if (iArr[i][i9] > i7) {
                    i7 = iArr[i][i9];
                    i6 = i;
                    i8 = i9;
                }
            }
            i++;
        }
        if (i7 == 0) {
            return new StringStabilityInfo(str, i3 - 1);
        }
        int i10 = i6 + 1;
        if (i10 == i2 || (i5 = i8 + 1) == i4) {
            return new StringStabilityInfo(str, i8);
        }
        return getRightMostStabilityInfoLeaf(str, i10, i2, i5, i4, iArr);
    }

    /* loaded from: classes.dex */
    public static final class StringStabilityInfo {
        public final String stable;
        public final String unstable;

        public StringStabilityInfo(String str) {
            this.stable = "";
            this.unstable = str == null ? "" : str;
        }

        public StringStabilityInfo(String str, int i) {
            if (i >= str.length()) {
                this.stable = str;
                this.unstable = "";
                return;
            }
            int i2 = i + 1;
            this.stable = str.substring(0, i2);
            this.unstable = str.substring(i2);
        }
    }
}
