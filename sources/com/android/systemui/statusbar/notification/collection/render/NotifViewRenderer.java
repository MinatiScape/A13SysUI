package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.List;
/* compiled from: NotifViewRenderer.kt */
/* loaded from: classes.dex */
public interface NotifViewRenderer {
    NotifViewController getGroupController(GroupEntry groupEntry);

    NotifViewController getRowController(NotificationEntry notificationEntry);

    NotifStackController getStackController();

    void onDispatchComplete();

    void onRenderList(List<? extends ListEntry> list);
}
