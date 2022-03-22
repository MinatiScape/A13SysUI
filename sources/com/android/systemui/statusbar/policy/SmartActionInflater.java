package com.android.systemui.statusbar.policy;

import android.app.Notification;
import android.view.ContextThemeWrapper;
import android.widget.Button;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.SmartReplyView;
/* compiled from: SmartReplyStateInflater.kt */
/* loaded from: classes.dex */
public interface SmartActionInflater {
    Button inflateActionButton(SmartReplyView smartReplyView, NotificationEntry notificationEntry, SmartReplyView.SmartActions smartActions, int i, Notification.Action action, boolean z, ContextThemeWrapper contextThemeWrapper);
}
