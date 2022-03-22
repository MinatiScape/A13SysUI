package com.google.android.systemui.columbus.gates;

import android.content.Context;
import android.os.Handler;
import com.android.systemui.unfold.UnfoldProgressProvider;
import com.android.systemui.unfold.UnfoldTransitionModule;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import com.android.systemui.unfold.config.UnfoldTransitionConfig;
import com.android.wm.shell.unfold.ShellUnfoldProgressProvider;
import com.google.android.systemui.columbus.ColumbusModule_ProvideTransientGateDurationFactory;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ChargingState_Factory implements Factory {
    public final /* synthetic */ int $r8$classId = 1;
    public final Provider contextProvider;
    public final Object gateDurationProvider;
    public final Provider handlerProvider;

    public ChargingState_Factory(Provider provider, Provider provider2) {
        ColumbusModule_ProvideTransientGateDurationFactory columbusModule_ProvideTransientGateDurationFactory = ColumbusModule_ProvideTransientGateDurationFactory.InstanceHolder.INSTANCE;
        this.contextProvider = provider;
        this.handlerProvider = provider2;
        this.gateDurationProvider = columbusModule_ProvideTransientGateDurationFactory;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new ChargingState((Context) this.contextProvider.mo144get(), (Handler) this.handlerProvider.mo144get(), ((Long) ((Provider) this.gateDurationProvider).mo144get()).longValue());
            default:
                Optional optional = (Optional) this.handlerProvider.mo144get();
                Objects.requireNonNull((UnfoldTransitionModule) this.gateDurationProvider);
                if (!((UnfoldTransitionConfig) this.contextProvider.mo144get()).isEnabled() || !optional.isPresent()) {
                    return ShellUnfoldProgressProvider.NO_PROVIDER;
                }
                return new UnfoldProgressProvider((UnfoldTransitionProgressProvider) optional.get());
        }
    }

    public ChargingState_Factory(UnfoldTransitionModule unfoldTransitionModule, Provider provider, Provider provider2) {
        this.gateDurationProvider = unfoldTransitionModule;
        this.contextProvider = provider;
        this.handlerProvider = provider2;
    }
}
