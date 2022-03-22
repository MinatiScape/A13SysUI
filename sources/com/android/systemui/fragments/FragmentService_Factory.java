package com.android.systemui.fragments;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.fragments.FragmentService;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FragmentService_Factory implements Factory<FragmentService> {
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<FragmentService.FragmentCreator.Factory> fragmentCreatorFactoryProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new FragmentService(this.fragmentCreatorFactoryProvider.mo144get(), this.configurationControllerProvider.mo144get(), this.dumpManagerProvider.mo144get());
    }

    public FragmentService_Factory(Provider<FragmentService.FragmentCreator.Factory> provider, Provider<ConfigurationController> provider2, Provider<DumpManager> provider3) {
        this.fragmentCreatorFactoryProvider = provider;
        this.configurationControllerProvider = provider2;
        this.dumpManagerProvider = provider3;
    }
}
