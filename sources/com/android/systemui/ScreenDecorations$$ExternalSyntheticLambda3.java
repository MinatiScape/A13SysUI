package com.android.systemui;

import androidx.lifecycle.Lifecycle;
import com.android.systemui.shared.plugins.PluginActionManager;
import com.android.systemui.statusbar.connectivity.AccessPointControllerImpl;
import com.android.systemui.statusbar.phone.StatusBarSignalPolicy;
import com.android.wifitrackerlib.StandardWifiEntry$$ExternalSyntheticLambda0;
import com.android.wm.shell.bubbles.BubbleStackView;
import com.google.android.systemui.gamedashboard.ScreenRecordController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ScreenDecorations$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ ScreenDecorations$$ExternalSyntheticLambda3(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        int i;
        switch (this.$r8$classId) {
            case 0:
                ScreenDecorations screenDecorations = (ScreenDecorations) this.f$0;
                boolean z = ScreenDecorations.DEBUG_DISABLE_SCREEN_DECORATIONS;
                Objects.requireNonNull(screenDecorations);
                screenDecorations.mTunerService.removeTunable(screenDecorations);
                return;
            case 1:
                PluginActionManager.$r8$lambda$kBZKG6dtfbpWOlthMBUiG2NGTM4((PluginActionManager) this.f$0);
                return;
            case 2:
                AccessPointControllerImpl accessPointControllerImpl = (AccessPointControllerImpl) this.f$0;
                boolean z2 = AccessPointControllerImpl.DEBUG;
                Objects.requireNonNull(accessPointControllerImpl);
                accessPointControllerImpl.mLifecycle.setCurrentState(Lifecycle.State.DESTROYED);
                return;
            case 3:
                StatusBarSignalPolicy statusBarSignalPolicy = (StatusBarSignalPolicy) this.f$0;
                boolean z3 = StatusBarSignalPolicy.DEBUG;
                Objects.requireNonNull(statusBarSignalPolicy);
                boolean isVpnEnabled = statusBarSignalPolicy.mSecurityController.isVpnEnabled();
                if (statusBarSignalPolicy.mSecurityController.isVpnBranded()) {
                    i = 2131232674;
                } else {
                    i = 2131232697;
                }
                statusBarSignalPolicy.mIconController.setIcon(statusBarSignalPolicy.mSlotVpn, i, statusBarSignalPolicy.mContext.getResources().getString(2131951820));
                statusBarSignalPolicy.mIconController.setIconVisibility(statusBarSignalPolicy.mSlotVpn, isVpnEnabled);
                return;
            case 4:
                BubbleStackView bubbleStackView = (BubbleStackView) this.f$0;
                int i2 = BubbleStackView.FLYOUT_HIDE_AFTER;
                Objects.requireNonNull(bubbleStackView);
                bubbleStackView.post(new StandardWifiEntry$$ExternalSyntheticLambda0(bubbleStackView, 9));
                return;
            default:
                ScreenRecordController screenRecordController = (ScreenRecordController) this.f$0;
                Objects.requireNonNull(screenRecordController);
                screenRecordController.mLifecycle.setCurrentState(Lifecycle.State.CREATED);
                return;
        }
    }
}
