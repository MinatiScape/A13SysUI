package com.android.systemui.screenshot;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.accessibility.AccessibilityManager;
/* loaded from: classes.dex */
public final class TimeoutHandler extends Handler {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final Context mContext;
    public int mDefaultTimeout = 6000;
    public Runnable mOnTimeout = TimeoutHandler$$ExternalSyntheticLambda0.INSTANCE;

    public final void resetTimeout() {
        removeMessages(2);
        sendMessageDelayed(obtainMessage(2), ((AccessibilityManager) this.mContext.getSystemService("accessibility")).getRecommendedTimeoutMillis(this.mDefaultTimeout, 4));
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        if (message.what == 2) {
            this.mOnTimeout.run();
        }
    }

    public TimeoutHandler(Context context) {
        super(Looper.getMainLooper());
        this.mContext = context;
    }
}
