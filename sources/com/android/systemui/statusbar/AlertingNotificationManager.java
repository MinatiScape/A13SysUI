package com.android.systemui.statusbar;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.ArrayMap;
import android.util.ArraySet;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.screenshot.ScreenshotController$$ExternalSyntheticLambda3;
import com.android.systemui.statusbar.NotificationLifetimeExtender;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.policy.HeadsUpManagerLogger;
import com.android.wm.shell.transition.OneShotRemoteHandler$2$$ExternalSyntheticLambda0;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;
/* loaded from: classes.dex */
public abstract class AlertingNotificationManager implements NotificationLifetimeExtender {
    public int mAutoDismissNotificationDecay;
    public final HeadsUpManagerLogger mLogger;
    public int mMinimumDisplayTime;
    public NotificationLifetimeExtender.NotificationSafeToRemoveCallback mNotificationLifetimeFinishedCallback;
    public final Clock mClock = new Clock();
    public final ArrayMap<String, AlertEntry> mAlertEntries = new ArrayMap<>();
    public final ArraySet<NotificationEntry> mExtendedLifetimeAlertEntries = new ArraySet<>();
    @VisibleForTesting
    public Handler mHandler = new Handler(Looper.getMainLooper());

    /* loaded from: classes.dex */
    public class AlertEntry implements Comparable<AlertEntry> {
        public long mEarliestRemovaltime;
        public NotificationEntry mEntry;
        public long mPostTime;
        public Runnable mRemoveAlertRunnable;

        public boolean isSticky() {
            return false;
        }

        public void reset() {
            this.mEntry = null;
            removeAutoRemovalCallbacks();
            this.mRemoveAlertRunnable = null;
        }

        public AlertEntry() {
        }

        public long calculateFinishTime() {
            return this.mPostTime + AlertingNotificationManager.this.mAutoDismissNotificationDecay;
        }

        public long calculatePostTime() {
            Objects.requireNonNull(AlertingNotificationManager.this.mClock);
            return SystemClock.elapsedRealtime();
        }

        public int compareTo(AlertEntry alertEntry) {
            long j = this.mPostTime;
            long j2 = alertEntry.mPostTime;
            if (j < j2) {
                return 1;
            }
            if (j != j2) {
                return -1;
            }
            NotificationEntry notificationEntry = this.mEntry;
            Objects.requireNonNull(notificationEntry);
            String str = notificationEntry.mKey;
            NotificationEntry notificationEntry2 = alertEntry.mEntry;
            Objects.requireNonNull(notificationEntry2);
            return str.compareTo(notificationEntry2.mKey);
        }

        public final void removeAsSoonAsPossible() {
            if (this.mRemoveAlertRunnable != null) {
                removeAutoRemovalCallbacks();
                AlertingNotificationManager alertingNotificationManager = AlertingNotificationManager.this;
                Handler handler = alertingNotificationManager.mHandler;
                Runnable runnable = this.mRemoveAlertRunnable;
                long j = this.mEarliestRemovaltime;
                Objects.requireNonNull(alertingNotificationManager.mClock);
                handler.postDelayed(runnable, j - SystemClock.elapsedRealtime());
            }
        }

        public final void removeAutoRemovalCallbacks() {
            Runnable runnable = this.mRemoveAlertRunnable;
            if (runnable != null) {
                AlertingNotificationManager.this.mHandler.removeCallbacks(runnable);
            }
        }

        public void setEntry(NotificationEntry notificationEntry) {
            OneShotRemoteHandler$2$$ExternalSyntheticLambda0 oneShotRemoteHandler$2$$ExternalSyntheticLambda0 = new OneShotRemoteHandler$2$$ExternalSyntheticLambda0(this, notificationEntry, 3);
            this.mEntry = notificationEntry;
            this.mRemoveAlertRunnable = oneShotRemoteHandler$2$$ExternalSyntheticLambda0;
            this.mPostTime = calculatePostTime();
            updateEntry(true);
        }

        public void updateEntry(boolean z) {
            HeadsUpManagerLogger headsUpManagerLogger = AlertingNotificationManager.this.mLogger;
            NotificationEntry notificationEntry = this.mEntry;
            Objects.requireNonNull(notificationEntry);
            headsUpManagerLogger.logUpdateEntry(notificationEntry.mKey, z);
            Objects.requireNonNull(AlertingNotificationManager.this.mClock);
            long elapsedRealtime = SystemClock.elapsedRealtime();
            this.mEarliestRemovaltime = AlertingNotificationManager.this.mMinimumDisplayTime + elapsedRealtime;
            if (z) {
                this.mPostTime = Math.max(this.mPostTime, elapsedRealtime);
            }
            removeAutoRemovalCallbacks();
            if (!isSticky()) {
                AlertingNotificationManager.this.mHandler.postDelayed(this.mRemoveAlertRunnable, Math.max(calculateFinishTime() - elapsedRealtime, AlertingNotificationManager.this.mMinimumDisplayTime));
            }
        }
    }

    /* loaded from: classes.dex */
    public static final class Clock {
    }

    public abstract void onAlertEntryAdded(AlertEntry alertEntry);

    public abstract void onAlertEntryRemoved(AlertEntry alertEntry);

    public boolean canRemoveImmediately(String str) {
        boolean z;
        AlertEntry alertEntry = this.mAlertEntries.get(str);
        if (alertEntry != null) {
            long j = alertEntry.mEarliestRemovaltime;
            Objects.requireNonNull(AlertingNotificationManager.this.mClock);
            if (j < SystemClock.elapsedRealtime()) {
                z = true;
            } else {
                z = false;
            }
            if (!z && !alertEntry.mEntry.isRowDismissed()) {
                return false;
            }
        }
        return true;
    }

    public AlertEntry createAlertEntry() {
        return new AlertEntry();
    }

    public final Stream<NotificationEntry> getAllEntries() {
        return this.mAlertEntries.values().stream().map(AlertingNotificationManager$$ExternalSyntheticLambda0.INSTANCE);
    }

    public final boolean isAlerting(String str) {
        return this.mAlertEntries.containsKey(str);
    }

    public final void releaseAllImmediately() {
        this.mLogger.logReleaseAllImmediately();
        Iterator it = new ArraySet(this.mAlertEntries.keySet()).iterator();
        while (it.hasNext()) {
            removeAlertEntry((String) it.next());
        }
    }

    public final void removeAlertEntry(String str) {
        AlertEntry alertEntry = this.mAlertEntries.get(str);
        if (alertEntry != null) {
            NotificationEntry notificationEntry = alertEntry.mEntry;
            if (notificationEntry == null || !notificationEntry.mExpandAnimationRunning) {
                this.mAlertEntries.remove(str);
                onAlertEntryRemoved(alertEntry);
                Objects.requireNonNull(notificationEntry);
                ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
                if (expandableNotificationRow != null) {
                    expandableNotificationRow.sendAccessibilityEvent(2048);
                }
                alertEntry.reset();
                if (this.mExtendedLifetimeAlertEntries.contains(notificationEntry)) {
                    NotificationLifetimeExtender.NotificationSafeToRemoveCallback notificationSafeToRemoveCallback = this.mNotificationLifetimeFinishedCallback;
                    if (notificationSafeToRemoveCallback != null) {
                        ((ScreenshotController$$ExternalSyntheticLambda3) notificationSafeToRemoveCallback).onSafeToRemove(str);
                    }
                    this.mExtendedLifetimeAlertEntries.remove(notificationEntry);
                }
            }
        }
    }

    public final boolean removeNotification(String str, boolean z) {
        this.mLogger.logRemoveNotification(str, z);
        AlertEntry alertEntry = this.mAlertEntries.get(str);
        if (alertEntry == null) {
            return true;
        }
        if (z || canRemoveImmediately(str)) {
            removeAlertEntry(str);
            return true;
        }
        alertEntry.removeAsSoonAsPossible();
        return false;
    }

    @Override // com.android.systemui.statusbar.NotificationLifetimeExtender
    public final void setShouldManageLifetime(NotificationEntry notificationEntry, boolean z) {
        if (z) {
            this.mExtendedLifetimeAlertEntries.add(notificationEntry);
            this.mAlertEntries.get(notificationEntry.mKey).removeAsSoonAsPossible();
            return;
        }
        this.mExtendedLifetimeAlertEntries.remove(notificationEntry);
    }

    @Override // com.android.systemui.statusbar.NotificationLifetimeExtender
    public boolean shouldExtendLifetime(NotificationEntry notificationEntry) {
        return !canRemoveImmediately(notificationEntry.mKey);
    }

    public final void showNotification(NotificationEntry notificationEntry) {
        HeadsUpManagerLogger headsUpManagerLogger = this.mLogger;
        Objects.requireNonNull(notificationEntry);
        headsUpManagerLogger.logShowNotification(notificationEntry.mKey);
        AlertEntry createAlertEntry = createAlertEntry();
        createAlertEntry.setEntry(notificationEntry);
        this.mAlertEntries.put(notificationEntry.mKey, createAlertEntry);
        onAlertEntryAdded(createAlertEntry);
        ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
        if (expandableNotificationRow != null) {
            expandableNotificationRow.sendAccessibilityEvent(2048);
        }
        notificationEntry.mIsAlerting = true;
        updateNotification(notificationEntry.mKey, true);
        notificationEntry.interruption = true;
    }

    public void updateNotification(String str, boolean z) {
        boolean z2;
        AlertEntry alertEntry = this.mAlertEntries.get(str);
        HeadsUpManagerLogger headsUpManagerLogger = this.mLogger;
        if (alertEntry != null) {
            z2 = true;
        } else {
            z2 = false;
        }
        headsUpManagerLogger.logUpdateNotification(str, z, z2);
        if (alertEntry != null) {
            NotificationEntry notificationEntry = alertEntry.mEntry;
            Objects.requireNonNull(notificationEntry);
            ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
            if (expandableNotificationRow != null) {
                expandableNotificationRow.sendAccessibilityEvent(2048);
            }
            if (z) {
                alertEntry.updateEntry(true);
            }
        }
    }

    public AlertingNotificationManager(HeadsUpManagerLogger headsUpManagerLogger) {
        this.mLogger = headsUpManagerLogger;
    }

    @Override // com.android.systemui.statusbar.NotificationLifetimeExtender
    public final void setCallback(ScreenshotController$$ExternalSyntheticLambda3 screenshotController$$ExternalSyntheticLambda3) {
        this.mNotificationLifetimeFinishedCallback = screenshotController$$ExternalSyntheticLambda3;
    }
}
