package com.android.systemui.theme;

import android.content.om.OverlayIdentifier;
import android.content.om.OverlayInfo;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.text.TextUtils;
import com.android.systemui.communal.CommunalSourceMonitor;
import com.android.wifitrackerlib.PasspointNetworkDetailsTracker;
import com.android.wifitrackerlib.PasspointWifiEntry;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ThemeOverlayApplier$$ExternalSyntheticLambda4 implements Predicate {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ ThemeOverlayApplier$$ExternalSyntheticLambda4(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                return !((Map) this.f$0).containsValue(new OverlayIdentifier(((OverlayInfo) obj).packageName));
            case 1:
                CommunalSourceMonitor.Callback callback = (CommunalSourceMonitor.Callback) this.f$0;
                boolean z = CommunalSourceMonitor.DEBUG;
                if (((WeakReference) obj).get() == callback) {
                    return true;
                }
                return false;
            default:
                PasspointNetworkDetailsTracker passpointNetworkDetailsTracker = (PasspointNetworkDetailsTracker) this.f$0;
                Objects.requireNonNull(passpointNetworkDetailsTracker);
                String uniqueIdToPasspointWifiEntryKey = PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(((PasspointConfiguration) obj).getUniqueId());
                PasspointWifiEntry passpointWifiEntry = passpointNetworkDetailsTracker.mChosenEntry;
                Objects.requireNonNull(passpointWifiEntry);
                return TextUtils.equals(uniqueIdToPasspointWifiEntryKey, passpointWifiEntry.mKey);
        }
    }
}
