package com.google.android.systemui.columbus;

import com.google.android.systemui.columbus.gates.CameraVisibility;
import com.google.android.systemui.columbus.gates.FlagEnabled;
import com.google.android.systemui.columbus.gates.Gate;
import com.google.android.systemui.columbus.gates.KeyguardProximity;
import com.google.android.systemui.columbus.gates.PowerSaveState;
import com.google.android.systemui.columbus.gates.PowerState;
import com.google.android.systemui.columbus.gates.SetupWizard;
import com.google.android.systemui.columbus.gates.TelephonyActivity;
import com.google.android.systemui.columbus.gates.VrMode;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Set;
import javax.inject.Provider;
import kotlin.collections.SetsKt__SetsKt;
/* loaded from: classes.dex */
public final class ColumbusModule_ProvideColumbusGatesFactory implements Factory<Set<Gate>> {
    public final Provider<CameraVisibility> cameraVisibilityProvider;
    public final Provider<FlagEnabled> flagEnabledProvider;
    public final Provider<KeyguardProximity> keyguardProximityProvider;
    public final Provider<PowerSaveState> powerSaveStateProvider;
    public final Provider<PowerState> powerStateProvider;
    public final Provider<SetupWizard> setupWizardProvider;
    public final Provider<TelephonyActivity> telephonyActivityProvider;
    public final Provider<VrMode> vrModeProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        Set of = SetsKt__SetsKt.setOf(this.flagEnabledProvider.mo144get(), this.keyguardProximityProvider.mo144get(), this.setupWizardProvider.mo144get(), this.telephonyActivityProvider.mo144get(), this.vrModeProvider.mo144get(), this.cameraVisibilityProvider.mo144get(), this.powerSaveStateProvider.mo144get(), this.powerStateProvider.mo144get());
        Objects.requireNonNull(of, "Cannot return null from a non-@Nullable @Provides method");
        return of;
    }

    public ColumbusModule_ProvideColumbusGatesFactory(Provider<FlagEnabled> provider, Provider<KeyguardProximity> provider2, Provider<SetupWizard> provider3, Provider<TelephonyActivity> provider4, Provider<VrMode> provider5, Provider<CameraVisibility> provider6, Provider<PowerSaveState> provider7, Provider<PowerState> provider8) {
        this.flagEnabledProvider = provider;
        this.keyguardProximityProvider = provider2;
        this.setupWizardProvider = provider3;
        this.telephonyActivityProvider = provider4;
        this.vrModeProvider = provider5;
        this.cameraVisibilityProvider = provider6;
        this.powerSaveStateProvider = provider7;
        this.powerStateProvider = provider8;
    }
}
