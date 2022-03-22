package com.android.systemui.statusbar.phone;

import android.util.DisplayMetrics;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger;
import java.util.Objects;
/* compiled from: LSShadeTransitionLogger.kt */
/* loaded from: classes.dex */
public final class LSShadeTransitionLogger {
    public final LogBuffer buffer;
    public final DisplayMetrics displayMetrics;
    public final LockscreenGestureLogger lockscreenGestureLogger;

    public final void logAnimationCancelled(boolean z) {
        LogLevel logLevel = LogLevel.DEBUG;
        if (z) {
            LogBuffer logBuffer = this.buffer;
            LSShadeTransitionLogger$logAnimationCancelled$2 lSShadeTransitionLogger$logAnimationCancelled$2 = LSShadeTransitionLogger$logAnimationCancelled$2.INSTANCE;
            Objects.requireNonNull(logBuffer);
            if (!logBuffer.frozen) {
                logBuffer.push(logBuffer.obtain("LockscreenShadeTransitionController", logLevel, lSShadeTransitionLogger$logAnimationCancelled$2));
                return;
            }
            return;
        }
        LogBuffer logBuffer2 = this.buffer;
        LSShadeTransitionLogger$logAnimationCancelled$4 lSShadeTransitionLogger$logAnimationCancelled$4 = LSShadeTransitionLogger$logAnimationCancelled$4.INSTANCE;
        Objects.requireNonNull(logBuffer2);
        if (!logBuffer2.frozen) {
            logBuffer2.push(logBuffer2.obtain("LockscreenShadeTransitionController", logLevel, lSShadeTransitionLogger$logAnimationCancelled$4));
        }
    }

    public final void logDefaultGoToFullShadeAnimation(long j) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        LSShadeTransitionLogger$logDefaultGoToFullShadeAnimation$2 lSShadeTransitionLogger$logDefaultGoToFullShadeAnimation$2 = LSShadeTransitionLogger$logDefaultGoToFullShadeAnimation$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("LockscreenShadeTransitionController", logLevel, lSShadeTransitionLogger$logDefaultGoToFullShadeAnimation$2);
            obtain.long1 = j;
            logBuffer.push(obtain);
        }
    }

    public final void logDragDownAborted() {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        LSShadeTransitionLogger$logDragDownAborted$2 lSShadeTransitionLogger$logDragDownAborted$2 = LSShadeTransitionLogger$logDragDownAborted$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            logBuffer.push(logBuffer.obtain("LockscreenShadeTransitionController", logLevel, lSShadeTransitionLogger$logDragDownAborted$2));
        }
    }

    public final void logDragDownAmountResetWhenFullyCollapsed() {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.WARNING;
        LSShadeTransitionLogger$logDragDownAmountResetWhenFullyCollapsed$2 lSShadeTransitionLogger$logDragDownAmountResetWhenFullyCollapsed$2 = LSShadeTransitionLogger$logDragDownAmountResetWhenFullyCollapsed$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            logBuffer.push(logBuffer.obtain("LockscreenShadeTransitionController", logLevel, lSShadeTransitionLogger$logDragDownAmountResetWhenFullyCollapsed$2));
        }
    }

    public final void logDragDownAnimation(float f) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        LSShadeTransitionLogger$logDragDownAnimation$2 lSShadeTransitionLogger$logDragDownAnimation$2 = LSShadeTransitionLogger$logDragDownAnimation$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("LockscreenShadeTransitionController", logLevel, lSShadeTransitionLogger$logDragDownAnimation$2);
            obtain.double1 = f;
            logBuffer.push(obtain);
        }
    }

    public final void logDragDownStarted(ExpandableView expandableView) {
        ExpandableNotificationRow expandableNotificationRow;
        String str;
        NotificationEntry notificationEntry = null;
        if (expandableView instanceof ExpandableNotificationRow) {
            expandableNotificationRow = (ExpandableNotificationRow) expandableView;
        } else {
            expandableNotificationRow = null;
        }
        if (expandableNotificationRow != null) {
            notificationEntry = expandableNotificationRow.mEntry;
        }
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        LSShadeTransitionLogger$logDragDownStarted$2 lSShadeTransitionLogger$logDragDownStarted$2 = LSShadeTransitionLogger$logDragDownStarted$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("LockscreenShadeTransitionController", logLevel, lSShadeTransitionLogger$logDragDownStarted$2);
            if (notificationEntry == null || (str = notificationEntry.mKey) == null) {
                str = "no entry";
            }
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
    }

    public final void logDraggedDown(ExpandableView expandableView, int i) {
        ExpandableNotificationRow expandableNotificationRow;
        String str;
        NotificationEntry notificationEntry = null;
        if (expandableView instanceof ExpandableNotificationRow) {
            expandableNotificationRow = (ExpandableNotificationRow) expandableView;
        } else {
            expandableNotificationRow = null;
        }
        if (expandableNotificationRow != null) {
            notificationEntry = expandableNotificationRow.mEntry;
        }
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        LSShadeTransitionLogger$logDraggedDown$2 lSShadeTransitionLogger$logDraggedDown$2 = LSShadeTransitionLogger$logDraggedDown$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("LockscreenShadeTransitionController", logLevel, lSShadeTransitionLogger$logDraggedDown$2);
            if (notificationEntry == null || (str = notificationEntry.mKey) == null) {
                str = "no entry";
            }
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
        this.lockscreenGestureLogger.write(187, (int) (i / this.displayMetrics.density), 0);
        LockscreenGestureLogger lockscreenGestureLogger = this.lockscreenGestureLogger;
        LockscreenGestureLogger.LockscreenUiEvent lockscreenUiEvent = LockscreenGestureLogger.LockscreenUiEvent.LOCKSCREEN_PULL_SHADE_OPEN;
        Objects.requireNonNull(lockscreenGestureLogger);
        LockscreenGestureLogger.log(lockscreenUiEvent);
    }

    public final void logDraggedDownLockDownShade(ExpandableView expandableView) {
        ExpandableNotificationRow expandableNotificationRow;
        String str;
        NotificationEntry notificationEntry = null;
        if (expandableView instanceof ExpandableNotificationRow) {
            expandableNotificationRow = (ExpandableNotificationRow) expandableView;
        } else {
            expandableNotificationRow = null;
        }
        if (expandableNotificationRow != null) {
            notificationEntry = expandableNotificationRow.mEntry;
        }
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        LSShadeTransitionLogger$logDraggedDownLockDownShade$2 lSShadeTransitionLogger$logDraggedDownLockDownShade$2 = LSShadeTransitionLogger$logDraggedDownLockDownShade$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("LockscreenShadeTransitionController", logLevel, lSShadeTransitionLogger$logDraggedDownLockDownShade$2);
            if (notificationEntry == null || (str = notificationEntry.mKey) == null) {
                str = "no entry";
            }
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
    }

    public final void logGoingToLockedShade(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        LSShadeTransitionLogger$logGoingToLockedShade$2 lSShadeTransitionLogger$logGoingToLockedShade$2 = new LSShadeTransitionLogger$logGoingToLockedShade$2(z);
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("LockscreenShadeTransitionController", logLevel, lSShadeTransitionLogger$logGoingToLockedShade$2);
            obtain.bool1 = z;
            logBuffer.push(obtain);
        }
    }

    public final void logGoingToLockedShadeAborted() {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        LSShadeTransitionLogger$logGoingToLockedShadeAborted$2 lSShadeTransitionLogger$logGoingToLockedShadeAborted$2 = LSShadeTransitionLogger$logGoingToLockedShadeAborted$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            logBuffer.push(logBuffer.obtain("LockscreenShadeTransitionController", logLevel, lSShadeTransitionLogger$logGoingToLockedShadeAborted$2));
        }
    }

    public final void logOnHideKeyguard() {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        LSShadeTransitionLogger$logOnHideKeyguard$2 lSShadeTransitionLogger$logOnHideKeyguard$2 = LSShadeTransitionLogger$logOnHideKeyguard$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            logBuffer.push(logBuffer.obtain("LockscreenShadeTransitionController", logLevel, lSShadeTransitionLogger$logOnHideKeyguard$2));
        }
    }

    public final void logPulseExpansionFinished(boolean z) {
        LogLevel logLevel = LogLevel.INFO;
        if (z) {
            LogBuffer logBuffer = this.buffer;
            LSShadeTransitionLogger$logPulseExpansionFinished$2 lSShadeTransitionLogger$logPulseExpansionFinished$2 = LSShadeTransitionLogger$logPulseExpansionFinished$2.INSTANCE;
            Objects.requireNonNull(logBuffer);
            if (!logBuffer.frozen) {
                logBuffer.push(logBuffer.obtain("LockscreenShadeTransitionController", logLevel, lSShadeTransitionLogger$logPulseExpansionFinished$2));
                return;
            }
            return;
        }
        LogBuffer logBuffer2 = this.buffer;
        LSShadeTransitionLogger$logPulseExpansionFinished$4 lSShadeTransitionLogger$logPulseExpansionFinished$4 = LSShadeTransitionLogger$logPulseExpansionFinished$4.INSTANCE;
        Objects.requireNonNull(logBuffer2);
        if (!logBuffer2.frozen) {
            logBuffer2.push(logBuffer2.obtain("LockscreenShadeTransitionController", logLevel, lSShadeTransitionLogger$logPulseExpansionFinished$4));
        }
    }

    public final void logPulseExpansionStarted() {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        LSShadeTransitionLogger$logPulseExpansionStarted$2 lSShadeTransitionLogger$logPulseExpansionStarted$2 = LSShadeTransitionLogger$logPulseExpansionStarted$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            logBuffer.push(logBuffer.obtain("LockscreenShadeTransitionController", logLevel, lSShadeTransitionLogger$logPulseExpansionStarted$2));
        }
    }

    public final void logPulseHeightNotResetWhenFullyCollapsed() {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.WARNING;
        LSShadeTransitionLogger$logPulseHeightNotResetWhenFullyCollapsed$2 lSShadeTransitionLogger$logPulseHeightNotResetWhenFullyCollapsed$2 = LSShadeTransitionLogger$logPulseHeightNotResetWhenFullyCollapsed$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            logBuffer.push(logBuffer.obtain("LockscreenShadeTransitionController", logLevel, lSShadeTransitionLogger$logPulseHeightNotResetWhenFullyCollapsed$2));
        }
    }

    public final void logShadeDisabledOnGoToLockedShade() {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.WARNING;
        LSShadeTransitionLogger$logShadeDisabledOnGoToLockedShade$2 lSShadeTransitionLogger$logShadeDisabledOnGoToLockedShade$2 = LSShadeTransitionLogger$logShadeDisabledOnGoToLockedShade$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            logBuffer.push(logBuffer.obtain("LockscreenShadeTransitionController", logLevel, lSShadeTransitionLogger$logShadeDisabledOnGoToLockedShade$2));
        }
    }

    public final void logShowBouncerOnGoToLockedShade() {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        LSShadeTransitionLogger$logShowBouncerOnGoToLockedShade$2 lSShadeTransitionLogger$logShowBouncerOnGoToLockedShade$2 = LSShadeTransitionLogger$logShowBouncerOnGoToLockedShade$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            logBuffer.push(logBuffer.obtain("LockscreenShadeTransitionController", logLevel, lSShadeTransitionLogger$logShowBouncerOnGoToLockedShade$2));
        }
    }

    public final void logTryGoToLockedShade(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        LSShadeTransitionLogger$logTryGoToLockedShade$2 lSShadeTransitionLogger$logTryGoToLockedShade$2 = LSShadeTransitionLogger$logTryGoToLockedShade$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("LockscreenShadeTransitionController", logLevel, lSShadeTransitionLogger$logTryGoToLockedShade$2);
            obtain.bool1 = z;
            logBuffer.push(obtain);
        }
    }

    public final void logUnSuccessfulDragDown(ExpandableView expandableView) {
        ExpandableNotificationRow expandableNotificationRow;
        String str;
        NotificationEntry notificationEntry = null;
        if (expandableView instanceof ExpandableNotificationRow) {
            expandableNotificationRow = (ExpandableNotificationRow) expandableView;
        } else {
            expandableNotificationRow = null;
        }
        if (expandableNotificationRow != null) {
            notificationEntry = expandableNotificationRow.mEntry;
        }
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        LSShadeTransitionLogger$logUnSuccessfulDragDown$2 lSShadeTransitionLogger$logUnSuccessfulDragDown$2 = LSShadeTransitionLogger$logUnSuccessfulDragDown$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("LockscreenShadeTransitionController", logLevel, lSShadeTransitionLogger$logUnSuccessfulDragDown$2);
            if (notificationEntry == null || (str = notificationEntry.mKey) == null) {
                str = "no entry";
            }
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
    }

    public LSShadeTransitionLogger(LogBuffer logBuffer, LockscreenGestureLogger lockscreenGestureLogger, DisplayMetrics displayMetrics) {
        this.buffer = logBuffer;
        this.lockscreenGestureLogger = lockscreenGestureLogger;
        this.displayMetrics = displayMetrics;
    }
}
