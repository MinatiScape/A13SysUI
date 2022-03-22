package com.android.systemui.statusbar.phone;

import android.graphics.drawable.ColorDrawable;
import android.os.RemoteException;
import android.telephony.CarrierConfigManager;
import android.util.Log;
import com.android.systemui.qs.tiles.dialog.InternetDialogController;
import com.android.systemui.util.CarrierConfigTracker;
import com.android.wifitrackerlib.MergedCarrierEntry;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class StatusBar$$ExternalSyntheticLambda24 implements Runnable {
    public final /* synthetic */ int $r8$classId = 1;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ StatusBar$$ExternalSyntheticLambda24(InternetDialogController internetDialogController, int i, boolean z) {
        this.f$0 = internetDialogController;
        this.f$2 = i;
        this.f$1 = z;
    }

    public /* synthetic */ StatusBar$$ExternalSyntheticLambda24(StatusBar statusBar, boolean z, int i) {
        this.f$0 = statusBar;
        this.f$1 = z;
        this.f$2 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        boolean z;
        switch (this.$r8$classId) {
            case 0:
                StatusBar statusBar = (StatusBar) this.f$0;
                boolean z2 = this.f$1;
                int i = this.f$2;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                Objects.requireNonNull(statusBar);
                try {
                    statusBar.mBarService.onPanelRevealed(z2, i);
                    return;
                } catch (RemoteException unused) {
                    return;
                }
            default:
                InternetDialogController internetDialogController = (InternetDialogController) this.f$0;
                int i2 = this.f$2;
                boolean z3 = this.f$1;
                ColorDrawable colorDrawable = InternetDialogController.EMPTY_DRAWABLE;
                Objects.requireNonNull(internetDialogController);
                CarrierConfigTracker carrierConfigTracker = internetDialogController.mCarrierConfigTracker;
                Objects.requireNonNull(carrierConfigTracker);
                synchronized (carrierConfigTracker.mCarrierProvisionsWifiMergedNetworks) {
                    if (carrierConfigTracker.mCarrierProvisionsWifiMergedNetworks.indexOfKey(i2) >= 0) {
                        z = carrierConfigTracker.mCarrierProvisionsWifiMergedNetworks.get(i2);
                    } else {
                        if (!carrierConfigTracker.mDefaultCarrierProvisionsWifiMergedNetworksLoaded) {
                            carrierConfigTracker.mDefaultCarrierProvisionsWifiMergedNetworks = CarrierConfigManager.getDefaultConfig().getBoolean("carrier_provisions_wifi_merged_networks_bool");
                            carrierConfigTracker.mDefaultCarrierProvisionsWifiMergedNetworksLoaded = true;
                        }
                        z = carrierConfigTracker.mDefaultCarrierProvisionsWifiMergedNetworks;
                    }
                }
                if (!z) {
                    MergedCarrierEntry mergedCarrierEntry = internetDialogController.mAccessPointController.getMergedCarrierEntry();
                    if (mergedCarrierEntry != null) {
                        mergedCarrierEntry.mWifiManager.setCarrierNetworkOffloadEnabled(mergedCarrierEntry.mSubscriptionId, true, z3);
                        if (!z3) {
                            mergedCarrierEntry.mWifiManager.stopRestrictingAutoJoinToSubscriptionId();
                            mergedCarrierEntry.mWifiManager.startScan();
                            return;
                        }
                        return;
                    } else if (InternetDialogController.DEBUG) {
                        Log.d("InternetDialogController", "MergedCarrierEntry is null, can not set the status.");
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
        }
    }
}
