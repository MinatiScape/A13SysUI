package com.google.android.systemui.columbus.actions;

import android.app.IActivityManager;
import android.content.Context;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.DynamicPrivacyController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.google.android.systemui.columbus.gates.SilenceAlertsDisabled;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DismissTimer_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider activityManagerServiceProvider;
    public final Provider contextProvider;
    public final Provider silenceAlertsDisabledProvider;

    public /* synthetic */ DismissTimer_Factory(Provider provider, Provider provider2, Provider provider3, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.silenceAlertsDisabledProvider = provider2;
        this.activityManagerServiceProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new DismissTimer((Context) this.contextProvider.mo144get(), (SilenceAlertsDisabled) this.silenceAlertsDisabledProvider.mo144get(), (IActivityManager) this.activityManagerServiceProvider.mo144get());
            default:
                return new DynamicPrivacyController((NotificationLockscreenUserManager) this.contextProvider.mo144get(), (KeyguardStateController) this.silenceAlertsDisabledProvider.mo144get(), (StatusBarStateController) this.activityManagerServiceProvider.mo144get());
        }
    }
}
