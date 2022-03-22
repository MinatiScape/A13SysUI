package com.android.systemui.controls.controller;

import android.content.Context;
import android.os.Handler;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.ui.ControlsUiController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.SectionHeaderVisibilityProvider;
import com.android.systemui.statusbar.notification.collection.coordinator.KeyguardCoordinator;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ControlsControllerImpl_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider bindingControllerProvider;
    public final Provider broadcastDispatcherProvider;
    public final Provider contextProvider;
    public final Provider dumpManagerProvider;
    public final Provider executorProvider;
    public final Provider listingControllerProvider;
    public final Provider optionalWrapperProvider;
    public final Provider uiControllerProvider;
    public final Provider userTrackerProvider;

    public /* synthetic */ ControlsControllerImpl_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.executorProvider = provider2;
        this.uiControllerProvider = provider3;
        this.bindingControllerProvider = provider4;
        this.listingControllerProvider = provider5;
        this.broadcastDispatcherProvider = provider6;
        this.optionalWrapperProvider = provider7;
        this.dumpManagerProvider = provider8;
        this.userTrackerProvider = provider9;
    }

    public static ControlsControllerImpl_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9) {
        return new ControlsControllerImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, 0);
    }

    public static ControlsControllerImpl_Factory create$1(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9) {
        return new ControlsControllerImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, 1);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new ControlsControllerImpl((Context) this.contextProvider.mo144get(), (DelayableExecutor) this.executorProvider.mo144get(), (ControlsUiController) this.uiControllerProvider.mo144get(), (ControlsBindingController) this.bindingControllerProvider.mo144get(), (ControlsListingController) this.listingControllerProvider.mo144get(), (BroadcastDispatcher) this.broadcastDispatcherProvider.mo144get(), (Optional) this.optionalWrapperProvider.mo144get(), (DumpManager) this.dumpManagerProvider.mo144get(), (UserTracker) this.userTrackerProvider.mo144get());
            default:
                return new KeyguardCoordinator((Context) this.contextProvider.mo144get(), (Handler) this.executorProvider.mo144get(), (KeyguardStateController) this.uiControllerProvider.mo144get(), (NotificationLockscreenUserManager) this.bindingControllerProvider.mo144get(), (BroadcastDispatcher) this.listingControllerProvider.mo144get(), (StatusBarStateController) this.broadcastDispatcherProvider.mo144get(), (KeyguardUpdateMonitor) this.optionalWrapperProvider.mo144get(), (HighPriorityProvider) this.dumpManagerProvider.mo144get(), (SectionHeaderVisibilityProvider) this.userTrackerProvider.mo144get());
        }
    }
}
