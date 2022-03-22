package com.android.wifitrackerlib;

import com.android.systemui.util.service.ObservableServiceConnection;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda13 implements Predicate {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda13(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                Set set = (Set) this.f$0;
                Map.Entry entry = (Map.Entry) obj;
                PasspointWifiEntry passpointWifiEntry = (PasspointWifiEntry) entry.getValue();
                Objects.requireNonNull(passpointWifiEntry);
                if (passpointWifiEntry.mLevel == -1 || (!set.contains(entry.getKey()) && ((PasspointWifiEntry) entry.getValue()).getConnectedState() == 0)) {
                    return true;
                }
                return false;
            default:
                ObservableServiceConnection.Callback callback = (ObservableServiceConnection.Callback) this.f$0;
                boolean z = ObservableServiceConnection.DEBUG;
                if (((WeakReference) obj).get() == callback) {
                    return true;
                }
                return false;
        }
    }
}
