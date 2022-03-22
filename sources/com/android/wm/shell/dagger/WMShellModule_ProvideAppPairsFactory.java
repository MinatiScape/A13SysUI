package com.android.wm.shell.dagger;

import android.content.Context;
import com.android.keyguard.KeyguardViewController;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.apppairs.AppPairsController;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WMShellModule_ProvideAppPairsFactory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider displayControllerProvider;
    public final Provider displayImeControllerProvider;
    public final Provider displayInsetsControllerProvider;
    public final Provider mainExecutorProvider;
    public final Provider shellTaskOrganizerProvider;
    public final Provider syncQueueProvider;

    public /* synthetic */ WMShellModule_ProvideAppPairsFactory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, int i) {
        this.$r8$classId = i;
        this.shellTaskOrganizerProvider = provider;
        this.syncQueueProvider = provider2;
        this.displayControllerProvider = provider3;
        this.mainExecutorProvider = provider4;
        this.displayImeControllerProvider = provider5;
        this.displayInsetsControllerProvider = provider6;
    }

    public static WMShellModule_ProvideAppPairsFactory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6) {
        return new WMShellModule_ProvideAppPairsFactory(provider, provider2, provider3, provider4, provider5, provider6, 1);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new AppPairsController((ShellTaskOrganizer) this.shellTaskOrganizerProvider.mo144get(), (SyncTransactionQueue) this.syncQueueProvider.mo144get(), (DisplayController) this.displayControllerProvider.mo144get(), (ShellExecutor) this.mainExecutorProvider.mo144get(), (DisplayImeController) this.displayImeControllerProvider.mo144get(), (DisplayInsetsController) this.displayInsetsControllerProvider.mo144get());
            default:
                return new KeyguardUnlockAnimationController((Context) this.shellTaskOrganizerProvider.mo144get(), (KeyguardStateController) this.syncQueueProvider.mo144get(), DoubleCheck.lazy(this.displayControllerProvider), (KeyguardViewController) this.mainExecutorProvider.mo144get(), (FeatureFlags) this.displayImeControllerProvider.mo144get(), DoubleCheck.lazy(this.displayInsetsControllerProvider));
        }
    }
}
