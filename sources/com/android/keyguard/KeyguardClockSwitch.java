package com.android.keyguard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.ClockPlugin;
/* loaded from: classes.dex */
public class KeyguardClockSwitch extends RelativeLayout {
    public FrameLayout mClockFrame;
    public ClockPlugin mClockPlugin;
    public int mClockSwitchYAmount;
    public AnimatableClockView mClockView;
    public int[] mColorPalette;
    public float mDarkAmount;
    public FrameLayout mLargeClockFrame;
    public AnimatableClockView mLargeClockView;
    public int mSmartspaceTopOffset;
    public View mStatusArea;
    public boolean mSupportsDarkText;
    public Integer mDisplayedClockSize = null;
    public AnimatorSet mClockInAnim = null;
    public AnimatorSet mClockOutAnim = null;
    public ObjectAnimator mStatusAreaAnim = null;
    public boolean mChildrenAreLaidOut = false;

    public final void onDensityOrFontScaleChanged() {
        this.mLargeClockView.setTextSize(0, ((RelativeLayout) this).mContext.getResources().getDimensionPixelSize(2131165873));
        this.mClockView.setTextSize(0, ((RelativeLayout) this).mContext.getResources().getDimensionPixelSize(2131165493));
        this.mClockSwitchYAmount = ((RelativeLayout) this).mContext.getResources().getDimensionPixelSize(2131165843);
        this.mSmartspaceTopOffset = ((RelativeLayout) this).mContext.getResources().getDimensionPixelSize(2131165862);
    }

    public final void setClockPlugin(ClockPlugin clockPlugin) {
        ClockPlugin clockPlugin2 = this.mClockPlugin;
        if (clockPlugin2 != null) {
            View view = clockPlugin2.getView();
            if (view != null) {
                ViewParent parent = view.getParent();
                FrameLayout frameLayout = this.mClockFrame;
                if (parent == frameLayout) {
                    frameLayout.removeView(view);
                }
            }
            View bigClockView = this.mClockPlugin.getBigClockView();
            if (bigClockView != null) {
                ViewParent parent2 = bigClockView.getParent();
                FrameLayout frameLayout2 = this.mLargeClockFrame;
                if (parent2 == frameLayout2) {
                    frameLayout2.removeView(bigClockView);
                }
            }
            this.mClockPlugin.onDestroyView();
            this.mClockPlugin = null;
        }
        if (clockPlugin == null) {
            this.mClockView.setVisibility(0);
            this.mLargeClockView.setVisibility(0);
            return;
        }
        View view2 = clockPlugin.getView();
        if (view2 != null) {
            this.mClockFrame.addView(view2, -1, new ViewGroup.LayoutParams(-1, -2));
            this.mClockView.setVisibility(8);
        }
        View bigClockView2 = clockPlugin.getBigClockView();
        if (bigClockView2 != null) {
            this.mLargeClockFrame.addView(bigClockView2);
            this.mLargeClockView.setVisibility(8);
        }
        this.mClockPlugin = clockPlugin;
        clockPlugin.setStyle(this.mClockView.getPaint().getStyle());
        this.mClockPlugin.setTextColor(this.mClockView.getCurrentTextColor());
        this.mClockPlugin.setDarkAmount(this.mDarkAmount);
        int[] iArr = this.mColorPalette;
        if (iArr != null) {
            this.mClockPlugin.setColorPalette(this.mSupportsDarkText, iArr);
        }
    }

    public final void updateClockViews(boolean z, boolean z2) {
        FrameLayout frameLayout;
        float f;
        FrameLayout frameLayout2;
        AnimatorSet animatorSet = this.mClockInAnim;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        AnimatorSet animatorSet2 = this.mClockOutAnim;
        if (animatorSet2 != null) {
            animatorSet2.cancel();
        }
        ObjectAnimator objectAnimator = this.mStatusAreaAnim;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
        this.mClockInAnim = null;
        this.mClockOutAnim = null;
        this.mStatusAreaAnim = null;
        int i = -1;
        if (z) {
            frameLayout = this.mClockFrame;
            frameLayout2 = this.mLargeClockFrame;
            if (indexOfChild(frameLayout2) == -1) {
                addView(frameLayout2);
            }
            f = (this.mClockFrame.getTop() - this.mStatusArea.getTop()) + this.mSmartspaceTopOffset;
        } else {
            frameLayout2 = this.mClockFrame;
            frameLayout = this.mLargeClockFrame;
            removeView(frameLayout);
            f = 0.0f;
            i = 1;
        }
        if (!z2) {
            frameLayout.setAlpha(0.0f);
            frameLayout2.setAlpha(1.0f);
            frameLayout2.setVisibility(0);
            this.mStatusArea.setTranslationY(f);
            return;
        }
        AnimatorSet animatorSet3 = new AnimatorSet();
        this.mClockOutAnim = animatorSet3;
        animatorSet3.setDuration(150L);
        this.mClockOutAnim.setInterpolator(Interpolators.FAST_OUT_LINEAR_IN);
        this.mClockOutAnim.playTogether(ObjectAnimator.ofFloat(frameLayout, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(frameLayout, View.TRANSLATION_Y, 0.0f, (-this.mClockSwitchYAmount) * i));
        this.mClockOutAnim.addListener(new AnimatorListenerAdapter() { // from class: com.android.keyguard.KeyguardClockSwitch.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                KeyguardClockSwitch.this.mClockOutAnim = null;
            }
        });
        frameLayout2.setAlpha(0.0f);
        frameLayout2.setVisibility(0);
        AnimatorSet animatorSet4 = new AnimatorSet();
        this.mClockInAnim = animatorSet4;
        animatorSet4.setDuration(200L);
        this.mClockInAnim.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
        this.mClockInAnim.playTogether(ObjectAnimator.ofFloat(frameLayout2, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(frameLayout2, View.TRANSLATION_Y, i * this.mClockSwitchYAmount, 0.0f));
        this.mClockInAnim.setStartDelay(75L);
        this.mClockInAnim.addListener(new AnimatorListenerAdapter() { // from class: com.android.keyguard.KeyguardClockSwitch.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                KeyguardClockSwitch.this.mClockInAnim = null;
            }
        });
        this.mClockInAnim.start();
        this.mClockOutAnim.start();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mStatusArea, View.TRANSLATION_Y, f);
        this.mStatusAreaAnim = ofFloat;
        ofFloat.setDuration(350L);
        this.mStatusAreaAnim.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        this.mStatusAreaAnim.addListener(new AnimatorListenerAdapter() { // from class: com.android.keyguard.KeyguardClockSwitch.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                KeyguardClockSwitch.this.mStatusAreaAnim = null;
            }
        });
        this.mStatusAreaAnim.start();
    }

    public KeyguardClockSwitch(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mClockFrame = (FrameLayout) findViewById(2131428282);
        this.mClockView = (AnimatableClockView) findViewById(2131427488);
        this.mLargeClockFrame = (FrameLayout) findViewById(2131428283);
        this.mLargeClockView = (AnimatableClockView) findViewById(2131427489);
        this.mStatusArea = findViewById(2131428194);
        onDensityOrFontScaleChanged();
    }

    @Override // android.widget.RelativeLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        boolean z2;
        super.onLayout(z, i, i2, i3, i4);
        Integer num = this.mDisplayedClockSize;
        if (num != null && !this.mChildrenAreLaidOut) {
            if (num.intValue() == 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            updateClockViews(z2, true);
        }
        this.mChildrenAreLaidOut = true;
    }
}
