package com.google.android.systemui.assist.uihints;

import android.content.Context;
import android.hardware.devicestate.DeviceStateManager;
import com.android.systemui.assist.AssistLogger;
import com.android.systemui.controls.management.ControlsListingControllerImpl;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.DevicePostureControllerImpl;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TouchInsideHandler_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider assistLoggerProvider;
    public final Provider assistManagerProvider;
    public final Provider navigationModeControllerProvider;

    public /* synthetic */ TouchInsideHandler_Factory(Provider provider, Provider provider2, Provider provider3, int i) {
        this.$r8$classId = i;
        this.assistManagerProvider = provider;
        this.navigationModeControllerProvider = provider2;
        this.assistLoggerProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new TouchInsideHandler(DoubleCheck.lazy(this.assistManagerProvider), (NavigationModeController) this.navigationModeControllerProvider.mo144get(), (AssistLogger) this.assistLoggerProvider.mo144get());
            case 1:
                return new ControlsListingControllerImpl((Context) this.assistManagerProvider.mo144get(), (Executor) this.navigationModeControllerProvider.mo144get(), (UserTracker) this.assistLoggerProvider.mo144get());
            default:
                return new DevicePostureControllerImpl((Context) this.assistManagerProvider.mo144get(), (DeviceStateManager) this.navigationModeControllerProvider.mo144get(), (Executor) this.assistLoggerProvider.mo144get());
        }
    }
}
