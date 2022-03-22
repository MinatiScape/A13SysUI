package com.android.systemui.qs.external;

import android.content.Intent;
import android.os.UserHandle;
import com.android.systemui.qs.external.TileLifecycleManager;
import java.util.Objects;
/* loaded from: classes.dex */
public final class TileLifecycleManager_Factory_Impl implements TileLifecycleManager.Factory {
    public final C0013TileLifecycleManager_Factory delegateFactory;

    @Override // com.android.systemui.qs.external.TileLifecycleManager.Factory
    public final TileLifecycleManager create(Intent intent, UserHandle userHandle) {
        C0013TileLifecycleManager_Factory tileLifecycleManager_Factory = this.delegateFactory;
        Objects.requireNonNull(tileLifecycleManager_Factory);
        return new TileLifecycleManager(tileLifecycleManager_Factory.handlerProvider.mo144get(), tileLifecycleManager_Factory.contextProvider.mo144get(), tileLifecycleManager_Factory.serviceProvider.mo144get(), tileLifecycleManager_Factory.packageManagerAdapterProvider.mo144get(), tileLifecycleManager_Factory.broadcastDispatcherProvider.mo144get(), intent, userHandle);
    }

    public TileLifecycleManager_Factory_Impl(C0013TileLifecycleManager_Factory tileLifecycleManager_Factory) {
        this.delegateFactory = tileLifecycleManager_Factory;
    }
}
