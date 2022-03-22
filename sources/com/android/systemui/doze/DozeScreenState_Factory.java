package com.android.systemui.doze;

import android.os.Handler;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.UdfpsController;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.util.wakelock.WakeLock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozeScreenState_Factory implements Factory<DozeScreenState> {
    public final Provider<AuthController> authControllerProvider;
    public final Provider<DozeLog> dozeLogProvider;
    public final Provider<DozeScreenBrightness> dozeScreenBrightnessProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<DozeHost> hostProvider;
    public final Provider<DozeParameters> parametersProvider;
    public final Provider<DozeMachine.Service> serviceProvider;
    public final Provider<UdfpsController> udfpsControllerProvider;
    public final Provider<WakeLock> wakeLockProvider;

    public static DozeScreenState_Factory create(Provider<DozeMachine.Service> provider, Provider<Handler> provider2, Provider<DozeHost> provider3, Provider<DozeParameters> provider4, Provider<WakeLock> provider5, Provider<AuthController> provider6, Provider<UdfpsController> provider7, Provider<DozeLog> provider8, Provider<DozeScreenBrightness> provider9) {
        return new DozeScreenState_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new DozeScreenState(this.serviceProvider.mo144get(), this.handlerProvider.mo144get(), this.hostProvider.mo144get(), this.parametersProvider.mo144get(), this.wakeLockProvider.mo144get(), this.authControllerProvider.mo144get(), this.udfpsControllerProvider, this.dozeLogProvider.mo144get(), this.dozeScreenBrightnessProvider.mo144get());
    }

    public DozeScreenState_Factory(Provider<DozeMachine.Service> provider, Provider<Handler> provider2, Provider<DozeHost> provider3, Provider<DozeParameters> provider4, Provider<WakeLock> provider5, Provider<AuthController> provider6, Provider<UdfpsController> provider7, Provider<DozeLog> provider8, Provider<DozeScreenBrightness> provider9) {
        this.serviceProvider = provider;
        this.handlerProvider = provider2;
        this.hostProvider = provider3;
        this.parametersProvider = provider4;
        this.wakeLockProvider = provider5;
        this.authControllerProvider = provider6;
        this.udfpsControllerProvider = provider7;
        this.dozeLogProvider = provider8;
        this.dozeScreenBrightnessProvider = provider9;
    }
}
