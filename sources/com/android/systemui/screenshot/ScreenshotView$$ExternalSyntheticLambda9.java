package com.android.systemui.screenshot;

import android.content.IntentFilter;
import android.view.View;
import com.android.systemui.media.dialog.MediaOutputBaseDialog;
import com.android.systemui.screenshot.ScreenshotView;
import com.google.android.systemui.gamedashboard.GameMenuActivity;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ScreenshotView$$ExternalSyntheticLambda9 implements View.OnClickListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ ScreenshotView$$ExternalSyntheticLambda9(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (this.$r8$classId) {
            case 0:
                ScreenshotView screenshotView = (ScreenshotView) this.f$0;
                int i = ScreenshotView.$r8$clinit;
                Objects.requireNonNull(screenshotView);
                screenshotView.mShareChip.setIsPending(false);
                screenshotView.mEditChip.setIsPending(false);
                screenshotView.mQuickShareChip.setIsPending(true);
                screenshotView.mPendingInteraction = ScreenshotView.PendingInteraction.QUICK_SHARE;
                return;
            case 1:
                int i2 = MediaOutputBaseDialog.$r8$clinit;
                Objects.requireNonNull((MediaOutputBaseDialog) this.f$0);
                return;
            default:
                GameMenuActivity gameMenuActivity = (GameMenuActivity) this.f$0;
                IntentFilter intentFilter = GameMenuActivity.DND_FILTER;
                Objects.requireNonNull(gameMenuActivity);
                gameMenuActivity.onGameModeSelectionChanged(view);
                return;
        }
    }
}
