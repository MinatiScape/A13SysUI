package com.android.systemui.doze;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.util.TimeUtils;
import android.view.Display;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.Dumpable;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DozeLog implements Dumpable {
    public final DozeLogger mLogger;
    public boolean mPulsing;
    public final AnonymousClass1 mKeyguardCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.doze.DozeLog.1
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onEmergencyCallAction() {
            DozeLog dozeLog = DozeLog.this;
            Objects.requireNonNull(dozeLog);
            DozeLogger dozeLogger = dozeLog.mLogger;
            Objects.requireNonNull(dozeLogger);
            LogBuffer logBuffer = dozeLogger.buffer;
            LogLevel logLevel = LogLevel.INFO;
            DozeLogger$logEmergencyCall$2 dozeLogger$logEmergencyCall$2 = DozeLogger$logEmergencyCall$2.INSTANCE;
            Objects.requireNonNull(logBuffer);
            if (!logBuffer.frozen) {
                logBuffer.push(logBuffer.obtain("DozeLog", logLevel, dozeLogger$logEmergencyCall$2));
            }
            SummaryStats summaryStats = dozeLog.mEmergencyCallStats;
            Objects.requireNonNull(summaryStats);
            summaryStats.mCount++;
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onFinishedGoingToSleep(int i) {
            DozeLog dozeLog = DozeLog.this;
            Objects.requireNonNull(dozeLog);
            DozeLogger dozeLogger = dozeLog.mLogger;
            Objects.requireNonNull(dozeLogger);
            LogBuffer logBuffer = dozeLogger.buffer;
            LogLevel logLevel = LogLevel.INFO;
            DozeLogger$logScreenOff$2 dozeLogger$logScreenOff$2 = DozeLogger$logScreenOff$2.INSTANCE;
            Objects.requireNonNull(logBuffer);
            if (!logBuffer.frozen) {
                LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logScreenOff$2);
                obtain.int1 = i;
                logBuffer.push(obtain);
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onKeyguardBouncerChanged(boolean z) {
            DozeLog dozeLog = DozeLog.this;
            Objects.requireNonNull(dozeLog);
            DozeLogger dozeLogger = dozeLog.mLogger;
            Objects.requireNonNull(dozeLogger);
            LogBuffer logBuffer = dozeLogger.buffer;
            LogLevel logLevel = LogLevel.INFO;
            DozeLogger$logKeyguardBouncerChanged$2 dozeLogger$logKeyguardBouncerChanged$2 = DozeLogger$logKeyguardBouncerChanged$2.INSTANCE;
            Objects.requireNonNull(logBuffer);
            if (!logBuffer.frozen) {
                LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logKeyguardBouncerChanged$2);
                obtain.bool1 = z;
                logBuffer.push(obtain);
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onKeyguardVisibilityChanged(boolean z) {
            DozeLog dozeLog = DozeLog.this;
            Objects.requireNonNull(dozeLog);
            DozeLogger dozeLogger = dozeLog.mLogger;
            Objects.requireNonNull(dozeLogger);
            LogBuffer logBuffer = dozeLogger.buffer;
            LogLevel logLevel = LogLevel.INFO;
            DozeLogger$logKeyguardVisibilityChange$2 dozeLogger$logKeyguardVisibilityChange$2 = DozeLogger$logKeyguardVisibilityChange$2.INSTANCE;
            Objects.requireNonNull(logBuffer);
            if (!logBuffer.frozen) {
                LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logKeyguardVisibilityChange$2);
                obtain.bool1 = z;
                logBuffer.push(obtain);
            }
            if (!z) {
                dozeLog.mPulsing = false;
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onStartedWakingUp() {
            SummaryStats summaryStats;
            DozeLog dozeLog = DozeLog.this;
            Objects.requireNonNull(dozeLog);
            DozeLogger dozeLogger = dozeLog.mLogger;
            boolean z = dozeLog.mPulsing;
            Objects.requireNonNull(dozeLogger);
            LogBuffer logBuffer = dozeLogger.buffer;
            LogLevel logLevel = LogLevel.INFO;
            DozeLogger$logScreenOn$2 dozeLogger$logScreenOn$2 = DozeLogger$logScreenOn$2.INSTANCE;
            Objects.requireNonNull(logBuffer);
            if (!logBuffer.frozen) {
                LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logScreenOn$2);
                obtain.bool1 = z;
                logBuffer.push(obtain);
            }
            if (dozeLog.mPulsing) {
                summaryStats = dozeLog.mScreenOnPulsingStats;
            } else {
                summaryStats = dozeLog.mScreenOnNotPulsingStats;
            }
            Objects.requireNonNull(summaryStats);
            summaryStats.mCount++;
            dozeLog.mPulsing = false;
        }
    };
    public long mSince = System.currentTimeMillis();
    public SummaryStats mPickupPulseNearVibrationStats = new SummaryStats();
    public SummaryStats mPickupPulseNotNearVibrationStats = new SummaryStats();
    public SummaryStats mNotificationPulseStats = new SummaryStats();
    public SummaryStats mScreenOnPulsingStats = new SummaryStats();
    public SummaryStats mScreenOnNotPulsingStats = new SummaryStats();
    public SummaryStats mEmergencyCallStats = new SummaryStats();
    public SummaryStats[][] mProxStats = (SummaryStats[][]) Array.newInstance(SummaryStats.class, 12, 2);

    /* loaded from: classes.dex */
    public class SummaryStats {
        public int mCount;

        public SummaryStats() {
        }

        public final void dump(PrintWriter printWriter, String str) {
            if (this.mCount != 0) {
                printWriter.print("    ");
                printWriter.print(str);
                printWriter.print(": n=");
                printWriter.print(this.mCount);
                printWriter.print(" (");
                printWriter.print((this.mCount / (System.currentTimeMillis() - DozeLog.this.mSince)) * 1000.0d * 60.0d * 60.0d);
                printWriter.print("/hr)");
                printWriter.println();
            }
        }
    }

    public final void tracePulseDropped(boolean z, DozeMachine.State state, boolean z2) {
        DozeLogger dozeLogger = this.mLogger;
        Objects.requireNonNull(dozeLogger);
        LogBuffer logBuffer = dozeLogger.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logPulseDropped$2 dozeLogger$logPulseDropped$2 = DozeLogger$logPulseDropped$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logPulseDropped$2);
            obtain.bool1 = z;
            obtain.str1 = state.name();
            obtain.bool2 = z2;
            logBuffer.push(obtain);
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        synchronized (DozeLog.class) {
            printWriter.print("  Doze summary stats (for ");
            TimeUtils.formatDuration(System.currentTimeMillis() - this.mSince, printWriter);
            printWriter.println("):");
            this.mPickupPulseNearVibrationStats.dump(printWriter, "Pickup pulse (near vibration)");
            this.mPickupPulseNotNearVibrationStats.dump(printWriter, "Pickup pulse (not near vibration)");
            this.mNotificationPulseStats.dump(printWriter, "Notification pulse");
            this.mScreenOnPulsingStats.dump(printWriter, "Screen on (pulsing)");
            this.mScreenOnNotPulsingStats.dump(printWriter, "Screen on (not pulsing)");
            this.mEmergencyCallStats.dump(printWriter, "Emergency call");
            for (int i = 0; i < 12; i++) {
                String reasonToString = reasonToString(i);
                this.mProxStats[i][0].dump(printWriter, "Proximity near (" + reasonToString + ")");
                this.mProxStats[i][1].dump(printWriter, "Proximity far (" + reasonToString + ")");
            }
        }
    }

    public final void traceDisplayStateDelayedByUdfps(int i) {
        DozeLogger dozeLogger = this.mLogger;
        Objects.requireNonNull(dozeLogger);
        LogBuffer logBuffer = dozeLogger.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logDisplayStateDelayedByUdfps$2 dozeLogger$logDisplayStateDelayedByUdfps$2 = DozeLogger$logDisplayStateDelayedByUdfps$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logDisplayStateDelayedByUdfps$2);
            obtain.str1 = Display.stateToString(i);
            logBuffer.push(obtain);
        }
    }

    public final void traceDozeScreenBrightness(int i) {
        DozeLogger dozeLogger = this.mLogger;
        Objects.requireNonNull(dozeLogger);
        LogBuffer logBuffer = dozeLogger.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logDozeScreenBrightness$2 dozeLogger$logDozeScreenBrightness$2 = DozeLogger$logDozeScreenBrightness$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logDozeScreenBrightness$2);
            obtain.int1 = i;
            logBuffer.push(obtain);
        }
    }

    public final void traceDozing(boolean z) {
        DozeLogger dozeLogger = this.mLogger;
        Objects.requireNonNull(dozeLogger);
        LogBuffer logBuffer = dozeLogger.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logDozing$2 dozeLogger$logDozing$2 = DozeLogger$logDozing$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logDozing$2);
            obtain.bool1 = z;
            logBuffer.push(obtain);
        }
        this.mPulsing = false;
    }

    public final void traceDozingChanged(boolean z) {
        DozeLogger dozeLogger = this.mLogger;
        Objects.requireNonNull(dozeLogger);
        LogBuffer logBuffer = dozeLogger.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logDozingChanged$2 dozeLogger$logDozingChanged$2 = DozeLogger$logDozingChanged$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logDozingChanged$2);
            obtain.bool1 = z;
            logBuffer.push(obtain);
        }
    }

    public final void traceDozingSuppressed(boolean z) {
        DozeLogger dozeLogger = this.mLogger;
        Objects.requireNonNull(dozeLogger);
        LogBuffer logBuffer = dozeLogger.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logDozingSuppressed$2 dozeLogger$logDozingSuppressed$2 = DozeLogger$logDozingSuppressed$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logDozingSuppressed$2);
            obtain.bool1 = z;
            logBuffer.push(obtain);
        }
    }

    public final void traceFling(boolean z, boolean z2, boolean z3) {
        DozeLogger dozeLogger = this.mLogger;
        Objects.requireNonNull(dozeLogger);
        LogBuffer logBuffer = dozeLogger.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        DozeLogger$logFling$2 dozeLogger$logFling$2 = DozeLogger$logFling$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logFling$2);
            obtain.bool1 = z;
            obtain.bool2 = z2;
            obtain.bool3 = true;
            obtain.bool4 = z3;
            logBuffer.push(obtain);
        }
    }

    public final void tracePostureChanged(int i, String str) {
        DozeLogger dozeLogger = this.mLogger;
        Objects.requireNonNull(dozeLogger);
        LogBuffer logBuffer = dozeLogger.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logPostureChanged$2 dozeLogger$logPostureChanged$2 = DozeLogger$logPostureChanged$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logPostureChanged$2);
            obtain.int1 = i;
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
    }

    public final void tracePulseFinish() {
        DozeLogger dozeLogger = this.mLogger;
        Objects.requireNonNull(dozeLogger);
        LogBuffer logBuffer = dozeLogger.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logPulseFinish$2 dozeLogger$logPulseFinish$2 = DozeLogger$logPulseFinish$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            logBuffer.push(logBuffer.obtain("DozeLog", logLevel, dozeLogger$logPulseFinish$2));
        }
        this.mPulsing = false;
    }

    public final void tracePulseStart(int i) {
        DozeLogger dozeLogger = this.mLogger;
        Objects.requireNonNull(dozeLogger);
        LogBuffer logBuffer = dozeLogger.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logPulseStart$2 dozeLogger$logPulseStart$2 = DozeLogger$logPulseStart$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logPulseStart$2);
            obtain.int1 = i;
            logBuffer.push(obtain);
        }
        this.mPulsing = true;
    }

    public final void tracePulseTouchDisabledByProx(boolean z) {
        DozeLogger dozeLogger = this.mLogger;
        Objects.requireNonNull(dozeLogger);
        LogBuffer logBuffer = dozeLogger.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        DozeLogger$logPulseTouchDisabledByProx$2 dozeLogger$logPulseTouchDisabledByProx$2 = DozeLogger$logPulseTouchDisabledByProx$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logPulseTouchDisabledByProx$2);
            obtain.bool1 = z;
            logBuffer.push(obtain);
        }
    }

    public final void traceSensor(int i) {
        DozeLogger dozeLogger = this.mLogger;
        Objects.requireNonNull(dozeLogger);
        LogBuffer logBuffer = dozeLogger.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        DozeLogger$logSensorTriggered$2 dozeLogger$logSensorTriggered$2 = DozeLogger$logSensorTriggered$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logSensorTriggered$2);
            obtain.int1 = i;
            logBuffer.push(obtain);
        }
    }

    public final void traceSensorEventDropped(int i, String str) {
        DozeLogger dozeLogger = this.mLogger;
        Objects.requireNonNull(dozeLogger);
        LogBuffer logBuffer = dozeLogger.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logSensorEventDropped$2 dozeLogger$logSensorEventDropped$2 = DozeLogger$logSensorEventDropped$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logSensorEventDropped$2);
            obtain.int1 = i;
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
    }

    public final void traceSetAodDimmingScrim(float f) {
        DozeLogger dozeLogger = this.mLogger;
        long j = f;
        Objects.requireNonNull(dozeLogger);
        LogBuffer logBuffer = dozeLogger.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logSetAodDimmingScrim$2 dozeLogger$logSetAodDimmingScrim$2 = DozeLogger$logSetAodDimmingScrim$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logSetAodDimmingScrim$2);
            obtain.long1 = j;
            logBuffer.push(obtain);
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.doze.DozeLog$1] */
    public DozeLog(KeyguardUpdateMonitor keyguardUpdateMonitor, DumpManager dumpManager, DozeLogger dozeLogger) {
        this.mLogger = dozeLogger;
        for (int i = 0; i < 12; i++) {
            SummaryStats[][] summaryStatsArr = this.mProxStats;
            summaryStatsArr[i][0] = new SummaryStats();
            summaryStatsArr[i][1] = new SummaryStats();
        }
        if (keyguardUpdateMonitor != null) {
            keyguardUpdateMonitor.registerCallback(this.mKeyguardCallback);
        }
        dumpManager.registerDumpable("DumpStats", this);
    }

    public static String reasonToString(int i) {
        switch (i) {
            case 0:
                return "intent";
            case 1:
                return "notification";
            case 2:
                return "sigmotion";
            case 3:
                return "pickup";
            case 4:
                return "doubletap";
            case 5:
                return "longpress";
            case FalsingManager.VERSION /* 6 */:
                return "docking";
            case 7:
                return "presence-wakeup";
            case 8:
                return "reach-wakelockscreen";
            case 9:
                return "tap";
            case 10:
                return "udfps";
            case QSTileImpl.H.STALE /* 11 */:
                return "quickPickup";
            default:
                throw new IllegalArgumentException(VendorAtomValue$$ExternalSyntheticOutline0.m("invalid reason: ", i));
        }
    }

    public final void tracePulseDropped(String str) {
        DozeLogger dozeLogger = this.mLogger;
        Objects.requireNonNull(dozeLogger);
        LogBuffer logBuffer = dozeLogger.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logPulseDropped$4 dozeLogger$logPulseDropped$4 = DozeLogger$logPulseDropped$4.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logPulseDropped$4);
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
    }
}
