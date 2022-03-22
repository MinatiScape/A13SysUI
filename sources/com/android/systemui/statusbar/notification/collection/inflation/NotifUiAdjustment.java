package com.android.systemui.statusbar.notification.collection.inflation;

import android.app.Notification;
import java.util.List;
/* compiled from: NotifUiAdjustment.kt */
/* loaded from: classes.dex */
public final class NotifUiAdjustment {
    public final boolean isConversation;
    public final boolean isMinimized;
    public final List<Notification.Action> smartActions;
    public final List<CharSequence> smartReplies;

    public NotifUiAdjustment(List list, List list2, boolean z, boolean z2) {
        this.smartActions = list;
        this.smartReplies = list2;
        this.isConversation = z;
        this.isMinimized = z2;
    }
}
