package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.icon.IconPack;
import java.util.Objects;
import java.util.function.Function;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationIconAreaController$$ExternalSyntheticLambda2 implements Function {
    public static final /* synthetic */ NotificationIconAreaController$$ExternalSyntheticLambda2 INSTANCE = new NotificationIconAreaController$$ExternalSyntheticLambda2();

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        NotificationEntry notificationEntry = (NotificationEntry) obj;
        int i = NotificationIconAreaController.$r8$clinit;
        Objects.requireNonNull(notificationEntry);
        IconPack iconPack = notificationEntry.mIcons;
        Objects.requireNonNull(iconPack);
        return iconPack.mStatusBarIcon;
    }
}
