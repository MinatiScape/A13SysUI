package com.android.wifitrackerlib;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class SavedNetworkTracker$$ExternalSyntheticLambda1 implements Predicate {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ SavedNetworkTracker$$ExternalSyntheticLambda1(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                StandardWifiEntry standardWifiEntry = (StandardWifiEntry) obj;
                Objects.requireNonNull(standardWifiEntry);
                standardWifiEntry.updateConfig((List) ((Map) this.f$0).remove(standardWifiEntry.mKey));
                return !standardWifiEntry.isSaved();
            default:
                NotificationEntry notificationEntry = (NotificationEntry) obj;
                Objects.requireNonNull(notificationEntry);
                return Objects.equals(notificationEntry.mKey, (String) this.f$0);
        }
    }
}
