package com.android.systemui.doze;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.util.Log;
import com.android.internal.util.Preconditions;
import com.android.systemui.doze.DozeHost;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.util.AlarmTimeout;
import com.android.systemui.util.Assert;
import com.android.systemui.util.wakelock.WakeLock;
import java.util.Calendar;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DozeUi implements DozeMachine.Part {
    public final boolean mCanAnimateTransition;
    public final Context mContext;
    public final DozeLog mDozeLog;
    public final DozeParameters mDozeParameters;
    public final Handler mHandler;
    public final DozeHost mHost;
    public final AnonymousClass1 mKeyguardVisibilityCallback;
    public long mLastTimeTickElapsed = 0;
    public DozeMachine mMachine;
    public final StatusBarStateController mStatusBarStateController;
    public final AlarmTimeout mTimeTicker;
    public final WakeLock mWakeLock;

    public final void scheduleTimeTick() {
        AlarmTimeout alarmTimeout = this.mTimeTicker;
        Objects.requireNonNull(alarmTimeout);
        if (!alarmTimeout.mScheduled) {
            long currentTimeMillis = System.currentTimeMillis();
            Calendar instance = Calendar.getInstance();
            instance.setTimeInMillis(currentTimeMillis);
            instance.set(14, 0);
            instance.set(13, 0);
            instance.add(12, 1);
            long timeInMillis = instance.getTimeInMillis() - System.currentTimeMillis();
            if (this.mTimeTicker.schedule(timeInMillis)) {
                DozeLog dozeLog = this.mDozeLog;
                long j = timeInMillis + currentTimeMillis;
                Objects.requireNonNull(dozeLog);
                DozeLogger dozeLogger = dozeLog.mLogger;
                Objects.requireNonNull(dozeLogger);
                LogBuffer logBuffer = dozeLogger.buffer;
                LogLevel logLevel = LogLevel.DEBUG;
                DozeLogger$logTimeTickScheduled$2 dozeLogger$logTimeTickScheduled$2 = DozeLogger$logTimeTickScheduled$2.INSTANCE;
                Objects.requireNonNull(logBuffer);
                if (!logBuffer.frozen) {
                    LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logTimeTickScheduled$2);
                    obtain.long1 = currentTimeMillis;
                    obtain.long2 = j;
                    logBuffer.push(obtain);
                }
            }
            this.mLastTimeTickElapsed = SystemClock.elapsedRealtime();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.doze.DozeUi$1, com.android.keyguard.KeyguardUpdateMonitorCallback] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public DozeUi(android.content.Context r4, android.app.AlarmManager r5, com.android.systemui.util.wakelock.WakeLock r6, com.android.systemui.doze.DozeHost r7, android.os.Handler r8, com.android.systemui.statusbar.phone.DozeParameters r9, com.android.keyguard.KeyguardUpdateMonitor r10, com.android.systemui.plugins.statusbar.StatusBarStateController r11, com.android.systemui.doze.DozeLog r12) {
        /*
            r3 = this;
            r3.<init>()
            com.android.systemui.doze.DozeUi$1 r0 = new com.android.systemui.doze.DozeUi$1
            r0.<init>()
            r3.mKeyguardVisibilityCallback = r0
            r1 = 0
            r3.mLastTimeTickElapsed = r1
            r3.mContext = r4
            r3.mWakeLock = r6
            r3.mHost = r7
            r3.mHandler = r8
            boolean r4 = r9.getDisplayNeedsBlanking()
            r4 = r4 ^ 1
            r3.mCanAnimateTransition = r4
            r3.mDozeParameters = r9
            com.android.systemui.util.AlarmTimeout r4 = new com.android.systemui.util.AlarmTimeout
            com.android.systemui.doze.DozeUi$$ExternalSyntheticLambda0 r6 = new com.android.systemui.doze.DozeUi$$ExternalSyntheticLambda0
            r6.<init>()
            java.lang.String r7 = "doze_time_tick"
            r4.<init>(r5, r6, r7, r8)
            r3.mTimeTicker = r4
            r10.registerCallback(r0)
            r3.mDozeLog = r12
            r3.mStatusBarStateController = r11
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.doze.DozeUi.<init>(android.content.Context, android.app.AlarmManager, com.android.systemui.util.wakelock.WakeLock, com.android.systemui.doze.DozeHost, android.os.Handler, com.android.systemui.statusbar.phone.DozeParameters, com.android.keyguard.KeyguardUpdateMonitor, com.android.systemui.plugins.statusbar.StatusBarStateController, com.android.systemui.doze.DozeLog):void");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v9, types: [com.android.systemui.doze.DozeUi$2] */
    @Override // com.android.systemui.doze.DozeMachine.Part
    public final void transitionTo(DozeMachine.State state, DozeMachine.State state2) {
        boolean z;
        int ordinal = state2.ordinal();
        boolean z2 = false;
        if (ordinal != 1) {
            if (ordinal != 2) {
                if (ordinal != 3) {
                    if (ordinal != 4) {
                        switch (ordinal) {
                            case 8:
                                this.mHost.stopDozing();
                                AlarmTimeout alarmTimeout = this.mTimeTicker;
                                Objects.requireNonNull(alarmTimeout);
                                if (alarmTimeout.mScheduled) {
                                    verifyLastTimeTick();
                                    this.mTimeTicker.cancel();
                                    break;
                                }
                                break;
                            case 10:
                                scheduleTimeTick();
                                break;
                        }
                    } else {
                        scheduleTimeTick();
                        DozeMachine dozeMachine = this.mMachine;
                        Objects.requireNonNull(dozeMachine);
                        Assert.isMainThread();
                        DozeMachine.State state3 = dozeMachine.mState;
                        if (state3 == DozeMachine.State.DOZE_REQUEST_PULSE || state3 == DozeMachine.State.DOZE_PULSING || state3 == DozeMachine.State.DOZE_PULSING_BRIGHT || state3 == DozeMachine.State.DOZE_PULSE_DONE) {
                            z = true;
                        } else {
                            z = false;
                        }
                        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("must be in pulsing state, but is ");
                        m.append(dozeMachine.mState);
                        Preconditions.checkState(z, m.toString());
                        final int i = dozeMachine.mPulseReason;
                        this.mHost.pulseWhileDozing(new DozeHost.PulseCallback() { // from class: com.android.systemui.doze.DozeUi.2
                            @Override // com.android.systemui.doze.DozeHost.PulseCallback
                            public final void onPulseFinished() {
                                DozeUi.this.mMachine.requestState(DozeMachine.State.DOZE_PULSE_DONE);
                            }

                            @Override // com.android.systemui.doze.DozeHost.PulseCallback
                            public final void onPulseStarted() {
                                DozeMachine.State state4;
                                try {
                                    DozeMachine dozeMachine2 = DozeUi.this.mMachine;
                                    if (i == 8) {
                                        state4 = DozeMachine.State.DOZE_PULSING_BRIGHT;
                                    } else {
                                        state4 = DozeMachine.State.DOZE_PULSING;
                                    }
                                    dozeMachine2.requestState(state4);
                                } catch (IllegalStateException unused) {
                                }
                            }
                        }, i);
                    }
                }
                if (state == DozeMachine.State.DOZE_AOD_PAUSED || state == DozeMachine.State.DOZE) {
                    this.mHost.dozeTimeTick();
                    Handler handler = this.mHandler;
                    WakeLock wakeLock = this.mWakeLock;
                    DozeHost dozeHost = this.mHost;
                    Objects.requireNonNull(dozeHost);
                    handler.postDelayed(wakeLock.wrap(new DozeUi$$ExternalSyntheticLambda1(dozeHost, 0)), 500L);
                }
                scheduleTimeTick();
            }
            AlarmTimeout alarmTimeout2 = this.mTimeTicker;
            Objects.requireNonNull(alarmTimeout2);
            if (alarmTimeout2.mScheduled) {
                verifyLastTimeTick();
                this.mTimeTicker.cancel();
            }
        } else {
            this.mHost.startDozing();
        }
        switch (state2.ordinal()) {
            case 4:
            case 5:
            case FalsingManager.VERSION /* 6 */:
            case 7:
                this.mHost.setAnimateWakeup(true);
                return;
            case 8:
                return;
            default:
                DozeHost dozeHost2 = this.mHost;
                if (this.mCanAnimateTransition && this.mDozeParameters.getAlwaysOn()) {
                    z2 = true;
                }
                dozeHost2.setAnimateWakeup(z2);
                return;
        }
    }

    public final void verifyLastTimeTick() {
        long elapsedRealtime = SystemClock.elapsedRealtime() - this.mLastTimeTickElapsed;
        if (elapsedRealtime > 90000) {
            String formatShortElapsedTime = Formatter.formatShortElapsedTime(this.mContext, elapsedRealtime);
            DozeLog dozeLog = this.mDozeLog;
            Objects.requireNonNull(dozeLog);
            DozeLogger dozeLogger = dozeLog.mLogger;
            Objects.requireNonNull(dozeLogger);
            LogBuffer logBuffer = dozeLogger.buffer;
            LogLevel logLevel = LogLevel.ERROR;
            DozeLogger$logMissedTick$2 dozeLogger$logMissedTick$2 = DozeLogger$logMissedTick$2.INSTANCE;
            Objects.requireNonNull(logBuffer);
            if (!logBuffer.frozen) {
                LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logMissedTick$2);
                obtain.str1 = formatShortElapsedTime;
                logBuffer.push(obtain);
            }
            Log.e("DozeMachine", "Missed AOD time tick by " + formatShortElapsedTime);
        }
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public final void setDozeMachine(DozeMachine dozeMachine) {
        this.mMachine = dozeMachine;
    }
}
