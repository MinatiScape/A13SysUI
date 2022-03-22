package com.android.systemui.statusbar.phone;

import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class StatusBarIconController_TintedIconManager_Factory_Factory implements Factory<StatusBarIconController.TintedIconManager.Factory> {
    public final Provider<FeatureFlags> featureFlagsProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new StatusBarIconController.TintedIconManager.Factory(this.featureFlagsProvider.mo144get());
    }

    public StatusBarIconController_TintedIconManager_Factory_Factory(Provider<FeatureFlags> provider) {
        this.featureFlagsProvider = provider;
    }
}
