package com.android.systemui.unfold.util;

import android.animation.ValueAnimator;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.provider.Settings;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import java.util.Objects;
/* compiled from: ScaleAwareTransitionProgressProvider.kt */
/* loaded from: classes.dex */
public final class ScaleAwareTransitionProgressProvider implements UnfoldTransitionProgressProvider {
    public final ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider;

    /* compiled from: ScaleAwareTransitionProgressProvider.kt */
    /* loaded from: classes.dex */
    public interface Factory {
        ScaleAwareTransitionProgressProvider wrap(UnfoldTransitionProgressProvider unfoldTransitionProgressProvider);
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(UnfoldTransitionProgressProvider.TransitionProgressListener transitionProgressListener) {
        ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider = this.scopedUnfoldTransitionProgressProvider;
        Objects.requireNonNull(scopedUnfoldTransitionProgressProvider);
        scopedUnfoldTransitionProgressProvider.listeners.add(transitionProgressListener);
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(UnfoldTransitionProgressProvider.TransitionProgressListener transitionProgressListener) {
        ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider = this.scopedUnfoldTransitionProgressProvider;
        Objects.requireNonNull(scopedUnfoldTransitionProgressProvider);
        scopedUnfoldTransitionProgressProvider.listeners.remove(transitionProgressListener);
    }

    public ScaleAwareTransitionProgressProvider(UnfoldTransitionProgressProvider unfoldTransitionProgressProvider, ContentResolver contentResolver) {
        ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider = new ScopedUnfoldTransitionProgressProvider(unfoldTransitionProgressProvider);
        this.scopedUnfoldTransitionProgressProvider = scopedUnfoldTransitionProgressProvider;
        contentResolver.registerContentObserver(Settings.Global.getUriFor("animator_duration_scale"), false, new ContentObserver() { // from class: com.android.systemui.unfold.util.ScaleAwareTransitionProgressProvider$animatorDurationScaleObserver$1
            {
                super(null);
            }

            @Override // android.database.ContentObserver
            public final void onChange(boolean z) {
                ScaleAwareTransitionProgressProvider scaleAwareTransitionProgressProvider = ScaleAwareTransitionProgressProvider.this;
                Objects.requireNonNull(scaleAwareTransitionProgressProvider);
                scaleAwareTransitionProgressProvider.scopedUnfoldTransitionProgressProvider.setReadyToHandleTransition(ValueAnimator.areAnimatorsEnabled());
            }
        });
        scopedUnfoldTransitionProgressProvider.setReadyToHandleTransition(ValueAnimator.areAnimatorsEnabled());
    }
}
