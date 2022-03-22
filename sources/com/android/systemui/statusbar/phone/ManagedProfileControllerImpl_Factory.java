package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.tv.notifications.TvNotificationHandler;
import com.google.android.systemui.columbus.sensors.config.GestureConfiguration;
import com.google.android.systemui.columbus.sensors.config.SensorConfiguration;
import com.google.android.systemui.lowlightclock.LowLightClockControllerImpl;
import dagger.internal.Factory;
import java.util.List;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ManagedProfileControllerImpl_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider broadcastDispatcherProvider;
    public final Provider contextProvider;

    public /* synthetic */ ManagedProfileControllerImpl_Factory(Provider provider, Provider provider2, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.broadcastDispatcherProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new ManagedProfileControllerImpl((Context) this.contextProvider.mo144get(), (BroadcastDispatcher) this.broadcastDispatcherProvider.mo144get());
            case 1:
                return new TvNotificationHandler((Context) this.contextProvider.mo144get(), (NotificationListener) this.broadcastDispatcherProvider.mo144get());
            case 2:
                return new GestureConfiguration((List) this.contextProvider.mo144get(), (SensorConfiguration) this.broadcastDispatcherProvider.mo144get());
            default:
                return new LowLightClockControllerImpl((Resources) this.contextProvider.mo144get(), (LayoutInflater) this.broadcastDispatcherProvider.mo144get());
        }
    }
}
