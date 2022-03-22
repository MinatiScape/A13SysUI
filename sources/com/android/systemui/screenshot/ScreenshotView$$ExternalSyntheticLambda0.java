package com.android.systemui.screenshot;

import android.animation.ValueAnimator;
import android.view.View;
import com.android.systemui.R$array;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationView;
import com.google.android.systemui.gamedashboard.GameDashboardButton;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ScreenshotView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ View f$0;

    public /* synthetic */ ScreenshotView$$ExternalSyntheticLambda0(View view, int i) {
        this.$r8$classId = i;
        this.f$0 = view;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        switch (this.$r8$classId) {
            case 0:
                ScreenshotView screenshotView = (ScreenshotView) this.f$0;
                int i = ScreenshotView.$r8$clinit;
                Objects.requireNonNull(screenshotView);
                screenshotView.mScreenshotPreviewBorder.setAlpha(valueAnimator.getAnimatedFraction());
                return;
            case 1:
                ActivatableNotificationView activatableNotificationView = (ActivatableNotificationView) this.f$0;
                int i2 = ActivatableNotificationView.$r8$clinit;
                Objects.requireNonNull(activatableNotificationView);
                activatableNotificationView.setBackgroundTintColor(R$array.interpolateColors(activatableNotificationView.mStartTint, activatableNotificationView.mTargetTint, valueAnimator.getAnimatedFraction()));
                return;
            default:
                GameDashboardButton gameDashboardButton = (GameDashboardButton) this.f$0;
                int i3 = GameDashboardButton.$r8$clinit;
                Objects.requireNonNull(gameDashboardButton);
                gameDashboardButton.mBackgroundDrawable.setCornerRadius(((Float) valueAnimator.getAnimatedValue()).floatValue());
                return;
        }
    }
}
