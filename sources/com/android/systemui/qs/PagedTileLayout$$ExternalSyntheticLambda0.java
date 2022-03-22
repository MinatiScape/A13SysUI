package com.android.systemui.qs;

import android.view.animation.Interpolator;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PagedTileLayout$$ExternalSyntheticLambda0 implements Interpolator {
    public static final /* synthetic */ PagedTileLayout$$ExternalSyntheticLambda0 INSTANCE = new PagedTileLayout$$ExternalSyntheticLambda0();

    @Override // android.animation.TimeInterpolator
    public final float getInterpolation(float f) {
        int i = PagedTileLayout.$r8$clinit;
        float f2 = f - 1.0f;
        return (f2 * f2 * f2) + 1.0f;
    }
}
