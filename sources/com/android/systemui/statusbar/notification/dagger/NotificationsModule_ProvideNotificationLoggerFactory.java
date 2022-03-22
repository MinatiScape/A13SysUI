package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStore;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.logging.NotificationPanelLogger;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationsModule_ProvideNotificationLoggerFactory implements Factory<NotificationLogger> {
    public final Provider<NotificationEntryManager> entryManagerProvider;
    public final Provider<NotificationLogger.ExpansionStateLogger> expansionStateLoggerProvider;
    public final Provider<NotifLiveDataStore> notifLiveDataStoreProvider;
    public final Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
    public final Provider<NotifPipeline> notifPipelineProvider;
    public final Provider<NotificationListener> notificationListenerProvider;
    public final Provider<NotificationPanelLogger> notificationPanelLoggerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<Executor> uiBgExecutorProvider;
    public final Provider<NotificationVisibilityProvider> visibilityProvider;

    public static NotificationsModule_ProvideNotificationLoggerFactory create(Provider<NotificationListener> provider, Provider<Executor> provider2, Provider<NotifPipelineFlags> provider3, Provider<NotifLiveDataStore> provider4, Provider<NotificationVisibilityProvider> provider5, Provider<NotificationEntryManager> provider6, Provider<NotifPipeline> provider7, Provider<StatusBarStateController> provider8, Provider<NotificationLogger.ExpansionStateLogger> provider9, Provider<NotificationPanelLogger> provider10) {
        return new NotificationsModule_ProvideNotificationLoggerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new NotificationLogger(this.notificationListenerProvider.mo144get(), this.uiBgExecutorProvider.mo144get(), this.notifPipelineFlagsProvider.mo144get(), this.notifLiveDataStoreProvider.mo144get(), this.visibilityProvider.mo144get(), this.entryManagerProvider.mo144get(), this.notifPipelineProvider.mo144get(), this.statusBarStateControllerProvider.mo144get(), this.expansionStateLoggerProvider.mo144get(), this.notificationPanelLoggerProvider.mo144get());
    }

    public NotificationsModule_ProvideNotificationLoggerFactory(Provider<NotificationListener> provider, Provider<Executor> provider2, Provider<NotifPipelineFlags> provider3, Provider<NotifLiveDataStore> provider4, Provider<NotificationVisibilityProvider> provider5, Provider<NotificationEntryManager> provider6, Provider<NotifPipeline> provider7, Provider<StatusBarStateController> provider8, Provider<NotificationLogger.ExpansionStateLogger> provider9, Provider<NotificationPanelLogger> provider10) {
        this.notificationListenerProvider = provider;
        this.uiBgExecutorProvider = provider2;
        this.notifPipelineFlagsProvider = provider3;
        this.notifLiveDataStoreProvider = provider4;
        this.visibilityProvider = provider5;
        this.entryManagerProvider = provider6;
        this.notifPipelineProvider = provider7;
        this.statusBarStateControllerProvider = provider8;
        this.expansionStateLoggerProvider = provider9;
        this.notificationPanelLoggerProvider = provider10;
    }
}
