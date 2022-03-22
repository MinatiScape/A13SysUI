package com.android.systemui.toast;

import android.app.AlarmManager;
import android.view.LayoutInflater;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.statusbar.policy.NextAlarmControllerImpl;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ToastFactory_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider dumpManagerProvider;
    public final Provider layoutInflaterProvider;
    public final Provider pluginManagerProvider;

    public /* synthetic */ ToastFactory_Factory(Provider provider, Provider provider2, Provider provider3, int i) {
        this.$r8$classId = i;
        this.layoutInflaterProvider = provider;
        this.pluginManagerProvider = provider2;
        this.dumpManagerProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new ToastFactory((LayoutInflater) this.layoutInflaterProvider.mo144get(), (PluginManager) this.pluginManagerProvider.mo144get(), (DumpManager) this.dumpManagerProvider.mo144get());
            default:
                return new NextAlarmControllerImpl((AlarmManager) this.layoutInflaterProvider.mo144get(), (BroadcastDispatcher) this.pluginManagerProvider.mo144get(), (DumpManager) this.dumpManagerProvider.mo144get());
        }
    }
}
