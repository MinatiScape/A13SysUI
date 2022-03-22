package com.android.systemui.biometrics;

import android.os.Handler;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.unfold.FoldAodAnimationController;
import com.android.systemui.util.settings.GlobalSettings;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class UdfpsHapticsSimulator_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider commandRegistryProvider;
    public final Provider keyguardUpdateMonitorProvider;
    public final Provider vibratorProvider;

    public /* synthetic */ UdfpsHapticsSimulator_Factory(Provider provider, Provider provider2, Provider provider3, int i) {
        this.$r8$classId = i;
        this.commandRegistryProvider = provider;
        this.vibratorProvider = provider2;
        this.keyguardUpdateMonitorProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                KeyguardUpdateMonitor keyguardUpdateMonitor = (KeyguardUpdateMonitor) this.keyguardUpdateMonitorProvider.mo144get();
                return new UdfpsHapticsSimulator((CommandRegistry) this.commandRegistryProvider.mo144get(), (VibratorHelper) this.vibratorProvider.mo144get());
            case 1:
                return new ScreenOffAnimationController((Optional) this.commandRegistryProvider.mo144get(), (UnlockedScreenOffAnimationController) this.vibratorProvider.mo144get(), (WakefulnessLifecycle) this.keyguardUpdateMonitorProvider.mo144get());
            default:
                return new FoldAodAnimationController((Handler) this.commandRegistryProvider.mo144get(), (WakefulnessLifecycle) this.vibratorProvider.mo144get(), (GlobalSettings) this.keyguardUpdateMonitorProvider.mo144get());
        }
    }
}
