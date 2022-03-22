package com.android.systemui.qs.external;

import com.android.systemui.qs.external.TileServiceRequestController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TileServiceRequestController_Builder_Factory implements Factory<TileServiceRequestController.Builder> {
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<CommandRegistry> commandRegistryProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new TileServiceRequestController.Builder(this.commandQueueProvider.mo144get(), this.commandRegistryProvider.mo144get());
    }

    public TileServiceRequestController_Builder_Factory(Provider<CommandQueue> provider, Provider<CommandRegistry> provider2) {
        this.commandQueueProvider = provider;
        this.commandRegistryProvider = provider2;
    }
}
