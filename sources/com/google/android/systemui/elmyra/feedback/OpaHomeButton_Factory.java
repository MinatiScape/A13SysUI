package com.google.android.systemui.elmyra.feedback;

import android.content.Context;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.statusbar.notification.collection.coordinator.GutsCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.GutsCoordinatorLogger;
import com.android.systemui.statusbar.notification.collection.render.NotifGutsViewManager;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.wm.shell.pip.PipSnapAlgorithm;
import com.android.wm.shell.pip.tv.TvPipBoundsAlgorithm;
import com.android.wm.shell.pip.tv.TvPipBoundsState;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class OpaHomeButton_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider keyguardViewMediatorProvider;
    public final Provider navModeControllerProvider;
    public final Provider statusBarProvider;

    public /* synthetic */ OpaHomeButton_Factory(Provider provider, Provider provider2, Provider provider3, int i) {
        this.$r8$classId = i;
        this.keyguardViewMediatorProvider = provider;
        this.statusBarProvider = provider2;
        this.navModeControllerProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new OpaHomeButton((KeyguardViewMediator) this.keyguardViewMediatorProvider.mo144get(), (StatusBar) this.statusBarProvider.mo144get(), (NavigationModeController) this.navModeControllerProvider.mo144get());
            case 1:
                return new GutsCoordinator((NotifGutsViewManager) this.keyguardViewMediatorProvider.mo144get(), (GutsCoordinatorLogger) this.statusBarProvider.mo144get(), (DumpManager) this.navModeControllerProvider.mo144get());
            default:
                return new TvPipBoundsAlgorithm((Context) this.keyguardViewMediatorProvider.mo144get(), (TvPipBoundsState) this.statusBarProvider.mo144get(), (PipSnapAlgorithm) this.navModeControllerProvider.mo144get());
        }
    }
}
