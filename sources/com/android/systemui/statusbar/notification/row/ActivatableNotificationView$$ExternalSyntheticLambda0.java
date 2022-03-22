package com.android.systemui.statusbar.notification.row;

import android.animation.ValueAnimator;
import android.util.MathUtils;
import com.android.systemui.animation.Interpolators;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ActivatableNotificationView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ ActivatableNotificationView$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        switch (this.$r8$classId) {
            case 0:
                ActivatableNotificationView activatableNotificationView = (ActivatableNotificationView) this.f$0;
                int i = ActivatableNotificationView.$r8$clinit;
                Objects.requireNonNull(activatableNotificationView);
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                activatableNotificationView.mAppearAnimationFraction = floatValue;
                activatableNotificationView.setContentAlpha(Interpolators.ALPHA_IN.getInterpolation((MathUtils.constrain(floatValue, 0.4f, 1.0f) - 0.4f) / 0.6f));
                activatableNotificationView.updateAppearRect();
                activatableNotificationView.invalidate();
                return;
            default:
                ((Runnable) this.f$0).run();
                return;
        }
    }
}
