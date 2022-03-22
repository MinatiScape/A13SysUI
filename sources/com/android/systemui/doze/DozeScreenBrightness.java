package com.android.systemui.doze;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.IndentingPrintWriter;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.ScreenOffAnimation;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.policy.DevicePostureController;
import com.android.systemui.util.sensors.AsyncSensorManager;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public final class DozeScreenBrightness extends BroadcastReceiver implements DozeMachine.Part, SensorEventListener {
    public final Context mContext;
    public int mDefaultDozeBrightness;
    public int mDevicePosture;
    public final AnonymousClass1 mDevicePostureCallback;
    public final DevicePostureController mDevicePostureController;
    public final DozeHost mDozeHost;
    public final DozeLog mDozeLog;
    public final DozeParameters mDozeParameters;
    public final DozeMachine.Service mDozeService;
    public final Handler mHandler;
    public final Optional<Sensor>[] mLightSensorOptional;
    public boolean mRegistered;
    public final int mScreenBrightnessDim;
    public final float mScreenBrightnessMinimumDimAmountFloat;
    public final AsyncSensorManager mSensorManager;
    public final int[] mSensorToBrightness;
    public final int[] mSensorToScrimOpacity;
    public final WakefulnessLifecycle mWakefulnessLifecycle;
    public boolean mPaused = false;
    public boolean mScreenOff = false;
    public int mLastSensorValue = -1;
    public int mDebugBrightnessBucket = -1;

    @Override // android.hardware.SensorEventListener
    public final void onAccuracyChanged(Sensor sensor, int i) {
    }

    public final void setLightSensorEnabled(boolean z) {
        Sensor sensor;
        boolean z2 = false;
        if (z && !this.mRegistered && isLightSensorPresent()) {
            AsyncSensorManager asyncSensorManager = this.mSensorManager;
            Optional<Sensor>[] optionalArr = this.mLightSensorOptional;
            if (optionalArr != null && this.mDevicePosture < optionalArr.length) {
                z2 = true;
            }
            if (!z2) {
                sensor = null;
            } else {
                sensor = optionalArr[this.mDevicePosture].get();
            }
            this.mRegistered = asyncSensorManager.registerListener(this, sensor, 3, this.mHandler);
            this.mLastSensorValue = -1;
        } else if (!z && this.mRegistered) {
            this.mSensorManager.unregisterListener(this);
            this.mRegistered = false;
            this.mLastSensorValue = -1;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0020  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0022  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0025  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0049  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x004b  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0059  */
    /* JADX WARN: Removed duplicated region for block: B:34:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateBrightnessAndReady(boolean r10) {
        /*
            r9 = this;
            r0 = -1
            if (r10 != 0) goto L_0x000b
            boolean r10 = r9.mRegistered
            if (r10 != 0) goto L_0x000b
            int r10 = r9.mDebugBrightnessBucket
            if (r10 == r0) goto L_0x0062
        L_0x000b:
            int r10 = r9.mDebugBrightnessBucket
            if (r10 != r0) goto L_0x0011
            int r10 = r9.mLastSensorValue
        L_0x0011:
            if (r10 < 0) goto L_0x001c
            int[] r1 = r9.mSensorToBrightness
            int r2 = r1.length
            if (r10 < r2) goto L_0x0019
            goto L_0x001c
        L_0x0019:
            r1 = r1[r10]
            goto L_0x001d
        L_0x001c:
            r1 = r0
        L_0x001d:
            r2 = 0
            if (r1 <= 0) goto L_0x0022
            r3 = 1
            goto L_0x0023
        L_0x0022:
            r3 = r2
        L_0x0023:
            if (r3 == 0) goto L_0x0043
            com.android.systemui.doze.DozeMachine$Service r4 = r9.mDozeService
            android.content.Context r5 = r9.mContext
            android.content.ContentResolver r5 = r5.getContentResolver()
            r6 = 2147483647(0x7fffffff, float:NaN)
            r7 = -2
            java.lang.String r8 = "screen_brightness"
            int r5 = android.provider.Settings.System.getIntForUser(r5, r8, r6, r7)
            int r1 = java.lang.Math.min(r1, r5)
            int r1 = r9.clampToDimBrightnessForScreenOff(r1)
            r4.setDozeScreenBrightness(r1)
        L_0x0043:
            boolean r1 = r9.isLightSensorPresent()
            if (r1 != 0) goto L_0x004b
            r0 = r2
            goto L_0x0057
        L_0x004b:
            if (r3 == 0) goto L_0x0057
            if (r10 < 0) goto L_0x0057
            int[] r1 = r9.mSensorToScrimOpacity
            int r2 = r1.length
            if (r10 < r2) goto L_0x0055
            goto L_0x0057
        L_0x0055:
            r0 = r1[r10]
        L_0x0057:
            if (r0 < 0) goto L_0x0062
            com.android.systemui.doze.DozeHost r9 = r9.mDozeHost
            float r10 = (float) r0
            r0 = 1132396544(0x437f0000, float:255.0)
            float r10 = r10 / r0
            r9.setAodDimmingScrim(r10)
        L_0x0062:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.doze.DozeScreenBrightness.updateBrightnessAndReady(boolean):void");
    }

    static {
        SystemProperties.getBoolean("debug.aod_brightness", false);
    }

    public final int clampToDimBrightnessForScreenOff(int i) {
        boolean z;
        DozeParameters dozeParameters = this.mDozeParameters;
        Objects.requireNonNull(dozeParameters);
        ScreenOffAnimationController screenOffAnimationController = dozeParameters.mScreenOffAnimationController;
        Objects.requireNonNull(screenOffAnimationController);
        ArrayList arrayList = screenOffAnimationController.animations;
        boolean z2 = true;
        if (!(arrayList instanceof Collection) || !arrayList.isEmpty()) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                if (((ScreenOffAnimation) it.next()).shouldPlayAnimation()) {
                    z = true;
                    break;
                }
            }
        }
        z = false;
        if (!z) {
            WakefulnessLifecycle wakefulnessLifecycle = this.mWakefulnessLifecycle;
            Objects.requireNonNull(wakefulnessLifecycle);
            if (wakefulnessLifecycle.mWakefulness != 3) {
                z2 = false;
            }
        }
        if (z2) {
            WakefulnessLifecycle wakefulnessLifecycle2 = this.mWakefulnessLifecycle;
            Objects.requireNonNull(wakefulnessLifecycle2);
            if (wakefulnessLifecycle2.mLastSleepReason == 2) {
                return Math.max(0, Math.min(i - ((int) Math.floor(this.mScreenBrightnessMinimumDimAmountFloat * 255.0f)), this.mScreenBrightnessDim));
            }
        }
        return i;
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public final void dump(PrintWriter printWriter) {
        printWriter.println("DozeScreenBrightness:");
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter);
        indentingPrintWriter.increaseIndent();
        StringBuilder m = DozeScreenBrightness$$ExternalSyntheticOutline0.m(VendorAtomValue$$ExternalSyntheticOutline1.m("registered="), this.mRegistered, indentingPrintWriter, "posture=");
        m.append(DevicePostureController.devicePostureToString(this.mDevicePosture));
        indentingPrintWriter.println(m.toString());
    }

    public final boolean isLightSensorPresent() {
        boolean z;
        Optional<Sensor>[] optionalArr = this.mLightSensorOptional;
        if (optionalArr == null || this.mDevicePosture >= optionalArr.length) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            return optionalArr[this.mDevicePosture].isPresent();
        }
        if (optionalArr == null || !optionalArr[0].isPresent()) {
            return false;
        }
        return true;
    }

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        this.mDebugBrightnessBucket = intent.getIntExtra("brightness_bucket", -1);
        updateBrightnessAndReady(false);
    }

    @Override // android.hardware.SensorEventListener
    public final void onSensorChanged(SensorEvent sensorEvent) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("DozeScreenBrightness.onSensorChanged");
        m.append(sensorEvent.values[0]);
        Trace.beginSection(m.toString());
        try {
            if (this.mRegistered) {
                this.mLastSensorValue = (int) sensorEvent.values[0];
                updateBrightnessAndReady(false);
            }
        } finally {
            Trace.endSection();
        }
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.Object, com.android.systemui.doze.DozeScreenBrightness$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public DozeScreenBrightness(android.content.Context r2, com.android.systemui.doze.DozeMachine.Service r3, com.android.systemui.util.sensors.AsyncSensorManager r4, java.util.Optional<android.hardware.Sensor>[] r5, com.android.systemui.doze.DozeHost r6, android.os.Handler r7, com.android.systemui.doze.AlwaysOnDisplayPolicy r8, com.android.systemui.keyguard.WakefulnessLifecycle r9, com.android.systemui.statusbar.phone.DozeParameters r10, com.android.systemui.statusbar.policy.DevicePostureController r11, com.android.systemui.doze.DozeLog r12) {
        /*
            r1 = this;
            r1.<init>()
            r0 = 0
            r1.mPaused = r0
            r1.mScreenOff = r0
            r0 = -1
            r1.mLastSensorValue = r0
            r1.mDebugBrightnessBucket = r0
            com.android.systemui.doze.DozeScreenBrightness$1 r0 = new com.android.systemui.doze.DozeScreenBrightness$1
            r0.<init>()
            r1.mDevicePostureCallback = r0
            r1.mContext = r2
            r1.mDozeService = r3
            r1.mSensorManager = r4
            r1.mLightSensorOptional = r5
            r1.mDevicePostureController = r11
            int r3 = r11.getDevicePosture()
            r1.mDevicePosture = r3
            r1.mWakefulnessLifecycle = r9
            r1.mDozeParameters = r10
            r1.mDozeHost = r6
            r1.mHandler = r7
            r1.mDozeLog = r12
            android.content.res.Resources r2 = r2.getResources()
            r3 = 17105102(0x10500ce, float:2.442882E-38)
            float r2 = r2.getFloat(r3)
            r1.mScreenBrightnessMinimumDimAmountFloat = r2
            int r2 = r8.defaultDozeBrightness
            r1.mDefaultDozeBrightness = r2
            int r2 = r8.dimBrightness
            r1.mScreenBrightnessDim = r2
            int[] r2 = r8.screenBrightnessArray
            r1.mSensorToBrightness = r2
            int[] r2 = r8.dimmingScrimArray
            r1.mSensorToScrimOpacity = r2
            r11.addCallback(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.doze.DozeScreenBrightness.<init>(android.content.Context, com.android.systemui.doze.DozeMachine$Service, com.android.systemui.util.sensors.AsyncSensorManager, java.util.Optional[], com.android.systemui.doze.DozeHost, android.os.Handler, com.android.systemui.doze.AlwaysOnDisplayPolicy, com.android.systemui.keyguard.WakefulnessLifecycle, com.android.systemui.statusbar.phone.DozeParameters, com.android.systemui.statusbar.policy.DevicePostureController, com.android.systemui.doze.DozeLog):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x001f, code lost:
        if (r9 != 11) goto L_0x0071;
     */
    @Override // com.android.systemui.doze.DozeMachine.Part
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void transitionTo(com.android.systemui.doze.DozeMachine.State r9, com.android.systemui.doze.DozeMachine.State r10) {
        /*
            r8 = this;
            int r9 = r10.ordinal()
            r0 = 0
            java.lang.String r1 = "screen_brightness"
            r2 = -2
            r3 = 2147483647(0x7fffffff, float:NaN)
            r4 = 0
            r5 = 1
            if (r9 == r5) goto L_0x0053
            r6 = 2
            if (r9 == r6) goto L_0x0031
            r0 = 3
            if (r9 == r0) goto L_0x002d
            r0 = 4
            if (r9 == r0) goto L_0x002d
            r0 = 8
            if (r9 == r0) goto L_0x0022
            r0 = 11
            if (r9 == r0) goto L_0x002d
            goto L_0x0071
        L_0x0022:
            r8.setLightSensorEnabled(r4)
            com.android.systemui.statusbar.policy.DevicePostureController r9 = r8.mDevicePostureController
            com.android.systemui.doze.DozeScreenBrightness$1 r0 = r8.mDevicePostureCallback
            r9.removeCallback(r0)
            goto L_0x0071
        L_0x002d:
            r8.setLightSensorEnabled(r5)
            goto L_0x0071
        L_0x0031:
            r8.setLightSensorEnabled(r4)
            com.android.systemui.doze.DozeMachine$Service r9 = r8.mDozeService
            int r6 = r8.mDefaultDozeBrightness
            android.content.Context r7 = r8.mContext
            android.content.ContentResolver r7 = r7.getContentResolver()
            int r1 = android.provider.Settings.System.getIntForUser(r7, r1, r3, r2)
            int r1 = java.lang.Math.min(r6, r1)
            int r1 = r8.clampToDimBrightnessForScreenOff(r1)
            r9.setDozeScreenBrightness(r1)
            com.android.systemui.doze.DozeHost r9 = r8.mDozeHost
            r9.setAodDimmingScrim(r0)
            goto L_0x0071
        L_0x0053:
            com.android.systemui.doze.DozeMachine$Service r9 = r8.mDozeService
            int r6 = r8.mDefaultDozeBrightness
            android.content.Context r7 = r8.mContext
            android.content.ContentResolver r7 = r7.getContentResolver()
            int r1 = android.provider.Settings.System.getIntForUser(r7, r1, r3, r2)
            int r1 = java.lang.Math.min(r6, r1)
            int r1 = r8.clampToDimBrightnessForScreenOff(r1)
            r9.setDozeScreenBrightness(r1)
            com.android.systemui.doze.DozeHost r9 = r8.mDozeHost
            r9.setAodDimmingScrim(r0)
        L_0x0071:
            com.android.systemui.doze.DozeMachine$State r9 = com.android.systemui.doze.DozeMachine.State.FINISH
            if (r10 == r9) goto L_0x0094
            com.android.systemui.doze.DozeMachine$State r9 = com.android.systemui.doze.DozeMachine.State.DOZE
            if (r10 != r9) goto L_0x007b
            r9 = r5
            goto L_0x007c
        L_0x007b:
            r9 = r4
        L_0x007c:
            boolean r0 = r8.mScreenOff
            if (r0 == r9) goto L_0x0085
            r8.mScreenOff = r9
            r8.updateBrightnessAndReady(r5)
        L_0x0085:
            com.android.systemui.doze.DozeMachine$State r9 = com.android.systemui.doze.DozeMachine.State.DOZE_AOD_PAUSED
            if (r10 != r9) goto L_0x008a
            goto L_0x008b
        L_0x008a:
            r5 = r4
        L_0x008b:
            boolean r9 = r8.mPaused
            if (r9 == r5) goto L_0x0094
            r8.mPaused = r5
            r8.updateBrightnessAndReady(r4)
        L_0x0094:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.doze.DozeScreenBrightness.transitionTo(com.android.systemui.doze.DozeMachine$State, com.android.systemui.doze.DozeMachine$State):void");
    }
}
