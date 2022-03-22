package com.android.wm.shell.pip.phone;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.android.systemui.media.dialog.MediaOutputBaseDialog;
import com.android.systemui.media.dialog.MediaOutputController;
import com.android.wm.shell.pip.PipUiEventLogger;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PipMenuView$$ExternalSyntheticLambda4 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ PipMenuView$$ExternalSyntheticLambda4(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        Intent intent;
        switch (this.$r8$classId) {
            case 0:
                PipMenuView pipMenuView = (PipMenuView) this.f$0;
                Objects.requireNonNull(pipMenuView);
                if (pipMenuView.mMenuContainer.getAlpha() != 0.0f) {
                    PhonePipMenuController phonePipMenuController = pipMenuView.mController;
                    Objects.requireNonNull(phonePipMenuController);
                    pipMenuView.hideMenu(new PipMenuView$$ExternalSyntheticLambda7(phonePipMenuController, 0), false, true, 1);
                    pipMenuView.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_EXPAND_TO_FULLSCREEN);
                    return;
                }
                return;
            default:
                MediaOutputBaseDialog mediaOutputBaseDialog = (MediaOutputBaseDialog) this.f$0;
                int i = MediaOutputBaseDialog.$r8$clinit;
                Objects.requireNonNull(mediaOutputBaseDialog);
                mediaOutputBaseDialog.mContext.sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
                MediaOutputController mediaOutputController = mediaOutputBaseDialog.mMediaOutputController;
                Objects.requireNonNull(mediaOutputController);
                Intent intent2 = null;
                if (mediaOutputController.mPackageName.isEmpty()) {
                    intent = null;
                } else {
                    intent = mediaOutputController.mContext.getPackageManager().getLaunchIntentForPackage(mediaOutputController.mPackageName);
                }
                if (intent != null) {
                    Context context = mediaOutputBaseDialog.mContext;
                    MediaOutputController mediaOutputController2 = mediaOutputBaseDialog.mMediaOutputController;
                    Objects.requireNonNull(mediaOutputController2);
                    if (!mediaOutputController2.mPackageName.isEmpty()) {
                        intent2 = mediaOutputController2.mContext.getPackageManager().getLaunchIntentForPackage(mediaOutputController2.mPackageName);
                    }
                    context.startActivity(intent2);
                }
                mediaOutputBaseDialog.dismiss();
                return;
        }
    }
}
