package com.android.systemui.util.condition;

import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.Objects;
import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class Monitor$$ExternalSyntheticLambda5 implements Predicate {
    public final /* synthetic */ int $r8$classId;
    public static final /* synthetic */ Monitor$$ExternalSyntheticLambda5 INSTANCE$1 = new Monitor$$ExternalSyntheticLambda5(1);
    public static final /* synthetic */ Monitor$$ExternalSyntheticLambda5 INSTANCE = new Monitor$$ExternalSyntheticLambda5(0);

    public /* synthetic */ Monitor$$ExternalSyntheticLambda5(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                return ((Boolean) obj).booleanValue();
            default:
                NotificationEntry notificationEntry = (NotificationEntry) obj;
                int i = NotifCollection.$r8$clinit;
                Objects.requireNonNull(notificationEntry);
                return !notificationEntry.mSbn.getNotification().isGroupSummary();
        }
    }
}
