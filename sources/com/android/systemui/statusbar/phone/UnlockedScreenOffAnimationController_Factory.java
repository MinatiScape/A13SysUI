package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.StatusBarStateControllerImpl;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.settings.GlobalSettings;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class UnlockedScreenOffAnimationController_Factory implements Factory<UnlockedScreenOffAnimationController> {
    public final Provider<Context> contextProvider;
    public final Provider<DozeParameters> dozeParametersProvider;
    public final Provider<GlobalSettings> globalSettingsProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<InteractionJankMonitor> interactionJankMonitorProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<KeyguardViewMediator> keyguardViewMediatorLazyProvider;
    public final Provider<PowerManager> powerManagerProvider;
    public final Provider<StatusBarStateControllerImpl> statusBarStateControllerImplProvider;
    public final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public static UnlockedScreenOffAnimationController_Factory create(Provider<Context> provider, Provider<WakefulnessLifecycle> provider2, Provider<StatusBarStateControllerImpl> provider3, Provider<KeyguardViewMediator> provider4, Provider<KeyguardStateController> provider5, Provider<DozeParameters> provider6, Provider<GlobalSettings> provider7, Provider<InteractionJankMonitor> provider8, Provider<PowerManager> provider9, Provider<Handler> provider10) {
        return new UnlockedScreenOffAnimationController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new UnlockedScreenOffAnimationController(this.contextProvider.mo144get(), this.wakefulnessLifecycleProvider.mo144get(), this.statusBarStateControllerImplProvider.mo144get(), DoubleCheck.lazy(this.keyguardViewMediatorLazyProvider), this.keyguardStateControllerProvider.mo144get(), DoubleCheck.lazy(this.dozeParametersProvider), this.globalSettingsProvider.mo144get(), this.interactionJankMonitorProvider.mo144get(), this.powerManagerProvider.mo144get(), this.handlerProvider.mo144get());
    }

    public UnlockedScreenOffAnimationController_Factory(Provider<Context> provider, Provider<WakefulnessLifecycle> provider2, Provider<StatusBarStateControllerImpl> provider3, Provider<KeyguardViewMediator> provider4, Provider<KeyguardStateController> provider5, Provider<DozeParameters> provider6, Provider<GlobalSettings> provider7, Provider<InteractionJankMonitor> provider8, Provider<PowerManager> provider9, Provider<Handler> provider10) {
        this.contextProvider = provider;
        this.wakefulnessLifecycleProvider = provider2;
        this.statusBarStateControllerImplProvider = provider3;
        this.keyguardViewMediatorLazyProvider = provider4;
        this.keyguardStateControllerProvider = provider5;
        this.dozeParametersProvider = provider6;
        this.globalSettingsProvider = provider7;
        this.interactionJankMonitorProvider = provider8;
        this.powerManagerProvider = provider9;
        this.handlerProvider = provider10;
    }
}
