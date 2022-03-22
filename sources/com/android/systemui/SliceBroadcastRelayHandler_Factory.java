package com.android.systemui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.util.NotificationMessagingUtil;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.DependencyProvider;
import com.android.systemui.qs.QSPanel;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SliceBroadcastRelayHandler_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Object broadcastDispatcherProvider;
    public final Provider contextProvider;

    public /* synthetic */ SliceBroadcastRelayHandler_Factory(Provider provider, Provider provider2, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.broadcastDispatcherProvider = provider2;
    }

    public SliceBroadcastRelayHandler_Factory(DependencyProvider dependencyProvider, Provider provider) {
        this.$r8$classId = 2;
        this.broadcastDispatcherProvider = dependencyProvider;
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new SliceBroadcastRelayHandler((Context) this.contextProvider.mo144get(), (BroadcastDispatcher) ((Provider) this.broadcastDispatcherProvider).mo144get());
            case 1:
                View inflate = ((LayoutInflater) this.contextProvider.mo144get()).inflate(2131624441, (ViewGroup) ((QSPanel) ((Provider) this.broadcastDispatcherProvider).mo144get()), false);
                Objects.requireNonNull(inflate, "Cannot return null from a non-@Nullable @Provides method");
                return inflate;
            default:
                Objects.requireNonNull((DependencyProvider) this.broadcastDispatcherProvider);
                return new NotificationMessagingUtil((Context) this.contextProvider.mo144get());
        }
    }
}
