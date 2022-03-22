package com.android.wm.shell.bubbles;

import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
/* loaded from: classes.dex */
public final class BubbleEntry {
    public boolean mIsClearable;
    public NotificationListenerService.Ranking mRanking;
    public StatusBarNotification mSbn;
    public boolean mShouldSuppressNotificationDot;
    public boolean mShouldSuppressNotificationList;
    public boolean mShouldSuppressPeek;

    public final Notification.BubbleMetadata getBubbleMetadata() {
        return this.mSbn.getNotification().getBubbleMetadata();
    }

    public final String getKey() {
        return this.mSbn.getKey();
    }

    public final boolean isBubble() {
        if ((this.mSbn.getNotification().flags & 4096) != 0) {
            return true;
        }
        return false;
    }

    public BubbleEntry(StatusBarNotification statusBarNotification, NotificationListenerService.Ranking ranking, boolean z, boolean z2, boolean z3, boolean z4) {
        this.mSbn = statusBarNotification;
        this.mRanking = ranking;
        this.mIsClearable = z;
        this.mShouldSuppressNotificationDot = z2;
        this.mShouldSuppressNotificationList = z3;
        this.mShouldSuppressPeek = z4;
    }

    public final boolean setFlagBubble(boolean z) {
        boolean isBubble = isBubble();
        if (!z) {
            this.mSbn.getNotification().flags &= -4097;
        } else if (getBubbleMetadata() != null && this.mRanking.canBubble()) {
            this.mSbn.getNotification().flags |= 4096;
        }
        if (isBubble != isBubble()) {
            return true;
        }
        return false;
    }
}
