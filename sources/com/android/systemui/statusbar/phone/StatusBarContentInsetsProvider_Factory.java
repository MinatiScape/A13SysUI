package com.android.systemui.statusbar.phone;

import android.content.Context;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.collection.render.NotifShadeEventSource;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarContentInsetsProvider_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider configurationControllerProvider;
    public final Provider contextProvider;
    public final Provider dumpManagerProvider;

    public /* synthetic */ StatusBarContentInsetsProvider_Factory(Provider provider, Provider provider2, Provider provider3, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.configurationControllerProvider = provider2;
        this.dumpManagerProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        NotifShadeEventSource notifShadeEventSource;
        switch (this.$r8$classId) {
            case 0:
                return new StatusBarContentInsetsProvider((Context) this.contextProvider.mo144get(), (ConfigurationController) this.configurationControllerProvider.mo144get(), (DumpManager) this.dumpManagerProvider.mo144get());
            default:
                Lazy lazy = DoubleCheck.lazy(this.configurationControllerProvider);
                Lazy lazy2 = DoubleCheck.lazy(this.dumpManagerProvider);
                if (((NotifPipelineFlags) this.contextProvider.mo144get()).isNewPipelineEnabled()) {
                    notifShadeEventSource = (NotifShadeEventSource) lazy.get();
                } else {
                    notifShadeEventSource = (NotifShadeEventSource) lazy2.get();
                }
                Objects.requireNonNull(notifShadeEventSource, "Cannot return null from a non-@Nullable @Provides method");
                return notifShadeEventSource;
        }
    }
}
