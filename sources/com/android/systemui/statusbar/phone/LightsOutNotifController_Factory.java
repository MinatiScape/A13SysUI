package com.android.systemui.statusbar.phone;

import android.view.View;
import android.view.WindowManager;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStore;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LightsOutNotifController_Factory implements Factory<LightsOutNotifController> {
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<View> lightsOutNotifViewProvider;
    public final Provider<NotifLiveDataStore> notifDataStoreProvider;
    public final Provider<WindowManager> windowManagerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new LightsOutNotifController(this.lightsOutNotifViewProvider.mo144get(), this.windowManagerProvider.mo144get(), this.notifDataStoreProvider.mo144get(), this.commandQueueProvider.mo144get());
    }

    public LightsOutNotifController_Factory(Provider<View> provider, Provider<WindowManager> provider2, Provider<NotifLiveDataStore> provider3, Provider<CommandQueue> provider4) {
        this.lightsOutNotifViewProvider = provider;
        this.windowManagerProvider = provider2;
        this.notifDataStoreProvider = provider3;
        this.commandQueueProvider = provider4;
    }
}
