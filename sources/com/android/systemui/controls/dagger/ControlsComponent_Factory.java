package com.android.systemui.controls.dagger;

import android.content.Context;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.controller.ControlsTileResourceConfiguration;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.ui.ControlsUiController;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ControlsComponent_Factory implements Factory<ControlsComponent> {
    public final Provider<Context> contextProvider;
    public final Provider<ControlsController> controlsControllerProvider;
    public final Provider<ControlsListingController> controlsListingControllerProvider;
    public final Provider<ControlsUiController> controlsUiControllerProvider;
    public final Provider<Boolean> featureEnabledProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<LockPatternUtils> lockPatternUtilsProvider;
    public final Provider<Optional<ControlsTileResourceConfiguration>> optionalControlsTileResourceConfigurationProvider;
    public final Provider<SecureSettings> secureSettingsProvider;
    public final Provider<UserTracker> userTrackerProvider;

    public static ControlsComponent_Factory create(Provider<Boolean> provider, Provider<Context> provider2, Provider<ControlsController> provider3, Provider<ControlsUiController> provider4, Provider<ControlsListingController> provider5, Provider<LockPatternUtils> provider6, Provider<KeyguardStateController> provider7, Provider<UserTracker> provider8, Provider<SecureSettings> provider9, Provider<Optional<ControlsTileResourceConfiguration>> provider10) {
        return new ControlsComponent_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        boolean booleanValue = this.featureEnabledProvider.mo144get().booleanValue();
        this.contextProvider.mo144get();
        return new ControlsComponent(booleanValue, DoubleCheck.lazy(this.controlsControllerProvider), DoubleCheck.lazy(this.controlsUiControllerProvider), DoubleCheck.lazy(this.controlsListingControllerProvider), this.lockPatternUtilsProvider.mo144get(), this.keyguardStateControllerProvider.mo144get(), this.userTrackerProvider.mo144get(), this.secureSettingsProvider.mo144get(), this.optionalControlsTileResourceConfigurationProvider.mo144get());
    }

    public ControlsComponent_Factory(Provider<Boolean> provider, Provider<Context> provider2, Provider<ControlsController> provider3, Provider<ControlsUiController> provider4, Provider<ControlsListingController> provider5, Provider<LockPatternUtils> provider6, Provider<KeyguardStateController> provider7, Provider<UserTracker> provider8, Provider<SecureSettings> provider9, Provider<Optional<ControlsTileResourceConfiguration>> provider10) {
        this.featureEnabledProvider = provider;
        this.contextProvider = provider2;
        this.controlsControllerProvider = provider3;
        this.controlsUiControllerProvider = provider4;
        this.controlsListingControllerProvider = provider5;
        this.lockPatternUtilsProvider = provider6;
        this.keyguardStateControllerProvider = provider7;
        this.userTrackerProvider = provider8;
        this.secureSettingsProvider = provider9;
        this.optionalControlsTileResourceConfigurationProvider = provider10;
    }
}
