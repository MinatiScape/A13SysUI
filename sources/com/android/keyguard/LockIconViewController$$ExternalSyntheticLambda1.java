package com.android.keyguard;

import android.view.View;
import android.widget.ImageButton;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.QuickStatusBarHeader;
import com.android.systemui.qs.tiles.dialog.InternetDialog;
import com.android.systemui.statusbar.connectivity.NetworkControllerImpl;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.util.AlarmTimeout;
import com.android.systemui.volume.VolumeDialogImpl;
import com.android.wifitrackerlib.SavedNetworkTracker;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class LockIconViewController$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ LockIconViewController$$ExternalSyntheticLambda1(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        long j;
        switch (this.$r8$classId) {
            case 0:
                LockIconViewController.$r8$lambda$h98ceOtiS5JD1Nfnu1Y0fyk_1uo((LockIconViewController) this.f$0);
                return;
            case 1:
                QuickStatusBarHeader quickStatusBarHeader = (QuickStatusBarHeader) this.f$0;
                int i = QuickStatusBarHeader.$r8$clinit;
                Objects.requireNonNull(quickStatusBarHeader);
                quickStatusBarHeader.setClickable(!quickStatusBarHeader.mExpanded);
                return;
            case 2:
                InternetDialog internetDialog = (InternetDialog) this.f$0;
                boolean z = InternetDialog.DEBUG;
                Objects.requireNonNull(internetDialog);
                internetDialog.updateDialog(true);
                return;
            case 3:
                NetworkControllerImpl networkControllerImpl = (NetworkControllerImpl) this.f$0;
                boolean z2 = NetworkControllerImpl.DEBUG;
                Objects.requireNonNull(networkControllerImpl);
                networkControllerImpl.mInternetDialogFactory.create(networkControllerImpl.mAccessPoints.canConfigMobileData(), networkControllerImpl.mAccessPoints.canConfigWifi(), null);
                return;
            case 4:
                AutoTileManager.AnonymousClass3 r3 = (AutoTileManager.AnonymousClass3) this.f$0;
                Objects.requireNonNull(r3);
                AutoTileManager autoTileManager = AutoTileManager.this;
                autoTileManager.mHotspotController.removeCallback(autoTileManager.mHotspotCallback);
                return;
            case 5:
                ScrimController scrimController = (ScrimController) this.f$0;
                boolean z3 = ScrimController.DEBUG;
                Objects.requireNonNull(scrimController);
                AlarmTimeout alarmTimeout = scrimController.mTimeTicker;
                DozeParameters dozeParameters = scrimController.mDozeParameters;
                Objects.requireNonNull(dozeParameters);
                if (dozeParameters.mControlScreenOffAnimation) {
                    j = 2500;
                } else {
                    j = dozeParameters.mAlwaysOnPolicy.wallpaperVisibilityDuration;
                }
                alarmTimeout.schedule(j);
                return;
            case FalsingManager.VERSION /* 6 */:
                ((ShadeController) this.f$0).runPostCollapseRunnables();
                return;
            case 7:
                ImageButton imageButton = (ImageButton) this.f$0;
                String str = VolumeDialogImpl.TAG;
                if (imageButton != null) {
                    imageButton.setPressed(false);
                    return;
                }
                return;
            case 8:
                ((SavedNetworkTracker.SavedNetworkTrackerCallback) this.f$0).onSavedWifiEntriesChanged();
                return;
            default:
                ((View) this.f$0).setTag(2131428691, null);
                return;
        }
    }
}
