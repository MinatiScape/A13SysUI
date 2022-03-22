package com.android.systemui.qs.tileimpl;

import android.animation.ArgbEvaluator;
import android.animation.PropertyValuesHolder;
import java.util.Arrays;
/* compiled from: QSTileViewImpl.kt */
/* loaded from: classes.dex */
public final class QSTileViewImplKt {
    public static final PropertyValuesHolder access$colorValuesHolder(String str, int... iArr) {
        PropertyValuesHolder ofInt = PropertyValuesHolder.ofInt(str, Arrays.copyOf(iArr, iArr.length));
        ofInt.setEvaluator(ArgbEvaluator.getInstance());
        return ofInt;
    }
}
