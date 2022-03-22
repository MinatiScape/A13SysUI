package com.android.wm.shell;

import android.content.Intent;
import android.view.SurfaceControl;
import android.view.View;
import com.android.keyguard.CarrierTextManager;
import com.android.systemui.biometrics.AuthContainerView;
import com.android.systemui.doze.DozeUi$$ExternalSyntheticLambda1;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.tiles.WifiTile;
import com.android.systemui.statusbar.notification.row.NotificationConversationInfo;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.volume.VolumeDialogImpl;
import com.android.wm.shell.common.split.SplitLayout;
import com.android.wm.shell.pip.phone.PhonePipMenuController;
import com.google.android.systemui.dreamliner.DockGestureController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class TaskView$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ TaskView$$ExternalSyntheticLambda3(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                TaskView taskView = (TaskView) this.f$0;
                int i = TaskView.$r8$clinit;
                Objects.requireNonNull(taskView);
                taskView.mTaskOrganizer.removeListener(taskView);
                taskView.mTaskInfo = null;
                taskView.mTaskToken = null;
                taskView.mTaskLeash = null;
                return;
            case 1:
                CarrierTextManager carrierTextManager = (CarrierTextManager) this.f$0;
                boolean z = CarrierTextManager.DEBUG;
                Objects.requireNonNull(carrierTextManager);
                carrierTextManager.mKeyguardUpdateMonitor.registerCallback(carrierTextManager.mCallback);
                carrierTextManager.mWakefulnessLifecycle.addObserver(carrierTextManager.mWakefulnessObserver);
                return;
            case 2:
                ((AuthContainerView) this.f$0).onDialogAnimatedIn();
                return;
            case 3:
                WifiTile wifiTile = (WifiTile) this.f$0;
                Intent intent = WifiTile.WIFI_SETTINGS;
                Objects.requireNonNull(wifiTile);
                if (wifiTile.mExpectDisabled) {
                    wifiTile.mExpectDisabled = false;
                    wifiTile.refreshState(null);
                    return;
                }
                return;
            case 4:
                NotificationConversationInfo notificationConversationInfo = (NotificationConversationInfo) this.f$0;
                int i2 = NotificationConversationInfo.$r8$clinit;
                Objects.requireNonNull(notificationConversationInfo);
                notificationConversationInfo.mOnUserInteractionCallback.onImportanceChanged(notificationConversationInfo.mEntry);
                return;
            case 5:
                int i3 = SystemUIDialog.$r8$clinit;
                ((SystemUIDialog) this.f$0).updateWindowSize();
                return;
            case FalsingManager.VERSION /* 6 */:
                VolumeDialogImpl volumeDialogImpl = (VolumeDialogImpl) this.f$0;
                String str = VolumeDialogImpl.TAG;
                Objects.requireNonNull(volumeDialogImpl);
                volumeDialogImpl.mHandler.postDelayed(new DozeUi$$ExternalSyntheticLambda1(volumeDialogImpl, 9), 50L);
                return;
            case 7:
                ((View) this.f$0).setTranslationZ(0.0f);
                return;
            case 8:
                SplitLayout splitLayout = (SplitLayout) this.f$0;
                Objects.requireNonNull(splitLayout);
                splitLayout.mSplitLayoutHandler.onSnappedToDismiss(false);
                return;
            case 9:
                ((PhonePipMenuController) this.f$0).hideMenu();
                return;
            case 10:
                ((SurfaceControl) this.f$0).release();
                return;
            default:
                int i4 = DockGestureController.$r8$clinit;
                ((DockGestureController) this.f$0).hideGear();
                return;
        }
    }
}
