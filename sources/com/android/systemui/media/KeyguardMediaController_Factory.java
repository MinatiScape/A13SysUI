package com.android.systemui.media;

import android.content.Context;
import android.os.Handler;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.transition.Transitions;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardMediaController_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider bypassControllerProvider;
    public final Provider configurationControllerProvider;
    public final Provider contextProvider;
    public final Provider mediaFlagsProvider;
    public final Provider mediaHostProvider;
    public final Provider notifLockscreenUserManagerProvider;
    public final Provider statusBarStateControllerProvider;

    public /* synthetic */ KeyguardMediaController_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, int i) {
        this.$r8$classId = i;
        this.mediaHostProvider = provider;
        this.bypassControllerProvider = provider2;
        this.statusBarStateControllerProvider = provider3;
        this.notifLockscreenUserManagerProvider = provider4;
        this.contextProvider = provider5;
        this.configurationControllerProvider = provider6;
        this.mediaFlagsProvider = provider7;
    }

    public static KeyguardMediaController_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7) {
        return new KeyguardMediaController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, 0);
    }

    public static KeyguardMediaController_Factory create$1(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7) {
        return new KeyguardMediaController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, 1);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new KeyguardMediaController((MediaHost) this.mediaHostProvider.mo144get(), (KeyguardBypassController) this.bypassControllerProvider.mo144get(), (SysuiStatusBarStateController) this.statusBarStateControllerProvider.mo144get(), (NotificationLockscreenUserManager) this.notifLockscreenUserManagerProvider.mo144get(), (Context) this.contextProvider.mo144get(), (ConfigurationController) this.configurationControllerProvider.mo144get(), (MediaFlags) this.mediaFlagsProvider.mo144get());
            default:
                return new Transitions((ShellTaskOrganizer) this.mediaHostProvider.mo144get(), (TransactionPool) this.bypassControllerProvider.mo144get(), (DisplayController) this.statusBarStateControllerProvider.mo144get(), (Context) this.notifLockscreenUserManagerProvider.mo144get(), (ShellExecutor) this.contextProvider.mo144get(), (Handler) this.configurationControllerProvider.mo144get(), (ShellExecutor) this.mediaFlagsProvider.mo144get());
        }
    }
}
