package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.media.MediaBrowserFactory_Factory;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinder;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class HeadsUpCoordinator_Factory implements Factory<HeadsUpCoordinator> {
    public final Provider<DelayableExecutor> mExecutorProvider;
    public final Provider<HeadsUpManager> mHeadsUpManagerProvider;
    public final Provider<HeadsUpViewBinder> mHeadsUpViewBinderProvider;
    public final Provider<NodeController> mIncomingHeaderControllerProvider;
    public final Provider<HeadsUpCoordinatorLogger> mLoggerProvider;
    public final Provider<NotificationInterruptStateProvider> mNotificationInterruptStateProvider;
    public final Provider<NotificationRemoteInputManager> mRemoteInputManagerProvider;
    public final Provider<SystemClock> mSystemClockProvider;

    public static HeadsUpCoordinator_Factory create(MediaBrowserFactory_Factory mediaBrowserFactory_Factory, Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7) {
        return new HeadsUpCoordinator_Factory(mediaBrowserFactory_Factory, provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new HeadsUpCoordinator(this.mLoggerProvider.mo144get(), this.mSystemClockProvider.mo144get(), this.mHeadsUpManagerProvider.mo144get(), this.mHeadsUpViewBinderProvider.mo144get(), this.mNotificationInterruptStateProvider.mo144get(), this.mRemoteInputManagerProvider.mo144get(), this.mIncomingHeaderControllerProvider.mo144get(), this.mExecutorProvider.mo144get());
    }

    public HeadsUpCoordinator_Factory(MediaBrowserFactory_Factory mediaBrowserFactory_Factory, Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7) {
        this.mLoggerProvider = mediaBrowserFactory_Factory;
        this.mSystemClockProvider = provider;
        this.mHeadsUpManagerProvider = provider2;
        this.mHeadsUpViewBinderProvider = provider3;
        this.mNotificationInterruptStateProvider = provider4;
        this.mRemoteInputManagerProvider = provider5;
        this.mIncomingHeaderControllerProvider = provider6;
        this.mExecutorProvider = provider7;
    }
}
