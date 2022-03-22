package com.android.systemui.qs;

import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.settings.SecureSettingsImpl_Factory;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class QSFooterViewController_Factory implements Factory<QSFooterViewController> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<QSPanelController> qsPanelControllerProvider;
    public final Provider<UserTracker> userTrackerProvider;
    public final Provider<QSFooterView> viewProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new QSFooterViewController(this.viewProvider.mo144get(), this.userTrackerProvider.mo144get(), this.falsingManagerProvider.mo144get(), this.activityStarterProvider.mo144get(), this.qsPanelControllerProvider.mo144get());
    }

    public QSFooterViewController_Factory(SecureSettingsImpl_Factory secureSettingsImpl_Factory, Provider provider, Provider provider2, Provider provider3, Provider provider4) {
        this.viewProvider = secureSettingsImpl_Factory;
        this.userTrackerProvider = provider;
        this.falsingManagerProvider = provider2;
        this.activityStarterProvider = provider3;
        this.qsPanelControllerProvider = provider4;
    }
}
