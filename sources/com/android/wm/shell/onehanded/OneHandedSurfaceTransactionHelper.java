package com.android.wm.shell.onehanded;

import android.content.Context;
import android.content.res.Resources;
/* loaded from: classes.dex */
public final class OneHandedSurfaceTransactionHelper {
    public final float mCornerRadius;
    public final float mCornerRadiusAdjustment;
    public final boolean mEnableCornerRadius;

    public OneHandedSurfaceTransactionHelper(Context context) {
        Resources resources = context.getResources();
        float dimension = resources.getDimension(17105513);
        this.mCornerRadiusAdjustment = dimension;
        this.mCornerRadius = resources.getDimension(17105512) - dimension;
        this.mEnableCornerRadius = resources.getBoolean(2131034148);
    }
}
