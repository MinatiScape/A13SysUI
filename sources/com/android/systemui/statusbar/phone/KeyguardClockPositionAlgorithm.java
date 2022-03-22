package com.android.systemui.statusbar.phone;

import android.util.MathUtils;
import com.android.systemui.R$anim;
import com.android.systemui.animation.Interpolators;
/* loaded from: classes.dex */
public final class KeyguardClockPositionAlgorithm {
    public int mBurnInPreventionOffsetX;
    public int mBurnInPreventionOffsetYClock;
    public boolean mBypassEnabled;
    public float mClockBottom;
    public int mContainerTopPadding;
    public int mCutoutTopInset = 0;
    public float mDarkAmount;
    public boolean mIsClockTopAligned;
    public boolean mIsSplitShade;
    public int mKeyguardStatusHeight;
    public int mMinTopMargin;
    public float mOverStretchAmount;
    public float mPanelExpansion;
    public float mQsExpansion;
    public int mSplitShadeTargetTopMargin;
    public int mSplitShadeTopNotificationsMargin;
    public int mStatusViewBottomMargin;
    public float mUdfpsTop;
    public int mUnlockedStackScrollerPadding;
    public int mUserSwitchHeight;
    public int mUserSwitchPreferredY;

    /* loaded from: classes.dex */
    public static class Result {
        public float clockAlpha;
        public float clockScale;
        public int clockX;
        public int clockY;
        public int clockYFullyDozing;
        public int stackScrollerPadding;
        public int stackScrollerPaddingExpanded;
        public int userSwitchY;
    }

    public final int getClockY(float f, float f2) {
        int i;
        boolean z;
        if (this.mIsSplitShade) {
            i = this.mSplitShadeTargetTopMargin;
        } else {
            i = this.mMinTopMargin;
        }
        float lerp = MathUtils.lerp((-this.mKeyguardStatusHeight) / 3.0f, i, Interpolators.FAST_OUT_LINEAR_IN.getInterpolation(f));
        float f3 = 0.0f;
        int i2 = this.mBurnInPreventionOffsetYClock;
        float f4 = lerp - i2;
        float f5 = this.mCutoutTopInset;
        if (f4 < f5) {
            f3 = f5 - f4;
        }
        float f6 = this.mUdfpsTop;
        if (f6 > -1.0f) {
            z = true;
        } else {
            z = false;
        }
        if (z && !this.mIsClockTopAligned) {
            float f7 = this.mClockBottom;
            if (f6 < f7) {
                int i3 = ((int) (lerp - f5)) / 2;
                if (i2 >= i3) {
                    i2 = i3;
                }
                f3 = -i2;
            } else {
                float f8 = lerp - f5;
                float f9 = f6 - f7;
                int i4 = ((int) (f9 + f8)) / 2;
                if (i2 >= i4) {
                    i2 = i4;
                }
                f3 = (f9 - f8) / 2.0f;
            }
        }
        return (int) (MathUtils.lerp(lerp, (R$anim.getBurnInOffset(i2 * 2, false) - i2) + lerp + f3, f2) + this.mOverStretchAmount);
    }
}
