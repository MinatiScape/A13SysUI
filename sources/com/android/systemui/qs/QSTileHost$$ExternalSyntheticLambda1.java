package com.android.systemui.qs;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Rect;
import android.view.View;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline2;
import com.android.systemui.accessibility.floatingmenu.AnnotationLinkSpan;
import com.android.systemui.plugins.IntentButtonProvider;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.statusbar.phone.KeyguardBottomAreaView;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.wm.shell.pip.tv.TvPipController;
import com.android.wm.shell.splitscreen.StageCoordinator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class QSTileHost$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ QSTileHost$$ExternalSyntheticLambda1(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                QSTileHost qSTileHost = (QSTileHost) this.f$0;
                Map.Entry entry = (Map.Entry) obj;
                Objects.requireNonNull(qSTileHost);
                StringBuilder sb = new StringBuilder();
                sb.append("Destroying tile: ");
                ExifInterface$$ExternalSyntheticOutline2.m(sb, (String) entry.getKey(), "QSTileHost");
                qSTileHost.mQSLogger.logTileDestroyed((String) entry.getKey(), "Tile removed");
                ((QSTile) entry.getValue()).destroy();
                return;
            case 1:
                ((StatusBar) obj).postStartActivityDismissingKeyguard((PendingIntent) this.f$0);
                return;
            case 2:
                int i = AnnotationLinkSpan.$r8$clinit;
                ((View.OnClickListener) obj).onClick((View) this.f$0);
                return;
            case 3:
                KeyguardBottomAreaView keyguardBottomAreaView = (KeyguardBottomAreaView) this.f$0;
                Intent intent = KeyguardBottomAreaView.PHONE_INTENT;
                Objects.requireNonNull(keyguardBottomAreaView);
                keyguardBottomAreaView.mRightButton = (IntentButtonProvider.IntentButton) obj;
                keyguardBottomAreaView.updateRightAffordanceIcon();
                keyguardBottomAreaView.updateCameraVisibility();
                keyguardBottomAreaView.inflateCameraPreview();
                return;
            case 4:
                TvPipController tvPipController = (TvPipController) this.f$0;
                Rect rect = (Rect) obj;
                Objects.requireNonNull(tvPipController);
                tvPipController.mTvPipMenuController.updateExpansionState();
                return;
            default:
                StageCoordinator stageCoordinator = (StageCoordinator) this.f$0;
                boolean booleanValue = ((Boolean) obj).booleanValue();
                Objects.requireNonNull(stageCoordinator);
                stageCoordinator.mTopStageAfterFoldDismiss = -1;
                if (booleanValue) {
                    if (stageCoordinator.mMainStage.isFocused()) {
                        stageCoordinator.mTopStageAfterFoldDismiss = 0;
                        return;
                    } else if (stageCoordinator.mSideStage.isFocused()) {
                        stageCoordinator.mTopStageAfterFoldDismiss = 1;
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
        }
    }
}
