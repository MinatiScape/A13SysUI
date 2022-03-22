package com.google.android.systemui.dagger;

import android.content.Context;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.collection.provider.VisualStabilityProvider;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.HeadsUpManagerLogger;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SystemUIGoogleModule_ProvideHeadsUpManagerPhoneFactory implements Factory<HeadsUpManagerPhone> {
    public final Provider<KeyguardBypassController> bypassControllerProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<GroupMembershipManager> groupManagerProvider;
    public final Provider<HeadsUpManagerLogger> headsUpManagerLoggerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<VisualStabilityProvider> visualStabilityProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new HeadsUpManagerPhone(this.contextProvider.mo144get(), this.headsUpManagerLoggerProvider.mo144get(), this.statusBarStateControllerProvider.mo144get(), this.bypassControllerProvider.mo144get(), this.groupManagerProvider.mo144get(), this.visualStabilityProvider.mo144get(), this.configurationControllerProvider.mo144get());
    }

    public SystemUIGoogleModule_ProvideHeadsUpManagerPhoneFactory(Provider<Context> provider, Provider<HeadsUpManagerLogger> provider2, Provider<StatusBarStateController> provider3, Provider<KeyguardBypassController> provider4, Provider<GroupMembershipManager> provider5, Provider<VisualStabilityProvider> provider6, Provider<ConfigurationController> provider7) {
        this.contextProvider = provider;
        this.headsUpManagerLoggerProvider = provider2;
        this.statusBarStateControllerProvider = provider3;
        this.bypassControllerProvider = provider4;
        this.groupManagerProvider = provider5;
        this.visualStabilityProvider = provider6;
        this.configurationControllerProvider = provider7;
    }
}
