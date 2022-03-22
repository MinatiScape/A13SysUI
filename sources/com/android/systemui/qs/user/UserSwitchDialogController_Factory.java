package com.android.systemui.qs.user;

import com.android.internal.logging.UiEventLogger;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.tiles.UserDetailView;
import com.android.systemui.qs.tiles.UserDetailView_Adapter_Factory;
import com.android.systemui.qs.user.UserSwitchDialogController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class UserSwitchDialogController_Factory implements Factory<UserSwitchDialogController> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<DialogLaunchAnimator> dialogLaunchAnimatorProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;
    public final Provider<UserDetailView.Adapter> userDetailViewAdapterProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new UserSwitchDialogController(this.userDetailViewAdapterProvider, this.activityStarterProvider.mo144get(), this.falsingManagerProvider.mo144get(), this.dialogLaunchAnimatorProvider.mo144get(), this.uiEventLoggerProvider.mo144get(), UserSwitchDialogController.AnonymousClass1.INSTANCE);
    }

    public UserSwitchDialogController_Factory(UserDetailView_Adapter_Factory userDetailView_Adapter_Factory, Provider provider, Provider provider2, Provider provider3, Provider provider4) {
        this.userDetailViewAdapterProvider = userDetailView_Adapter_Factory;
        this.activityStarterProvider = provider;
        this.falsingManagerProvider = provider2;
        this.dialogLaunchAnimatorProvider = provider3;
        this.uiEventLoggerProvider = provider4;
    }
}
