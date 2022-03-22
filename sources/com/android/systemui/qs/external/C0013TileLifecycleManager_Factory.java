package com.android.systemui.qs.external;

import android.content.Context;
import android.os.Handler;
import android.service.quicksettings.IQSService;
import com.android.systemui.broadcast.BroadcastDispatcher;
import javax.inject.Provider;
/* renamed from: com.android.systemui.qs.external.TileLifecycleManager_Factory  reason: case insensitive filesystem */
/* loaded from: classes.dex */
public final class C0013TileLifecycleManager_Factory {
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Context> contextProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<PackageManagerAdapter> packageManagerAdapterProvider;
    public final Provider<IQSService> serviceProvider;

    public C0013TileLifecycleManager_Factory(Provider provider, Provider provider2, Provider provider3, PackageManagerAdapter_Factory packageManagerAdapter_Factory, Provider provider4) {
        this.handlerProvider = provider;
        this.contextProvider = provider2;
        this.serviceProvider = provider3;
        this.packageManagerAdapterProvider = packageManagerAdapter_Factory;
        this.broadcastDispatcherProvider = provider4;
    }
}
