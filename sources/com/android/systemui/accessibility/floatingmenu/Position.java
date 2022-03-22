package com.android.systemui.accessibility.floatingmenu;

import android.text.TextUtils;
/* loaded from: classes.dex */
public final class Position {
    public static final TextUtils.SimpleStringSplitter sStringCommaSplitter = new TextUtils.SimpleStringSplitter(',');
    public float mPercentageX;
    public float mPercentageY;

    public final String toString() {
        return this.mPercentageX + ", " + this.mPercentageY;
    }

    public Position(float f, float f2) {
        this.mPercentageX = f;
        this.mPercentageY = f2;
    }
}
