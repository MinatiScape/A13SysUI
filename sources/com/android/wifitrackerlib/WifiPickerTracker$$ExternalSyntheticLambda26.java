package com.android.wifitrackerlib;

import com.android.systemui.statusbar.notification.collection.GroupEntry;
import java.util.Objects;
import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda26 implements Predicate {
    public final /* synthetic */ int $r8$classId;
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda26 INSTANCE$1 = new WifiPickerTracker$$ExternalSyntheticLambda26(1);
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda26 INSTANCE = new WifiPickerTracker$$ExternalSyntheticLambda26(0);

    public /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda26(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        boolean z;
        switch (this.$r8$classId) {
            case 0:
                StandardWifiEntry standardWifiEntry = (StandardWifiEntry) obj;
                if (standardWifiEntry.getConnectedState() == 0) {
                    synchronized (standardWifiEntry) {
                        z = standardWifiEntry.mIsUserShareable;
                    }
                    if (z) {
                        return true;
                    }
                }
                return false;
            default:
                GroupEntry groupEntry = (GroupEntry) obj;
                Objects.requireNonNull(groupEntry);
                if (groupEntry.mSummary != null || !groupEntry.mUnmodifiableChildren.isEmpty()) {
                    return false;
                }
                return true;
        }
    }
}
