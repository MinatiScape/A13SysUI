package com.android.systemui.settings.brightness;

import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.settings.brightness.BrightnessSliderController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class BrightnessSliderController_Factory_Factory implements Factory<BrightnessSliderController.Factory> {
    public final Provider<FalsingManager> falsingManagerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new BrightnessSliderController.Factory(this.falsingManagerProvider.mo144get());
    }

    public BrightnessSliderController_Factory_Factory(Provider<FalsingManager> provider) {
        this.falsingManagerProvider = provider;
    }
}
