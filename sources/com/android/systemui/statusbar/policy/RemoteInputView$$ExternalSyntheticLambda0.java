package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import com.android.systemui.clipboardoverlay.ClipboardOverlayController;
import com.google.android.systemui.gamedashboard.GameMenuActivity;
import com.google.android.systemui.gamedashboard.GameModeDndController;
import com.google.android.systemui.gamedashboard.ShortcutBarController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class RemoteInputView$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ RemoteInputView$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                RemoteInputView remoteInputView = (RemoteInputView) this.f$0;
                Object obj = RemoteInputView.VIEW_TAG;
                Objects.requireNonNull(remoteInputView);
                remoteInputView.setAttachment(null);
                return;
            case 1:
                ClipboardOverlayController clipboardOverlayController = (ClipboardOverlayController) this.f$0;
                Objects.requireNonNull(clipboardOverlayController);
                Context context = clipboardOverlayController.mContext;
                Intent intent = new Intent("android.intent.action.REMOTE_COPY");
                intent.addFlags(268468224);
                context.startActivity(intent);
                clipboardOverlayController.animateOut();
                return;
            default:
                GameMenuActivity gameMenuActivity = (GameMenuActivity) this.f$0;
                IntentFilter intentFilter = GameMenuActivity.DND_FILTER;
                Objects.requireNonNull(gameMenuActivity);
                boolean z = !gameMenuActivity.mDndController.isGameModeDndOn();
                ShortcutBarController shortcutBarController = gameMenuActivity.mShortcutBarController;
                if (z) {
                    shortcutBarController.mToast.showShortcutText(2131952374);
                } else {
                    Objects.requireNonNull(shortcutBarController);
                }
                GameModeDndController gameModeDndController = gameMenuActivity.mDndController;
                Objects.requireNonNull(gameModeDndController);
                gameModeDndController.mFilterActive = z;
                gameModeDndController.mGameActive = z;
                gameModeDndController.updateRule();
                return;
        }
    }
}
