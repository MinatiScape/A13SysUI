package com.android.systemui.unfold;

import com.android.internal.util.FrameworkStatsLog;
import com.android.systemui.unfold.FoldStateLoggingProvider;
/* compiled from: FoldStateLogger.kt */
/* loaded from: classes.dex */
public final class FoldStateLogger implements FoldStateLoggingProvider.FoldStateLoggingListener {
    public final FoldStateLoggingProvider foldStateLoggingProvider;

    @Override // com.android.systemui.unfold.FoldStateLoggingProvider.FoldStateLoggingListener
    public final void onFoldUpdate(FoldStateChange foldStateChange) {
        FrameworkStatsLog.write(414, foldStateChange.previous, foldStateChange.current, foldStateChange.dtMillis);
    }

    public FoldStateLogger(FoldStateLoggingProvider foldStateLoggingProvider) {
        this.foldStateLoggingProvider = foldStateLoggingProvider;
    }
}
