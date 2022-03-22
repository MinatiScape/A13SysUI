package com.android.systemui.dreams.complication;

import android.view.ViewGroup;
import java.util.HashMap;
/* loaded from: classes.dex */
public final class ComplicationLayoutParams extends ViewGroup.LayoutParams {
    public static final HashMap INVALID_DIRECTIONS;
    public static final int[] INVALID_POSITIONS = {3, 12};
    public final int mDirection;
    public final int mPosition;
    public final boolean mSnapToGuide;
    public final int mWeight;

    static {
        HashMap hashMap = new HashMap();
        INVALID_DIRECTIONS = hashMap;
        hashMap.put(2, 2);
        hashMap.put(1, 1);
        hashMap.put(4, 4);
        hashMap.put(8, 8);
    }

    public ComplicationLayoutParams(int i, int i2, int i3) {
        this(0, i, i2, i3, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x001d  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x005f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public ComplicationLayoutParams(int r7, int r8, int r9, int r10, boolean r11) {
        /*
            r6 = this;
            r0 = -2
            r6.<init>(r7, r0)
            r7 = 1
            r0 = 0
            if (r8 != 0) goto L_0x0009
            goto L_0x0015
        L_0x0009:
            int[] r1 = com.android.systemui.dreams.complication.ComplicationLayoutParams.INVALID_POSITIONS
            r2 = 2
            r3 = r0
        L_0x000d:
            if (r3 >= r2) goto L_0x001a
            r4 = r1[r3]
            r5 = r8 & r4
            if (r5 != r4) goto L_0x0017
        L_0x0015:
            r1 = r0
            goto L_0x001b
        L_0x0017:
            int r3 = r3 + 1
            goto L_0x000d
        L_0x001a:
            r1 = r7
        L_0x001b:
            if (r1 == 0) goto L_0x005f
            r6.mPosition = r8
            r1 = r7
        L_0x0020:
            r2 = 8
            if (r1 > r2) goto L_0x004a
            r2 = r8 & r1
            if (r2 != r1) goto L_0x0047
            java.util.HashMap r2 = com.android.systemui.dreams.complication.ComplicationLayoutParams.INVALID_DIRECTIONS
            java.lang.Integer r3 = java.lang.Integer.valueOf(r1)
            boolean r3 = r2.containsKey(r3)
            if (r3 == 0) goto L_0x0047
            java.lang.Integer r3 = java.lang.Integer.valueOf(r1)
            java.lang.Object r2 = r2.get(r3)
            java.lang.Integer r2 = (java.lang.Integer) r2
            int r2 = r2.intValue()
            r2 = r2 & r9
            if (r2 == 0) goto L_0x0047
            r7 = r0
            goto L_0x004a
        L_0x0047:
            int r1 = r1 << 1
            goto L_0x0020
        L_0x004a:
            if (r7 == 0) goto L_0x0053
            r6.mDirection = r9
            r6.mWeight = r10
            r6.mSnapToGuide = r11
            return
        L_0x0053:
            java.lang.IllegalArgumentException r6 = new java.lang.IllegalArgumentException
            java.lang.String r7 = "invalid direction:"
            java.lang.String r7 = android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0.m(r7, r9)
            r6.<init>(r7)
            throw r6
        L_0x005f:
            java.lang.IllegalArgumentException r6 = new java.lang.IllegalArgumentException
            java.lang.String r7 = "invalid position:"
            java.lang.String r7 = android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0.m(r7, r8)
            r6.<init>(r7)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dreams.complication.ComplicationLayoutParams.<init>(int, int, int, int, boolean):void");
    }
}
