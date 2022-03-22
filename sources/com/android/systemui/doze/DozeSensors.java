package com.android.systemui.doze;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.hardware.Sensor;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.hardware.display.AmbientDisplayConfiguration;
import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline2;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.keyguard.KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.doze.DozeSensors;
import com.android.systemui.plugins.SensorManagerPlugin;
import com.android.systemui.statusbar.policy.DevicePostureController;
import com.android.systemui.util.sensors.AsyncSensorManager;
import com.android.systemui.util.sensors.AsyncSensorManager$$ExternalSyntheticLambda1;
import com.android.systemui.util.sensors.AsyncSensorManager$$ExternalSyntheticLambda2;
import com.android.systemui.util.sensors.ProximitySensor;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.util.wakelock.WakeLock;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class DozeSensors {
    public static final boolean DEBUG = DozeService.DEBUG;
    public static final UiEventLoggerImpl UI_EVENT_LOGGER = new UiEventLoggerImpl();
    public final AuthController mAuthController;
    public final AnonymousClass2 mAuthControllerCallback;
    public final AmbientDisplayConfiguration mConfig;
    public long mDebounceFrom;
    public int mDevicePosture;
    public final DozeSensors$$ExternalSyntheticLambda0 mDevicePostureCallback = new DevicePostureController.Callback() { // from class: com.android.systemui.doze.DozeSensors$$ExternalSyntheticLambda0
        @Override // com.android.systemui.statusbar.policy.DevicePostureController.Callback
        public final void onPostureChanged(int i) {
            DozeSensors.TriggerSensor[] triggerSensorArr;
            DozeSensors dozeSensors = DozeSensors.this;
            Objects.requireNonNull(dozeSensors);
            if (dozeSensors.mDevicePosture != i) {
                dozeSensors.mDevicePosture = i;
                for (DozeSensors.TriggerSensor triggerSensor : dozeSensors.mTriggerSensors) {
                    int i2 = dozeSensors.mDevicePosture;
                    Objects.requireNonNull(triggerSensor);
                    int i3 = triggerSensor.mPosture;
                    if (i3 != i2) {
                        Sensor[] sensorArr = triggerSensor.mSensors;
                        if (sensorArr.length >= 2 && i2 < sensorArr.length) {
                            Sensor sensor = sensorArr[i3];
                            Sensor sensor2 = sensorArr[i2];
                            if (Objects.equals(sensor, sensor2)) {
                                triggerSensor.mPosture = i2;
                            } else {
                                if (triggerSensor.mRegistered) {
                                    boolean cancelTriggerSensor = DozeSensors.this.mSensorManager.cancelTriggerSensor(triggerSensor, sensor);
                                    if (DozeSensors.DEBUG) {
                                        Log.d("DozeSensors", "posture changed, cancelTriggerSensor[" + sensor + "] " + cancelTriggerSensor);
                                    }
                                    triggerSensor.mRegistered = false;
                                }
                                triggerSensor.mPosture = i2;
                                triggerSensor.updateListening();
                                DozeSensors.this.mDozeLog.tracePostureChanged(triggerSensor.mPosture, "DozeSensors swap {" + sensor + "} => {" + sensor2 + "}, mRegistered=" + triggerSensor.mRegistered);
                            }
                        }
                    }
                }
            }
        }
    };
    public final DevicePostureController mDevicePostureController;
    public final DozeLog mDozeLog;
    public final Handler mHandler;
    public boolean mListening;
    public boolean mListeningProxSensors;
    public boolean mListeningTouchScreenSensors;
    public final Consumer<Boolean> mProxCallback;
    public final ProximitySensor mProximitySensor;
    public final boolean mScreenOffUdfpsEnabled;
    public final SecureSettings mSecureSettings;
    public boolean mSelectivelyRegisterProxSensors;
    public final Callback mSensorCallback;
    public final AsyncSensorManager mSensorManager;
    public boolean mSettingRegistered;
    public final AnonymousClass1 mSettingsObserver;
    public TriggerSensor[] mTriggerSensors;
    public boolean mUdfpsEnrolled;
    public final WakeLock mWakeLock;

    /* loaded from: classes.dex */
    public interface Callback {
    }

    /* JADX WARN: Failed to restore enum class, 'enum' modifier removed */
    /* loaded from: classes.dex */
    public static final class DozeSensorsUiEvent extends Enum<DozeSensorsUiEvent> implements UiEventLogger.UiEventEnum {
        public static final /* synthetic */ DozeSensorsUiEvent[] $VALUES;
        public static final DozeSensorsUiEvent ACTION_AMBIENT_GESTURE_PICKUP;
        private final int mId = 459;

        static {
            DozeSensorsUiEvent dozeSensorsUiEvent = new DozeSensorsUiEvent();
            ACTION_AMBIENT_GESTURE_PICKUP = dozeSensorsUiEvent;
            $VALUES = new DozeSensorsUiEvent[]{dozeSensorsUiEvent};
        }

        public static DozeSensorsUiEvent valueOf(String str) {
            return (DozeSensorsUiEvent) Enum.valueOf(DozeSensorsUiEvent.class, str);
        }

        public static DozeSensorsUiEvent[] values() {
            return (DozeSensorsUiEvent[]) $VALUES.clone();
        }

        public final int getId() {
            return this.mId;
        }
    }

    /* loaded from: classes.dex */
    public class PluginSensor extends TriggerSensor implements SensorManagerPlugin.SensorEventListener {
        public static final /* synthetic */ int $r8$clinit = 0;
        public long mDebounce;
        public final SensorManagerPlugin.Sensor mPluginSensor;

        public PluginSensor(SensorManagerPlugin.Sensor sensor, String str, boolean z, int i, long j) {
            super(DozeSensors.this, null, str, z, i, false, false);
            this.mPluginSensor = sensor;
            this.mDebounce = j;
        }

        public static String triggerEventToString(SensorManagerPlugin.SensorEvent sensorEvent) {
            if (sensorEvent == null) {
                return null;
            }
            StringBuilder sb = new StringBuilder("PluginTriggerEvent[");
            sb.append(sensorEvent.getSensor());
            sb.append(',');
            sb.append(sensorEvent.getVendorType());
            if (sensorEvent.getValues() != null) {
                for (int i = 0; i < sensorEvent.getValues().length; i++) {
                    sb.append(',');
                    sb.append(sensorEvent.getValues()[i]);
                }
            }
            sb.append(']');
            return sb.toString();
        }

        @Override // com.android.systemui.plugins.SensorManagerPlugin.SensorEventListener
        public final void onSensorChanged(final SensorManagerPlugin.SensorEvent sensorEvent) {
            DozeSensors.this.mDozeLog.traceSensor(this.mPulseReason);
            DozeSensors dozeSensors = DozeSensors.this;
            dozeSensors.mHandler.post(dozeSensors.mWakeLock.wrap(new Runnable() { // from class: com.android.systemui.doze.DozeSensors$PluginSensor$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    DozeSensors.PluginSensor pluginSensor = DozeSensors.PluginSensor.this;
                    SensorManagerPlugin.SensorEvent sensorEvent2 = sensorEvent;
                    int i = DozeSensors.PluginSensor.$r8$clinit;
                    Objects.requireNonNull(pluginSensor);
                    if (SystemClock.uptimeMillis() < DozeSensors.this.mDebounceFrom + pluginSensor.mDebounce) {
                        ExifInterface$$ExternalSyntheticOutline2.m(VendorAtomValue$$ExternalSyntheticOutline1.m("onSensorEvent dropped: "), DozeSensors.PluginSensor.triggerEventToString(sensorEvent2), "DozeSensors");
                        return;
                    }
                    if (DozeSensors.DEBUG) {
                        ExifInterface$$ExternalSyntheticOutline2.m(VendorAtomValue$$ExternalSyntheticOutline1.m("onSensorEvent: "), DozeSensors.PluginSensor.triggerEventToString(sensorEvent2), "DozeSensors");
                    }
                    DozeSensors.Callback callback = DozeSensors.this.mSensorCallback;
                    int i2 = pluginSensor.mPulseReason;
                    float[] values = sensorEvent2.getValues();
                    DozeTriggers$$ExternalSyntheticLambda0 dozeTriggers$$ExternalSyntheticLambda0 = (DozeTriggers$$ExternalSyntheticLambda0) callback;
                    Objects.requireNonNull(dozeTriggers$$ExternalSyntheticLambda0);
                    ((DozeTriggers) dozeTriggers$$ExternalSyntheticLambda0.f$0).onSensor(i2, -1.0f, -1.0f, values);
                }
            }));
        }

        @Override // com.android.systemui.doze.DozeSensors.TriggerSensor
        public final String toString() {
            return "{mRegistered=" + this.mRegistered + ", mRequested=" + this.mRequested + ", mDisabled=false, mConfigured=" + this.mConfigured + ", mIgnoresSetting=" + this.mIgnoresSetting + ", mSensor=" + this.mPluginSensor + "}";
        }

        @Override // com.android.systemui.doze.DozeSensors.TriggerSensor
        public final void updateListening() {
            if (this.mConfigured) {
                AsyncSensorManager asyncSensorManager = DozeSensors.this.mSensorManager;
                if (this.mRequested && ((enabledBySetting() || this.mIgnoresSetting) && !this.mRegistered)) {
                    SensorManagerPlugin.Sensor sensor = this.mPluginSensor;
                    Objects.requireNonNull(asyncSensorManager);
                    if (asyncSensorManager.mPlugins.isEmpty()) {
                        Log.w("AsyncSensorManager", "No plugins registered");
                    } else {
                        asyncSensorManager.mExecutor.execute(new AsyncSensorManager$$ExternalSyntheticLambda1(asyncSensorManager, sensor, this, 0));
                    }
                    this.mRegistered = true;
                    if (DozeSensors.DEBUG) {
                        Log.d("DozeSensors", "registerPluginListener");
                    }
                } else if (this.mRegistered) {
                    SensorManagerPlugin.Sensor sensor2 = this.mPluginSensor;
                    Objects.requireNonNull(asyncSensorManager);
                    asyncSensorManager.mExecutor.execute(new AsyncSensorManager$$ExternalSyntheticLambda2(asyncSensorManager, sensor2, this, 0));
                    this.mRegistered = false;
                    if (DozeSensors.DEBUG) {
                        Log.d("DozeSensors", "unregisterPluginListener");
                    }
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public class TriggerSensor extends TriggerEventListener {
        public static final /* synthetic */ int $r8$clinit = 0;
        public boolean mConfigured;
        public boolean mIgnoresSetting;
        public int mPosture;
        public final int mPulseReason;
        public boolean mRegistered;
        public final boolean mReportsTouchCoordinates;
        public boolean mRequested;
        public final boolean mRequiresProx;
        public final boolean mRequiresTouchscreen;
        public final Sensor[] mSensors;
        public final String mSetting;
        public final boolean mSettingDefault;

        public TriggerSensor(DozeSensors dozeSensors, Sensor sensor, String str, boolean z, int i, boolean z2, boolean z3) {
            this(dozeSensors, sensor, str, true, z, i, z2, z3, false);
        }

        public TriggerSensor(DozeSensors dozeSensors, Sensor sensor, String str, boolean z, boolean z2, int i, boolean z3, boolean z4, boolean z5) {
            this(new Sensor[]{sensor}, str, z, z2, i, z3, z4, z5, 0);
        }

        public final boolean enabledBySetting() {
            if (!DozeSensors.this.mConfig.enabled(-2)) {
                return false;
            }
            if (!TextUtils.isEmpty(this.mSetting) && DozeSensors.this.mSecureSettings.getIntForUser(this.mSetting, this.mSettingDefault ? 1 : 0, -2) == 0) {
                return false;
            }
            return true;
        }

        @Override // android.hardware.TriggerEventListener
        public final void onTrigger(final TriggerEvent triggerEvent) {
            final Sensor sensor = this.mSensors[this.mPosture];
            DozeSensors.this.mDozeLog.traceSensor(this.mPulseReason);
            DozeSensors dozeSensors = DozeSensors.this;
            dozeSensors.mHandler.post(dozeSensors.mWakeLock.wrap(new Runnable() { // from class: com.android.systemui.doze.DozeSensors$TriggerSensor$$ExternalSyntheticLambda0
                /* JADX WARN: Removed duplicated region for block: B:27:0x0097  */
                /* JADX WARN: Removed duplicated region for block: B:30:? A[RETURN, SYNTHETIC] */
                @Override // java.lang.Runnable
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public final void run() {
                    /*
                        r8 = this;
                        com.android.systemui.doze.DozeSensors$TriggerSensor r0 = com.android.systemui.doze.DozeSensors.TriggerSensor.this
                        android.hardware.TriggerEvent r1 = r2
                        android.hardware.Sensor r8 = r3
                        int r2 = com.android.systemui.doze.DozeSensors.TriggerSensor.$r8$clinit
                        java.util.Objects.requireNonNull(r0)
                        boolean r2 = com.android.systemui.doze.DozeSensors.DEBUG
                        r3 = 0
                        if (r2 == 0) goto L_0x0059
                        java.lang.String r2 = "onTrigger: "
                        java.lang.StringBuilder r2 = android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1.m(r2)
                        if (r1 != 0) goto L_0x001a
                        r4 = 0
                        goto L_0x0054
                    L_0x001a:
                        java.lang.StringBuilder r4 = new java.lang.StringBuilder
                        java.lang.String r5 = "SensorEvent["
                        r4.<init>(r5)
                        long r5 = r1.timestamp
                        r4.append(r5)
                        r5 = 44
                        r4.append(r5)
                        android.hardware.Sensor r6 = r1.sensor
                        java.lang.String r6 = r6.getName()
                        r4.append(r6)
                        float[] r6 = r1.values
                        if (r6 == 0) goto L_0x004b
                        r6 = r3
                    L_0x0039:
                        float[] r7 = r1.values
                        int r7 = r7.length
                        if (r6 >= r7) goto L_0x004b
                        r4.append(r5)
                        float[] r7 = r1.values
                        r7 = r7[r6]
                        r4.append(r7)
                        int r6 = r6 + 1
                        goto L_0x0039
                    L_0x004b:
                        r5 = 93
                        r4.append(r5)
                        java.lang.String r4 = r4.toString()
                    L_0x0054:
                        java.lang.String r5 = "DozeSensors"
                        androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline2.m(r2, r4, r5)
                    L_0x0059:
                        if (r8 == 0) goto L_0x006a
                        int r8 = r8.getType()
                        r2 = 25
                        if (r8 != r2) goto L_0x006a
                        com.android.internal.logging.UiEventLoggerImpl r8 = com.android.systemui.doze.DozeSensors.UI_EVENT_LOGGER
                        com.android.systemui.doze.DozeSensors$DozeSensorsUiEvent r2 = com.android.systemui.doze.DozeSensors.DozeSensorsUiEvent.ACTION_AMBIENT_GESTURE_PICKUP
                        r8.log(r2)
                    L_0x006a:
                        r0.mRegistered = r3
                        boolean r8 = r0.mReportsTouchCoordinates
                        r2 = -1082130432(0xffffffffbf800000, float:-1.0)
                        if (r8 == 0) goto L_0x007e
                        float[] r8 = r1.values
                        int r4 = r8.length
                        r5 = 2
                        if (r4 < r5) goto L_0x007e
                        r2 = r8[r3]
                        r3 = 1
                        r8 = r8[r3]
                        goto L_0x007f
                    L_0x007e:
                        r8 = r2
                    L_0x007f:
                        com.android.systemui.doze.DozeSensors r3 = com.android.systemui.doze.DozeSensors.this
                        com.android.systemui.doze.DozeSensors$Callback r3 = r3.mSensorCallback
                        int r4 = r0.mPulseReason
                        float[] r1 = r1.values
                        com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda0 r3 = (com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda0) r3
                        java.util.Objects.requireNonNull(r3)
                        java.lang.Object r3 = r3.f$0
                        com.android.systemui.doze.DozeTriggers r3 = (com.android.systemui.doze.DozeTriggers) r3
                        r3.onSensor(r4, r2, r8, r1)
                        boolean r8 = r0.mRegistered
                        if (r8 != 0) goto L_0x009a
                        r0.updateListening()
                    L_0x009a:
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.doze.DozeSensors$TriggerSensor$$ExternalSyntheticLambda0.run():void");
                }
            }));
        }

        public void updateListening() {
            Sensor sensor = this.mSensors[this.mPosture];
            if (this.mConfigured && sensor != null) {
                if (!this.mRequested || (!enabledBySetting() && !this.mIgnoresSetting)) {
                    if (this.mRegistered) {
                        boolean cancelTriggerSensor = DozeSensors.this.mSensorManager.cancelTriggerSensor(this, sensor);
                        if (DozeSensors.DEBUG) {
                            Log.d("DozeSensors", "cancelTriggerSensor[" + sensor + "] " + cancelTriggerSensor);
                        }
                        this.mRegistered = false;
                    }
                } else if (!this.mRegistered) {
                    this.mRegistered = DozeSensors.this.mSensorManager.requestTriggerSensor(this, sensor);
                    if (DozeSensors.DEBUG) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("requestTriggerSensor[");
                        sb.append(sensor);
                        sb.append("] ");
                        KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0.m(sb, this.mRegistered, "DozeSensors");
                    }
                } else if (DozeSensors.DEBUG) {
                    Log.d("DozeSensors", "requestTriggerSensor[" + sensor + "] already registered");
                }
            }
        }

        public TriggerSensor(Sensor[] sensorArr, String str, boolean z, boolean z2, int i, boolean z3, boolean z4, boolean z5, int i2) {
            this.mSensors = sensorArr;
            this.mSetting = str;
            this.mSettingDefault = z;
            this.mConfigured = z2;
            this.mPulseReason = i;
            this.mReportsTouchCoordinates = z3;
            this.mRequiresTouchscreen = z4;
            this.mIgnoresSetting = false;
            this.mRequiresProx = z5;
            this.mPosture = i2;
        }

        public String toString() {
            StringBuilder m = DebugInfo$$ExternalSyntheticOutline0.m("{", "mRegistered=");
            m.append(this.mRegistered);
            m.append(", mRequested=");
            m.append(this.mRequested);
            m.append(", mDisabled=");
            m.append(false);
            m.append(", mConfigured=");
            m.append(this.mConfigured);
            m.append(", mIgnoresSetting=");
            m.append(this.mIgnoresSetting);
            m.append(", mSensors=");
            m.append(Arrays.toString(this.mSensors));
            if (this.mSensors.length > 2) {
                m.append(", mPosture=");
                m.append(DevicePostureController.devicePostureToString(DozeSensors.this.mDevicePosture));
            }
            m.append("}");
            return m.toString();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.systemui.doze.DozeSensors$$ExternalSyntheticLambda0] */
    /* JADX WARN: Type inference failed for: r2v2, types: [com.android.systemui.doze.DozeSensors$2, com.android.systemui.biometrics.AuthController$Callback] */
    /* JADX WARN: Type inference failed for: r3v0, types: [com.android.systemui.doze.DozeSensors$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public DozeSensors(com.android.systemui.util.sensors.AsyncSensorManager r21, com.android.systemui.statusbar.phone.DozeParameters r22, android.hardware.display.AmbientDisplayConfiguration r23, com.android.systemui.util.wakelock.WakeLock r24, com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda0 r25, com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda4 r26, com.android.systemui.doze.DozeLog r27, com.android.systemui.util.sensors.ProximitySensor r28, com.android.systemui.util.settings.SecureSettings r29, com.android.systemui.biometrics.AuthController r30, com.android.systemui.statusbar.policy.DevicePostureController r31) {
        /*
            Method dump skipped, instructions count: 656
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.doze.DozeSensors.<init>(com.android.systemui.util.sensors.AsyncSensorManager, com.android.systemui.statusbar.phone.DozeParameters, android.hardware.display.AmbientDisplayConfiguration, com.android.systemui.util.wakelock.WakeLock, com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda0, com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda4, com.android.systemui.doze.DozeLog, com.android.systemui.util.sensors.ProximitySensor, com.android.systemui.util.settings.SecureSettings, com.android.systemui.biometrics.AuthController, com.android.systemui.statusbar.policy.DevicePostureController):void");
    }

    public final void setProxListening(boolean z) {
        if (this.mProximitySensor.isRegistered() && z) {
            this.mProximitySensor.alertListeners();
        } else if (z) {
            this.mProximitySensor.resume();
        } else {
            this.mProximitySensor.pause();
        }
    }

    public final void updateListening() {
        TriggerSensor[] triggerSensorArr;
        TriggerSensor[] triggerSensorArr2;
        boolean z;
        boolean z2 = false;
        for (TriggerSensor triggerSensor : this.mTriggerSensors) {
            if (!this.mListening || ((triggerSensor.mRequiresTouchscreen && !this.mListeningTouchScreenSensors) || (triggerSensor.mRequiresProx && !this.mListeningProxSensors))) {
                z = false;
            } else {
                z = true;
            }
            Objects.requireNonNull(triggerSensor);
            if (triggerSensor.mRequested != z) {
                triggerSensor.mRequested = z;
                triggerSensor.updateListening();
            }
            if (z) {
                z2 = true;
            }
        }
        if (!z2) {
            this.mSecureSettings.unregisterContentObserver(this.mSettingsObserver);
        } else if (!this.mSettingRegistered) {
            for (TriggerSensor triggerSensor2 : this.mTriggerSensors) {
                Objects.requireNonNull(triggerSensor2);
                if (triggerSensor2.mConfigured && !TextUtils.isEmpty(triggerSensor2.mSetting)) {
                    DozeSensors dozeSensors = DozeSensors.this;
                    dozeSensors.mSecureSettings.registerContentObserverForUser(triggerSensor2.mSetting, dozeSensors.mSettingsObserver, -1);
                }
            }
        }
        this.mSettingRegistered = z2;
    }

    public static Sensor findSensor(AsyncSensorManager asyncSensorManager, String str, String str2) {
        boolean z = !TextUtils.isEmpty(str2);
        boolean z2 = !TextUtils.isEmpty(str);
        if (!(z || z2)) {
            return null;
        }
        for (Sensor sensor : asyncSensorManager.getSensorList(-1)) {
            if (!z || str2.equals(sensor.getName())) {
                if (!z2 || str.equals(sensor.getStringType())) {
                    return sensor;
                }
            }
        }
        return null;
    }
}
