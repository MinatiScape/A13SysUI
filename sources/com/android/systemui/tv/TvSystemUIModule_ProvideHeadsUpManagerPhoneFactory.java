package com.android.systemui.tv;

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
public final class TvSystemUIModule_ProvideHeadsUpManagerPhoneFactory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider bypassControllerProvider;
    public final Provider configurationControllerProvider;
    public final Provider contextProvider;
    public final Provider groupManagerProvider;
    public final Provider headsUpManagerLoggerProvider;
    public final Provider statusBarStateControllerProvider;
    public final Provider visualStabilityProvider;

    public /* synthetic */ TvSystemUIModule_ProvideHeadsUpManagerPhoneFactory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.headsUpManagerLoggerProvider = provider2;
        this.statusBarStateControllerProvider = provider3;
        this.bypassControllerProvider = provider4;
        this.groupManagerProvider = provider5;
        this.visualStabilityProvider = provider6;
        this.configurationControllerProvider = provider7;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final HeadsUpManagerPhone mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new HeadsUpManagerPhone((Context) this.contextProvider.mo144get(), (HeadsUpManagerLogger) this.headsUpManagerLoggerProvider.mo144get(), (StatusBarStateController) this.statusBarStateControllerProvider.mo144get(), (KeyguardBypassController) this.bypassControllerProvider.mo144get(), (GroupMembershipManager) this.groupManagerProvider.mo144get(), (VisualStabilityProvider) this.visualStabilityProvider.mo144get(), (ConfigurationController) this.configurationControllerProvider.mo144get());
            default:
                return new HeadsUpManagerPhone((Context) this.contextProvider.mo144get(), (HeadsUpManagerLogger) this.headsUpManagerLoggerProvider.mo144get(), (StatusBarStateController) this.statusBarStateControllerProvider.mo144get(), (KeyguardBypassController) this.bypassControllerProvider.mo144get(), (GroupMembershipManager) this.groupManagerProvider.mo144get(), (VisualStabilityProvider) this.visualStabilityProvider.mo144get(), (ConfigurationController) this.configurationControllerProvider.mo144get());
        }
    }
}
