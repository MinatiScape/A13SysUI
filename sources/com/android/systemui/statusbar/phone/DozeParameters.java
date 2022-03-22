package com.android.systemui.statusbar.phone;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.hardware.display.AmbientDisplayConfiguration;
import android.os.PowerManager;
import android.os.SystemProperties;
import android.util.MathUtils;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.Dumpable;
import com.android.systemui.doze.AlwaysOnDisplayPolicy;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.unfold.FoldAodAnimationController;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public final class DozeParameters implements TunerService.Tunable, com.android.systemui.plugins.statusbar.DozeParameters, Dumpable, ConfigurationController.ConfigurationListener, StatusBarStateController.StateListener, FoldAodAnimationController.FoldAodAnimationStatus {
    public final AlwaysOnDisplayPolicy mAlwaysOnPolicy;
    public final AmbientDisplayConfiguration mAmbientDisplayConfiguration;
    public final BatteryController mBatteryController;
    public final HashSet mCallbacks = new HashSet();
    public boolean mControlScreenOffAnimation;
    public boolean mDozeAlwaysOn;
    public final FeatureFlags mFeatureFlags;
    public boolean mKeyguardShowing;
    public final KeyguardUpdateMonitorCallback mKeyguardVisibilityCallback;
    public final PowerManager mPowerManager;
    public final Resources mResources;
    public final ScreenOffAnimationController mScreenOffAnimationController;
    public final UnlockedScreenOffAnimationController mUnlockedScreenOffAnimationController;
    public static final boolean FORCE_NO_BLANKING = SystemProperties.getBoolean("debug.force_no_blanking", false);
    public static final boolean FORCE_BLANKING = SystemProperties.getBoolean("debug.force_blanking", false);

    /* loaded from: classes.dex */
    public interface Callback {
        void onAlwaysOnChange();
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.print("getAlwaysOn(): ");
        printWriter.println(getAlwaysOn());
        printWriter.print("getDisplayStateSupported(): ");
        printWriter.println(SystemProperties.getBoolean("doze.display.supported", this.mResources.getBoolean(2131034183)));
        printWriter.print("getPulseDuration(): ");
        int i = getInt("doze.pulse.duration.in", 2131492916);
        printWriter.println(getInt("doze.pulse.duration.out", 2131492917) + getInt("doze.pulse.duration.visible", 2131492918) + i);
        printWriter.print("getPulseInDuration(): ");
        printWriter.println(getInt("doze.pulse.duration.in", 2131492916));
        printWriter.print("getPulseInVisibleDuration(): ");
        printWriter.println(getInt("doze.pulse.duration.visible", 2131492918));
        printWriter.print("getPulseOutDuration(): ");
        printWriter.println(getInt("doze.pulse.duration.out", 2131492917));
        printWriter.print("getPulseOnSigMotion(): ");
        printWriter.println(SystemProperties.getBoolean("doze.pulse.sigmotion", this.mResources.getBoolean(2131034187)));
        printWriter.print("getVibrateOnSigMotion(): ");
        printWriter.println(SystemProperties.getBoolean("doze.vibrate.sigmotion", false));
        printWriter.print("getVibrateOnPickup(): ");
        printWriter.println(SystemProperties.getBoolean("doze.vibrate.pickup", false));
        printWriter.print("getProxCheckBeforePulse(): ");
        printWriter.println(SystemProperties.getBoolean("doze.pulse.proxcheck", this.mResources.getBoolean(2131034186)));
        printWriter.print("getPickupVibrationThreshold(): ");
        printWriter.println(getInt("doze.pickup.vibration.threshold", 2131492915));
        printWriter.print("getSelectivelyRegisterSensorsUsingProx(): ");
        printWriter.println(SystemProperties.getBoolean("doze.prox.selectively_register", this.mResources.getBoolean(2131034188)));
    }

    public final boolean getAlwaysOn() {
        if (!this.mDozeAlwaysOn || this.mBatteryController.isAodPowerSave()) {
            return false;
        }
        return true;
    }

    public final boolean getDisplayNeedsBlanking() {
        if (FORCE_BLANKING || (!FORCE_NO_BLANKING && this.mResources.getBoolean(17891598))) {
            return true;
        }
        return false;
    }

    public final int getInt(String str, int i) {
        return MathUtils.constrain(SystemProperties.getInt(str, this.mResources.getInteger(i)), 0, 60000);
    }

    @Override // com.android.systemui.tuner.TunerService.Tunable
    public final void onTuningChanged(String str, String str2) {
        this.mDozeAlwaysOn = this.mAmbientDisplayConfiguration.alwaysOnEnabled(-2);
        if (str.equals("doze_always_on")) {
            updateControlScreenOff();
        }
        Iterator it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            ((Callback) it.next()).onAlwaysOnChange();
        }
        ScreenOffAnimationController screenOffAnimationController = this.mScreenOffAnimationController;
        boolean alwaysOn = getAlwaysOn();
        Objects.requireNonNull(screenOffAnimationController);
        Iterator it2 = screenOffAnimationController.animations.iterator();
        while (it2.hasNext()) {
            ((ScreenOffAnimation) it2.next()).onAlwaysOnChanged(alwaysOn);
        }
    }

    public final boolean shouldDelayKeyguardShow() {
        ScreenOffAnimationController screenOffAnimationController = this.mScreenOffAnimationController;
        Objects.requireNonNull(screenOffAnimationController);
        ArrayList arrayList = screenOffAnimationController.animations;
        if (!(arrayList instanceof Collection) || !arrayList.isEmpty()) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                if (((ScreenOffAnimation) it.next()).shouldDelayKeyguardShow()) {
                    return true;
                }
            }
        }
        return false;
    }

    public final boolean shouldShowLightRevealScrim() {
        ScreenOffAnimationController screenOffAnimationController = this.mScreenOffAnimationController;
        Objects.requireNonNull(screenOffAnimationController);
        ArrayList arrayList = screenOffAnimationController.animations;
        if (!(arrayList instanceof Collection) || !arrayList.isEmpty()) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                if (((ScreenOffAnimation) it.next()).shouldPlayAnimation()) {
                    return true;
                }
            }
        }
        return false;
    }

    public DozeParameters(Resources resources, AmbientDisplayConfiguration ambientDisplayConfiguration, AlwaysOnDisplayPolicy alwaysOnDisplayPolicy, PowerManager powerManager, BatteryController batteryController, TunerService tunerService, DumpManager dumpManager, FeatureFlags featureFlags, ScreenOffAnimationController screenOffAnimationController, Optional<SysUIUnfoldComponent> optional, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController, KeyguardUpdateMonitor keyguardUpdateMonitor, ConfigurationController configurationController, StatusBarStateController statusBarStateController) {
        KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.statusbar.phone.DozeParameters.1
            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public final void onKeyguardVisibilityChanged(boolean z) {
                DozeParameters dozeParameters = DozeParameters.this;
                dozeParameters.mKeyguardShowing = z;
                dozeParameters.updateControlScreenOff();
            }

            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public final void onShadeExpandedChanged(boolean z) {
                DozeParameters.this.updateControlScreenOff();
            }
        };
        this.mKeyguardVisibilityCallback = keyguardUpdateMonitorCallback;
        this.mResources = resources;
        this.mAmbientDisplayConfiguration = ambientDisplayConfiguration;
        this.mAlwaysOnPolicy = alwaysOnDisplayPolicy;
        this.mBatteryController = batteryController;
        dumpManager.registerDumpable("DozeParameters", this);
        boolean z = !getDisplayNeedsBlanking();
        this.mControlScreenOffAnimation = z;
        this.mPowerManager = powerManager;
        powerManager.setDozeAfterScreenOff(!z);
        this.mFeatureFlags = featureFlags;
        this.mScreenOffAnimationController = screenOffAnimationController;
        this.mUnlockedScreenOffAnimationController = unlockedScreenOffAnimationController;
        keyguardUpdateMonitor.registerCallback(keyguardUpdateMonitorCallback);
        tunerService.addTunable(this, "doze_always_on", "accessibility_display_inversion_enabled");
        configurationController.addCallback(this);
        statusBarStateController.addCallback(this);
        FoldAodAnimationController foldAodAnimationController = (FoldAodAnimationController) optional.map(DozeParameters$$ExternalSyntheticLambda0.INSTANCE).orElse(null);
        if (foldAodAnimationController != null) {
            foldAodAnimationController.statusListeners.add(this);
        }
    }

    public final boolean canControlUnlockedScreenOff() {
        if (!getAlwaysOn() || !this.mFeatureFlags.isEnabled(Flags.LOCKSCREEN_ANIMATIONS) || getDisplayNeedsBlanking()) {
            return false;
        }
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0023, code lost:
        if (r0 != false) goto L_0x0025;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateControlScreenOff() {
        /*
            r3 = this;
            boolean r0 = r3.getDisplayNeedsBlanking()
            if (r0 != 0) goto L_0x0034
            boolean r0 = r3.getAlwaysOn()
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L_0x0026
            boolean r0 = r3.mKeyguardShowing
            if (r0 != 0) goto L_0x0025
            boolean r0 = r3.canControlUnlockedScreenOff()
            if (r0 == 0) goto L_0x0022
            com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController r0 = r3.mUnlockedScreenOffAnimationController
            boolean r0 = r0.shouldPlayUnlockedScreenOffAnimation()
            if (r0 == 0) goto L_0x0022
            r0 = r2
            goto L_0x0023
        L_0x0022:
            r0 = r1
        L_0x0023:
            if (r0 == 0) goto L_0x0026
        L_0x0025:
            r1 = r2
        L_0x0026:
            boolean r0 = r3.mControlScreenOffAnimation
            if (r0 != r1) goto L_0x002b
            goto L_0x0034
        L_0x002b:
            r3.mControlScreenOffAnimation = r1
            android.os.PowerManager r3 = r3.mPowerManager
            r0 = r1 ^ 1
            r3.setDozeAfterScreenOff(r0)
        L_0x0034:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.DozeParameters.updateControlScreenOff():void");
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public final void onConfigChanged(Configuration configuration) {
        updateControlScreenOff();
    }

    @Override // com.android.systemui.unfold.FoldAodAnimationController.FoldAodAnimationStatus
    public final void onFoldToAodAnimationChanged() {
        updateControlScreenOff();
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public final void onStatePostChange() {
        updateControlScreenOff();
    }

    @Override // com.android.systemui.plugins.statusbar.DozeParameters
    public final boolean shouldControlScreenOff() {
        return this.mControlScreenOffAnimation;
    }
}
