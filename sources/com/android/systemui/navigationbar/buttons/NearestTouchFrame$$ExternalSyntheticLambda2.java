package com.android.systemui.navigationbar.buttons;

import android.view.View;
import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class NearestTouchFrame$$ExternalSyntheticLambda2 implements Predicate {
    public static final /* synthetic */ NearestTouchFrame$$ExternalSyntheticLambda2 INSTANCE = new NearestTouchFrame$$ExternalSyntheticLambda2();

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        return ((View) obj).isAttachedToWindow();
    }
}
