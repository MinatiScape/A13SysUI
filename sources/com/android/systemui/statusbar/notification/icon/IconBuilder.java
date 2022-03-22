package com.android.systemui.statusbar.notification.icon;

import android.content.Context;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
/* compiled from: IconBuilder.kt */
/* loaded from: classes.dex */
public final class IconBuilder {
    public final Context context;

    public final StatusBarIconView createIconView(NotificationEntry notificationEntry) {
        Context context = this.context;
        return new StatusBarIconView(context, ((Object) notificationEntry.mSbn.getPackageName()) + "/0x" + ((Object) Integer.toHexString(notificationEntry.mSbn.getId())), notificationEntry.mSbn);
    }

    public IconBuilder(Context context) {
        this.context = context;
    }
}
