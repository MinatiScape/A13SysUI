package com.android.systemui.media;

import android.media.MediaRouter2Manager;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.muteawait.MediaMuteAwaitConnectionManagerFactory;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MediaDeviceManager_Factory implements Factory<MediaDeviceManager> {
    public final Provider<Executor> bgExecutorProvider;
    public final Provider<MediaControllerFactory> controllerFactoryProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<Executor> fgExecutorProvider;
    public final Provider<LocalMediaManagerFactory> localMediaManagerFactoryProvider;
    public final Provider<MediaRouter2Manager> mr2managerProvider;
    public final Provider<MediaMuteAwaitConnectionManagerFactory> muteAwaitConnectionManagerFactoryProvider;

    public static MediaDeviceManager_Factory create(Provider<MediaControllerFactory> provider, Provider<LocalMediaManagerFactory> provider2, Provider<MediaRouter2Manager> provider3, Provider<MediaMuteAwaitConnectionManagerFactory> provider4, Provider<Executor> provider5, Provider<Executor> provider6, Provider<DumpManager> provider7) {
        return new MediaDeviceManager_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new MediaDeviceManager(this.controllerFactoryProvider.mo144get(), this.localMediaManagerFactoryProvider.mo144get(), this.mr2managerProvider.mo144get(), this.muteAwaitConnectionManagerFactoryProvider.mo144get(), this.fgExecutorProvider.mo144get(), this.bgExecutorProvider.mo144get(), this.dumpManagerProvider.mo144get());
    }

    public MediaDeviceManager_Factory(Provider<MediaControllerFactory> provider, Provider<LocalMediaManagerFactory> provider2, Provider<MediaRouter2Manager> provider3, Provider<MediaMuteAwaitConnectionManagerFactory> provider4, Provider<Executor> provider5, Provider<Executor> provider6, Provider<DumpManager> provider7) {
        this.controllerFactoryProvider = provider;
        this.localMediaManagerFactoryProvider = provider2;
        this.mr2managerProvider = provider3;
        this.muteAwaitConnectionManagerFactoryProvider = provider4;
        this.fgExecutorProvider = provider5;
        this.bgExecutorProvider = provider6;
        this.dumpManagerProvider = provider7;
    }
}
