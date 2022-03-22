package com.android.systemui.biometrics;

import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.view.ViewGroup;
import com.google.android.systemui.ambientmusic.AmbientIndicationContainer;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class AuthBiometricView$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ ViewGroup f$0;

    public /* synthetic */ AuthBiometricView$$ExternalSyntheticLambda0(ViewGroup viewGroup, int i) {
        this.$r8$classId = i;
        this.f$0 = viewGroup;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        switch (this.$r8$classId) {
            case 0:
                AuthBiometricView authBiometricView = (AuthBiometricView) this.f$0;
                int i = AuthBiometricView.$r8$clinit;
                Objects.requireNonNull(authBiometricView);
                authBiometricView.mIconHolderView.setY(((Float) valueAnimator.getAnimatedValue()).floatValue());
                return;
            default:
                AmbientIndicationContainer ambientIndicationContainer = (AmbientIndicationContainer) this.f$0;
                int i2 = AmbientIndicationContainer.$r8$clinit;
                Objects.requireNonNull(ambientIndicationContainer);
                int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                ambientIndicationContainer.mTextView.setTextColor(intValue);
                ambientIndicationContainer.mIconView.setImageTintList(ColorStateList.valueOf(intValue));
                return;
        }
    }
}
