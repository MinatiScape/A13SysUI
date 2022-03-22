package com.android.wifitrackerlib;

import android.net.wifi.hotspot2.PasspointConfiguration;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.HeadsUpAppearanceController;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda1(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                WifiPickerTracker wifiPickerTracker = (WifiPickerTracker) this.f$0;
                OsuWifiEntry osuWifiEntry = (OsuWifiEntry) obj;
                Objects.requireNonNull(wifiPickerTracker);
                Objects.requireNonNull(osuWifiEntry);
                PasspointConfiguration passpointConfiguration = (PasspointConfiguration) ((Map) this.f$1).get(osuWifiEntry.mOsuProvider);
                if (passpointConfiguration == null) {
                    synchronized (osuWifiEntry) {
                        osuWifiEntry.mIsAlreadyProvisioned = false;
                    }
                    return;
                }
                synchronized (osuWifiEntry) {
                    osuWifiEntry.mIsAlreadyProvisioned = true;
                }
                PasspointWifiEntry passpointWifiEntry = (PasspointWifiEntry) wifiPickerTracker.mPasspointWifiEntryCache.get(PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(passpointConfiguration.getUniqueId()));
                if (passpointWifiEntry != null) {
                    synchronized (passpointWifiEntry) {
                        passpointWifiEntry.mOsuWifiEntry = osuWifiEntry;
                        synchronized (osuWifiEntry) {
                            osuWifiEntry.mListener = passpointWifiEntry;
                        }
                    }
                    return;
                }
                return;
            default:
                NotificationStackScrollLayoutController.NotificationListContainerImpl notificationListContainerImpl = (NotificationStackScrollLayoutController.NotificationListContainerImpl) this.f$0;
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) this.f$1;
                Boolean bool = (Boolean) obj;
                Objects.requireNonNull(notificationListContainerImpl);
                NotificationStackScrollLayoutController.this.mNotificationRoundnessManager.updateView(expandableNotificationRow, false);
                HeadsUpAppearanceController headsUpAppearanceController = NotificationStackScrollLayoutController.this.mHeadsUpAppearanceController;
                Objects.requireNonNull(expandableNotificationRow);
                headsUpAppearanceController.updateHeader(expandableNotificationRow.mEntry);
                return;
        }
    }
}
