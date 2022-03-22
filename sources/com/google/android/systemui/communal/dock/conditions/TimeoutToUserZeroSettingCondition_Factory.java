package com.google.android.systemui.communal.dock.conditions;

import android.os.Handler;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TimeoutToUserZeroSettingCondition_Factory implements Factory<TimeoutToUserZeroSettingCondition> {
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<SecureSettings> secureSettingsProvider;
    public final Provider<Integer> timeoutDurationSettingProvider;
    public final Provider<Integer> userIdProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new TimeoutToUserZeroSettingCondition(this.mainHandlerProvider.mo144get(), this.secureSettingsProvider.mo144get(), this.timeoutDurationSettingProvider, this.userIdProvider);
    }

    public TimeoutToUserZeroSettingCondition_Factory(Provider<Handler> provider, Provider<SecureSettings> provider2, Provider<Integer> provider3, Provider<Integer> provider4) {
        this.mainHandlerProvider = provider;
        this.secureSettingsProvider = provider2;
        this.timeoutDurationSettingProvider = provider3;
        this.userIdProvider = provider4;
    }
}
