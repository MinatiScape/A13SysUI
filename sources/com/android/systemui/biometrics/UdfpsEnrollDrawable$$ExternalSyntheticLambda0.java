package com.android.systemui.biometrics;

import android.animation.ValueAnimator;
import com.android.systemui.navigationbar.buttons.ButtonDispatcher;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class UdfpsEnrollDrawable$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ UdfpsEnrollDrawable$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        switch (this.$r8$classId) {
            case 0:
                UdfpsEnrollDrawable udfpsEnrollDrawable = (UdfpsEnrollDrawable) this.f$0;
                Objects.requireNonNull(udfpsEnrollDrawable);
                udfpsEnrollDrawable.mCurrentX = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                udfpsEnrollDrawable.invalidateSelf();
                return;
            default:
                ButtonDispatcher buttonDispatcher = (ButtonDispatcher) this.f$0;
                Objects.requireNonNull(buttonDispatcher);
                buttonDispatcher.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue(), false, false);
                return;
        }
    }
}
