package com.android.settingslib;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline1;
/* loaded from: classes.dex */
public class SignalIcon$IconGroup {
    public final int[] contentDesc;
    public final int discContentDesc;
    public final String name;
    public final int qsDiscState;
    public final int[][] qsIcons;
    public final int qsNullState;
    public final int sbDiscState;
    public final int[][] sbIcons;
    public final int sbNullState;

    public final String toString() {
        return MotionController$$ExternalSyntheticOutline1.m(VendorAtomValue$$ExternalSyntheticOutline1.m("IconGroup("), this.name, ")");
    }

    public SignalIcon$IconGroup(String str, int[][] iArr, int[][] iArr2, int[] iArr3, int i, int i2, int i3, int i4, int i5) {
        this.name = str;
        this.sbIcons = iArr;
        this.qsIcons = iArr2;
        this.contentDesc = iArr3;
        this.sbNullState = i;
        this.qsNullState = i2;
        this.sbDiscState = i3;
        this.qsDiscState = i4;
        this.discContentDesc = i5;
    }
}
