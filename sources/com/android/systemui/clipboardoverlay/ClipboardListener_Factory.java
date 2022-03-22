package com.android.systemui.clipboardoverlay;

import android.content.ClipboardManager;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import com.android.systemui.controls.dagger.ControlsComponent;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.notification.InstantAppNotifier;
import com.android.systemui.statusbar.policy.DeviceControlsControllerImpl;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.settings.SecureSettings;
import com.google.android.systemui.columbus.ColumbusSettings;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ClipboardListener_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider clipboardManagerProvider;
    public final Provider contextProvider;
    public final Provider deviceConfigProxyProvider;
    public final Provider overlayFactoryProvider;

    public /* synthetic */ ClipboardListener_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.deviceConfigProxyProvider = provider2;
        this.overlayFactoryProvider = provider3;
        this.clipboardManagerProvider = provider4;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                DeviceConfigProxy deviceConfigProxy = (DeviceConfigProxy) this.deviceConfigProxyProvider.mo144get();
                return new ClipboardListener((Context) this.contextProvider.mo144get(), (ClipboardOverlayControllerFactory) this.overlayFactoryProvider.mo144get(), (ClipboardManager) this.clipboardManagerProvider.mo144get());
            case 1:
                return new InstantAppNotifier((Context) this.contextProvider.mo144get(), (CommandQueue) this.deviceConfigProxyProvider.mo144get(), (Executor) this.overlayFactoryProvider.mo144get(), (Optional) this.clipboardManagerProvider.mo144get());
            case 2:
                return new DeviceControlsControllerImpl((Context) this.contextProvider.mo144get(), (ControlsComponent) this.deviceConfigProxyProvider.mo144get(), (UserContextProvider) this.overlayFactoryProvider.mo144get(), (SecureSettings) this.clipboardManagerProvider.mo144get());
            default:
                Context context = (Context) this.contextProvider.mo144get();
                ColumbusSettings columbusSettings = (ColumbusSettings) this.deviceConfigProxyProvider.mo144get();
                Lazy lazy = DoubleCheck.lazy(this.overlayFactoryProvider);
                Lazy lazy2 = DoubleCheck.lazy(this.clipboardManagerProvider);
                Objects.requireNonNull(columbusSettings);
                boolean z = false;
                if (Settings.Secure.getIntForUser(columbusSettings.contentResolver, "columbus_ap_sensor", 0, columbusSettings.userTracker.getUserId()) != 0) {
                    z = true;
                }
                if (z || !context.getPackageManager().hasSystemFeature("android.hardware.context_hub")) {
                    Log.i("Columbus/Module", "Creating AP sensor");
                    return (GestureSensor) lazy2.get();
                }
                Log.i("Columbus/Module", "Creating CHRE sensor");
                return (GestureSensor) lazy.get();
        }
    }
}
