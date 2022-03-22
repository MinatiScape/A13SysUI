package com.android.systemui.telephony;

import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class TelephonyListenerManager {
    public final Executor mExecutor;
    public boolean mListening = false;
    public final TelephonyCallback mTelephonyCallback;
    public final TelephonyManager mTelephonyManager;

    public final void addCallStateListener(TelephonyCallback.CallStateListener callStateListener) {
        TelephonyCallback telephonyCallback = this.mTelephonyCallback;
        Objects.requireNonNull(telephonyCallback);
        telephonyCallback.mCallStateListeners.add(callStateListener);
        updateListening();
    }

    public final void removeCallStateListener(TelephonyCallback.CallStateListener callStateListener) {
        TelephonyCallback telephonyCallback = this.mTelephonyCallback;
        Objects.requireNonNull(telephonyCallback);
        telephonyCallback.mCallStateListeners.remove(callStateListener);
        updateListening();
    }

    public final void updateListening() {
        if (!this.mListening && this.mTelephonyCallback.hasAnyListeners()) {
            this.mListening = true;
            this.mTelephonyManager.registerTelephonyCallback(this.mExecutor, this.mTelephonyCallback);
        } else if (this.mListening && !this.mTelephonyCallback.hasAnyListeners()) {
            this.mTelephonyManager.unregisterTelephonyCallback(this.mTelephonyCallback);
            this.mListening = false;
        }
    }

    public TelephonyListenerManager(TelephonyManager telephonyManager, Executor executor, TelephonyCallback telephonyCallback) {
        this.mTelephonyManager = telephonyManager;
        this.mExecutor = executor;
        this.mTelephonyCallback = telephonyCallback;
    }
}
