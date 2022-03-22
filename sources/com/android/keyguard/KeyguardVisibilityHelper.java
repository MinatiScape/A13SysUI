package com.android.keyguard;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewPropertyAnimator;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.communal.CommunalStateController;
import com.android.systemui.statusbar.notification.AnimatableProperty;
import com.android.systemui.statusbar.notification.PropertyAnimator;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.phone.ScreenOffAnimation;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda10;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class KeyguardVisibilityHelper {
    public boolean mAnimateYPos;
    public final CommunalStateController mCommunalStateController;
    public final KeyguardStateController mKeyguardStateController;
    public boolean mKeyguardViewVisibilityAnimating;
    public final ScreenOffAnimationController mScreenOffAnimationController;
    public View mView;
    public final boolean mVisibleOnCommunal;
    public boolean mLastOccludedState = false;
    public final AnimationProperties mAnimationProperties = new AnimationProperties();
    public final VolumeDialogImpl$$ExternalSyntheticLambda10 mAnimateKeyguardStatusViewInvisibleEndRunnable = new VolumeDialogImpl$$ExternalSyntheticLambda10(this, 2);
    public final KeyguardDisplayManager$$ExternalSyntheticLambda1 mAnimateKeyguardStatusViewGoneEndRunnable = new KeyguardDisplayManager$$ExternalSyntheticLambda1(this, 1);
    public final KeyguardVisibilityHelper$$ExternalSyntheticLambda0 mAnimateKeyguardStatusViewVisibleEndRunnable = new KeyguardVisibilityHelper$$ExternalSyntheticLambda0(this, 0);

    public final void setViewVisibility(int i, boolean z, boolean z2, int i2) {
        boolean z3;
        Object obj;
        boolean z4;
        this.mView.animate().cancel();
        boolean isOccluded = this.mKeyguardStateController.isOccluded();
        this.mKeyguardViewVisibilityAnimating = false;
        if (!this.mVisibleOnCommunal) {
            CommunalStateController communalStateController = this.mCommunalStateController;
            Objects.requireNonNull(communalStateController);
            if (communalStateController.mCommunalViewShowing) {
                this.mView.setVisibility(8);
                this.mView.setAlpha(1.0f);
                return;
            }
        }
        if ((!z && i2 == 1 && i != 1) || z2) {
            this.mKeyguardViewVisibilityAnimating = true;
            this.mView.animate().alpha(0.0f).setStartDelay(0L).setDuration(160L).setInterpolator(Interpolators.ALPHA_OUT).withEndAction(this.mAnimateKeyguardStatusViewGoneEndRunnable);
            if (z) {
                this.mView.animate().setStartDelay(this.mKeyguardStateController.getKeyguardFadingAwayDelay()).setDuration(this.mKeyguardStateController.getShortenedFadingAwayDuration()).start();
            }
        } else if (i2 == 2 && i == 1) {
            this.mView.setVisibility(0);
            this.mKeyguardViewVisibilityAnimating = true;
            this.mView.setAlpha(0.0f);
            this.mView.animate().alpha(1.0f).setStartDelay(0L).setDuration(320L).setInterpolator(Interpolators.ALPHA_IN).withEndAction(this.mAnimateKeyguardStatusViewVisibleEndRunnable);
        } else if (i != 1) {
            this.mView.setVisibility(8);
            this.mView.setAlpha(1.0f);
        } else if (z) {
            this.mKeyguardViewVisibilityAnimating = true;
            ViewPropertyAnimator withEndAction = this.mView.animate().alpha(0.0f).setInterpolator(Interpolators.FAST_OUT_LINEAR_IN).withEndAction(this.mAnimateKeyguardStatusViewInvisibleEndRunnable);
            if (this.mAnimateYPos) {
                float y = this.mView.getY() - (this.mView.getHeight() * 0.05f);
                AnimationProperties animationProperties = this.mAnimationProperties;
                long j = 125;
                Objects.requireNonNull(animationProperties);
                animationProperties.duration = j;
                long j2 = 0;
                animationProperties.delay = j2;
                View view = this.mView;
                AnimatableProperty.AnonymousClass7 r12 = AnimatableProperty.Y;
                ValueAnimator valueAnimator = (ValueAnimator) view.getTag(r12.getAnimatorTag());
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                }
                PropertyAnimator.setProperty(this.mView, r12, y, this.mAnimationProperties, true);
                withEndAction.setDuration(j).setStartDelay(j2);
            }
            withEndAction.start();
        } else {
            ScreenOffAnimationController screenOffAnimationController = this.mScreenOffAnimationController;
            Objects.requireNonNull(screenOffAnimationController);
            ArrayList arrayList = screenOffAnimationController.animations;
            if (!(arrayList instanceof Collection) || !arrayList.isEmpty()) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    if (((ScreenOffAnimation) it.next()).shouldAnimateInKeyguard()) {
                        z3 = true;
                        break;
                    }
                }
            }
            z3 = false;
            if (z3) {
                this.mKeyguardViewVisibilityAnimating = true;
                ScreenOffAnimationController screenOffAnimationController2 = this.mScreenOffAnimationController;
                View view2 = this.mView;
                KeyguardVisibilityHelper$$ExternalSyntheticLambda0 keyguardVisibilityHelper$$ExternalSyntheticLambda0 = this.mAnimateKeyguardStatusViewVisibleEndRunnable;
                Objects.requireNonNull(screenOffAnimationController2);
                Iterator it2 = screenOffAnimationController2.animations.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        obj = null;
                        break;
                    }
                    obj = it2.next();
                    ScreenOffAnimation screenOffAnimation = (ScreenOffAnimation) obj;
                    if (screenOffAnimation.shouldAnimateInKeyguard()) {
                        screenOffAnimation.animateInKeyguard(view2, keyguardVisibilityHelper$$ExternalSyntheticLambda0);
                        z4 = true;
                        continue;
                    } else {
                        z4 = false;
                        continue;
                    }
                    if (z4) {
                        break;
                    }
                }
                ScreenOffAnimation screenOffAnimation2 = (ScreenOffAnimation) obj;
            } else if (!this.mLastOccludedState || isOccluded) {
                this.mView.setVisibility(0);
                this.mView.setAlpha(1.0f);
            } else {
                this.mView.setVisibility(0);
                this.mView.setAlpha(0.0f);
                this.mView.animate().setDuration(500L).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).alpha(1.0f).withEndAction(this.mAnimateKeyguardStatusViewVisibleEndRunnable).start();
            }
        }
        this.mLastOccludedState = isOccluded;
    }

    public KeyguardVisibilityHelper(View view, CommunalStateController communalStateController, KeyguardStateController keyguardStateController, ScreenOffAnimationController screenOffAnimationController, boolean z, boolean z2) {
        this.mView = view;
        this.mCommunalStateController = communalStateController;
        this.mKeyguardStateController = keyguardStateController;
        this.mScreenOffAnimationController = screenOffAnimationController;
        this.mAnimateYPos = z;
        this.mVisibleOnCommunal = z2;
    }
}
