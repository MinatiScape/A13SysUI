package com.android.systemui.doze.dagger;

import android.os.SystemProperties;
import com.android.systemui.doze.DozeBrightnessHostForwarder;
import com.android.systemui.doze.DozeHost;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.doze.DozeScreenStatePreventingAdapter;
import com.android.systemui.doze.DozeSuspendScreenStatePreventingAdapter;
import com.android.systemui.statusbar.phone.DozeParameters;
import dagger.internal.Factory;
import dagger.internal.InstanceFactory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozeModule_ProvidesWrappedServiceFactory implements Factory<DozeMachine.Service> {
    public final Provider<DozeHost> dozeHostProvider;
    public final Provider<DozeMachine.Service> dozeMachineServiceProvider;
    public final Provider<DozeParameters> dozeParametersProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        DozeParameters dozeParameters = this.dozeParametersProvider.mo144get();
        DozeMachine.Service dozeBrightnessHostForwarder = new DozeBrightnessHostForwarder(this.dozeMachineServiceProvider.mo144get(), this.dozeHostProvider.mo144get());
        Objects.requireNonNull(dozeParameters);
        if (!SystemProperties.getBoolean("doze.display.supported", dozeParameters.mResources.getBoolean(2131034183))) {
            dozeBrightnessHostForwarder = new DozeScreenStatePreventingAdapter(dozeBrightnessHostForwarder);
        }
        if (!dozeParameters.mResources.getBoolean(2131034190)) {
            return new DozeSuspendScreenStatePreventingAdapter(dozeBrightnessHostForwarder);
        }
        return dozeBrightnessHostForwarder;
    }

    public DozeModule_ProvidesWrappedServiceFactory(InstanceFactory instanceFactory, Provider provider, Provider provider2) {
        this.dozeMachineServiceProvider = instanceFactory;
        this.dozeHostProvider = provider;
        this.dozeParametersProvider = provider2;
    }
}
