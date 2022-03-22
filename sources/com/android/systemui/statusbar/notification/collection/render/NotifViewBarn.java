package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import java.util.LinkedHashMap;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotifViewBarn.kt */
/* loaded from: classes.dex */
public final class NotifViewBarn {
    public final LinkedHashMap rowMap = new LinkedHashMap();

    public final NotifViewController requireNodeController(ListEntry listEntry) {
        NotifViewController notifViewController = (NotifViewController) this.rowMap.get(listEntry.getKey());
        if (notifViewController != null) {
            return notifViewController;
        }
        throw new IllegalStateException(Intrinsics.stringPlus("No view has been registered for entry: ", listEntry.getKey()).toString());
    }
}
