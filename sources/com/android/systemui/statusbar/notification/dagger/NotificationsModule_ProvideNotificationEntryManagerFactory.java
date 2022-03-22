package com.android.systemui.statusbar.notification.dagger;

import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.ForegroundServiceDismissalFeatureController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationEntryManagerLogger;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStoreImpl;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinder;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.util.leak.LeakDetector;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationsModule_ProvideNotificationEntryManagerFactory implements Factory<NotificationEntryManager> {
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<ForegroundServiceDismissalFeatureController> fgsFeatureControllerProvider;
    public final Provider<NotificationGroupManagerLegacy> groupManagerProvider;
    public final Provider<LeakDetector> leakDetectorProvider;
    public final Provider<NotificationEntryManagerLogger> loggerProvider;
    public final Provider<NotifLiveDataStoreImpl> notifLiveDataStoreProvider;
    public final Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
    public final Provider<NotificationRemoteInputManager> notificationRemoteInputManagerLazyProvider;
    public final Provider<NotificationRowBinder> notificationRowBinderLazyProvider;
    public final Provider<IStatusBarService> statusBarServiceProvider;

    public static NotificationsModule_ProvideNotificationEntryManagerFactory create(Provider<NotificationEntryManagerLogger> provider, Provider<NotificationGroupManagerLegacy> provider2, Provider<NotifPipelineFlags> provider3, Provider<NotificationRowBinder> provider4, Provider<NotificationRemoteInputManager> provider5, Provider<LeakDetector> provider6, Provider<ForegroundServiceDismissalFeatureController> provider7, Provider<IStatusBarService> provider8, Provider<NotifLiveDataStoreImpl> provider9, Provider<DumpManager> provider10) {
        return new NotificationsModule_ProvideNotificationEntryManagerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        NotificationEntryManagerLogger notificationEntryManagerLogger = this.loggerProvider.mo144get();
        NotificationGroupManagerLegacy notificationGroupManagerLegacy = this.groupManagerProvider.mo144get();
        NotifPipelineFlags notifPipelineFlags = this.notifPipelineFlagsProvider.mo144get();
        Lazy lazy = DoubleCheck.lazy(this.notificationRowBinderLazyProvider);
        Lazy lazy2 = DoubleCheck.lazy(this.notificationRemoteInputManagerLazyProvider);
        LeakDetector leakDetector = this.leakDetectorProvider.mo144get();
        this.fgsFeatureControllerProvider.mo144get();
        return new NotificationEntryManager(notificationEntryManagerLogger, notificationGroupManagerLegacy, notifPipelineFlags, lazy, lazy2, leakDetector, this.statusBarServiceProvider.mo144get(), this.notifLiveDataStoreProvider.mo144get(), this.dumpManagerProvider.mo144get());
    }

    public NotificationsModule_ProvideNotificationEntryManagerFactory(Provider<NotificationEntryManagerLogger> provider, Provider<NotificationGroupManagerLegacy> provider2, Provider<NotifPipelineFlags> provider3, Provider<NotificationRowBinder> provider4, Provider<NotificationRemoteInputManager> provider5, Provider<LeakDetector> provider6, Provider<ForegroundServiceDismissalFeatureController> provider7, Provider<IStatusBarService> provider8, Provider<NotifLiveDataStoreImpl> provider9, Provider<DumpManager> provider10) {
        this.loggerProvider = provider;
        this.groupManagerProvider = provider2;
        this.notifPipelineFlagsProvider = provider3;
        this.notificationRowBinderLazyProvider = provider4;
        this.notificationRemoteInputManagerLazyProvider = provider5;
        this.leakDetectorProvider = provider6;
        this.fgsFeatureControllerProvider = provider7;
        this.statusBarServiceProvider = provider8;
        this.notifLiveDataStoreProvider = provider9;
        this.dumpManagerProvider = provider10;
    }
}
