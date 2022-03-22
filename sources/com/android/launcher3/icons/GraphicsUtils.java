package com.android.launcher3.icons;

import android.content.Context;
import android.content.res.TypedArray;
/* loaded from: classes.dex */
public final class GraphicsUtils {
    public static final /* synthetic */ int $r8$clinit = 0;

    public static int getAttrColor(Context context, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{i});
        int color = obtainStyledAttributes.getColor(0, 0);
        obtainStyledAttributes.recycle();
        return color;
    }
}
