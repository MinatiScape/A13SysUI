package com.android.systemui.unfold.util;

import android.view.View;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.systemui.statusbar.window.StatusBarWindowController$$ExternalSyntheticLambda1;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import java.util.function.Supplier;
/* compiled from: JankMonitorTransitionProgressListener.kt */
/* loaded from: classes.dex */
public final class JankMonitorTransitionProgressListener implements UnfoldTransitionProgressProvider.TransitionProgressListener {
    public final Supplier<View> attachedViewProvider;
    public final InteractionJankMonitor interactionJankMonitor = InteractionJankMonitor.getInstance();

    @Override // com.android.systemui.unfold.UnfoldTransitionProgressProvider.TransitionProgressListener
    public final void onTransitionProgress(float f) {
    }

    @Override // com.android.systemui.unfold.UnfoldTransitionProgressProvider.TransitionProgressListener
    public final void onTransitionFinished() {
        this.interactionJankMonitor.end(44);
    }

    @Override // com.android.systemui.unfold.UnfoldTransitionProgressProvider.TransitionProgressListener
    public final void onTransitionStarted() {
        this.interactionJankMonitor.begin(this.attachedViewProvider.get(), 44);
    }

    public JankMonitorTransitionProgressListener(StatusBarWindowController$$ExternalSyntheticLambda1 statusBarWindowController$$ExternalSyntheticLambda1) {
        this.attachedViewProvider = statusBarWindowController$$ExternalSyntheticLambda1;
    }
}
