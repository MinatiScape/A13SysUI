package com.android.systemui.statusbar.policy;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SmartReplyStateInflater.kt */
/* loaded from: classes.dex */
public final class DelayedOnClickListener implements View.OnClickListener {
    public final View.OnClickListener mActualListener;
    public final long mInitDelayMs;
    public final long mInitTimeMs = SystemClock.elapsedRealtime();

    public DelayedOnClickListener(View.OnClickListener onClickListener, long j) {
        this.mActualListener = onClickListener;
        this.mInitDelayMs = j;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        boolean z;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.mInitTimeMs;
        long j2 = this.mInitDelayMs;
        if (elapsedRealtime >= j + j2) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            this.mActualListener.onClick(view);
        } else {
            Log.i("SmartReplyViewInflater", Intrinsics.stringPlus("Accidental Smart Suggestion click registered, delay: ", Long.valueOf(j2)));
        }
    }
}
