package com.android.systemui.people;

import android.app.people.ConversationStatus;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PeopleTileViewHelper$$ExternalSyntheticLambda3 implements Predicate {
    public static final /* synthetic */ PeopleTileViewHelper$$ExternalSyntheticLambda3 INSTANCE = new PeopleTileViewHelper$$ExternalSyntheticLambda3(0);
    public static final /* synthetic */ PeopleTileViewHelper$$ExternalSyntheticLambda3 INSTANCE$1 = new PeopleTileViewHelper$$ExternalSyntheticLambda3(1);
    public final /* synthetic */ int $r8$classId;

    public /* synthetic */ PeopleTileViewHelper$$ExternalSyntheticLambda3(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                Pattern pattern = PeopleTileViewHelper.DOUBLE_EXCLAMATION_PATTERN;
                if (((ConversationStatus) obj).getAvailability() == 0) {
                    return true;
                }
                return false;
            default:
                NotificationEntry notificationEntry = (NotificationEntry) obj;
                int i = NotifCollection.$r8$clinit;
                Objects.requireNonNull(notificationEntry);
                return notificationEntry.mSbn.getNotification().isGroupSummary();
        }
    }
}
