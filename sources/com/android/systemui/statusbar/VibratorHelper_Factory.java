package com.android.systemui.statusbar;

import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import com.android.systemui.dreams.SmartSpaceComplication;
import com.android.systemui.screenshot.DeleteScreenshotReceiver;
import com.android.systemui.screenshot.ScreenshotSmartActions;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController;
import com.android.systemui.statusbar.phone.KeyguardEnvironmentImpl;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.unfold.UnfoldSharedModule;
import com.android.systemui.unfold.updates.DeviceFoldStateProvider;
import com.android.systemui.unfold.updates.DeviceFoldStateProvider_Factory;
import com.android.wm.shell.pip.PipMediaController;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class VibratorHelper_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Object executorProvider;
    public final Provider vibratorProvider;

    public /* synthetic */ VibratorHelper_Factory(Provider provider, Provider provider2, int i) {
        this.$r8$classId = i;
        this.vibratorProvider = provider;
        this.executorProvider = provider2;
    }

    public VibratorHelper_Factory(UnfoldSharedModule unfoldSharedModule, DeviceFoldStateProvider_Factory deviceFoldStateProvider_Factory) {
        this.$r8$classId = 5;
        this.executorProvider = unfoldSharedModule;
        this.vibratorProvider = deviceFoldStateProvider_Factory;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new VibratorHelper((Vibrator) this.vibratorProvider.mo144get(), (Executor) ((Provider) this.executorProvider).mo144get());
            case 1:
                return new SmartSpaceComplication((Context) this.vibratorProvider.mo144get(), (LockscreenSmartspaceController) ((Provider) this.executorProvider).mo144get());
            case 2:
                return new DeleteScreenshotReceiver((ScreenshotSmartActions) this.vibratorProvider.mo144get(), (Executor) ((Provider) this.executorProvider).mo144get());
            case 3:
                return new KeyguardEnvironmentImpl((NotificationLockscreenUserManager) this.vibratorProvider.mo144get(), (DeviceProvisionedController) ((Provider) this.executorProvider).mo144get());
            case 4:
                return new PipMediaController((Context) this.vibratorProvider.mo144get(), (Handler) ((Provider) this.executorProvider).mo144get());
            default:
                DeviceFoldStateProvider deviceFoldStateProvider = (DeviceFoldStateProvider) this.vibratorProvider.mo144get();
                Objects.requireNonNull((UnfoldSharedModule) this.executorProvider);
                return deviceFoldStateProvider;
        }
    }
}
