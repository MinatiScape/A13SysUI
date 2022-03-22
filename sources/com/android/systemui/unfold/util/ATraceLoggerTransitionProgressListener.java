package com.android.systemui.unfold.util;

import android.os.Trace;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ATraceLoggerTransitionProgressListener.kt */
/* loaded from: classes.dex */
public final class ATraceLoggerTransitionProgressListener implements UnfoldTransitionProgressProvider.TransitionProgressListener {
    public final String traceName;

    @Override // com.android.systemui.unfold.UnfoldTransitionProgressProvider.TransitionProgressListener
    public final void onTransitionFinished() {
        Trace.endAsyncSection(this.traceName, 0);
    }

    @Override // com.android.systemui.unfold.UnfoldTransitionProgressProvider.TransitionProgressListener
    public final void onTransitionProgress(float f) {
        Trace.setCounter(this.traceName, f * 100);
    }

    @Override // com.android.systemui.unfold.UnfoldTransitionProgressProvider.TransitionProgressListener
    public final void onTransitionStarted() {
        Trace.beginAsyncSection(this.traceName, 0);
    }

    public ATraceLoggerTransitionProgressListener(String str) {
        this.traceName = Intrinsics.stringPlus(str, "#FoldUnfoldTransitionInProgress");
    }
}
