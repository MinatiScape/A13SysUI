package com.google.android.systemui.columbus.actions;

import android.content.Context;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.google.android.systemui.columbus.gates.SilenceAlertsDisabled;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SilenceCall_Factory implements Factory<SilenceCall> {
    public final Provider<Context> contextProvider;
    public final Provider<SilenceAlertsDisabled> silenceAlertsDisabledProvider;
    public final Provider<TelecomManager> telecomManagerProvider;
    public final Provider<TelephonyListenerManager> telephonyListenerManagerProvider;
    public final Provider<TelephonyManager> telephonyManagerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new SilenceCall(this.contextProvider.mo144get(), this.silenceAlertsDisabledProvider.mo144get(), DoubleCheck.lazy(this.telecomManagerProvider), DoubleCheck.lazy(this.telephonyManagerProvider), DoubleCheck.lazy(this.telephonyListenerManagerProvider));
    }

    public SilenceCall_Factory(Provider<Context> provider, Provider<SilenceAlertsDisabled> provider2, Provider<TelecomManager> provider3, Provider<TelephonyManager> provider4, Provider<TelephonyListenerManager> provider5) {
        this.contextProvider = provider;
        this.silenceAlertsDisabledProvider = provider2;
        this.telecomManagerProvider = provider3;
        this.telephonyManagerProvider = provider4;
        this.telephonyListenerManagerProvider = provider5;
    }
}
