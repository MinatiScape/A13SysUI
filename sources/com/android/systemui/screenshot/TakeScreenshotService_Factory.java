package com.android.systemui.screenshot;

import android.app.NotificationManager;
import android.content.Context;
import android.os.UserManager;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.AnimatedImageNotificationManager;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManager;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.BatteryStateNotifier;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TakeScreenshotService_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider notificationsControllerProvider;
    public final Provider screenshotControllerProvider;
    public final Provider uiEventLoggerProvider;
    public final Provider userManagerProvider;

    public /* synthetic */ TakeScreenshotService_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, int i) {
        this.$r8$classId = i;
        this.screenshotControllerProvider = provider;
        this.userManagerProvider = provider2;
        this.uiEventLoggerProvider = provider3;
        this.notificationsControllerProvider = provider4;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new TakeScreenshotService((ScreenshotController) this.screenshotControllerProvider.mo144get(), (UserManager) this.userManagerProvider.mo144get(), (UiEventLogger) this.uiEventLoggerProvider.mo144get(), (ScreenshotNotificationsController) this.notificationsControllerProvider.mo144get());
            case 1:
                return new AnimatedImageNotificationManager((CommonNotifCollection) this.screenshotControllerProvider.mo144get(), (BindEventManager) this.userManagerProvider.mo144get(), (HeadsUpManager) this.uiEventLoggerProvider.mo144get(), (StatusBarStateController) this.notificationsControllerProvider.mo144get());
            default:
                return new BatteryStateNotifier((BatteryController) this.screenshotControllerProvider.mo144get(), (NotificationManager) this.userManagerProvider.mo144get(), (DelayableExecutor) this.uiEventLoggerProvider.mo144get(), (Context) this.notificationsControllerProvider.mo144get());
        }
    }
}
