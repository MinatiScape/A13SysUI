package com.google.android.systemui.assist.uihints;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.android.systemui.assist.AssistManager;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import com.google.android.systemui.assist.uihints.TimeoutManager;
import dagger.Lazy;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public final class TimeoutManager implements NgaMessageHandler.KeepAliveListener {
    public static final long SESSION_TIMEOUT_MS = TimeUnit.SECONDS.toMillis(10);
    public final Handler mHandler = new Handler(Looper.getMainLooper());
    public final TimeoutManager$$ExternalSyntheticLambda0 mOnTimeout;
    public TimeoutCallback mTimeoutCallback;

    /* loaded from: classes.dex */
    public interface TimeoutCallback {
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.KeepAliveListener
    public final void onKeepAlive() {
        this.mHandler.removeCallbacks(this.mOnTimeout);
        this.mHandler.postDelayed(this.mOnTimeout, SESSION_TIMEOUT_MS);
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.google.android.systemui.assist.uihints.TimeoutManager$$ExternalSyntheticLambda0] */
    public TimeoutManager(final Lazy<AssistManager> lazy) {
        this.mOnTimeout = new Runnable() { // from class: com.google.android.systemui.assist.uihints.TimeoutManager$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                TimeoutManager timeoutManager = TimeoutManager.this;
                Lazy lazy2 = lazy;
                Objects.requireNonNull(timeoutManager);
                TimeoutManager.TimeoutCallback timeoutCallback = timeoutManager.mTimeoutCallback;
                if (timeoutCallback != null) {
                    ((Runnable) ((NgaUiController$$ExternalSyntheticLambda3) timeoutCallback).f$0).run();
                    return;
                }
                Log.e("TimeoutManager", "Timeout occurred, but there was no callback provided");
                ((AssistManager) lazy2.get()).hideAssist();
            }
        };
    }
}
