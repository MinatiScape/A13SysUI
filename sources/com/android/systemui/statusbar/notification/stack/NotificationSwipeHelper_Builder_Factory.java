package com.android.systemui.statusbar.notification.stack;

import android.content.res.Resources;
import android.view.ViewConfiguration;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.notification.stack.NotificationSwipeHelper;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationSwipeHelper_Builder_Factory implements Factory<NotificationSwipeHelper.Builder> {
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<FeatureFlags> featureFlagsProvider;
    public final Provider<Resources> resourcesProvider;
    public final Provider<ViewConfiguration> viewConfigurationProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new NotificationSwipeHelper.Builder(this.resourcesProvider.mo144get(), this.viewConfigurationProvider.mo144get(), this.falsingManagerProvider.mo144get(), this.featureFlagsProvider.mo144get());
    }

    public NotificationSwipeHelper_Builder_Factory(Provider<Resources> provider, Provider<ViewConfiguration> provider2, Provider<FalsingManager> provider3, Provider<FeatureFlags> provider4) {
        this.resourcesProvider = provider;
        this.viewConfigurationProvider = provider2;
        this.falsingManagerProvider = provider3;
        this.featureFlagsProvider = provider4;
    }
}
