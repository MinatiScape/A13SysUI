package com.android.systemui.globalactions;

import android.content.Context;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.BlurUtils;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationInteractionTracker;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.collection.NotifPipelineChoreographer;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder;
import com.android.systemui.statusbar.notification.collection.listbuilder.ShadeListBuilderLogger;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class GlobalActionsImpl_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider blurUtilsProvider;
    public final Provider commandQueueProvider;
    public final Provider contextProvider;
    public final Provider deviceProvisionedControllerProvider;
    public final Provider globalActionsDialogLazyProvider;
    public final Provider keyguardStateControllerProvider;

    public /* synthetic */ GlobalActionsImpl_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.commandQueueProvider = provider2;
        this.globalActionsDialogLazyProvider = provider3;
        this.blurUtilsProvider = provider4;
        this.keyguardStateControllerProvider = provider5;
        this.deviceProvisionedControllerProvider = provider6;
    }

    public static GlobalActionsImpl_Factory create(Provider provider, Provider provider2, GlobalActionsDialogLite_Factory globalActionsDialogLite_Factory, Provider provider3, Provider provider4, Provider provider5) {
        return new GlobalActionsImpl_Factory(provider, provider2, globalActionsDialogLite_Factory, provider3, provider4, provider5, 0);
    }

    public static GlobalActionsImpl_Factory create$1(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6) {
        return new GlobalActionsImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, 1);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new GlobalActionsImpl((Context) this.contextProvider.mo144get(), (CommandQueue) this.commandQueueProvider.mo144get(), DoubleCheck.lazy(this.globalActionsDialogLazyProvider), (BlurUtils) this.blurUtilsProvider.mo144get(), (KeyguardStateController) this.keyguardStateControllerProvider.mo144get(), (DeviceProvisionedController) this.deviceProvisionedControllerProvider.mo144get());
            default:
                return new ShadeListBuilder((DumpManager) this.contextProvider.mo144get(), (NotifPipelineChoreographer) this.commandQueueProvider.mo144get(), (NotifPipelineFlags) this.globalActionsDialogLazyProvider.mo144get(), (NotificationInteractionTracker) this.blurUtilsProvider.mo144get(), (ShadeListBuilderLogger) this.keyguardStateControllerProvider.mo144get(), (SystemClock) this.deviceProvisionedControllerProvider.mo144get());
        }
    }
}
