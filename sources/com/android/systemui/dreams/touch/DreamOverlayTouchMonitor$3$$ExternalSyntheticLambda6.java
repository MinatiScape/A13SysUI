package com.android.systemui.dreams.touch;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.icon.IconPack;
import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda6 implements Function {
    public static final /* synthetic */ DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda6 INSTANCE = new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda6(0);
    public static final /* synthetic */ DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda6 INSTANCE$1 = new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda6(1);
    public final /* synthetic */ int $r8$classId;

    public /* synthetic */ DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda6(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                return ((Collection) obj).stream();
            default:
                NotificationEntry notificationEntry = (NotificationEntry) obj;
                int i = NotificationIconAreaController.$r8$clinit;
                Objects.requireNonNull(notificationEntry);
                IconPack iconPack = notificationEntry.mIcons;
                Objects.requireNonNull(iconPack);
                return iconPack.mAodIcon;
        }
    }
}
