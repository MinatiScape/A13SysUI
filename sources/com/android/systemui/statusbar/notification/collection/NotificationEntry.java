package com.android.systemui.statusbar.notification.collection;

import android.app.Notification;
import android.app.NotificationChannel;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.net.Uri;
import android.os.SystemClock;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.SpannedString;
import android.view.ContentInfo;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.statusbar.InflationTask;
import com.android.systemui.statusbar.notification.AboveShelfObserver;
import com.android.systemui.statusbar.notification.icon.IconPack;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRowController;
import com.android.systemui.statusbar.notification.row.NotificationContentView;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import com.android.systemui.statusbar.policy.RemoteInputView;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class NotificationEntry extends ListEntry {
    public EditedSuggestionInfo editedSuggestionInfo;
    public boolean hasSentReply;
    public CharSequence headsUpStatusBarText;
    public CharSequence headsUpStatusBarTextPublic;
    public boolean interruption;
    public Notification.BubbleMetadata mBubbleMetadata;
    public Throwable mDebugThrowable;
    public boolean mExpandAnimationRunning;
    public boolean mIsAlerting;
    public boolean mIsMarkedForUserTriggeredMovement;
    public Boolean mIsSystemNotification;
    public final String mKey;
    public boolean mPulseSupressed;
    public NotificationListenerService.Ranking mRanking;
    public boolean mRemoteEditImeAnimatingAway;
    public boolean mRemoteEditImeVisible;
    public ExpandableNotificationRowController mRowController;
    public StatusBarNotification mSbn;
    public ContentInfo remoteInputAttachment;
    public String remoteInputMimeType;
    public CharSequence remoteInputText;
    public SpannedString remoteInputTextWhenReset;
    public Uri remoteInputUri;
    public ExpandableNotificationRow row;
    public int targetSdk;
    public final ArrayList mLifetimeExtenders = new ArrayList();
    public final ArrayList mDismissInterceptors = new ArrayList();
    public int mCancellationReason = -1;
    public DismissState mDismissState = DismissState.NOT_DISMISSED;
    public IconPack mIcons = new IconPack(false, null, null, null, null, null);
    public long lastFullScreenIntentLaunchTime = -2000;
    public int mCachedContrastColor = 1;
    public int mCachedContrastColorIsFor = 1;
    public InflationTask mRunningTask = null;
    public long lastRemoteInputSent = -2000;
    public long initializationTime = -1;
    public boolean mSensitive = true;
    public ArrayList mOnSensitivityChangedListeners = new ArrayList();
    public int mBucket = 5;

    /* loaded from: classes.dex */
    public enum DismissState {
        NOT_DISMISSED,
        DISMISSED,
        PARENT_DISMISSED
    }

    /* loaded from: classes.dex */
    public interface OnSensitivityChangedListener {
        void onSensitivityChanged(NotificationEntry notificationEntry);
    }

    @Override // com.android.systemui.statusbar.notification.collection.ListEntry
    public final NotificationEntry getRepresentativeEntry() {
        return this;
    }

    /* loaded from: classes.dex */
    public static class EditedSuggestionInfo {
        public final int index;
        public final CharSequence originalText;

        public EditedSuggestionInfo(CharSequence charSequence, int i) {
            this.originalText = charSequence;
            this.index = i;
        }
    }

    public final void abortTask() {
        InflationTask inflationTask = this.mRunningTask;
        if (inflationTask != null) {
            inflationTask.abort();
            this.mRunningTask = null;
        }
    }

    public final boolean areGutsExposed() {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        if (expandableNotificationRow != null) {
            Objects.requireNonNull(expandableNotificationRow);
            if (expandableNotificationRow.mGuts != null) {
                ExpandableNotificationRow expandableNotificationRow2 = this.row;
                Objects.requireNonNull(expandableNotificationRow2);
                NotificationGuts notificationGuts = expandableNotificationRow2.mGuts;
                Objects.requireNonNull(notificationGuts);
                if (notificationGuts.mExposed) {
                    return true;
                }
            }
        }
        return false;
    }

    public final ArrayList getAttachedNotifChildren() {
        ArrayList<ExpandableNotificationRow> attachedChildren;
        ExpandableNotificationRow expandableNotificationRow = this.row;
        if (expandableNotificationRow == null || (attachedChildren = expandableNotificationRow.getAttachedChildren()) == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (ExpandableNotificationRow expandableNotificationRow2 : attachedChildren) {
            Objects.requireNonNull(expandableNotificationRow2);
            arrayList.add(expandableNotificationRow2.mEntry);
        }
        return arrayList;
    }

    public final NotificationChannel getChannel() {
        return this.mRanking.getChannel();
    }

    public final int getImportance() {
        return this.mRanking.getImportance();
    }

    public final boolean hasFinishedInitialization() {
        if (this.initializationTime == -1 || SystemClock.elapsedRealtime() <= this.initializationTime + 400) {
            return false;
        }
        return true;
    }

    public final boolean isAmbient() {
        return this.mRanking.isAmbient();
    }

    public final boolean isBubble() {
        if ((this.mSbn.getNotification().flags & 4096) != 0) {
            return true;
        }
        return false;
    }

    public final boolean isClearable() {
        if (!this.mSbn.isClearable()) {
            return false;
        }
        ArrayList attachedNotifChildren = getAttachedNotifChildren();
        if (attachedNotifChildren == null || attachedNotifChildren.size() <= 0) {
            return true;
        }
        for (int i = 0; i < attachedNotifChildren.size(); i++) {
            NotificationEntry notificationEntry = (NotificationEntry) attachedNotifChildren.get(i);
            Objects.requireNonNull(notificationEntry);
            if (!notificationEntry.mSbn.isClearable()) {
                return false;
            }
        }
        return true;
    }

    public final boolean isDismissable() {
        if (this.mSbn.isOngoing()) {
            return false;
        }
        ArrayList attachedNotifChildren = getAttachedNotifChildren();
        if (attachedNotifChildren == null || attachedNotifChildren.size() <= 0) {
            return true;
        }
        for (int i = 0; i < attachedNotifChildren.size(); i++) {
            NotificationEntry notificationEntry = (NotificationEntry) attachedNotifChildren.get(i);
            Objects.requireNonNull(notificationEntry);
            if (notificationEntry.mSbn.isOngoing()) {
                return false;
            }
        }
        return true;
    }

    @VisibleForTesting
    public boolean isExemptFromDndVisualSuppression() {
        boolean z;
        Notification notification = this.mSbn.getNotification();
        if (Objects.equals(notification.category, "call") || Objects.equals(notification.category, "msg") || Objects.equals(notification.category, "alarm") || Objects.equals(notification.category, "event") || Objects.equals(notification.category, "reminder")) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            return false;
        }
        if ((this.mSbn.getNotification().flags & 64) != 0 || this.mSbn.getNotification().isMediaNotification()) {
            return true;
        }
        Boolean bool = this.mIsSystemNotification;
        if (bool == null || !bool.booleanValue()) {
            return false;
        }
        return true;
    }

    public final boolean isRowDismissed() {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        if (expandableNotificationRow != null) {
            Objects.requireNonNull(expandableNotificationRow);
            if (expandableNotificationRow.mDismissed) {
                return true;
            }
        }
        return false;
    }

    public final boolean isRowHeadsUp() {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        if (expandableNotificationRow != null) {
            Objects.requireNonNull(expandableNotificationRow);
            if (expandableNotificationRow.mIsHeadsUp) {
                return true;
            }
        }
        return false;
    }

    public final boolean isRowPinned() {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        if (expandableNotificationRow != null) {
            Objects.requireNonNull(expandableNotificationRow);
            if (expandableNotificationRow.mIsPinned) {
                return true;
            }
        }
        return false;
    }

    public final boolean isRowRemoved() {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        if (expandableNotificationRow != null) {
            Objects.requireNonNull(expandableNotificationRow);
            if (expandableNotificationRow.mRemoved) {
                return true;
            }
        }
        return false;
    }

    public final void removeRow() {
        NotificationContentView[] notificationContentViewArr;
        ExpandableNotificationRow expandableNotificationRow = this.row;
        if (expandableNotificationRow != null) {
            Objects.requireNonNull(expandableNotificationRow);
            expandableNotificationRow.mRemoved = true;
            expandableNotificationRow.mTranslationWhenRemoved = expandableNotificationRow.getTranslationY();
            expandableNotificationRow.mWasChildInGroupWhenRemoved = expandableNotificationRow.isChildInGroup();
            if (expandableNotificationRow.isChildInGroup()) {
                expandableNotificationRow.mTranslationWhenRemoved = expandableNotificationRow.mNotificationParent.getTranslationY() + expandableNotificationRow.mTranslationWhenRemoved;
            }
            for (NotificationContentView notificationContentView : expandableNotificationRow.mLayouts) {
                Objects.requireNonNull(notificationContentView);
                RemoteInputView remoteInputView = notificationContentView.mExpandedRemoteInput;
                if (remoteInputView != null) {
                    remoteInputView.mRemoved = true;
                }
                RemoteInputView remoteInputView2 = notificationContentView.mHeadsUpRemoteInput;
                if (remoteInputView2 != null) {
                    remoteInputView2.mRemoved = true;
                }
            }
        }
    }

    public final boolean rowExists() {
        if (this.row != null) {
            return true;
        }
        return false;
    }

    public final void setHeadsUp(boolean z) {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        if (expandableNotificationRow != null) {
            Objects.requireNonNull(expandableNotificationRow);
            boolean isAboveShelf = expandableNotificationRow.isAboveShelf();
            int intrinsicHeight = expandableNotificationRow.getIntrinsicHeight();
            expandableNotificationRow.mIsHeadsUp = z;
            NotificationContentView notificationContentView = expandableNotificationRow.mPrivateLayout;
            Objects.requireNonNull(notificationContentView);
            notificationContentView.mIsHeadsUp = z;
            notificationContentView.selectLayout(false, true);
            notificationContentView.updateExpandButtonsDuringLayout(notificationContentView.mExpandable, false);
            if (expandableNotificationRow.mIsSummaryWithChildren) {
                expandableNotificationRow.mChildrenContainer.updateGroupOverflow();
            }
            if (intrinsicHeight != expandableNotificationRow.getIntrinsicHeight()) {
                expandableNotificationRow.notifyHeightChanged(false);
            }
            if (z) {
                expandableNotificationRow.mMustStayOnScreen = true;
                expandableNotificationRow.setAboveShelf(true);
            } else if (expandableNotificationRow.isAboveShelf() != isAboveShelf) {
                ((AboveShelfObserver) expandableNotificationRow.mAboveShelfChangedListener).onAboveShelfStateChanged(!isAboveShelf);
            }
        }
    }

    public final void setSensitive(boolean z, boolean z2) {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        Objects.requireNonNull(expandableNotificationRow);
        expandableNotificationRow.mSensitive = z;
        expandableNotificationRow.mSensitiveHiddenInGeneral = z2;
        if (z != this.mSensitive) {
            this.mSensitive = z;
            for (int i = 0; i < this.mOnSensitivityChangedListeners.size(); i++) {
                ((OnSensitivityChangedListener) this.mOnSensitivityChangedListeners.get(i)).onSensitivityChanged(this);
            }
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public NotificationEntry(android.service.notification.StatusBarNotification r8, android.service.notification.NotificationListenerService.Ranking r9, long r10) {
        /*
            r7 = this;
            java.util.Objects.requireNonNull(r8)
            java.lang.String r0 = r8.getKey()
            java.util.Objects.requireNonNull(r0)
            r7.<init>(r0, r10)
            java.util.ArrayList r10 = new java.util.ArrayList
            r10.<init>()
            r7.mLifetimeExtenders = r10
            java.util.ArrayList r10 = new java.util.ArrayList
            r10.<init>()
            r7.mDismissInterceptors = r10
            r10 = -1
            r7.mCancellationReason = r10
            com.android.systemui.statusbar.notification.collection.NotificationEntry$DismissState r10 = com.android.systemui.statusbar.notification.collection.NotificationEntry.DismissState.NOT_DISMISSED
            r7.mDismissState = r10
            com.android.systemui.statusbar.notification.icon.IconPack r10 = new com.android.systemui.statusbar.notification.icon.IconPack
            r1 = 0
            r2 = 0
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            r0 = r10
            r0.<init>(r1, r2, r3, r4, r5, r6)
            r7.mIcons = r10
            r10 = -2000(0xfffffffffffff830, double:NaN)
            r7.lastFullScreenIntentLaunchTime = r10
            r0 = 1
            r7.mCachedContrastColor = r0
            r7.mCachedContrastColorIsFor = r0
            r1 = 0
            r7.mRunningTask = r1
            r7.lastRemoteInputSent = r10
            android.util.ArraySet r10 = new android.util.ArraySet
            r11 = 3
            r10.<init>(r11)
            r10 = -1
            r7.initializationTime = r10
            r7.mSensitive = r0
            java.util.ArrayList r10 = new java.util.ArrayList
            r10.<init>()
            r7.mOnSensitivityChangedListeners = r10
            r10 = 5
            r7.mBucket = r10
            java.util.Objects.requireNonNull(r9)
            java.lang.String r10 = r8.getKey()
            r7.mKey = r10
            r7.setSbn(r8)
            r7.setRanking(r9)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.collection.NotificationEntry.<init>(android.service.notification.StatusBarNotification, android.service.notification.NotificationListenerService$Ranking, long):void");
    }

    public final void setRanking(NotificationListenerService.Ranking ranking) {
        Objects.requireNonNull(ranking);
        Objects.requireNonNull(ranking.getKey());
        if (ranking.getKey().equals(this.mKey)) {
            this.mRanking = ranking.withAudiblyAlertedInfo(this.mRanking);
            return;
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("New key ");
        m.append(ranking.getKey());
        m.append(" doesn't match existing key ");
        m.append(this.mKey);
        throw new IllegalArgumentException(m.toString());
    }

    public final void setSbn(StatusBarNotification statusBarNotification) {
        Objects.requireNonNull(statusBarNotification);
        Objects.requireNonNull(statusBarNotification.getKey());
        if (statusBarNotification.getKey().equals(this.mKey)) {
            this.mSbn = statusBarNotification;
            this.mBubbleMetadata = statusBarNotification.getNotification().getBubbleMetadata();
            return;
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("New key ");
        m.append(statusBarNotification.getKey());
        m.append(" doesn't match existing key ");
        m.append(this.mKey);
        throw new IllegalArgumentException(m.toString());
    }

    public final boolean shouldSuppressVisualEffect(int i) {
        if (!isExemptFromDndVisualSuppression() && (this.mRanking.getSuppressedVisualEffects() & i) != 0) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.notification.collection.ListEntry
    public final String getKey() {
        return this.mKey;
    }

    @VisibleForTesting
    public InflationTask getRunningTask() {
        return this.mRunningTask;
    }
}
