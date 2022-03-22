package com.android.systemui.theme;

import android.content.om.OverlayInfo;
import android.net.wifi.WifiConfiguration;
import android.text.TextUtils;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.wifitrackerlib.PasspointWifiEntry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ThemeOverlayApplier$$ExternalSyntheticLambda6 implements Predicate {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ ThemeOverlayApplier$$ExternalSyntheticLambda6(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                return ((Set) this.f$0).contains(((OverlayInfo) obj).category);
            case 1:
                NotificationEntry notificationEntry = (NotificationEntry) obj;
                int i = NotifCollection.$r8$clinit;
                Objects.requireNonNull(notificationEntry);
                return Objects.equals(notificationEntry.mSbn.getGroup(), (String) this.f$0);
            default:
                String str = (String) this.f$0;
                WifiConfiguration wifiConfiguration = (WifiConfiguration) obj;
                if (!wifiConfiguration.isPasspoint() || !TextUtils.equals(str, PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(wifiConfiguration.getKey()))) {
                    return false;
                }
                return true;
        }
    }
}
