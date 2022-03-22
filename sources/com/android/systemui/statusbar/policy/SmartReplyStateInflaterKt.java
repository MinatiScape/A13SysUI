package com.android.systemui.statusbar.policy;

import android.util.Log;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
/* compiled from: SmartReplyStateInflater.kt */
/* loaded from: classes.dex */
public final class SmartReplyStateInflaterKt {
    public static final boolean DEBUG = Log.isLoggable("SmartReplyViewInflater", 3);

    public static final boolean shouldShowSmartReplyView(NotificationEntry notificationEntry, InflatedSmartReplyState inflatedSmartReplyState) {
        if ((inflatedSmartReplyState.smartReplies != null || inflatedSmartReplyState.smartActions != null) && !notificationEntry.mSbn.getNotification().extras.getBoolean("android.remoteInputSpinner", false)) {
            return !notificationEntry.mSbn.getNotification().extras.getBoolean("android.hideSmartReplies", false);
        }
        return false;
    }
}
