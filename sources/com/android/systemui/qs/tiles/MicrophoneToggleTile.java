package com.android.systemui.qs.tiles;

import android.os.Handler;
import android.os.Looper;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.DejankUtils;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.wm.shell.pip.PipAnimationController$$ExternalSyntheticLambda0;
/* loaded from: classes.dex */
public final class MicrophoneToggleTile extends SensorPrivacyToggleTile {
    public static final /* synthetic */ int $r8$clinit = 0;

    public MicrophoneToggleTile(QSHost qSHost, Looper looper, Handler handler, MetricsLogger metricsLogger, FalsingManager falsingManager, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, IndividualSensorPrivacyController individualSensorPrivacyController, KeyguardStateController keyguardStateController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger, individualSensorPrivacyController, keyguardStateController);
    }

    @Override // com.android.systemui.qs.tiles.SensorPrivacyToggleTile
    public final int getIconRes(boolean z) {
        return z ? 17302769 : 17302768;
    }

    @Override // com.android.systemui.qs.tiles.SensorPrivacyToggleTile
    public final String getRestriction() {
        return "disallow_microphone_toggle";
    }

    @Override // com.android.systemui.qs.tiles.SensorPrivacyToggleTile
    public final int getSensorId() {
        return 1;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final CharSequence getTileLabel() {
        return this.mContext.getString(2131953119);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final boolean isAvailable() {
        if (!this.mSensorPrivacyController.supportsSensorToggle(1) || !((Boolean) DejankUtils.whitelistIpcs(PipAnimationController$$ExternalSyntheticLambda0.INSTANCE$1)).booleanValue()) {
            return false;
        }
        return true;
    }
}
