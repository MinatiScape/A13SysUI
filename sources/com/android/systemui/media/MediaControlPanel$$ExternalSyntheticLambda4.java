package com.android.systemui.media;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import com.google.android.systemui.gamedashboard.GameDashboardButton;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class MediaControlPanel$$ExternalSyntheticLambda4 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ MediaControlPanel$$ExternalSyntheticLambda4(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                MediaControlPanel mediaControlPanel = (MediaControlPanel) this.f$0;
                SmartspaceMediaData smartspaceMediaData = (SmartspaceMediaData) this.f$1;
                Objects.requireNonNull(mediaControlPanel);
                if (!mediaControlPanel.mFalsingManager.isFalseTap(1)) {
                    mediaControlPanel.logSmartspaceCardReported(761, true, 0, 0);
                    mediaControlPanel.closeGuts(false);
                    Objects.requireNonNull(smartspaceMediaData);
                    mediaControlPanel.mMediaDataManagerLazy.get().dismissSmartspaceRecommendation(smartspaceMediaData.targetId, 600L);
                    Intent intent = smartspaceMediaData.dismissIntent;
                    if (intent == null) {
                        Log.w("MediaControlPanel", "Cannot create dismiss action click action: extras missing dismiss_intent.");
                        return;
                    } else if (intent.getComponent() == null || !intent.getComponent().getClassName().equals("com.google.android.apps.gsa.staticplugins.opa.smartspace.ExportedSmartspaceTrampolineActivity")) {
                        mediaControlPanel.mContext.sendBroadcast(intent);
                        return;
                    } else {
                        mediaControlPanel.mContext.startActivity(intent);
                        return;
                    }
                } else {
                    return;
                }
            default:
                GameDashboardButton gameDashboardButton = (GameDashboardButton) this.f$0;
                int i = GameDashboardButton.$r8$clinit;
                Objects.requireNonNull(gameDashboardButton);
                ((View.OnClickListener) this.f$1).onClick(view);
                gameDashboardButton.setToggled(!gameDashboardButton.mToggled, true);
                return;
        }
    }
}
