package com.android.wm.shell.bubbles;

import com.android.wifitrackerlib.StandardWifiEntry;
import com.android.wifitrackerlib.WifiPickerTracker;
import java.util.Objects;
import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BubbleData$$ExternalSyntheticLambda7 implements Predicate {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ BubbleData$$ExternalSyntheticLambda7(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        boolean z;
        switch (this.$r8$classId) {
            case 0:
                Bubble bubble = (Bubble) obj;
                Objects.requireNonNull(bubble);
                return bubble.mPackageName.equals((String) this.f$0);
            default:
                WifiPickerTracker wifiPickerTracker = (WifiPickerTracker) this.f$0;
                StandardWifiEntry standardWifiEntry = (StandardWifiEntry) obj;
                Objects.requireNonNull(wifiPickerTracker);
                Objects.requireNonNull(standardWifiEntry);
                synchronized (standardWifiEntry) {
                    z = standardWifiEntry.mIsUserShareable;
                }
                if (z || standardWifiEntry == wifiPickerTracker.mConnectedWifiEntry) {
                    return true;
                }
                return false;
        }
    }
}
