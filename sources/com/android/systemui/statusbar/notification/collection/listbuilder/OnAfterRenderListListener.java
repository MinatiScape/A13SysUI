package com.android.systemui.statusbar.notification.collection.listbuilder;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.render.NotifStackController;
import java.util.List;
/* loaded from: classes.dex */
public interface OnAfterRenderListListener {
    void onAfterRenderList(List<ListEntry> list, NotifStackController notifStackController);
}
