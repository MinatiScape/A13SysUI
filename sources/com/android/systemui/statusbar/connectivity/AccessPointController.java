package com.android.systemui.statusbar.connectivity;

import com.android.systemui.qs.tiles.dialog.InternetDialogController;
import com.android.wifitrackerlib.MergedCarrierEntry;
import com.android.wifitrackerlib.WifiEntry;
import java.util.List;
/* compiled from: AccessPointController.kt */
/* loaded from: classes.dex */
public interface AccessPointController {

    /* compiled from: AccessPointController.kt */
    /* loaded from: classes.dex */
    public interface AccessPointCallback {
        void onAccessPointsChanged(List<WifiEntry> list);
    }

    void addAccessPointCallback(InternetDialogController internetDialogController);

    boolean canConfigMobileData();

    boolean canConfigWifi();

    MergedCarrierEntry getMergedCarrierEntry();

    void removeAccessPointCallback(InternetDialogController internetDialogController);

    void scanForAccessPoints();
}
