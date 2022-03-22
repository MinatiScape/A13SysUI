package com.android.systemui.statusbar.notification.collection;

import android.os.Handler;
import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.dump.LogBufferEulogizer;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionLogger;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotifCollection_Factory implements Factory<NotifCollection> {
    public final Provider<SystemClock> clockProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<LogBufferEulogizer> logBufferEulogizerProvider;
    public final Provider<NotifCollectionLogger> loggerProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
    public final Provider<IStatusBarService> statusBarServiceProvider;

    public static NotifCollection_Factory create(Provider<IStatusBarService> provider, Provider<SystemClock> provider2, Provider<NotifPipelineFlags> provider3, Provider<NotifCollectionLogger> provider4, Provider<Handler> provider5, Provider<LogBufferEulogizer> provider6, Provider<DumpManager> provider7) {
        return new NotifCollection_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new NotifCollection(this.statusBarServiceProvider.mo144get(), this.clockProvider.mo144get(), this.notifPipelineFlagsProvider.mo144get(), this.loggerProvider.mo144get(), this.mainHandlerProvider.mo144get(), this.logBufferEulogizerProvider.mo144get(), this.dumpManagerProvider.mo144get());
    }

    public NotifCollection_Factory(Provider<IStatusBarService> provider, Provider<SystemClock> provider2, Provider<NotifPipelineFlags> provider3, Provider<NotifCollectionLogger> provider4, Provider<Handler> provider5, Provider<LogBufferEulogizer> provider6, Provider<DumpManager> provider7) {
        this.statusBarServiceProvider = provider;
        this.clockProvider = provider2;
        this.notifPipelineFlagsProvider = provider3;
        this.loggerProvider = provider4;
        this.mainHandlerProvider = provider5;
        this.logBufferEulogizerProvider = provider6;
        this.dumpManagerProvider = provider7;
    }
}
