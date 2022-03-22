package com.android.systemui.unfold;

import com.android.systemui.statusbar.policy.CallbackController;
/* compiled from: FoldStateLoggingProvider.kt */
/* loaded from: classes.dex */
public interface FoldStateLoggingProvider extends CallbackController<FoldStateLoggingListener> {

    /* compiled from: FoldStateLoggingProvider.kt */
    /* loaded from: classes.dex */
    public interface FoldStateLoggingListener {
        void onFoldUpdate(FoldStateChange foldStateChange);
    }

    void init();
}
