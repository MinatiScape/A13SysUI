package com.android.systemui.biometrics;

import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.AttributeSet;
import android.util.MathUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.android.settingslib.Utils;
import com.android.systemui.R$anim;
import com.android.systemui.biometrics.UdfpsKeyguardView;
import java.util.Objects;
/* loaded from: classes.dex */
public class UdfpsKeyguardView extends UdfpsAnimationView {
    public int mAlpha;
    public LottieAnimationView mAodFp;
    public ImageView mBgProtection;
    public float mBurnInOffsetX;
    public float mBurnInOffsetY;
    public float mBurnInProgress;
    public UdfpsFpDrawable mFingerprintDrawable;
    public boolean mFullyInflated;
    public float mInterpolatedDarkAmount;
    public LottieAnimationView mLockScreenFp;
    public final int mMaxBurnInOffsetX;
    public final int mMaxBurnInOffsetY;
    public int mTextColorPrimary;
    public boolean mUdfpsRequested;
    public AnimatorSet mBackgroundInAnimator = new AnimatorSet();
    public final AnonymousClass2 mLayoutInflaterFinishListener = new AnonymousClass2();

    /* renamed from: com.android.systemui.biometrics.UdfpsKeyguardView$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 implements AsyncLayoutInflater.OnInflateFinishedListener {
        public AnonymousClass2() {
        }

        @Override // androidx.asynclayoutinflater.view.AsyncLayoutInflater.OnInflateFinishedListener
        public final void onInflateFinished(View view, ViewGroup viewGroup) {
            UdfpsKeyguardView.this.mFullyInflated = true;
            viewGroup.addView(view);
            UdfpsKeyguardView udfpsKeyguardView = UdfpsKeyguardView.this;
            udfpsKeyguardView.mAodFp = (LottieAnimationView) udfpsKeyguardView.findViewById(2131429137);
            UdfpsKeyguardView udfpsKeyguardView2 = UdfpsKeyguardView.this;
            udfpsKeyguardView2.mLockScreenFp = (LottieAnimationView) udfpsKeyguardView2.findViewById(2131429143);
            UdfpsKeyguardView udfpsKeyguardView3 = UdfpsKeyguardView.this;
            udfpsKeyguardView3.mBgProtection = (ImageView) udfpsKeyguardView3.findViewById(2131429142);
            UdfpsKeyguardView.this.updateBurnInOffsets();
            UdfpsKeyguardView.this.updateColor();
            UdfpsKeyguardView.this.updateAlpha();
            UdfpsKeyguardView.this.mLockScreenFp.addValueCallback(new KeyPath("**"), (KeyPath) LottieProperty.COLOR_FILTER, (SimpleLottieValueCallback<KeyPath>) new SimpleLottieValueCallback() { // from class: com.android.systemui.biometrics.UdfpsKeyguardView$2$$ExternalSyntheticLambda0
                @Override // com.airbnb.lottie.value.SimpleLottieValueCallback
                public final PorterDuffColorFilter getValue() {
                    UdfpsKeyguardView.AnonymousClass2 r2 = UdfpsKeyguardView.AnonymousClass2.this;
                    Objects.requireNonNull(r2);
                    return new PorterDuffColorFilter(UdfpsKeyguardView.this.mTextColorPrimary, PorterDuff.Mode.SRC_ATOP);
                }
            });
        }
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationView
    public final void onIlluminationStarting() {
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationView
    public final void onIlluminationStopped() {
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationView
    public final int calculateAlpha() {
        if (this.mPauseAuth) {
            return 0;
        }
        return this.mAlpha;
    }

    public final void updateBurnInOffsets() {
        if (this.mFullyInflated) {
            this.mBurnInOffsetX = MathUtils.lerp(0.0f, R$anim.getBurnInOffset(this.mMaxBurnInOffsetX * 2, true) - this.mMaxBurnInOffsetX, this.mInterpolatedDarkAmount);
            this.mBurnInOffsetY = MathUtils.lerp(0.0f, R$anim.getBurnInOffset(this.mMaxBurnInOffsetY * 2, false) - this.mMaxBurnInOffsetY, this.mInterpolatedDarkAmount);
            this.mBurnInProgress = MathUtils.lerp(0.0f, R$anim.zigzag(((float) System.currentTimeMillis()) / 60000.0f, 1.0f, 89.0f), this.mInterpolatedDarkAmount);
            this.mAodFp.setTranslationX(this.mBurnInOffsetX);
            this.mAodFp.setTranslationY(this.mBurnInOffsetY);
            this.mAodFp.setProgress(this.mBurnInProgress);
            this.mAodFp.setAlpha(this.mInterpolatedDarkAmount * 255.0f);
            this.mLockScreenFp.setTranslationX(this.mBurnInOffsetX);
            this.mLockScreenFp.setTranslationY(this.mBurnInOffsetY);
            this.mLockScreenFp.setProgress(1.0f - this.mInterpolatedDarkAmount);
            this.mLockScreenFp.setAlpha((1.0f - this.mInterpolatedDarkAmount) * 255.0f);
        }
    }

    public final void updateColor() {
        if (this.mFullyInflated) {
            this.mTextColorPrimary = Utils.getColorAttrDefaultColor(((FrameLayout) this).mContext, 16842806);
            this.mBgProtection.setImageDrawable(getContext().getDrawable(2131231707));
            this.mLockScreenFp.invalidate();
        }
    }

    public UdfpsKeyguardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mFingerprintDrawable = new UdfpsFpDrawable(context);
        this.mMaxBurnInOffsetX = context.getResources().getDimensionPixelSize(2131167259);
        this.mMaxBurnInOffsetY = context.getResources().getDimensionPixelSize(2131167260);
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationView
    public final boolean dozeTimeTick() {
        updateBurnInOffsets();
        return true;
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        new AsyncLayoutInflater(((FrameLayout) this).mContext).inflate(2131624640, this, this.mLayoutInflaterFinishListener);
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationView
    public final int updateAlpha() {
        int updateAlpha = super.updateAlpha();
        if (this.mFullyInflated) {
            float f = updateAlpha / 255.0f;
            this.mLockScreenFp.setAlpha(f);
            float f2 = this.mInterpolatedDarkAmount;
            if (f2 != 0.0f) {
                this.mBgProtection.setAlpha(1.0f - f2);
            } else {
                this.mBgProtection.setAlpha(f);
            }
        }
        return updateAlpha;
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationView
    public final UdfpsDrawable getDrawable() {
        return this.mFingerprintDrawable;
    }
}
