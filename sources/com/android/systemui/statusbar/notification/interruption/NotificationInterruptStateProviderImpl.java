package com.android.systemui.statusbar.notification.interruption;

import android.app.Notification;
import android.content.ContentResolver;
import android.hardware.display.AmbientDisplayConfiguration;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.service.dreams.IDreamManager;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationFilter;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class NotificationInterruptStateProviderImpl implements NotificationInterruptStateProvider {
    public final AmbientDisplayConfiguration mAmbientDisplayConfiguration;
    public final BatteryController mBatteryController;
    public final ContentResolver mContentResolver;
    public final IDreamManager mDreamManager;
    public final HeadsUpManager mHeadsUpManager;
    public final AnonymousClass1 mHeadsUpObserver;
    public final NotificationInterruptLogger mLogger;
    public final NotificationFilter mNotificationFilter;
    public final PowerManager mPowerManager;
    public final StatusBarStateController mStatusBarStateController;
    public final ArrayList mSuppressors = new ArrayList();
    @VisibleForTesting
    public boolean mUseHeadsUp = false;

    @Override // com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider
    public final void addSuppressor(NotificationInterruptSuppressor notificationInterruptSuppressor) {
        this.mSuppressors.add(notificationInterruptSuppressor);
    }

    public final boolean canAlertCommon(NotificationEntry notificationEntry) {
        boolean z;
        LogLevel logLevel = LogLevel.DEBUG;
        Objects.requireNonNull(notificationEntry);
        StatusBarNotification statusBarNotification = notificationEntry.mSbn;
        if (this.mNotificationFilter.shouldFilterOut(notificationEntry)) {
            NotificationInterruptLogger notificationInterruptLogger = this.mLogger;
            Objects.requireNonNull(notificationInterruptLogger);
            LogBuffer logBuffer = notificationInterruptLogger.hunBuffer;
            NotificationInterruptLogger$logNoAlertingFilteredOut$2 notificationInterruptLogger$logNoAlertingFilteredOut$2 = NotificationInterruptLogger$logNoAlertingFilteredOut$2.INSTANCE;
            Objects.requireNonNull(logBuffer);
            if (!logBuffer.frozen) {
                LogMessageImpl obtain = logBuffer.obtain("InterruptionStateProvider", logLevel, notificationInterruptLogger$logNoAlertingFilteredOut$2);
                obtain.str1 = statusBarNotification.getKey();
                logBuffer.push(obtain);
            }
            return false;
        } else if (!statusBarNotification.isGroup() || !statusBarNotification.getNotification().suppressAlertingDueToGrouping()) {
            for (int i = 0; i < this.mSuppressors.size(); i++) {
                if (((NotificationInterruptSuppressor) this.mSuppressors.get(i)).suppressInterruptions()) {
                    this.mLogger.logNoAlertingSuppressedBy(statusBarNotification, (NotificationInterruptSuppressor) this.mSuppressors.get(i), false);
                    return false;
                }
            }
            if (SystemClock.elapsedRealtime() < notificationEntry.lastFullScreenIntentLaunchTime + 2000) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                return true;
            }
            NotificationInterruptLogger notificationInterruptLogger2 = this.mLogger;
            Objects.requireNonNull(notificationInterruptLogger2);
            LogBuffer logBuffer2 = notificationInterruptLogger2.hunBuffer;
            NotificationInterruptLogger$logNoAlertingRecentFullscreen$2 notificationInterruptLogger$logNoAlertingRecentFullscreen$2 = NotificationInterruptLogger$logNoAlertingRecentFullscreen$2.INSTANCE;
            Objects.requireNonNull(logBuffer2);
            if (!logBuffer2.frozen) {
                LogMessageImpl obtain2 = logBuffer2.obtain("InterruptionStateProvider", logLevel, notificationInterruptLogger$logNoAlertingRecentFullscreen$2);
                obtain2.str1 = statusBarNotification.getKey();
                logBuffer2.push(obtain2);
            }
            return false;
        } else {
            NotificationInterruptLogger notificationInterruptLogger3 = this.mLogger;
            Objects.requireNonNull(notificationInterruptLogger3);
            LogBuffer logBuffer3 = notificationInterruptLogger3.hunBuffer;
            NotificationInterruptLogger$logNoAlertingGroupAlertBehavior$2 notificationInterruptLogger$logNoAlertingGroupAlertBehavior$2 = NotificationInterruptLogger$logNoAlertingGroupAlertBehavior$2.INSTANCE;
            Objects.requireNonNull(logBuffer3);
            if (!logBuffer3.frozen) {
                LogMessageImpl obtain3 = logBuffer3.obtain("InterruptionStateProvider", logLevel, notificationInterruptLogger$logNoAlertingGroupAlertBehavior$2);
                obtain3.str1 = statusBarNotification.getKey();
                logBuffer3.push(obtain3);
            }
            return false;
        }
    }

    @Override // com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider
    public final boolean shouldBubbleUp(NotificationEntry notificationEntry) {
        LogLevel logLevel = LogLevel.DEBUG;
        Objects.requireNonNull(notificationEntry);
        StatusBarNotification statusBarNotification = notificationEntry.mSbn;
        if (!canAlertCommon(notificationEntry) || !canAlertAwakeCommon(notificationEntry)) {
            return false;
        }
        if (!notificationEntry.mRanking.canBubble()) {
            NotificationInterruptLogger notificationInterruptLogger = this.mLogger;
            Objects.requireNonNull(notificationInterruptLogger);
            LogBuffer logBuffer = notificationInterruptLogger.notifBuffer;
            NotificationInterruptLogger$logNoBubbleNotAllowed$2 notificationInterruptLogger$logNoBubbleNotAllowed$2 = NotificationInterruptLogger$logNoBubbleNotAllowed$2.INSTANCE;
            Objects.requireNonNull(logBuffer);
            if (!logBuffer.frozen) {
                LogMessageImpl obtain = logBuffer.obtain("InterruptionStateProvider", logLevel, notificationInterruptLogger$logNoBubbleNotAllowed$2);
                obtain.str1 = statusBarNotification.getKey();
                logBuffer.push(obtain);
            }
            return false;
        }
        Notification.BubbleMetadata bubbleMetadata = notificationEntry.mBubbleMetadata;
        if (bubbleMetadata != null && (bubbleMetadata.getShortcutId() != null || notificationEntry.mBubbleMetadata.getIntent() != null)) {
            return true;
        }
        NotificationInterruptLogger notificationInterruptLogger2 = this.mLogger;
        Objects.requireNonNull(notificationInterruptLogger2);
        LogBuffer logBuffer2 = notificationInterruptLogger2.notifBuffer;
        NotificationInterruptLogger$logNoBubbleNoMetadata$2 notificationInterruptLogger$logNoBubbleNoMetadata$2 = NotificationInterruptLogger$logNoBubbleNoMetadata$2.INSTANCE;
        Objects.requireNonNull(logBuffer2);
        if (!logBuffer2.frozen) {
            LogMessageImpl obtain2 = logBuffer2.obtain("InterruptionStateProvider", logLevel, notificationInterruptLogger$logNoBubbleNoMetadata$2);
            obtain2.str1 = statusBarNotification.getKey();
            logBuffer2.push(obtain2);
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider
    public final boolean shouldHeadsUp(NotificationEntry notificationEntry) {
        boolean z;
        boolean z2;
        boolean z3;
        LogLevel logLevel = LogLevel.DEBUG;
        if (this.mStatusBarStateController.isDozing()) {
            Objects.requireNonNull(notificationEntry);
            StatusBarNotification statusBarNotification = notificationEntry.mSbn;
            if (!this.mAmbientDisplayConfiguration.pulseOnNotificationEnabled(-2)) {
                NotificationInterruptLogger notificationInterruptLogger = this.mLogger;
                Objects.requireNonNull(notificationInterruptLogger);
                LogBuffer logBuffer = notificationInterruptLogger.hunBuffer;
                NotificationInterruptLogger$logNoPulsingSettingDisabled$2 notificationInterruptLogger$logNoPulsingSettingDisabled$2 = NotificationInterruptLogger$logNoPulsingSettingDisabled$2.INSTANCE;
                Objects.requireNonNull(logBuffer);
                if (!logBuffer.frozen) {
                    LogMessageImpl obtain = logBuffer.obtain("InterruptionStateProvider", logLevel, notificationInterruptLogger$logNoPulsingSettingDisabled$2);
                    obtain.str1 = statusBarNotification.getKey();
                    logBuffer.push(obtain);
                }
            } else if (this.mBatteryController.isAodPowerSave()) {
                NotificationInterruptLogger notificationInterruptLogger2 = this.mLogger;
                Objects.requireNonNull(notificationInterruptLogger2);
                LogBuffer logBuffer2 = notificationInterruptLogger2.hunBuffer;
                NotificationInterruptLogger$logNoPulsingBatteryDisabled$2 notificationInterruptLogger$logNoPulsingBatteryDisabled$2 = NotificationInterruptLogger$logNoPulsingBatteryDisabled$2.INSTANCE;
                Objects.requireNonNull(logBuffer2);
                if (!logBuffer2.frozen) {
                    LogMessageImpl obtain2 = logBuffer2.obtain("InterruptionStateProvider", logLevel, notificationInterruptLogger$logNoPulsingBatteryDisabled$2);
                    obtain2.str1 = statusBarNotification.getKey();
                    logBuffer2.push(obtain2);
                }
            } else if (!canAlertCommon(notificationEntry)) {
                NotificationInterruptLogger notificationInterruptLogger3 = this.mLogger;
                Objects.requireNonNull(notificationInterruptLogger3);
                LogBuffer logBuffer3 = notificationInterruptLogger3.hunBuffer;
                NotificationInterruptLogger$logNoPulsingNoAlert$2 notificationInterruptLogger$logNoPulsingNoAlert$2 = NotificationInterruptLogger$logNoPulsingNoAlert$2.INSTANCE;
                Objects.requireNonNull(logBuffer3);
                if (!logBuffer3.frozen) {
                    LogMessageImpl obtain3 = logBuffer3.obtain("InterruptionStateProvider", logLevel, notificationInterruptLogger$logNoPulsingNoAlert$2);
                    obtain3.str1 = statusBarNotification.getKey();
                    logBuffer3.push(obtain3);
                }
            } else if (notificationEntry.shouldSuppressVisualEffect(128)) {
                NotificationInterruptLogger notificationInterruptLogger4 = this.mLogger;
                Objects.requireNonNull(notificationInterruptLogger4);
                LogBuffer logBuffer4 = notificationInterruptLogger4.hunBuffer;
                NotificationInterruptLogger$logNoPulsingNoAmbientEffect$2 notificationInterruptLogger$logNoPulsingNoAmbientEffect$2 = NotificationInterruptLogger$logNoPulsingNoAmbientEffect$2.INSTANCE;
                Objects.requireNonNull(logBuffer4);
                if (!logBuffer4.frozen) {
                    LogMessageImpl obtain4 = logBuffer4.obtain("InterruptionStateProvider", logLevel, notificationInterruptLogger$logNoPulsingNoAmbientEffect$2);
                    obtain4.str1 = statusBarNotification.getKey();
                    logBuffer4.push(obtain4);
                }
            } else if (notificationEntry.getImportance() < 3) {
                NotificationInterruptLogger notificationInterruptLogger5 = this.mLogger;
                Objects.requireNonNull(notificationInterruptLogger5);
                LogBuffer logBuffer5 = notificationInterruptLogger5.hunBuffer;
                NotificationInterruptLogger$logNoPulsingNotImportant$2 notificationInterruptLogger$logNoPulsingNotImportant$2 = NotificationInterruptLogger$logNoPulsingNotImportant$2.INSTANCE;
                Objects.requireNonNull(logBuffer5);
                if (!logBuffer5.frozen) {
                    LogMessageImpl obtain5 = logBuffer5.obtain("InterruptionStateProvider", logLevel, notificationInterruptLogger$logNoPulsingNotImportant$2);
                    obtain5.str1 = statusBarNotification.getKey();
                    logBuffer5.push(obtain5);
                }
            } else {
                NotificationInterruptLogger notificationInterruptLogger6 = this.mLogger;
                Objects.requireNonNull(notificationInterruptLogger6);
                LogBuffer logBuffer6 = notificationInterruptLogger6.hunBuffer;
                NotificationInterruptLogger$logPulsing$2 notificationInterruptLogger$logPulsing$2 = NotificationInterruptLogger$logPulsing$2.INSTANCE;
                Objects.requireNonNull(logBuffer6);
                if (logBuffer6.frozen) {
                    return true;
                }
                LogMessageImpl obtain6 = logBuffer6.obtain("InterruptionStateProvider", logLevel, notificationInterruptLogger$logPulsing$2);
                obtain6.str1 = statusBarNotification.getKey();
                logBuffer6.push(obtain6);
                return true;
            }
            return false;
        }
        Objects.requireNonNull(notificationEntry);
        StatusBarNotification statusBarNotification2 = notificationEntry.mSbn;
        if (!this.mUseHeadsUp) {
            NotificationInterruptLogger notificationInterruptLogger7 = this.mLogger;
            Objects.requireNonNull(notificationInterruptLogger7);
            LogBuffer logBuffer7 = notificationInterruptLogger7.hunBuffer;
            NotificationInterruptLogger$logNoHeadsUpFeatureDisabled$2 notificationInterruptLogger$logNoHeadsUpFeatureDisabled$2 = NotificationInterruptLogger$logNoHeadsUpFeatureDisabled$2.INSTANCE;
            Objects.requireNonNull(logBuffer7);
            if (!logBuffer7.frozen) {
                logBuffer7.push(logBuffer7.obtain("InterruptionStateProvider", logLevel, notificationInterruptLogger$logNoHeadsUpFeatureDisabled$2));
            }
        } else if (canAlertCommon(notificationEntry) && canAlertAwakeCommon(notificationEntry)) {
            if (this.mHeadsUpManager.isSnoozed(statusBarNotification2.getPackageName())) {
                NotificationInterruptLogger notificationInterruptLogger8 = this.mLogger;
                Objects.requireNonNull(notificationInterruptLogger8);
                LogBuffer logBuffer8 = notificationInterruptLogger8.hunBuffer;
                NotificationInterruptLogger$logNoHeadsUpPackageSnoozed$2 notificationInterruptLogger$logNoHeadsUpPackageSnoozed$2 = NotificationInterruptLogger$logNoHeadsUpPackageSnoozed$2.INSTANCE;
                Objects.requireNonNull(logBuffer8);
                if (!logBuffer8.frozen) {
                    LogMessageImpl obtain7 = logBuffer8.obtain("InterruptionStateProvider", logLevel, notificationInterruptLogger$logNoHeadsUpPackageSnoozed$2);
                    obtain7.str1 = statusBarNotification2.getKey();
                    logBuffer8.push(obtain7);
                }
            } else {
                if (this.mStatusBarStateController.getState() == 0) {
                    z = true;
                } else {
                    z = false;
                }
                if (notificationEntry.isBubble() && z) {
                    NotificationInterruptLogger notificationInterruptLogger9 = this.mLogger;
                    Objects.requireNonNull(notificationInterruptLogger9);
                    LogBuffer logBuffer9 = notificationInterruptLogger9.hunBuffer;
                    NotificationInterruptLogger$logNoHeadsUpAlreadyBubbled$2 notificationInterruptLogger$logNoHeadsUpAlreadyBubbled$2 = NotificationInterruptLogger$logNoHeadsUpAlreadyBubbled$2.INSTANCE;
                    Objects.requireNonNull(logBuffer9);
                    if (!logBuffer9.frozen) {
                        LogMessageImpl obtain8 = logBuffer9.obtain("InterruptionStateProvider", logLevel, notificationInterruptLogger$logNoHeadsUpAlreadyBubbled$2);
                        obtain8.str1 = statusBarNotification2.getKey();
                        logBuffer9.push(obtain8);
                    }
                } else if (notificationEntry.shouldSuppressVisualEffect(16)) {
                    NotificationInterruptLogger notificationInterruptLogger10 = this.mLogger;
                    Objects.requireNonNull(notificationInterruptLogger10);
                    LogBuffer logBuffer10 = notificationInterruptLogger10.hunBuffer;
                    NotificationInterruptLogger$logNoHeadsUpSuppressedByDnd$2 notificationInterruptLogger$logNoHeadsUpSuppressedByDnd$2 = NotificationInterruptLogger$logNoHeadsUpSuppressedByDnd$2.INSTANCE;
                    Objects.requireNonNull(logBuffer10);
                    if (!logBuffer10.frozen) {
                        LogMessageImpl obtain9 = logBuffer10.obtain("InterruptionStateProvider", logLevel, notificationInterruptLogger$logNoHeadsUpSuppressedByDnd$2);
                        obtain9.str1 = statusBarNotification2.getKey();
                        logBuffer10.push(obtain9);
                    }
                } else if (notificationEntry.getImportance() < 4) {
                    NotificationInterruptLogger notificationInterruptLogger11 = this.mLogger;
                    Objects.requireNonNull(notificationInterruptLogger11);
                    LogBuffer logBuffer11 = notificationInterruptLogger11.hunBuffer;
                    NotificationInterruptLogger$logNoHeadsUpNotImportant$2 notificationInterruptLogger$logNoHeadsUpNotImportant$2 = NotificationInterruptLogger$logNoHeadsUpNotImportant$2.INSTANCE;
                    Objects.requireNonNull(logBuffer11);
                    if (!logBuffer11.frozen) {
                        LogMessageImpl obtain10 = logBuffer11.obtain("InterruptionStateProvider", logLevel, notificationInterruptLogger$logNoHeadsUpNotImportant$2);
                        obtain10.str1 = statusBarNotification2.getKey();
                        logBuffer11.push(obtain10);
                    }
                } else {
                    try {
                        z2 = this.mDreamManager.isDreaming();
                    } catch (RemoteException e) {
                        Log.e("InterruptionStateProvider", "Failed to query dream manager.", e);
                        z2 = false;
                    }
                    if (!this.mPowerManager.isScreenOn() || z2) {
                        z3 = false;
                    } else {
                        z3 = true;
                    }
                    if (!z3) {
                        NotificationInterruptLogger notificationInterruptLogger12 = this.mLogger;
                        Objects.requireNonNull(notificationInterruptLogger12);
                        LogBuffer logBuffer12 = notificationInterruptLogger12.hunBuffer;
                        NotificationInterruptLogger$logNoHeadsUpNotInUse$2 notificationInterruptLogger$logNoHeadsUpNotInUse$2 = NotificationInterruptLogger$logNoHeadsUpNotInUse$2.INSTANCE;
                        Objects.requireNonNull(logBuffer12);
                        if (!logBuffer12.frozen) {
                            LogMessageImpl obtain11 = logBuffer12.obtain("InterruptionStateProvider", logLevel, notificationInterruptLogger$logNoHeadsUpNotInUse$2);
                            obtain11.str1 = statusBarNotification2.getKey();
                            logBuffer12.push(obtain11);
                        }
                    } else {
                        for (int i = 0; i < this.mSuppressors.size(); i++) {
                            if (((NotificationInterruptSuppressor) this.mSuppressors.get(i)).suppressAwakeHeadsUp(notificationEntry)) {
                                NotificationInterruptLogger notificationInterruptLogger13 = this.mLogger;
                                NotificationInterruptSuppressor notificationInterruptSuppressor = (NotificationInterruptSuppressor) this.mSuppressors.get(i);
                                Objects.requireNonNull(notificationInterruptLogger13);
                                LogBuffer logBuffer13 = notificationInterruptLogger13.hunBuffer;
                                NotificationInterruptLogger$logNoHeadsUpSuppressedBy$2 notificationInterruptLogger$logNoHeadsUpSuppressedBy$2 = NotificationInterruptLogger$logNoHeadsUpSuppressedBy$2.INSTANCE;
                                Objects.requireNonNull(logBuffer13);
                                if (!logBuffer13.frozen) {
                                    LogMessageImpl obtain12 = logBuffer13.obtain("InterruptionStateProvider", logLevel, notificationInterruptLogger$logNoHeadsUpSuppressedBy$2);
                                    obtain12.str1 = statusBarNotification2.getKey();
                                    obtain12.str2 = notificationInterruptSuppressor.getName();
                                    logBuffer13.push(obtain12);
                                }
                            }
                        }
                        NotificationInterruptLogger notificationInterruptLogger14 = this.mLogger;
                        Objects.requireNonNull(notificationInterruptLogger14);
                        LogBuffer logBuffer14 = notificationInterruptLogger14.hunBuffer;
                        NotificationInterruptLogger$logHeadsUp$2 notificationInterruptLogger$logHeadsUp$2 = NotificationInterruptLogger$logHeadsUp$2.INSTANCE;
                        Objects.requireNonNull(logBuffer14);
                        if (logBuffer14.frozen) {
                            return true;
                        }
                        LogMessageImpl obtain13 = logBuffer14.obtain("InterruptionStateProvider", logLevel, notificationInterruptLogger$logHeadsUp$2);
                        obtain13.str1 = statusBarNotification2.getKey();
                        logBuffer14.push(obtain13);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v1, types: [com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProviderImpl$1, android.database.ContentObserver] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public NotificationInterruptStateProviderImpl(android.content.ContentResolver r2, android.os.PowerManager r3, android.service.dreams.IDreamManager r4, android.hardware.display.AmbientDisplayConfiguration r5, com.android.systemui.statusbar.notification.NotificationFilter r6, com.android.systemui.statusbar.policy.BatteryController r7, com.android.systemui.plugins.statusbar.StatusBarStateController r8, com.android.systemui.statusbar.policy.HeadsUpManager r9, com.android.systemui.statusbar.notification.interruption.NotificationInterruptLogger r10, android.os.Handler r11) {
        /*
            r1 = this;
            r1.<init>()
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r1.mSuppressors = r0
            r0 = 0
            r1.mUseHeadsUp = r0
            r1.mContentResolver = r2
            r1.mPowerManager = r3
            r1.mDreamManager = r4
            r1.mBatteryController = r7
            r1.mAmbientDisplayConfiguration = r5
            r1.mNotificationFilter = r6
            r1.mStatusBarStateController = r8
            r1.mHeadsUpManager = r9
            r1.mLogger = r10
            com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProviderImpl$1 r3 = new com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProviderImpl$1
            r3.<init>(r11)
            r1.mHeadsUpObserver = r3
            java.lang.String r1 = "heads_up_notifications_enabled"
            android.net.Uri r1 = android.provider.Settings.Global.getUriFor(r1)
            r4 = 1
            r2.registerContentObserver(r1, r4, r3)
            java.lang.String r1 = "ticker_gets_heads_up"
            android.net.Uri r1 = android.provider.Settings.Global.getUriFor(r1)
            r2.registerContentObserver(r1, r4, r3)
            r3.onChange(r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProviderImpl.<init>(android.content.ContentResolver, android.os.PowerManager, android.service.dreams.IDreamManager, android.hardware.display.AmbientDisplayConfiguration, com.android.systemui.statusbar.notification.NotificationFilter, com.android.systemui.statusbar.policy.BatteryController, com.android.systemui.plugins.statusbar.StatusBarStateController, com.android.systemui.statusbar.policy.HeadsUpManager, com.android.systemui.statusbar.notification.interruption.NotificationInterruptLogger, android.os.Handler):void");
    }

    public final boolean canAlertAwakeCommon(NotificationEntry notificationEntry) {
        Objects.requireNonNull(notificationEntry);
        StatusBarNotification statusBarNotification = notificationEntry.mSbn;
        for (int i = 0; i < this.mSuppressors.size(); i++) {
            if (((NotificationInterruptSuppressor) this.mSuppressors.get(i)).suppressAwakeInterruptions()) {
                this.mLogger.logNoAlertingSuppressedBy(statusBarNotification, (NotificationInterruptSuppressor) this.mSuppressors.get(i), true);
                return false;
            }
        }
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider
    public final boolean shouldLaunchFullScreenIntentWhenAdded(NotificationEntry notificationEntry) {
        Objects.requireNonNull(notificationEntry);
        if (notificationEntry.mSbn.getNotification().fullScreenIntent == null || (shouldHeadsUp(notificationEntry) && this.mStatusBarStateController.getState() != 1)) {
            return false;
        }
        return true;
    }
}
