package com.android.systemui.statusbar.notification.collection.inflation;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.util.ListenerSet;
/* compiled from: BindEventManager.kt */
/* loaded from: classes.dex */
public class BindEventManager {
    public final ListenerSet<Listener> listeners = new ListenerSet<>();

    /* compiled from: BindEventManager.kt */
    /* loaded from: classes.dex */
    public interface Listener {
        void onViewBound(NotificationEntry notificationEntry);
    }
}
