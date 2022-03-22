package com.google.android.systemui.communal.dock.conditions;

import android.database.ContentObserver;
import android.os.Handler;
import com.android.systemui.util.condition.Condition;
import com.android.systemui.util.settings.SecureSettings;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TimeoutToUserZeroSettingCondition extends Condition {
    public final SecureSettings mSecureSettings;
    public final AnonymousClass1 mSettingContentObserver;
    public final Provider<Integer> mTimeoutDurationSettingProvider;
    public final Provider<Integer> mUserIdProvider;

    /* renamed from: com.google.android.systemui.communal.dock.conditions.TimeoutToUserZeroSettingCondition$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends ContentObserver {
        public AnonymousClass1(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public final void onChange(boolean z) {
            boolean z2;
            TimeoutToUserZeroSettingCondition timeoutToUserZeroSettingCondition = TimeoutToUserZeroSettingCondition.this;
            if (timeoutToUserZeroSettingCondition.mTimeoutDurationSettingProvider.mo144get().intValue() > 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            timeoutToUserZeroSettingCondition.updateCondition(z2);
        }
    }

    @Override // com.android.systemui.util.condition.Condition
    public final void start() {
        this.mSettingContentObserver.onChange(true);
        this.mSecureSettings.registerContentObserverForUser("timeout_to_user_zero", this.mSettingContentObserver, this.mUserIdProvider.mo144get().intValue());
    }

    @Override // com.android.systemui.util.condition.Condition
    public final void stop() {
        this.mSecureSettings.unregisterContentObserver(this.mSettingContentObserver);
    }

    public TimeoutToUserZeroSettingCondition(Handler handler, SecureSettings secureSettings, Provider<Integer> provider, Provider<Integer> provider2) {
        this.mSecureSettings = secureSettings;
        this.mTimeoutDurationSettingProvider = provider;
        this.mUserIdProvider = provider2;
        this.mSettingContentObserver = new AnonymousClass1(handler);
    }
}
