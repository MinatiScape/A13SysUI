package com.android.systemui.volume;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.View;
import com.android.internal.accessibility.dialog.AccessibilityTarget;
import com.android.keyguard.EmergencyButtonController;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.media.dialog.MediaOutputDialog;
import com.android.systemui.media.dialog.MediaOutputDialogFactory;
import com.android.systemui.util.Assert;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class VolumeDialogImpl$$ExternalSyntheticLambda4 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ VolumeDialogImpl$$ExternalSyntheticLambda4(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                VolumeDialogImpl volumeDialogImpl = (VolumeDialogImpl) this.f$0;
                Objects.requireNonNull(volumeDialogImpl);
                Events.writeEvent(8, new Object[0]);
                Intent intent = new Intent("android.settings.panel.action.VOLUME");
                volumeDialogImpl.dismissH(5);
                Objects.requireNonNull(volumeDialogImpl.mMediaOutputDialogFactory);
                MediaOutputDialog mediaOutputDialog = MediaOutputDialogFactory.mediaOutputDialog;
                if (mediaOutputDialog != null) {
                    mediaOutputDialog.dismiss();
                }
                MediaOutputDialogFactory.mediaOutputDialog = null;
                volumeDialogImpl.mActivityStarter.startActivity(intent, true);
                return;
            case 1:
                EmergencyButtonController emergencyButtonController = (EmergencyButtonController) this.f$0;
                Objects.requireNonNull(emergencyButtonController);
                emergencyButtonController.mMetricsLogger.action(200);
                PowerManager powerManager = emergencyButtonController.mPowerManager;
                if (powerManager != null) {
                    powerManager.userActivity(SystemClock.uptimeMillis(), true);
                }
                emergencyButtonController.mActivityTaskManager.stopSystemLockTaskMode();
                emergencyButtonController.mShadeController.collapsePanel(false);
                TelecomManager telecomManager = emergencyButtonController.mTelecomManager;
                if (telecomManager == null || !telecomManager.isInCall()) {
                    KeyguardUpdateMonitor keyguardUpdateMonitor = emergencyButtonController.mKeyguardUpdateMonitor;
                    Objects.requireNonNull(keyguardUpdateMonitor);
                    Assert.isMainThread();
                    keyguardUpdateMonitor.handleReportEmergencyCallAction();
                    TelecomManager telecomManager2 = emergencyButtonController.mTelecomManager;
                    if (telecomManager2 == null) {
                        Log.wtf("EmergencyButton", "TelecomManager was null, cannot launch emergency dialer");
                        return;
                    }
                    emergencyButtonController.getContext().startActivityAsUser(telecomManager2.createLaunchEmergencyDialerIntent(null).setFlags(343932928).putExtra("com.android.phone.EmergencyDialer.extra.ENTRY_TYPE", 1), ActivityOptions.makeCustomAnimation(emergencyButtonController.getContext(), 0, 0).toBundle(), new UserHandle(KeyguardUpdateMonitor.getCurrentUser()));
                    return;
                }
                emergencyButtonController.mTelecomManager.showInCallScreen(false);
                EmergencyButtonController.EmergencyButtonCallback emergencyButtonCallback = emergencyButtonController.mEmergencyButtonCallback;
                if (emergencyButtonCallback != null) {
                    emergencyButtonCallback.onEmergencyButtonClickedWhenInCall();
                    return;
                }
                return;
            default:
                ((AccessibilityTarget) this.f$0).onSelected();
                return;
        }
    }
}
