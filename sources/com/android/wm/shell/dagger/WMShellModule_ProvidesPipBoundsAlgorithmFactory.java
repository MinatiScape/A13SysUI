package com.android.wm.shell.dagger;

import android.content.Context;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipSnapAlgorithm;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WMShellModule_ProvidesPipBoundsAlgorithmFactory implements Factory<PipBoundsAlgorithm> {
    public final Provider<Context> contextProvider;
    public final Provider<PipBoundsState> pipBoundsStateProvider;
    public final Provider<PipSnapAlgorithm> pipSnapAlgorithmProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new PipBoundsAlgorithm(this.contextProvider.mo144get(), this.pipBoundsStateProvider.mo144get(), this.pipSnapAlgorithmProvider.mo144get());
    }

    public WMShellModule_ProvidesPipBoundsAlgorithmFactory(Provider<Context> provider, Provider<PipBoundsState> provider2, Provider<PipSnapAlgorithm> provider3) {
        this.contextProvider = provider;
        this.pipBoundsStateProvider = provider2;
        this.pipSnapAlgorithmProvider = provider3;
    }
}
