package com.android.keyguard;

import android.content.res.Resources;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.EmergencyButtonController;
import com.android.keyguard.KeyguardInputViewController;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.statusbar.policy.DevicePostureController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardInputViewController_Factory_Factory implements Factory<KeyguardInputViewController.Factory> {
    public final Provider<DevicePostureController> devicePostureControllerProvider;
    public final Provider<EmergencyButtonController.Factory> emergencyButtonControllerFactoryProvider;
    public final Provider<FalsingCollector> falsingCollectorProvider;
    public final Provider<InputMethodManager> inputMethodManagerProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<LatencyTracker> latencyTrackerProvider;
    public final Provider<LiftToActivateListener> liftToActivateListenerProvider;
    public final Provider<LockPatternUtils> lockPatternUtilsProvider;
    public final Provider<DelayableExecutor> mainExecutorProvider;
    public final Provider<KeyguardMessageAreaController.Factory> messageAreaControllerFactoryProvider;
    public final Provider<Resources> resourcesProvider;
    public final Provider<TelephonyManager> telephonyManagerProvider;

    public static KeyguardInputViewController_Factory_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, EmergencyButtonController_Factory_Factory emergencyButtonController_Factory_Factory, Provider provider11) {
        return new KeyguardInputViewController_Factory_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, emergencyButtonController_Factory_Factory, provider11);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new KeyguardInputViewController.Factory(this.keyguardUpdateMonitorProvider.mo144get(), this.lockPatternUtilsProvider.mo144get(), this.latencyTrackerProvider.mo144get(), this.messageAreaControllerFactoryProvider.mo144get(), this.inputMethodManagerProvider.mo144get(), this.mainExecutorProvider.mo144get(), this.resourcesProvider.mo144get(), this.liftToActivateListenerProvider.mo144get(), this.telephonyManagerProvider.mo144get(), this.falsingCollectorProvider.mo144get(), this.emergencyButtonControllerFactoryProvider.mo144get(), this.devicePostureControllerProvider.mo144get());
    }

    public KeyguardInputViewController_Factory_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8, Provider provider9, Provider provider10, EmergencyButtonController_Factory_Factory emergencyButtonController_Factory_Factory, Provider provider11) {
        this.keyguardUpdateMonitorProvider = provider;
        this.lockPatternUtilsProvider = provider2;
        this.latencyTrackerProvider = provider3;
        this.messageAreaControllerFactoryProvider = provider4;
        this.inputMethodManagerProvider = provider5;
        this.mainExecutorProvider = provider6;
        this.resourcesProvider = provider7;
        this.liftToActivateListenerProvider = provider8;
        this.telephonyManagerProvider = provider9;
        this.falsingCollectorProvider = provider10;
        this.emergencyButtonControllerFactoryProvider = emergencyButtonController_Factory_Factory;
        this.devicePostureControllerProvider = provider11;
    }
}
