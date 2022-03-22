package com.android.systemui.unfold;

import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import com.android.wm.shell.unfold.ShellUnfoldProgressProvider;
import java.util.Objects;
import java.util.concurrent.Executor;
/* compiled from: UnfoldProgressProvider.kt */
/* loaded from: classes.dex */
public final class UnfoldProgressProvider implements ShellUnfoldProgressProvider {
    public final UnfoldTransitionProgressProvider unfoldProgressProvider;

    @Override // com.android.wm.shell.unfold.ShellUnfoldProgressProvider
    public final void addListener(final Executor executor, final ShellUnfoldProgressProvider.UnfoldListener unfoldListener) {
        this.unfoldProgressProvider.addCallback(new UnfoldTransitionProgressProvider.TransitionProgressListener() { // from class: com.android.systemui.unfold.UnfoldProgressProvider$addListener$1
            @Override // com.android.systemui.unfold.UnfoldTransitionProgressProvider.TransitionProgressListener
            public final void onTransitionFinished() {
                Executor executor2 = executor;
                final ShellUnfoldProgressProvider.UnfoldListener unfoldListener2 = unfoldListener;
                executor2.execute(new Runnable() { // from class: com.android.systemui.unfold.UnfoldProgressProvider$addListener$1$onTransitionFinished$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        ShellUnfoldProgressProvider.UnfoldListener.this.onStateChangeFinished();
                    }
                });
            }

            @Override // com.android.systemui.unfold.UnfoldTransitionProgressProvider.TransitionProgressListener
            public final void onTransitionProgress(final float f) {
                Executor executor2 = executor;
                final ShellUnfoldProgressProvider.UnfoldListener unfoldListener2 = unfoldListener;
                executor2.execute(new Runnable() { // from class: com.android.systemui.unfold.UnfoldProgressProvider$addListener$1$onTransitionProgress$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        ShellUnfoldProgressProvider.UnfoldListener.this.onStateChangeProgress(f);
                    }
                });
            }

            @Override // com.android.systemui.unfold.UnfoldTransitionProgressProvider.TransitionProgressListener
            public final void onTransitionStarted() {
                Executor executor2 = executor;
                final ShellUnfoldProgressProvider.UnfoldListener unfoldListener2 = unfoldListener;
                executor2.execute(new Runnable() { // from class: com.android.systemui.unfold.UnfoldProgressProvider$addListener$1$onTransitionStarted$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        Objects.requireNonNull(ShellUnfoldProgressProvider.UnfoldListener.this);
                    }
                });
            }
        });
    }

    public UnfoldProgressProvider(UnfoldTransitionProgressProvider unfoldTransitionProgressProvider) {
        this.unfoldProgressProvider = unfoldTransitionProgressProvider;
    }
}
