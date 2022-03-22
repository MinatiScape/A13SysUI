package com.android.wm.shell.onehanded;

import com.android.wm.shell.common.ShellExecutor;
import com.google.android.apps.miphone.aiai.matchmaker.overview.ui.SuggestController$$ExternalSyntheticLambda1;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public final class OneHandedTimeoutHandler {
    public final ShellExecutor mMainExecutor;
    public int mTimeout = 8;
    public long mTimeoutMs = TimeUnit.SECONDS.toMillis(8);
    public final SuggestController$$ExternalSyntheticLambda1 mTimeoutRunnable = new SuggestController$$ExternalSyntheticLambda1(this, 9);
    public ArrayList mListeners = new ArrayList();

    /* loaded from: classes.dex */
    public interface TimeoutListener {
        void onTimeout();
    }

    public boolean hasScheduledTimeout() {
        return this.mMainExecutor.hasCallback(this.mTimeoutRunnable);
    }

    public final void resetTimer() {
        this.mMainExecutor.removeCallbacks(this.mTimeoutRunnable);
        int i = this.mTimeout;
        if (i != 0 && i != 0) {
            this.mMainExecutor.executeDelayed(this.mTimeoutRunnable, this.mTimeoutMs);
        }
    }

    public OneHandedTimeoutHandler(ShellExecutor shellExecutor) {
        this.mMainExecutor = shellExecutor;
    }
}
