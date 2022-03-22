package com.android.systemui.doze;

import android.app.AlarmManager;
import android.os.Handler;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozePauser_Factory implements Factory<DozePauser> {
    public final Provider<AlarmManager> alarmManagerProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<AlwaysOnDisplayPolicy> policyProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new DozePauser(this.handlerProvider.mo144get(), this.alarmManagerProvider.mo144get(), this.policyProvider.mo144get());
    }

    public DozePauser_Factory(Provider<Handler> provider, Provider<AlarmManager> provider2, Provider<AlwaysOnDisplayPolicy> provider3) {
        this.handlerProvider = provider;
        this.alarmManagerProvider = provider2;
        this.policyProvider = provider3;
    }
}
