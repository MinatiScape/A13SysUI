package com.android.systemui.biometrics;

import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.text.TextUtils;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class AuthBiometricView$$ExternalSyntheticLambda1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ AuthBiometricView$$ExternalSyntheticLambda1(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        switch (this.$r8$classId) {
            case 0:
                AuthBiometricView authBiometricView = (AuthBiometricView) this.f$0;
                int i = AuthBiometricView.$r8$clinit;
                Objects.requireNonNull(authBiometricView);
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                authBiometricView.mTitleView.setAlpha(floatValue);
                authBiometricView.mIndicatorView.setAlpha(floatValue);
                authBiometricView.mNegativeButton.setAlpha(floatValue);
                authBiometricView.mCancelButton.setAlpha(floatValue);
                authBiometricView.mTryAgainButton.setAlpha(floatValue);
                if (!TextUtils.isEmpty(authBiometricView.mSubtitleView.getText())) {
                    authBiometricView.mSubtitleView.setAlpha(floatValue);
                }
                if (!TextUtils.isEmpty(authBiometricView.mDescriptionView.getText())) {
                    authBiometricView.mDescriptionView.setAlpha(floatValue);
                    return;
                }
                return;
            default:
                NotificationPanelViewController notificationPanelViewController = (NotificationPanelViewController) this.f$0;
                Rect rect = NotificationPanelViewController.M_DUMMY_DIRTY_RECT;
                Objects.requireNonNull(notificationPanelViewController);
                notificationPanelViewController.setQsExpansion(((Float) valueAnimator.getAnimatedValue()).floatValue());
                return;
        }
    }
}
