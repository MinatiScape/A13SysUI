package com.android.systemui.unfold;

import com.android.systemui.statusbar.policy.CallbackController;
/* compiled from: UnfoldTransitionProgressProvider.kt */
/* loaded from: classes.dex */
public interface UnfoldTransitionProgressProvider extends CallbackController<TransitionProgressListener> {

    /* compiled from: UnfoldTransitionProgressProvider.kt */
    /* loaded from: classes.dex */
    public interface TransitionProgressListener {
        void onTransitionFinished();

        void onTransitionProgress(float f);

        void onTransitionStarted();
    }
}
