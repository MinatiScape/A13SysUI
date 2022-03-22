package com.android.systemui.screenshot;

import android.content.Context;
import android.view.ViewGroup;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.management.ControlsRequestDialog;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.google.android.systemui.assist.uihints.GlowController;
import com.google.android.systemui.assist.uihints.TouchInsideHandler;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ActionProxyReceiver_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider activityManagerWrapperProvider;
    public final Provider screenshotSmartActionsProvider;
    public final Provider statusBarProvider;

    public /* synthetic */ ActionProxyReceiver_Factory(Provider provider, Provider provider2, Provider provider3, int i) {
        this.$r8$classId = i;
        this.statusBarProvider = provider;
        this.activityManagerWrapperProvider = provider2;
        this.screenshotSmartActionsProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new ActionProxyReceiver((Optional) this.statusBarProvider.mo144get(), (ActivityManagerWrapper) this.activityManagerWrapperProvider.mo144get(), (ScreenshotSmartActions) this.screenshotSmartActionsProvider.mo144get());
            case 1:
                return new ControlsRequestDialog((ControlsController) this.statusBarProvider.mo144get(), (BroadcastDispatcher) this.activityManagerWrapperProvider.mo144get(), (ControlsListingController) this.screenshotSmartActionsProvider.mo144get());
            default:
                return new GlowController((Context) this.statusBarProvider.mo144get(), (ViewGroup) this.activityManagerWrapperProvider.mo144get(), (TouchInsideHandler) this.screenshotSmartActionsProvider.mo144get());
        }
    }
}
