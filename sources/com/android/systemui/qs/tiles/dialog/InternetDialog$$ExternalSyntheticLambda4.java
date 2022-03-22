package com.android.systemui.qs.tiles.dialog;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.animation.DialogLaunchAnimator$createActivityLaunchController$1;
import com.android.systemui.media.dialog.MediaOutputBaseDialog;
import com.android.systemui.media.dialog.MediaOutputController;
import com.android.systemui.shared.rotation.RotationButtonController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class InternetDialog$$ExternalSyntheticLambda4 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ InternetDialog$$ExternalSyntheticLambda4(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                InternetDialog internetDialog = (InternetDialog) this.f$0;
                Objects.requireNonNull(internetDialog);
                InternetDialogController internetDialogController = internetDialog.mInternetDialogController;
                Objects.requireNonNull(internetDialogController);
                internetDialogController.mConnectivityManager.setAirplaneMode(false);
                return;
            case 1:
                MediaOutputController mediaOutputController = (MediaOutputController) this.f$0;
                Objects.requireNonNull(mediaOutputController);
                DialogLaunchAnimator dialogLaunchAnimator = mediaOutputController.mDialogLaunchAnimator;
                Objects.requireNonNull(dialogLaunchAnimator);
                DialogLaunchAnimator$createActivityLaunchController$1 createActivityLaunchController$default = DialogLaunchAnimator.createActivityLaunchController$default(dialogLaunchAnimator, view);
                if (createActivityLaunchController$default == null) {
                    MediaOutputBaseDialog mediaOutputBaseDialog = (MediaOutputBaseDialog) mediaOutputController.mCallback;
                    Objects.requireNonNull(mediaOutputBaseDialog);
                    mediaOutputBaseDialog.dismiss();
                }
                Intent addFlags = new Intent("android.settings.BLUETOOTH_PAIRING_SETTINGS").addFlags(335544320);
                Intent intent = new Intent("android.settings.SETTINGS_EMBED_DEEP_LINK_ACTIVITY");
                if (intent.resolveActivity(mediaOutputController.mContext.getPackageManager()) != null) {
                    Log.d("MediaOutputController", "Device support split mode, launch page with deep link");
                    intent.setFlags(268435456);
                    intent.putExtra("android.provider.extra.SETTINGS_EMBEDDED_DEEP_LINK_INTENT_URI", addFlags.toUri(1));
                    intent.putExtra("android.provider.extra.SETTINGS_EMBEDDED_DEEP_LINK_HIGHLIGHT_MENU_KEY", "top_level_connected_devices");
                    mediaOutputController.mActivityStarter.startActivity(intent, true, (ActivityLaunchAnimator.Controller) createActivityLaunchController$default);
                    return;
                }
                mediaOutputController.mActivityStarter.startActivity(addFlags, true, (ActivityLaunchAnimator.Controller) createActivityLaunchController$default);
                return;
            default:
                RotationButtonController.$r8$lambda$zgkIWtDwDdf8jAM6lj_qLTw8at8((RotationButtonController) this.f$0, view);
                return;
        }
    }
}
