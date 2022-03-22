package com.android.systemui.unfold;

import com.android.systemui.unfold.SysUIUnfoldComponent;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import com.android.systemui.unfold.util.ScopedUnfoldTransitionProgressProvider;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SysUIUnfoldModule_ProvideSysUIUnfoldComponentFactory implements Factory<Optional<SysUIUnfoldComponent>> {
    public final Provider<SysUIUnfoldComponent.Factory> factoryProvider;
    public final SysUIUnfoldModule module;
    public final Provider<Optional<UnfoldTransitionProgressProvider>> providerProvider;
    public final Provider<Optional<NaturalRotationUnfoldProgressProvider>> rotationProvider;
    public final Provider<Optional<ScopedUnfoldTransitionProgressProvider>> scopedProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        SysUIUnfoldModule sysUIUnfoldModule = this.module;
        SysUIUnfoldComponent.Factory factory = this.factoryProvider.mo144get();
        Objects.requireNonNull(sysUIUnfoldModule);
        UnfoldTransitionProgressProvider orElse = this.providerProvider.mo144get().orElse(null);
        NaturalRotationUnfoldProgressProvider orElse2 = this.rotationProvider.mo144get().orElse(null);
        ScopedUnfoldTransitionProgressProvider orElse3 = this.scopedProvider.mo144get().orElse(null);
        if (orElse == null || orElse2 == null || orElse3 == null) {
            return Optional.empty();
        }
        return Optional.of(factory.create(orElse, orElse2, orElse3));
    }

    public SysUIUnfoldModule_ProvideSysUIUnfoldComponentFactory(SysUIUnfoldModule sysUIUnfoldModule, Provider<Optional<UnfoldTransitionProgressProvider>> provider, Provider<Optional<NaturalRotationUnfoldProgressProvider>> provider2, Provider<Optional<ScopedUnfoldTransitionProgressProvider>> provider3, Provider<SysUIUnfoldComponent.Factory> provider4) {
        this.module = sysUIUnfoldModule;
        this.providerProvider = provider;
        this.rotationProvider = provider2;
        this.scopedProvider = provider3;
        this.factoryProvider = provider4;
    }
}
