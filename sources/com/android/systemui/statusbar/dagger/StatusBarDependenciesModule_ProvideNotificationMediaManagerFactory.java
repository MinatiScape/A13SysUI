package com.android.systemui.statusbar.dagger;

import android.content.Context;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.statusbar.MediaArtworkProcessor;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarDependenciesModule_ProvideNotificationMediaManagerFactory implements Factory<NotificationMediaManager> {
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<KeyguardBypassController> keyguardBypassControllerProvider;
    public final Provider<DelayableExecutor> mainExecutorProvider;
    public final Provider<MediaArtworkProcessor> mediaArtworkProcessorProvider;
    public final Provider<MediaDataManager> mediaDataManagerProvider;
    public final Provider<NotifCollection> notifCollectionProvider;
    public final Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
    public final Provider<NotifPipeline> notifPipelineProvider;
    public final Provider<NotificationEntryManager> notificationEntryManagerProvider;
    public final Provider<NotificationShadeWindowController> notificationShadeWindowControllerProvider;
    public final Provider<Optional<StatusBar>> statusBarOptionalLazyProvider;
    public final Provider<NotificationVisibilityProvider> visibilityProvider;

    public static StatusBarDependenciesModule_ProvideNotificationMediaManagerFactory create(Provider<Context> provider, Provider<Optional<StatusBar>> provider2, Provider<NotificationShadeWindowController> provider3, Provider<NotificationVisibilityProvider> provider4, Provider<NotificationEntryManager> provider5, Provider<MediaArtworkProcessor> provider6, Provider<KeyguardBypassController> provider7, Provider<NotifPipeline> provider8, Provider<NotifCollection> provider9, Provider<NotifPipelineFlags> provider10, Provider<DelayableExecutor> provider11, Provider<MediaDataManager> provider12, Provider<DumpManager> provider13) {
        return new StatusBarDependenciesModule_ProvideNotificationMediaManagerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new NotificationMediaManager(this.contextProvider.mo144get(), DoubleCheck.lazy(this.statusBarOptionalLazyProvider), DoubleCheck.lazy(this.notificationShadeWindowControllerProvider), this.visibilityProvider.mo144get(), this.notificationEntryManagerProvider.mo144get(), this.mediaArtworkProcessorProvider.mo144get(), this.keyguardBypassControllerProvider.mo144get(), this.notifPipelineProvider.mo144get(), this.notifCollectionProvider.mo144get(), this.notifPipelineFlagsProvider.mo144get(), this.mainExecutorProvider.mo144get(), this.mediaDataManagerProvider.mo144get(), this.dumpManagerProvider.mo144get());
    }

    public StatusBarDependenciesModule_ProvideNotificationMediaManagerFactory(Provider<Context> provider, Provider<Optional<StatusBar>> provider2, Provider<NotificationShadeWindowController> provider3, Provider<NotificationVisibilityProvider> provider4, Provider<NotificationEntryManager> provider5, Provider<MediaArtworkProcessor> provider6, Provider<KeyguardBypassController> provider7, Provider<NotifPipeline> provider8, Provider<NotifCollection> provider9, Provider<NotifPipelineFlags> provider10, Provider<DelayableExecutor> provider11, Provider<MediaDataManager> provider12, Provider<DumpManager> provider13) {
        this.contextProvider = provider;
        this.statusBarOptionalLazyProvider = provider2;
        this.notificationShadeWindowControllerProvider = provider3;
        this.visibilityProvider = provider4;
        this.notificationEntryManagerProvider = provider5;
        this.mediaArtworkProcessorProvider = provider6;
        this.keyguardBypassControllerProvider = provider7;
        this.notifPipelineProvider = provider8;
        this.notifCollectionProvider = provider9;
        this.notifPipelineFlagsProvider = provider10;
        this.mainExecutorProvider = provider11;
        this.mediaDataManagerProvider = provider12;
        this.dumpManagerProvider = provider13;
    }
}
