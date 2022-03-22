package com.android.systemui.doze;

import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.hardware.display.AmbientDisplayConfiguration;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.text.format.Formatter;
import android.util.IndentingPrintWriter;
import android.util.Log;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline1;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.util.Preconditions;
import com.android.systemui.ScreenDecorations$2$$ExternalSyntheticLambda0;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dock.DockManager;
import com.android.systemui.doze.DozeHost;
import com.android.systemui.doze.DozeLog;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.doze.DozeSensors;
import com.android.systemui.doze.DozeTriggers;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.QSPanel$$ExternalSyntheticLambda1;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.DevicePostureController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.Assert;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.sensors.AsyncSensorManager;
import com.android.systemui.util.sensors.ProximityCheck;
import com.android.systemui.util.sensors.ProximitySensor;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.util.wakelock.WakeLock;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class DozeTriggers implements DozeMachine.Part {
    public static final boolean DEBUG = DozeService.DEBUG;
    public static boolean sWakeDisplaySensorState = true;
    public DozeTriggers$$ExternalSyntheticLambda1 mAodInterruptRunnable;
    public final AuthController mAuthController;
    public final BatteryController mBatteryController;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public final AmbientDisplayConfiguration mConfig;
    public final Context mContext;
    public final DevicePostureController mDevicePostureController;
    public final DockManager mDockManager;
    public final DozeHost mDozeHost;
    public final DozeLog mDozeLog;
    public final DozeParameters mDozeParameters;
    public final DozeSensors mDozeSensors;
    public final KeyguardStateController mKeyguardStateController;
    public DozeMachine mMachine;
    public final DelayableExecutor mMainExecutor;
    public long mNotificationPulseTime;
    public final ProximityCheck mProxCheck;
    public boolean mPulsePending;
    public final UiEventLogger mUiEventLogger;
    public final UiModeManager mUiModeManager;
    public final WakeLock mWakeLock;
    public boolean mWantProxSensor;
    public boolean mWantSensors;
    public boolean mWantTouchScreenSensors;
    public final TriggerReceiver mBroadcastReceiver = new TriggerReceiver();
    public final DockEventListener mDockEventListener = new DockEventListener();
    public AnonymousClass1 mHostCallback = new DozeHost.Callback() { // from class: com.android.systemui.doze.DozeTriggers.1
        @Override // com.android.systemui.doze.DozeHost.Callback
        public final void onDozeSuppressedChanged(boolean z) {
            DozeMachine.State state;
            if (!DozeTriggers.this.mConfig.alwaysOnEnabled(-2) || z) {
                state = DozeMachine.State.DOZE;
            } else {
                state = DozeMachine.State.DOZE_AOD;
            }
            DozeTriggers.this.mMachine.requestState(state);
        }

        @Override // com.android.systemui.doze.DozeHost.Callback
        public final void onNotificationAlerted(ScreenDecorations$2$$ExternalSyntheticLambda0 screenDecorations$2$$ExternalSyntheticLambda0) {
            DozeTriggers dozeTriggers = DozeTriggers.this;
            Objects.requireNonNull(dozeTriggers);
            if (DozeMachine.DEBUG) {
                Log.d("DozeTriggers", "requestNotificationPulse");
            }
            if (!DozeTriggers.sWakeDisplaySensorState) {
                Log.d("DozeTriggers", "Wake display false. Pulse denied.");
                screenDecorations$2$$ExternalSyntheticLambda0.run();
                dozeTriggers.mDozeLog.tracePulseDropped("wakeDisplaySensor");
                return;
            }
            dozeTriggers.mNotificationPulseTime = SystemClock.elapsedRealtime();
            if (!dozeTriggers.mConfig.pulseOnNotificationEnabled(-2)) {
                screenDecorations$2$$ExternalSyntheticLambda0.run();
                dozeTriggers.mDozeLog.tracePulseDropped("pulseOnNotificationsDisabled");
            } else if (dozeTriggers.mDozeHost.isDozeSuppressed()) {
                screenDecorations$2$$ExternalSyntheticLambda0.run();
                dozeTriggers.mDozeLog.tracePulseDropped("dozeSuppressed");
            } else {
                dozeTriggers.requestPulse(1, false, screenDecorations$2$$ExternalSyntheticLambda0);
                DozeLog dozeLog = dozeTriggers.mDozeLog;
                Objects.requireNonNull(dozeLog);
                DozeLogger dozeLogger = dozeLog.mLogger;
                Objects.requireNonNull(dozeLogger);
                LogBuffer logBuffer = dozeLogger.buffer;
                LogLevel logLevel = LogLevel.INFO;
                DozeLogger$logNotificationPulse$2 dozeLogger$logNotificationPulse$2 = DozeLogger$logNotificationPulse$2.INSTANCE;
                Objects.requireNonNull(logBuffer);
                if (!logBuffer.frozen) {
                    logBuffer.push(logBuffer.obtain("DozeLog", logLevel, dozeLogger$logNotificationPulse$2));
                }
                DozeLog.SummaryStats summaryStats = dozeLog.mNotificationPulseStats;
                Objects.requireNonNull(summaryStats);
                summaryStats.mCount++;
            }
        }

        @Override // com.android.systemui.doze.DozeHost.Callback
        public final void onPowerSaveChanged() {
            DozeMachine.State state = DozeMachine.State.DOZE;
            if (DozeTriggers.this.mDozeHost.isPowerSaveActive()) {
                DozeTriggers.this.mMachine.requestState(state);
            } else if (DozeTriggers.this.mMachine.getState() == state && DozeTriggers.this.mConfig.alwaysOnEnabled(-2)) {
                DozeTriggers.this.mMachine.requestState(DozeMachine.State.DOZE_AOD);
            }
        }
    };
    public final boolean mAllowPulseTriggers = true;

    /* loaded from: classes.dex */
    public class DockEventListener implements DockManager.DockEventListener {
        public DockEventListener() {
        }

        @Override // com.android.systemui.dock.DockManager.DockEventListener
        public final void onEvent(int i) {
            DozeSensors.TriggerSensor[] triggerSensorArr;
            DozeSensors.TriggerSensor[] triggerSensorArr2;
            if (DozeTriggers.DEBUG) {
                ExifInterface$$ExternalSyntheticOutline1.m("dock event = ", i, "DozeTriggers");
            }
            if (i == 0) {
                DozeSensors dozeSensors = DozeTriggers.this.mDozeSensors;
                Objects.requireNonNull(dozeSensors);
                for (DozeSensors.TriggerSensor triggerSensor : dozeSensors.mTriggerSensors) {
                    if (triggerSensor.mRequiresTouchscreen && triggerSensor.mIgnoresSetting) {
                        triggerSensor.mIgnoresSetting = false;
                        triggerSensor.updateListening();
                    }
                }
            } else if (i == 1 || i == 2) {
                DozeSensors dozeSensors2 = DozeTriggers.this.mDozeSensors;
                Objects.requireNonNull(dozeSensors2);
                for (DozeSensors.TriggerSensor triggerSensor2 : dozeSensors2.mTriggerSensors) {
                    if (triggerSensor2.mRequiresTouchscreen && !triggerSensor2.mIgnoresSetting) {
                        triggerSensor2.mIgnoresSetting = true;
                        triggerSensor2.updateListening();
                    }
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public class TriggerReceiver extends BroadcastReceiver {
        public boolean mRegistered;

        public TriggerReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            if ("com.android.systemui.doze.pulse".equals(intent.getAction())) {
                if (DozeMachine.DEBUG) {
                    Log.d("DozeTriggers", "Received pulse intent");
                }
                DozeTriggers.this.requestPulse(0, false, null);
            }
            if (UiModeManager.ACTION_ENTER_CAR_MODE.equals(intent.getAction())) {
                DozeTriggers.this.mMachine.requestState(DozeMachine.State.FINISH);
            }
            if ("android.intent.action.USER_SWITCHED".equals(intent.getAction())) {
                DozeSensors dozeSensors = DozeTriggers.this.mDozeSensors;
                Objects.requireNonNull(dozeSensors);
                for (DozeSensors.TriggerSensor triggerSensor : dozeSensors.mTriggerSensors) {
                    triggerSensor.updateListening();
                }
            }
        }
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [com.android.systemui.doze.DozeTriggers$1] */
    public DozeTriggers(Context context, DozeHost dozeHost, AmbientDisplayConfiguration ambientDisplayConfiguration, DozeParameters dozeParameters, AsyncSensorManager asyncSensorManager, WakeLock wakeLock, DockManager dockManager, ProximitySensor proximitySensor, ProximityCheck proximityCheck, DozeLog dozeLog, BroadcastDispatcher broadcastDispatcher, SecureSettings secureSettings, AuthController authController, DelayableExecutor delayableExecutor, UiEventLogger uiEventLogger, KeyguardStateController keyguardStateController, DevicePostureController devicePostureController, BatteryController batteryController) {
        this.mContext = context;
        this.mDozeHost = dozeHost;
        this.mConfig = ambientDisplayConfiguration;
        this.mDozeParameters = dozeParameters;
        this.mWakeLock = wakeLock;
        this.mDevicePostureController = devicePostureController;
        this.mDozeSensors = new DozeSensors(asyncSensorManager, dozeParameters, ambientDisplayConfiguration, wakeLock, new DozeTriggers$$ExternalSyntheticLambda0(this), new DozeTriggers$$ExternalSyntheticLambda4(this, 0), dozeLog, proximitySensor, secureSettings, authController, devicePostureController);
        this.mUiModeManager = (UiModeManager) context.getSystemService(UiModeManager.class);
        this.mDockManager = dockManager;
        this.mProxCheck = proximityCheck;
        this.mDozeLog = dozeLog;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mAuthController = authController;
        this.mMainExecutor = delayableExecutor;
        this.mUiEventLogger = uiEventLogger;
        this.mKeyguardStateController = keyguardStateController;
        this.mBatteryController = batteryController;
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public enum DozingUpdateUiEvent implements UiEventLogger.UiEventEnum {
        DOZING_UPDATE_NOTIFICATION(433),
        DOZING_UPDATE_SIGMOTION(434),
        DOZING_UPDATE_SENSOR_PICKUP(435),
        DOZING_UPDATE_SENSOR_DOUBLE_TAP(436),
        DOZING_UPDATE_SENSOR_LONG_SQUEEZE(437),
        DOZING_UPDATE_DOCKING(438),
        DOZING_UPDATE_SENSOR_WAKEUP(439),
        DOZING_UPDATE_SENSOR_WAKE_LOCKSCREEN(440),
        DOZING_UPDATE_SENSOR_TAP(441),
        DOZING_UPDATE_AUTH_TRIGGERED(657),
        DOZING_UPDATE_QUICK_PICKUP(708),
        DOZING_UPDATE_WAKE_TIMEOUT(794);
        
        private final int mId;

        DozingUpdateUiEvent(int i) {
            this.mId = i;
        }

        public static DozingUpdateUiEvent fromReason(int i) {
            switch (i) {
                case 1:
                    return DOZING_UPDATE_NOTIFICATION;
                case 2:
                    return DOZING_UPDATE_SIGMOTION;
                case 3:
                    return DOZING_UPDATE_SENSOR_PICKUP;
                case 4:
                    return DOZING_UPDATE_SENSOR_DOUBLE_TAP;
                case 5:
                    return DOZING_UPDATE_SENSOR_LONG_SQUEEZE;
                case FalsingManager.VERSION /* 6 */:
                    return DOZING_UPDATE_DOCKING;
                case 7:
                    return DOZING_UPDATE_SENSOR_WAKEUP;
                case 8:
                    return DOZING_UPDATE_SENSOR_WAKE_LOCKSCREEN;
                case 9:
                    return DOZING_UPDATE_SENSOR_TAP;
                case 10:
                    return DOZING_UPDATE_AUTH_TRIGGERED;
                case QSTileImpl.H.STALE /* 11 */:
                    return DOZING_UPDATE_QUICK_PICKUP;
                default:
                    return null;
            }
        }

        public final int getId() {
            return this.mId;
        }
    }

    public final boolean canPulse() {
        if (this.mMachine.getState() == DozeMachine.State.DOZE || this.mMachine.getState() == DozeMachine.State.DOZE_AOD || this.mMachine.getState() == DozeMachine.State.DOZE_AOD_DOCKED) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public final void destroy() {
        DozeSensors.TriggerSensor[] triggerSensorArr;
        DozeSensors dozeSensors = this.mDozeSensors;
        Objects.requireNonNull(dozeSensors);
        for (DozeSensors.TriggerSensor triggerSensor : dozeSensors.mTriggerSensors) {
            Objects.requireNonNull(triggerSensor);
            if (triggerSensor.mRequested) {
                triggerSensor.mRequested = false;
                triggerSensor.updateListening();
            }
        }
        dozeSensors.mProximitySensor.destroy();
        dozeSensors.mDevicePostureController.removeCallback(dozeSensors.mDevicePostureCallback);
        AuthController authController = dozeSensors.mAuthController;
        DozeSensors.AnonymousClass2 r0 = dozeSensors.mAuthControllerCallback;
        Objects.requireNonNull(authController);
        authController.mCallbacks.remove(r0);
        ProximityCheck proximityCheck = this.mProxCheck;
        Objects.requireNonNull(proximityCheck);
        proximityCheck.mSensor.destroy();
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public final void dump(PrintWriter printWriter) {
        DozeSensors.TriggerSensor[] triggerSensorArr;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m(" mAodInterruptRunnable=");
        m.append(this.mAodInterruptRunnable);
        printWriter.println(m.toString());
        printWriter.print(" notificationPulseTime=");
        printWriter.println(Formatter.formatShortElapsedTime(this.mContext, this.mNotificationPulseTime));
        printWriter.println(" pulsePending=" + this.mPulsePending);
        printWriter.println("DozeSensors:");
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter);
        indentingPrintWriter.increaseIndent();
        DozeSensors dozeSensors = this.mDozeSensors;
        Objects.requireNonNull(dozeSensors);
        StringBuilder sb = new StringBuilder();
        sb.append("mListening=");
        StringBuilder m2 = DozeScreenBrightness$$ExternalSyntheticOutline0.m(sb, dozeSensors.mListening, indentingPrintWriter, "mDevicePosture=");
        m2.append(DevicePostureController.devicePostureToString(dozeSensors.mDevicePosture));
        indentingPrintWriter.println(m2.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("mListeningTouchScreenSensors=");
        StringBuilder m3 = DozeScreenBrightness$$ExternalSyntheticOutline0.m(DozeScreenBrightness$$ExternalSyntheticOutline0.m(DozeScreenBrightness$$ExternalSyntheticOutline0.m(DozeScreenBrightness$$ExternalSyntheticOutline0.m(sb2, dozeSensors.mListeningTouchScreenSensors, indentingPrintWriter, "mSelectivelyRegisterProxSensors="), dozeSensors.mSelectivelyRegisterProxSensors, indentingPrintWriter, "mListeningProxSensors="), dozeSensors.mListeningProxSensors, indentingPrintWriter, "mScreenOffUdfpsEnabled="), dozeSensors.mScreenOffUdfpsEnabled, indentingPrintWriter, "mUdfpsEnrolled=");
        m3.append(dozeSensors.mUdfpsEnrolled);
        indentingPrintWriter.println(m3.toString());
        IndentingPrintWriter indentingPrintWriter2 = new IndentingPrintWriter(indentingPrintWriter);
        indentingPrintWriter2.increaseIndent();
        for (DozeSensors.TriggerSensor triggerSensor : dozeSensors.mTriggerSensors) {
            StringBuilder m4 = VendorAtomValue$$ExternalSyntheticOutline1.m("Sensor: ");
            m4.append(triggerSensor.toString());
            indentingPrintWriter2.println(m4.toString());
        }
        StringBuilder m5 = VendorAtomValue$$ExternalSyntheticOutline1.m("ProxSensor: ");
        m5.append(dozeSensors.mProximitySensor.toString());
        indentingPrintWriter2.println(m5.toString());
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public final void onScreenState(int i) {
        boolean z;
        boolean z2;
        boolean z3;
        DozeSensors dozeSensors = this.mDozeSensors;
        Objects.requireNonNull(dozeSensors);
        ProximitySensor proximitySensor = dozeSensors.mProximitySensor;
        boolean z4 = false;
        if (i == 3 || i == 4 || i == 1) {
            z = true;
        } else {
            z = false;
        }
        proximitySensor.setSecondarySafe(z);
        if (i == 3 || i == 4 || i == 1) {
            z2 = true;
        } else {
            z2 = false;
        }
        DozeSensors dozeSensors2 = this.mDozeSensors;
        if (!this.mWantProxSensor || !z2) {
            z3 = false;
        } else {
            z3 = true;
        }
        dozeSensors2.setProxListening(z3);
        DozeSensors dozeSensors3 = this.mDozeSensors;
        boolean z5 = this.mWantSensors;
        boolean z6 = this.mWantTouchScreenSensors;
        Objects.requireNonNull(dozeSensors3);
        if (!dozeSensors3.mSelectivelyRegisterProxSensors || z2) {
            z4 = true;
        }
        if (!(dozeSensors3.mListening == z5 && dozeSensors3.mListeningTouchScreenSensors == z6 && dozeSensors3.mListeningProxSensors == z4)) {
            dozeSensors3.mListening = z5;
            dozeSensors3.mListeningTouchScreenSensors = z6;
            dozeSensors3.mListeningProxSensors = z4;
            dozeSensors3.updateListening();
        }
        DozeTriggers$$ExternalSyntheticLambda1 dozeTriggers$$ExternalSyntheticLambda1 = this.mAodInterruptRunnable;
        if (dozeTriggers$$ExternalSyntheticLambda1 != null && i == 2) {
            dozeTriggers$$ExternalSyntheticLambda1.run();
            this.mAodInterruptRunnable = null;
        }
    }

    @VisibleForTesting
    public void onSensor(final int i, final float f, final float f2, final float[] fArr) {
        final boolean z;
        final boolean z2;
        final boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        final boolean z7;
        boolean z8;
        boolean z9;
        boolean z10;
        DozeParameters dozeParameters;
        DozeLog.SummaryStats summaryStats;
        boolean z11 = false;
        if (i == 4) {
            z = true;
        } else {
            z = false;
        }
        if (i == 9) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (i == 3) {
            z3 = true;
        } else {
            z3 = false;
        }
        if (i == 5) {
            z4 = true;
        } else {
            z4 = false;
        }
        if (i == 7) {
            z5 = true;
        } else {
            z5 = false;
        }
        if (i == 8) {
            z6 = true;
        } else {
            z6 = false;
        }
        if (i == 10) {
            z7 = true;
        } else {
            z7 = false;
        }
        if (i == 11) {
            z8 = true;
        } else {
            z8 = false;
        }
        if (z8 || ((z5 || z6) && fArr != null && fArr.length > 0 && fArr[0] != 0.0f)) {
            z9 = true;
        } else {
            z9 = false;
        }
        DozeMachine.State state = null;
        if (z5) {
            if (!this.mMachine.isExecutingTransition()) {
                state = this.mMachine.getState();
            }
            onWakeScreen(z9, state, i);
        } else if (z4) {
            requestPulse(i, true, null);
        } else if (!z6 && !z8) {
            proximityCheckThenCall(new Consumer() { // from class: com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda5
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DozeTriggers dozeTriggers = DozeTriggers.this;
                    int i2 = i;
                    boolean z12 = z;
                    boolean z13 = z2;
                    float f3 = f;
                    float f4 = f2;
                    boolean z14 = z3;
                    boolean z15 = z7;
                    float[] fArr2 = fArr;
                    Boolean bool = (Boolean) obj;
                    if (bool != null) {
                        Objects.requireNonNull(dozeTriggers);
                        if (bool.booleanValue()) {
                            dozeTriggers.mDozeLog.traceSensorEventDropped(i2, "prox reporting near");
                            return;
                        }
                    }
                    if (z12 || z13) {
                        if (!(f3 == -1.0f || f4 == -1.0f)) {
                            dozeTriggers.mDozeHost.onSlpiTap(f3, f4);
                        }
                        dozeTriggers.gentleWakeUp(i2);
                        return;
                    }
                    boolean z16 = true;
                    if (z14) {
                        Objects.requireNonNull(dozeTriggers);
                        if (!dozeTriggers.mKeyguardStateController.isOccluded() && !dozeTriggers.mBatteryController.isPluggedInWireless()) {
                            z16 = false;
                        }
                        if (z16) {
                            DozeLog dozeLog = dozeTriggers.mDozeLog;
                            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("keyguardOccluded=");
                            m.append(dozeTriggers.mKeyguardStateController.isOccluded());
                            m.append(" pluggedInWireless=");
                            m.append(dozeTriggers.mBatteryController.isPluggedInWireless());
                            dozeLog.traceSensorEventDropped(i2, m.toString());
                            return;
                        }
                        dozeTriggers.gentleWakeUp(i2);
                    } else if (z15) {
                        DozeMachine.State state2 = dozeTriggers.mMachine.getState();
                        if (state2 == DozeMachine.State.DOZE_AOD || state2 == DozeMachine.State.DOZE) {
                            dozeTriggers.mAodInterruptRunnable = new DozeTriggers$$ExternalSyntheticLambda1(dozeTriggers, f3, f4, fArr2);
                        }
                        dozeTriggers.requestPulse(10, true, null);
                    } else {
                        dozeTriggers.mDozeHost.extendPulse(i2);
                    }
                }
            }, true, i);
        } else if (z9) {
            requestPulse(i, true, null);
        }
        if (z3) {
            if (this.mKeyguardStateController.isOccluded() || this.mBatteryController.isPluggedInWireless()) {
                z10 = true;
            } else {
                z10 = false;
            }
            if (!z10) {
                long elapsedRealtime = SystemClock.elapsedRealtime() - this.mNotificationPulseTime;
                Objects.requireNonNull(this.mDozeParameters);
                if (elapsedRealtime < dozeParameters.getInt("doze.pickup.vibration.threshold", 2131492915)) {
                    z11 = true;
                }
                DozeLog dozeLog = this.mDozeLog;
                Objects.requireNonNull(dozeLog);
                DozeLogger dozeLogger = dozeLog.mLogger;
                Objects.requireNonNull(dozeLogger);
                LogBuffer logBuffer = dozeLogger.buffer;
                LogLevel logLevel = LogLevel.DEBUG;
                DozeLogger$logPickupWakeup$2 dozeLogger$logPickupWakeup$2 = DozeLogger$logPickupWakeup$2.INSTANCE;
                Objects.requireNonNull(logBuffer);
                if (!logBuffer.frozen) {
                    LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logPickupWakeup$2);
                    obtain.bool1 = z11;
                    logBuffer.push(obtain);
                }
                if (z11) {
                    summaryStats = dozeLog.mPickupPulseNearVibrationStats;
                } else {
                    summaryStats = dozeLog.mPickupPulseNotNearVibrationStats;
                }
                Objects.requireNonNull(summaryStats);
                summaryStats.mCount++;
            }
        }
    }

    public final void onWakeScreen(boolean z, final DozeMachine.State state, final int i) {
        boolean z2;
        DozeLog dozeLog = this.mDozeLog;
        Objects.requireNonNull(dozeLog);
        DozeLogger dozeLogger = dozeLog.mLogger;
        Objects.requireNonNull(dozeLogger);
        LogBuffer logBuffer = dozeLogger.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        DozeLogger$logWakeDisplay$2 dozeLogger$logWakeDisplay$2 = DozeLogger$logWakeDisplay$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logWakeDisplay$2);
            obtain.bool1 = z;
            obtain.int1 = i;
            logBuffer.push(obtain);
        }
        sWakeDisplaySensorState = z;
        boolean z3 = false;
        if (z) {
            proximityCheckThenCall(new Consumer() { // from class: com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda7
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DozeTriggers dozeTriggers = DozeTriggers.this;
                    DozeMachine.State state2 = state;
                    int i2 = i;
                    Boolean bool = (Boolean) obj;
                    Objects.requireNonNull(dozeTriggers);
                    if ((bool == null || !bool.booleanValue()) && state2 == DozeMachine.State.DOZE) {
                        dozeTriggers.mMachine.requestState(DozeMachine.State.DOZE_AOD);
                        Optional ofNullable = Optional.ofNullable(DozeTriggers.DozingUpdateUiEvent.fromReason(i2));
                        UiEventLogger uiEventLogger = dozeTriggers.mUiEventLogger;
                        Objects.requireNonNull(uiEventLogger);
                        ofNullable.ifPresent(new DozeTriggers$$ExternalSyntheticLambda3(uiEventLogger, 0));
                    }
                }
            }, false, i);
            return;
        }
        if (state == DozeMachine.State.DOZE_AOD_PAUSED) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (state == DozeMachine.State.DOZE_AOD_PAUSING) {
            z3 = true;
        }
        if (!z3 && !z2) {
            this.mMachine.requestState(DozeMachine.State.DOZE);
            this.mUiEventLogger.log(DozingUpdateUiEvent.DOZING_UPDATE_WAKE_TIMEOUT);
        }
    }

    public final void proximityCheckThenCall(final Consumer<Boolean> consumer, boolean z, final int i) {
        DozeSensors dozeSensors = this.mDozeSensors;
        Objects.requireNonNull(dozeSensors);
        Boolean isNear = dozeSensors.mProximitySensor.isNear();
        if (z) {
            consumer.accept(null);
        } else if (isNear != null) {
            consumer.accept(isNear);
        } else {
            final long uptimeMillis = SystemClock.uptimeMillis();
            ProximityCheck proximityCheck = this.mProxCheck;
            Consumer dozeTriggers$$ExternalSyntheticLambda6 = new Consumer() { // from class: com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda6
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    boolean z2;
                    DozeTriggers dozeTriggers = DozeTriggers.this;
                    long j = uptimeMillis;
                    int i2 = i;
                    Consumer consumer2 = consumer;
                    Boolean bool = (Boolean) obj;
                    Objects.requireNonNull(dozeTriggers);
                    long uptimeMillis2 = SystemClock.uptimeMillis();
                    DozeLog dozeLog = dozeTriggers.mDozeLog;
                    if (bool == null) {
                        z2 = false;
                    } else {
                        z2 = bool.booleanValue();
                    }
                    long j2 = uptimeMillis2 - j;
                    Objects.requireNonNull(dozeLog);
                    DozeLogger dozeLogger = dozeLog.mLogger;
                    Objects.requireNonNull(dozeLogger);
                    LogBuffer logBuffer = dozeLogger.buffer;
                    LogLevel logLevel = LogLevel.DEBUG;
                    DozeLogger$logProximityResult$2 dozeLogger$logProximityResult$2 = DozeLogger$logProximityResult$2.INSTANCE;
                    Objects.requireNonNull(logBuffer);
                    if (!logBuffer.frozen) {
                        LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logProximityResult$2);
                        obtain.bool1 = z2;
                        obtain.long1 = j2;
                        obtain.int1 = i2;
                        logBuffer.push(obtain);
                    }
                    DozeLog.SummaryStats summaryStats = dozeLog.mProxStats[i2][!z2 ? 1 : 0];
                    Objects.requireNonNull(summaryStats);
                    summaryStats.mCount++;
                    consumer2.accept(bool);
                    dozeTriggers.mWakeLock.release("DozeTriggers");
                }
            };
            Objects.requireNonNull(proximityCheck);
            if (!proximityCheck.mSensor.isLoaded()) {
                dozeTriggers$$ExternalSyntheticLambda6.accept(null);
            } else {
                proximityCheck.mCallbacks.add(dozeTriggers$$ExternalSyntheticLambda6);
                if (!proximityCheck.mRegistered.getAndSet(true)) {
                    proximityCheck.mSensor.register(proximityCheck.mListener);
                    proximityCheck.mDelayableExecutor.executeDelayed(proximityCheck, 500L);
                }
            }
            this.mWakeLock.acquire("DozeTriggers");
        }
    }

    public final void gentleWakeUp(int i) {
        Optional ofNullable = Optional.ofNullable(DozingUpdateUiEvent.fromReason(i));
        UiEventLogger uiEventLogger = this.mUiEventLogger;
        Objects.requireNonNull(uiEventLogger);
        ofNullable.ifPresent(new DozeTriggers$$ExternalSyntheticLambda2(uiEventLogger, 0));
        if (this.mDozeParameters.getDisplayNeedsBlanking()) {
            this.mDozeHost.setAodDimmingScrim(1.0f);
        }
        DozeMachine dozeMachine = this.mMachine;
        Objects.requireNonNull(dozeMachine);
        dozeMachine.mDozeService.requestWakeUp();
    }

    public final void requestPulse(final int i, boolean z, final ScreenDecorations$2$$ExternalSyntheticLambda0 screenDecorations$2$$ExternalSyntheticLambda0) {
        DozeMachine.State state;
        boolean z2;
        Assert.isMainThread();
        this.mDozeHost.extendPulse(i);
        if (this.mMachine.isExecutingTransition()) {
            state = null;
        } else {
            state = this.mMachine.getState();
        }
        if (state == DozeMachine.State.DOZE_PULSING && i == 8) {
            this.mMachine.requestState(DozeMachine.State.DOZE_PULSING_BRIGHT);
        } else if (this.mPulsePending || !this.mAllowPulseTriggers || !canPulse()) {
            if (this.mAllowPulseTriggers) {
                this.mDozeLog.tracePulseDropped(this.mPulsePending, state, this.mDozeHost.isPulsingBlocked());
            }
            if (screenDecorations$2$$ExternalSyntheticLambda0 != null) {
                screenDecorations$2$$ExternalSyntheticLambda0.run();
            }
        } else {
            this.mPulsePending = true;
            Consumer<Boolean> dozeTriggers$$ExternalSyntheticLambda8 = new Consumer() { // from class: com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda8
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DozeTriggers dozeTriggers = DozeTriggers.this;
                    Runnable runnable = screenDecorations$2$$ExternalSyntheticLambda0;
                    int i2 = i;
                    Boolean bool = (Boolean) obj;
                    if (bool != null) {
                        Objects.requireNonNull(dozeTriggers);
                        if (bool.booleanValue()) {
                            dozeTriggers.mDozeLog.tracePulseDropped("inPocket");
                            dozeTriggers.mPulsePending = false;
                            if (runnable != null) {
                                runnable.run();
                                return;
                            }
                            return;
                        }
                    }
                    dozeTriggers.mPulsePending = false;
                    if (dozeTriggers.mDozeHost.isPulsingBlocked() || !dozeTriggers.canPulse()) {
                        dozeTriggers.mDozeLog.tracePulseDropped(dozeTriggers.mPulsePending, dozeTriggers.mMachine.getState(), dozeTriggers.mDozeHost.isPulsingBlocked());
                        return;
                    }
                    DozeMachine dozeMachine = dozeTriggers.mMachine;
                    Objects.requireNonNull(dozeMachine);
                    Preconditions.checkState(!dozeMachine.isExecutingTransition());
                    dozeMachine.requestState(DozeMachine.State.DOZE_REQUEST_PULSE, i2);
                }
            };
            DozeParameters dozeParameters = this.mDozeParameters;
            Objects.requireNonNull(dozeParameters);
            if (!SystemProperties.getBoolean("doze.pulse.proxcheck", dozeParameters.mResources.getBoolean(2131034186)) || z) {
                z2 = true;
            } else {
                z2 = false;
            }
            proximityCheckThenCall(dozeTriggers$$ExternalSyntheticLambda8, z2, i);
            Optional ofNullable = Optional.ofNullable(DozingUpdateUiEvent.fromReason(i));
            UiEventLogger uiEventLogger = this.mUiEventLogger;
            Objects.requireNonNull(uiEventLogger);
            ofNullable.ifPresent(new QSPanel$$ExternalSyntheticLambda1(uiEventLogger, 1));
        }
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public final void transitionTo(DozeMachine.State state, DozeMachine.State state2) {
        boolean z;
        switch (state2.ordinal()) {
            case 1:
                this.mAodInterruptRunnable = null;
                sWakeDisplaySensorState = true;
                TriggerReceiver triggerReceiver = this.mBroadcastReceiver;
                BroadcastDispatcher broadcastDispatcher = this.mBroadcastDispatcher;
                Objects.requireNonNull(triggerReceiver);
                if (!triggerReceiver.mRegistered) {
                    IntentFilter intentFilter = new IntentFilter("com.android.systemui.doze.pulse");
                    intentFilter.addAction(UiModeManager.ACTION_ENTER_CAR_MODE);
                    intentFilter.addAction("android.intent.action.USER_SWITCHED");
                    broadcastDispatcher.registerReceiver(triggerReceiver, intentFilter);
                    triggerReceiver.mRegistered = true;
                }
                this.mDozeHost.addCallback(this.mHostCallback);
                this.mDockManager.addListener(this.mDockEventListener);
                DozeSensors dozeSensors = this.mDozeSensors;
                Objects.requireNonNull(dozeSensors);
                dozeSensors.mDebounceFrom = SystemClock.uptimeMillis();
                if (this.mUiModeManager.getCurrentModeType() == 3 || this.mDozeHost.isBlockingDoze() || !this.mDozeHost.isProvisioned()) {
                    this.mMachine.requestState(DozeMachine.State.FINISH);
                    break;
                }
                break;
            case 2:
            case 3:
                this.mAodInterruptRunnable = null;
                if (state2 != DozeMachine.State.DOZE) {
                    z = true;
                } else {
                    z = false;
                }
                this.mWantProxSensor = z;
                this.mWantSensors = true;
                this.mWantTouchScreenSensors = true;
                if (state2 == DozeMachine.State.DOZE_AOD && !sWakeDisplaySensorState) {
                    onWakeScreen(false, state2, 7);
                    break;
                }
                break;
            case 5:
            case FalsingManager.VERSION /* 6 */:
                this.mWantProxSensor = true;
                this.mWantTouchScreenSensors = false;
                break;
            case 7:
                DozeSensors dozeSensors2 = this.mDozeSensors;
                Objects.requireNonNull(dozeSensors2);
                dozeSensors2.mDebounceFrom = SystemClock.uptimeMillis();
                break;
            case 8:
                TriggerReceiver triggerReceiver2 = this.mBroadcastReceiver;
                BroadcastDispatcher broadcastDispatcher2 = this.mBroadcastDispatcher;
                Objects.requireNonNull(triggerReceiver2);
                if (triggerReceiver2.mRegistered) {
                    broadcastDispatcher2.unregisterReceiver(triggerReceiver2);
                    triggerReceiver2.mRegistered = false;
                }
                this.mDozeHost.removeCallback(this.mHostCallback);
                this.mDockManager.removeListener(this.mDockEventListener);
                DozeSensors dozeSensors3 = this.mDozeSensors;
                Objects.requireNonNull(dozeSensors3);
                if (dozeSensors3.mListening || dozeSensors3.mListeningTouchScreenSensors) {
                    dozeSensors3.mListening = false;
                    dozeSensors3.mListeningTouchScreenSensors = false;
                    dozeSensors3.updateListening();
                }
                this.mDozeSensors.setProxListening(false);
                this.mWantSensors = false;
                this.mWantProxSensor = false;
                this.mWantTouchScreenSensors = false;
                break;
            case 9:
            case 10:
                this.mWantProxSensor = true;
                break;
            case QSTileImpl.H.STALE /* 11 */:
                this.mWantProxSensor = false;
                this.mWantTouchScreenSensors = false;
                break;
        }
        DozeSensors dozeSensors4 = this.mDozeSensors;
        boolean z2 = this.mWantSensors;
        boolean z3 = this.mWantTouchScreenSensors;
        Objects.requireNonNull(dozeSensors4);
        if (dozeSensors4.mListening != z2 || dozeSensors4.mListeningTouchScreenSensors != z3) {
            dozeSensors4.mListening = z2;
            dozeSensors4.mListeningTouchScreenSensors = z3;
            dozeSensors4.updateListening();
        }
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public final void setDozeMachine(DozeMachine dozeMachine) {
        this.mMachine = dozeMachine;
    }
}
