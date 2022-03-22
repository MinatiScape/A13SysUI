package com.android.systemui.dreams.touch.dagger;

import android.content.res.Resources;
import android.util.TypedValue;
/* loaded from: classes.dex */
public final class BouncerSwipeModule {
    public static float providesSwipeToBouncerStartRegion(Resources resources) {
        TypedValue typedValue = new TypedValue();
        resources.getValue(2131165682, typedValue, true);
        return typedValue.getFloat();
    }
}
