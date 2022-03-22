package com.android.systemui.power;

import android.content.Context;
import android.os.UserHandle;
import androidx.lifecycle.Lifecycle;
import com.android.keyguard.AdminSecondaryLockScreenController;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.tiles.dialog.InternetDialogController;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.screenrecord.ScreenRecordDialog;
import com.android.systemui.statusbar.connectivity.AccessPointControllerImpl;
import com.android.systemui.wallet.ui.WalletScreenController;
import com.android.wifitrackerlib.MergedCarrierEntry;
import com.android.wifitrackerlib.WifiEntry;
import com.android.wifitrackerlib.WifiPickerTracker;
import com.android.wm.shell.transition.Transitions;
import com.google.android.systemui.gamedashboard.ScreenRecordController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PowerUI$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ PowerUI$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                PowerUI powerUI = (PowerUI) this.f$0;
                boolean z = PowerUI.DEBUG;
                Objects.requireNonNull(powerUI);
                powerUI.doSkinThermalEventListenerRegistration();
                powerUI.doUsbThermalEventListenerRegistration();
                return;
            case 1:
                AdminSecondaryLockScreenController.AnonymousClass2 r3 = (AdminSecondaryLockScreenController.AnonymousClass2) this.f$0;
                int i = AdminSecondaryLockScreenController.AnonymousClass2.$r8$clinit;
                Objects.requireNonNull(r3);
                AdminSecondaryLockScreenController.this.dismiss(UserHandle.getCallingUserId());
                return;
            case 2:
                AccessPointControllerImpl accessPointControllerImpl = (AccessPointControllerImpl) this.f$0;
                boolean z2 = AccessPointControllerImpl.DEBUG;
                Objects.requireNonNull(accessPointControllerImpl);
                accessPointControllerImpl.mLifecycle.setCurrentState(Lifecycle.State.CREATED);
                return;
            case 3:
                int i2 = WalletScreenController.$r8$clinit;
                ((WalletScreenController) this.f$0).selectCard();
                return;
            case 4:
                MergedCarrierEntry mergedCarrierEntry = (MergedCarrierEntry) this.f$0;
                Objects.requireNonNull(mergedCarrierEntry);
                WifiEntry.ConnectCallback connectCallback = mergedCarrierEntry.mConnectCallback;
                if (connectCallback != null) {
                    ((InternetDialogController.WifiEntryConnectCallback) connectCallback).onConnectResult(0);
                    return;
                }
                return;
            case 5:
                AccessPointControllerImpl accessPointControllerImpl2 = (AccessPointControllerImpl) ((WifiPickerTracker.WifiPickerTrackerCallback) this.f$0);
                Objects.requireNonNull(accessPointControllerImpl2);
                accessPointControllerImpl2.scanForAccessPoints();
                return;
            case FalsingManager.VERSION /* 6 */:
                ((Transitions.TransitionFinishCallback) this.f$0).onTransitionFinished(null);
                return;
            default:
                final ScreenRecordController screenRecordController = (ScreenRecordController) this.f$0;
                Objects.requireNonNull(screenRecordController);
                screenRecordController.mKeyguardDismissUtil.executeWhenUnlocked(new ActivityStarter.OnDismissAction() { // from class: com.google.android.systemui.gamedashboard.ScreenRecordController$$ExternalSyntheticLambda0
                    @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
                    public final boolean onDismiss() {
                        ScreenRecordController screenRecordController2 = ScreenRecordController.this;
                        Objects.requireNonNull(screenRecordController2);
                        RecordingController recordingController = screenRecordController2.mController;
                        Context context = screenRecordController2.mContext;
                        Objects.requireNonNull(recordingController);
                        new ScreenRecordDialog(context, recordingController, recordingController.mUserContextProvider, null).show();
                        return false;
                    }
                }, false, true);
                return;
        }
    }
}
