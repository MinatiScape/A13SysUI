package com.android.systemui.statusbar;

import android.animation.ValueAnimator;
import com.android.systemui.R$array;
import com.android.systemui.dreams.touch.BouncerSwipeTouchHandler;
import com.android.systemui.statusbar.StatusBarIconView;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class StatusBarIconView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ StatusBarIconView$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        switch (this.$r8$classId) {
            case 0:
                StatusBarIconView statusBarIconView = (StatusBarIconView) this.f$0;
                StatusBarIconView.AnonymousClass1 r0 = StatusBarIconView.ICON_APPEAR_AMOUNT;
                Objects.requireNonNull(statusBarIconView);
                statusBarIconView.mCurrentSetColor = R$array.interpolateColors(statusBarIconView.mAnimationStartColor, statusBarIconView.mIconColor, valueAnimator.getAnimatedFraction());
                statusBarIconView.updateIconColor();
                return;
            default:
                BouncerSwipeTouchHandler bouncerSwipeTouchHandler = (BouncerSwipeTouchHandler) this.f$0;
                Objects.requireNonNull(bouncerSwipeTouchHandler);
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                bouncerSwipeTouchHandler.mCurrentExpansion = floatValue;
                bouncerSwipeTouchHandler.mStatusBarKeyguardViewManager.onPanelExpansionChanged(floatValue, false, true);
                return;
        }
    }
}
