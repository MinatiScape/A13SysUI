package com.android.systemui.navigationbar.buttons;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.biometrics.UdfpsEnrollDrawable$$ExternalSyntheticLambda0;
import com.android.systemui.shared.rotation.RotationButtonController$$ExternalSyntheticLambda0;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class ButtonDispatcher {
    public View.AccessibilityDelegate mAccessibilityDelegate;
    public Float mAlpha;
    public View.OnClickListener mClickListener;
    public View mCurrentView;
    public Float mDarkIntensity;
    public ValueAnimator mFadeAnimator;
    public final int mId;
    public KeyButtonDrawable mImageDrawable;
    public View.OnLongClickListener mLongClickListener;
    public Boolean mLongClickable;
    public View.OnHoverListener mOnHoverListener;
    public View.OnTouchListener mTouchListener;
    public boolean mVertical;
    public final ArrayList<View> mViews = new ArrayList<>();
    public Integer mVisibility = 0;
    public final UdfpsEnrollDrawable$$ExternalSyntheticLambda0 mAlphaListener = new UdfpsEnrollDrawable$$ExternalSyntheticLambda0(this, 1);
    public final AnonymousClass1 mFadeListener = new AnimatorListenerAdapter() { // from class: com.android.systemui.navigationbar.buttons.ButtonDispatcher.1
        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationEnd(Animator animator) {
            int i;
            ButtonDispatcher buttonDispatcher = ButtonDispatcher.this;
            buttonDispatcher.mFadeAnimator = null;
            if (buttonDispatcher.getAlpha() == 1.0f) {
                i = 0;
            } else {
                i = 4;
            }
            buttonDispatcher.setVisibility(i);
        }
    };

    public final void addView(View view) {
        this.mViews.add(view);
        view.setOnClickListener(this.mClickListener);
        view.setOnTouchListener(this.mTouchListener);
        view.setOnLongClickListener(this.mLongClickListener);
        view.setOnHoverListener(this.mOnHoverListener);
        Boolean bool = this.mLongClickable;
        if (bool != null) {
            view.setLongClickable(bool.booleanValue());
        }
        Float f = this.mAlpha;
        if (f != null) {
            view.setAlpha(f.floatValue());
        }
        Integer num = this.mVisibility;
        if (num != null) {
            view.setVisibility(num.intValue());
        }
        View.AccessibilityDelegate accessibilityDelegate = this.mAccessibilityDelegate;
        if (accessibilityDelegate != null) {
            view.setAccessibilityDelegate(accessibilityDelegate);
        }
        if (view instanceof ButtonInterface) {
            ButtonInterface buttonInterface = (ButtonInterface) view;
            Float f2 = this.mDarkIntensity;
            if (f2 != null) {
                buttonInterface.setDarkIntensity(f2.floatValue());
            }
            KeyButtonDrawable keyButtonDrawable = this.mImageDrawable;
            if (keyButtonDrawable != null) {
                buttonInterface.setImageDrawable(keyButtonDrawable);
            }
            buttonInterface.setVertical(this.mVertical);
        }
    }

    public final float getAlpha() {
        Float f = this.mAlpha;
        if (f != null) {
            return f.floatValue();
        }
        return 1.0f;
    }

    public final int getVisibility() {
        Integer num = this.mVisibility;
        if (num != null) {
            return num.intValue();
        }
        return 0;
    }

    public final void setImageDrawable(KeyButtonDrawable keyButtonDrawable) {
        this.mImageDrawable = keyButtonDrawable;
        int size = this.mViews.size();
        for (int i = 0; i < size; i++) {
            if (this.mViews.get(i) instanceof ButtonInterface) {
                ((ButtonInterface) this.mViews.get(i)).setImageDrawable(this.mImageDrawable);
            }
        }
        KeyButtonDrawable keyButtonDrawable2 = this.mImageDrawable;
        if (keyButtonDrawable2 != null) {
            keyButtonDrawable2.setCallback(this.mCurrentView);
        }
    }

    public final void setOnClickListener(View.OnClickListener onClickListener) {
        this.mClickListener = onClickListener;
        int size = this.mViews.size();
        for (int i = 0; i < size; i++) {
            this.mViews.get(i).setOnClickListener(this.mClickListener);
        }
    }

    public final void setOnHoverListener(RotationButtonController$$ExternalSyntheticLambda0 rotationButtonController$$ExternalSyntheticLambda0) {
        this.mOnHoverListener = rotationButtonController$$ExternalSyntheticLambda0;
        int size = this.mViews.size();
        for (int i = 0; i < size; i++) {
            this.mViews.get(i).setOnHoverListener(this.mOnHoverListener);
        }
    }

    public final void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.mLongClickListener = onLongClickListener;
        int size = this.mViews.size();
        for (int i = 0; i < size; i++) {
            this.mViews.get(i).setOnLongClickListener(this.mLongClickListener);
        }
    }

    public void setVisibility(int i) {
        if (this.mVisibility.intValue() != i) {
            ValueAnimator valueAnimator = this.mFadeAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            this.mVisibility = Integer.valueOf(i);
            int size = this.mViews.size();
            for (int i2 = 0; i2 < size; i2++) {
                this.mViews.get(i2).setVisibility(this.mVisibility.intValue());
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v4, types: [com.android.systemui.navigationbar.buttons.ButtonDispatcher$1] */
    public ButtonDispatcher(int i) {
        this.mId = i;
    }

    public final boolean isVisible() {
        if (getVisibility() == 0) {
            return true;
        }
        return false;
    }

    public final void setAlpha(float f, boolean z, boolean z2) {
        long j;
        if (getAlpha() < f) {
            j = 150;
        } else {
            j = 250;
        }
        ValueAnimator valueAnimator = this.mFadeAnimator;
        if (valueAnimator != null && (z2 || z)) {
            valueAnimator.cancel();
        }
        if (z) {
            setVisibility(0);
            ValueAnimator ofFloat = ValueAnimator.ofFloat(getAlpha(), f);
            this.mFadeAnimator = ofFloat;
            ofFloat.setDuration(j);
            this.mFadeAnimator.setInterpolator(Interpolators.LINEAR);
            this.mFadeAnimator.addListener(this.mFadeListener);
            this.mFadeAnimator.addUpdateListener(this.mAlphaListener);
            this.mFadeAnimator.start();
            return;
        }
        int i = (int) (f * 255.0f);
        if (((int) (getAlpha() * 255.0f)) != i) {
            this.mAlpha = Float.valueOf(i / 255.0f);
            int size = this.mViews.size();
            for (int i2 = 0; i2 < size; i2++) {
                this.mViews.get(i2).setAlpha(this.mAlpha.floatValue());
            }
        }
    }

    public final void setDarkIntensity(float f) {
        this.mDarkIntensity = Float.valueOf(f);
        int size = this.mViews.size();
        for (int i = 0; i < size; i++) {
            if (this.mViews.get(i) instanceof ButtonInterface) {
                ((ButtonInterface) this.mViews.get(i)).setDarkIntensity(f);
            }
        }
    }

    public final void setLongClickable(boolean z) {
        this.mLongClickable = Boolean.valueOf(z);
        int size = this.mViews.size();
        for (int i = 0; i < size; i++) {
            this.mViews.get(i).setLongClickable(this.mLongClickable.booleanValue());
        }
    }

    public final View getCurrentView() {
        return this.mCurrentView;
    }
}
