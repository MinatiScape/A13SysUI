package com.android.wm.shell.common;

import android.os.Handler;
import com.google.android.apps.miphone.aiai.matchmaker.overview.ui.SuggestController$$ExternalSyntheticLambda1;
/* loaded from: classes.dex */
public final class HandlerExecutor implements ShellExecutor {
    public final Handler mHandler;

    @Override // com.android.wm.shell.common.ShellExecutor, java.util.concurrent.Executor
    public final void execute(Runnable runnable) {
        if (this.mHandler.getLooper().isCurrentThread()) {
            runnable.run();
        } else if (!this.mHandler.post(runnable)) {
            throw new RuntimeException(this.mHandler + " is probably exiting");
        }
    }

    @Override // com.android.wm.shell.common.ShellExecutor
    public final void executeDelayed(Runnable runnable, long j) {
        if (!this.mHandler.postDelayed(runnable, j)) {
            throw new RuntimeException(this.mHandler + " is probably exiting");
        }
    }

    @Override // com.android.wm.shell.common.ShellExecutor
    public final boolean hasCallback(SuggestController$$ExternalSyntheticLambda1 suggestController$$ExternalSyntheticLambda1) {
        return this.mHandler.hasCallbacks(suggestController$$ExternalSyntheticLambda1);
    }

    @Override // com.android.wm.shell.common.ShellExecutor
    public final void removeCallbacks(Runnable runnable) {
        this.mHandler.removeCallbacks(runnable);
    }

    public HandlerExecutor(Handler handler) {
        this.mHandler = handler;
    }
}
