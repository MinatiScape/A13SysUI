package com.android.systemui.dreams.touch.dagger;

import android.view.VelocityTracker;
import com.android.systemui.dreams.touch.BouncerSwipeTouchHandler;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BouncerSwipeModule$$ExternalSyntheticLambda1 implements BouncerSwipeTouchHandler.VelocityTrackerFactory {
    public static final /* synthetic */ BouncerSwipeModule$$ExternalSyntheticLambda1 INSTANCE = new BouncerSwipeModule$$ExternalSyntheticLambda1();

    @Override // com.android.systemui.dreams.touch.BouncerSwipeTouchHandler.VelocityTrackerFactory
    public final VelocityTracker obtain() {
        return VelocityTracker.obtain();
    }
}
