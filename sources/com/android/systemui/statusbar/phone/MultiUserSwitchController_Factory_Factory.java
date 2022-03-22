package com.android.systemui.statusbar.phone;

import android.os.UserManager;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.user.UserSwitchDialogController;
import com.android.systemui.statusbar.phone.MultiUserSwitchController;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MultiUserSwitchController_Factory_Factory implements Factory<MultiUserSwitchController.Factory> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<FeatureFlags> featureFlagsProvider;
    public final Provider<UserManager> userManagerProvider;
    public final Provider<UserSwitchDialogController> userSwitchDialogControllerProvider;
    public final Provider<UserSwitcherController> userSwitcherControllerProvider;

    public static MultiUserSwitchController_Factory_Factory create(Provider<UserManager> provider, Provider<UserSwitcherController> provider2, Provider<FalsingManager> provider3, Provider<UserSwitchDialogController> provider4, Provider<FeatureFlags> provider5, Provider<ActivityStarter> provider6) {
        return new MultiUserSwitchController_Factory_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new MultiUserSwitchController.Factory(this.userManagerProvider.mo144get(), this.userSwitcherControllerProvider.mo144get(), this.falsingManagerProvider.mo144get(), this.userSwitchDialogControllerProvider.mo144get(), this.featureFlagsProvider.mo144get(), this.activityStarterProvider.mo144get());
    }

    public MultiUserSwitchController_Factory_Factory(Provider<UserManager> provider, Provider<UserSwitcherController> provider2, Provider<FalsingManager> provider3, Provider<UserSwitchDialogController> provider4, Provider<FeatureFlags> provider5, Provider<ActivityStarter> provider6) {
        this.userManagerProvider = provider;
        this.userSwitcherControllerProvider = provider2;
        this.falsingManagerProvider = provider3;
        this.userSwitchDialogControllerProvider = provider4;
        this.featureFlagsProvider = provider5;
        this.activityStarterProvider = provider6;
    }
}
