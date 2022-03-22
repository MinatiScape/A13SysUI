package com.android.systemui.statusbar;

import java.util.Iterator;
import java.util.Objects;
/* compiled from: NotificationClickNotifier.kt */
/* loaded from: classes.dex */
public final class NotificationClickNotifier$onNotificationClick$1 implements Runnable {
    public final /* synthetic */ String $key;
    public final /* synthetic */ NotificationClickNotifier this$0;

    public NotificationClickNotifier$onNotificationClick$1(NotificationClickNotifier notificationClickNotifier, String str) {
        this.this$0 = notificationClickNotifier;
        this.$key = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        NotificationClickNotifier notificationClickNotifier = this.this$0;
        String str = this.$key;
        Objects.requireNonNull(notificationClickNotifier);
        Iterator it = notificationClickNotifier.listeners.iterator();
        while (it.hasNext()) {
            ((NotificationInteractionListener) it.next()).onNotificationInteraction(str);
        }
    }
}
