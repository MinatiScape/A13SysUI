package com.android.systemui.dreams.touch;

import android.view.GestureDetector;
import android.view.MotionEvent;
import com.android.systemui.dreams.touch.DreamOverlayTouchMonitor;
import com.google.android.systemui.smartspace.InterceptingViewPager;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda3 implements DreamOverlayTouchMonitor.Evaluator, InterceptingViewPager.EventProxy {
    public final /* synthetic */ Object f$0;

    @Override // com.google.android.systemui.smartspace.InterceptingViewPager.EventProxy
    public final boolean delegateEvent(MotionEvent motionEvent) {
        return InterceptingViewPager.m173$r8$lambda$Kinb8UkpjhBhKntCOQxRMNdiw((InterceptingViewPager) this.f$0, motionEvent);
    }

    public /* synthetic */ DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda3(Object obj) {
        this.f$0 = obj;
    }

    @Override // com.android.systemui.dreams.touch.DreamOverlayTouchMonitor.Evaluator
    public final boolean evaluate(GestureDetector.OnGestureListener onGestureListener) {
        return onGestureListener.onSingleTapUp((MotionEvent) this.f$0);
    }
}
