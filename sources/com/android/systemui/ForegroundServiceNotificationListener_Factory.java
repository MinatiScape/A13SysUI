package com.android.systemui;

import android.content.Context;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ForegroundServiceNotificationListener_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider contextProvider;
    public final Provider foregroundServiceControllerProvider;
    public final Provider notifPipelineProvider;
    public final Provider notificationEntryManagerProvider;
    public final Provider systemClockProvider;

    public /* synthetic */ ForegroundServiceNotificationListener_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.foregroundServiceControllerProvider = provider2;
        this.notificationEntryManagerProvider = provider3;
        this.notifPipelineProvider = provider4;
        this.systemClockProvider = provider5;
    }

    public static ForegroundServiceNotificationListener_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5) {
        return new ForegroundServiceNotificationListener_Factory(provider, provider2, provider3, provider4, provider5, 0);
    }

    public static ForegroundServiceNotificationListener_Factory create$1(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5) {
        return new ForegroundServiceNotificationListener_Factory(provider, provider2, provider3, provider4, provider5, 1);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                SystemClock systemClock = (SystemClock) this.systemClockProvider.mo144get();
                return new ForegroundServiceNotificationListener((Context) this.contextProvider.mo144get(), (ForegroundServiceController) this.foregroundServiceControllerProvider.mo144get(), (NotificationEntryManager) this.notificationEntryManagerProvider.mo144get(), (NotifPipeline) this.notifPipelineProvider.mo144get());
            default:
                return new NotificationWakeUpCoordinator((HeadsUpManager) this.contextProvider.mo144get(), (StatusBarStateController) this.foregroundServiceControllerProvider.mo144get(), (KeyguardBypassController) this.notificationEntryManagerProvider.mo144get(), (DozeParameters) this.notifPipelineProvider.mo144get(), (ScreenOffAnimationController) this.systemClockProvider.mo144get());
        }
    }
}
