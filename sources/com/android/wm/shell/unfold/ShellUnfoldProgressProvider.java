package com.android.wm.shell.unfold;

import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public interface ShellUnfoldProgressProvider {
    public static final AnonymousClass1 NO_PROVIDER = new ShellUnfoldProgressProvider() { // from class: com.android.wm.shell.unfold.ShellUnfoldProgressProvider.1
    };

    /* loaded from: classes.dex */
    public interface UnfoldListener {
        default void onStateChangeFinished() {
        }

        default void onStateChangeProgress(float f) {
        }
    }

    default void addListener(Executor executor, UnfoldListener unfoldListener) {
    }
}
