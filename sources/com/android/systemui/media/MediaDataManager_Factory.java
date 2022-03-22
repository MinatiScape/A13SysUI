package com.android.systemui.media;

import android.content.Context;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaDataCombineLatest_Factory;
import com.android.systemui.media.SmartspaceMediaDataProvider_Factory;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MediaDataManager_Factory implements Factory<MediaDataManager> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<Executor> backgroundExecutorProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<SystemClock> clockProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<DelayableExecutor> foregroundExecutorProvider;
    public final Provider<MediaControllerFactory> mediaControllerFactoryProvider;
    public final Provider<MediaDataCombineLatest> mediaDataCombineLatestProvider;
    public final Provider<MediaDataFilter> mediaDataFilterProvider;
    public final Provider<MediaDeviceManager> mediaDeviceManagerProvider;
    public final Provider<MediaFlags> mediaFlagsProvider;
    public final Provider<MediaResumeListener> mediaResumeListenerProvider;
    public final Provider<MediaSessionBasedFilter> mediaSessionBasedFilterProvider;
    public final Provider<MediaTimeoutListener> mediaTimeoutListenerProvider;
    public final Provider<SmartspaceMediaDataProvider> smartspaceMediaDataProvider;
    public final Provider<TunerService> tunerServiceProvider;

    public MediaDataManager_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, MediaDataFilter_Factory mediaDataFilter_Factory, Provider provider11, Provider provider12, Provider provider13, Provider provider14) {
        MediaDataCombineLatest_Factory mediaDataCombineLatest_Factory = MediaDataCombineLatest_Factory.InstanceHolder.INSTANCE;
        SmartspaceMediaDataProvider_Factory smartspaceMediaDataProvider_Factory = SmartspaceMediaDataProvider_Factory.InstanceHolder.INSTANCE;
        this.contextProvider = provider;
        this.backgroundExecutorProvider = provider2;
        this.foregroundExecutorProvider = provider3;
        this.mediaControllerFactoryProvider = provider4;
        this.dumpManagerProvider = provider5;
        this.broadcastDispatcherProvider = provider6;
        this.mediaTimeoutListenerProvider = provider7;
        this.mediaResumeListenerProvider = provider8;
        this.mediaSessionBasedFilterProvider = provider9;
        this.mediaDeviceManagerProvider = provider10;
        this.mediaDataCombineLatestProvider = mediaDataCombineLatest_Factory;
        this.mediaDataFilterProvider = mediaDataFilter_Factory;
        this.activityStarterProvider = provider11;
        this.smartspaceMediaDataProvider = smartspaceMediaDataProvider_Factory;
        this.clockProvider = provider12;
        this.tunerServiceProvider = provider13;
        this.mediaFlagsProvider = provider14;
    }

    public static MediaDataManager_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, MediaDataFilter_Factory mediaDataFilter_Factory, Provider provider11, Provider provider12, Provider provider13, Provider provider14) {
        return new MediaDataManager_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, mediaDataFilter_Factory, provider11, provider12, provider13, provider14);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new MediaDataManager(this.contextProvider.mo144get(), this.backgroundExecutorProvider.mo144get(), this.foregroundExecutorProvider.mo144get(), this.mediaControllerFactoryProvider.mo144get(), this.dumpManagerProvider.mo144get(), this.broadcastDispatcherProvider.mo144get(), this.mediaTimeoutListenerProvider.mo144get(), this.mediaResumeListenerProvider.mo144get(), this.mediaSessionBasedFilterProvider.mo144get(), this.mediaDeviceManagerProvider.mo144get(), this.mediaDataCombineLatestProvider.mo144get(), this.mediaDataFilterProvider.mo144get(), this.activityStarterProvider.mo144get(), this.smartspaceMediaDataProvider.mo144get(), this.clockProvider.mo144get(), this.tunerServiceProvider.mo144get(), this.mediaFlagsProvider.mo144get());
    }
}
