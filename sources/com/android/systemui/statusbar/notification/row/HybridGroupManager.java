package com.android.systemui.statusbar.notification.row;

import android.content.Context;
import android.content.res.Resources;
/* loaded from: classes.dex */
public final class HybridGroupManager {
    public final Context mContext;
    public int mOverflowNumberColor;
    public int mOverflowNumberPadding;
    public float mOverflowNumberSize;

    public HybridGroupManager(Context context) {
        this.mContext = context;
        Resources resources = context.getResources();
        this.mOverflowNumberSize = resources.getDimensionPixelSize(2131165792);
        this.mOverflowNumberPadding = resources.getDimensionPixelSize(2131165791);
    }
}
