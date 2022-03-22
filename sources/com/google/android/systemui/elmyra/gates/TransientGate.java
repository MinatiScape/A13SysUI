package com.google.android.systemui.elmyra.gates;

import android.content.Context;
import android.os.Handler;
/* loaded from: classes.dex */
public abstract class TransientGate extends Gate {
    public final long mBlockDuration;
    public boolean mIsBlocking;
    public final AnonymousClass1 mResetGate = new Runnable() { // from class: com.google.android.systemui.elmyra.gates.TransientGate.1
        @Override // java.lang.Runnable
        public final void run() {
            TransientGate transientGate = TransientGate.this;
            transientGate.mIsBlocking = false;
            transientGate.notifyListener();
        }
    };
    public final Handler mResetGateHandler;

    public final void block() {
        this.mIsBlocking = true;
        notifyListener();
        this.mResetGateHandler.removeCallbacks(this.mResetGate);
        this.mResetGateHandler.postDelayed(this.mResetGate, this.mBlockDuration);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.google.android.systemui.elmyra.gates.TransientGate$1] */
    public TransientGate(Context context, long j) {
        super(context);
        this.mBlockDuration = j;
        this.mResetGateHandler = new Handler(context.getMainLooper());
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final boolean isBlocked() {
        return this.mIsBlocking;
    }
}
