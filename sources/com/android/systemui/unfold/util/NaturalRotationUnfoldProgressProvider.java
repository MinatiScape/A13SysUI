package com.android.systemui.unfold.util;

import android.content.Context;
import android.view.IRotationWatcher;
import android.view.IWindowManager;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import java.util.Objects;
/* compiled from: NaturalRotationUnfoldProgressProvider.kt */
/* loaded from: classes.dex */
public final class NaturalRotationUnfoldProgressProvider implements UnfoldTransitionProgressProvider {
    public final Context context;
    public boolean isNaturalRotation;
    public final RotationWatcher rotationWatcher = new RotationWatcher();
    public final ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider;
    public final IWindowManager windowManagerInterface;

    /* compiled from: NaturalRotationUnfoldProgressProvider.kt */
    /* loaded from: classes.dex */
    public final class RotationWatcher extends IRotationWatcher.Stub {
        public RotationWatcher() {
        }

        public final void onRotationChanged(int i) {
            boolean z;
            NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider = NaturalRotationUnfoldProgressProvider.this;
            Objects.requireNonNull(naturalRotationUnfoldProgressProvider);
            if (i == 0 || i == 2) {
                z = true;
            } else {
                z = false;
            }
            if (naturalRotationUnfoldProgressProvider.isNaturalRotation != z) {
                naturalRotationUnfoldProgressProvider.isNaturalRotation = z;
                naturalRotationUnfoldProgressProvider.scopedUnfoldTransitionProgressProvider.setReadyToHandleTransition(z);
            }
        }
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

    public NaturalRotationUnfoldProgressProvider(Context context, IWindowManager iWindowManager, UnfoldTransitionProgressProvider unfoldTransitionProgressProvider) {
        this.context = context;
        this.windowManagerInterface = iWindowManager;
        this.scopedUnfoldTransitionProgressProvider = new ScopedUnfoldTransitionProgressProvider(unfoldTransitionProgressProvider);
    }
}
