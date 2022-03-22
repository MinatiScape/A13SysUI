package com.android.systemui.qs.tiles;

import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.tiles.UserDetailView;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class UserDetailView_Adapter_Factory implements Factory<UserDetailView.Adapter> {
    public final Provider<Context> contextProvider;
    public final Provider<UserSwitcherController> controllerProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new UserDetailView.Adapter(this.contextProvider.mo144get(), this.controllerProvider.mo144get(), this.uiEventLoggerProvider.mo144get(), this.falsingManagerProvider.mo144get());
    }

    public UserDetailView_Adapter_Factory(Provider<Context> provider, Provider<UserSwitcherController> provider2, Provider<UiEventLogger> provider3, Provider<FalsingManager> provider4) {
        this.contextProvider = provider;
        this.controllerProvider = provider2;
        this.uiEventLoggerProvider = provider3;
        this.falsingManagerProvider = provider4;
    }
}
