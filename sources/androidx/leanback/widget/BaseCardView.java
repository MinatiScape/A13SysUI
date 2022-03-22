package androidx.leanback.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import androidx.leanback.R$styleable;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class BaseCardView extends FrameLayout {
    public static final int[] LB_PRESSED_STATE_SET = {16842919};
    public final int mActivatedAnimDuration;
    public AnimationBase mAnim;
    public final AnonymousClass1 mAnimationTrigger;
    public int mCardType;
    public boolean mDelaySelectedAnim;
    public ArrayList<View> mExtraViewList;
    public float mInfoAlpha;
    public float mInfoOffset;
    public ArrayList<View> mInfoViewList;
    public float mInfoVisFraction;
    public int mInfoVisibility;
    public ArrayList<View> mMainViewList;
    public int mMeasuredHeight;
    public int mMeasuredWidth;
    public final int mSelectedAnimDuration;
    public int mSelectedAnimationDelay;

    /* loaded from: classes.dex */
    public class AnimationBase extends Animation {
        public AnimationBase() {
        }

        public final void mockEnd() {
            applyTransformation(1.0f, null);
            BaseCardView.this.cancelAnimations();
        }

        public final void mockStart() {
            getTransformation(0L, null);
        }
    }

    /* loaded from: classes.dex */
    public final class InfoAlphaAnimation extends AnimationBase {
        public float mDelta;
        public float mStartValue;

        public InfoAlphaAnimation(float f, float f2) {
            super();
            this.mStartValue = f;
            this.mDelta = f2 - f;
        }

        @Override // android.view.animation.Animation
        public final void applyTransformation(float f, Transformation transformation) {
            BaseCardView.this.mInfoAlpha = (f * this.mDelta) + this.mStartValue;
            for (int i = 0; i < BaseCardView.this.mInfoViewList.size(); i++) {
                BaseCardView.this.mInfoViewList.get(i).setAlpha(BaseCardView.this.mInfoAlpha);
            }
        }
    }

    /* loaded from: classes.dex */
    public final class InfoHeightAnimation extends AnimationBase {
        public float mDelta;
        public float mStartValue;

        public InfoHeightAnimation(float f, float f2) {
            super();
            this.mStartValue = f;
            this.mDelta = f2 - f;
        }

        @Override // android.view.animation.Animation
        public final void applyTransformation(float f, Transformation transformation) {
            BaseCardView baseCardView = BaseCardView.this;
            baseCardView.mInfoVisFraction = (f * this.mDelta) + this.mStartValue;
            baseCardView.requestLayout();
        }
    }

    /* loaded from: classes.dex */
    public final class InfoOffsetAnimation extends AnimationBase {
        public float mDelta;
        public float mStartValue;

        public InfoOffsetAnimation(float f, float f2) {
            super();
            this.mStartValue = f;
            this.mDelta = f2 - f;
        }

        @Override // android.view.animation.Animation
        public final void applyTransformation(float f, Transformation transformation) {
            BaseCardView baseCardView = BaseCardView.this;
            baseCardView.mInfoOffset = (f * this.mDelta) + this.mStartValue;
            baseCardView.requestLayout();
        }
    }

    public BaseCardView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 2130968676);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public final ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public final ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        boolean z2;
        float paddingTop = getPaddingTop();
        for (int i5 = 0; i5 < this.mMainViewList.size(); i5++) {
            View view = this.mMainViewList.get(i5);
            if (view.getVisibility() != 8) {
                view.layout(getPaddingLeft(), (int) paddingTop, getPaddingLeft() + this.mMeasuredWidth, (int) (view.getMeasuredHeight() + paddingTop));
                paddingTop += view.getMeasuredHeight();
            }
        }
        boolean z3 = true;
        if (this.mCardType != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (z2) {
            float f = 0.0f;
            for (int i6 = 0; i6 < this.mInfoViewList.size(); i6++) {
                f += this.mInfoViewList.get(i6).getMeasuredHeight();
            }
            int i7 = this.mCardType;
            if (i7 == 1) {
                paddingTop -= f;
                if (paddingTop < 0.0f) {
                    paddingTop = 0.0f;
                }
            } else if (i7 != 2) {
                paddingTop -= this.mInfoOffset;
            } else if (this.mInfoVisibility == 2) {
                f *= this.mInfoVisFraction;
            }
            for (int i8 = 0; i8 < this.mInfoViewList.size(); i8++) {
                View view2 = this.mInfoViewList.get(i8);
                if (view2.getVisibility() != 8) {
                    int measuredHeight = view2.getMeasuredHeight();
                    if (measuredHeight > f) {
                        measuredHeight = (int) f;
                    }
                    float f2 = measuredHeight;
                    paddingTop += f2;
                    view2.layout(getPaddingLeft(), (int) paddingTop, getPaddingLeft() + this.mMeasuredWidth, (int) paddingTop);
                    f -= f2;
                    if (f <= 0.0f) {
                        break;
                    }
                }
            }
            if (this.mCardType != 3) {
                z3 = false;
            }
            if (z3) {
                for (int i9 = 0; i9 < this.mExtraViewList.size(); i9++) {
                    View view3 = this.mExtraViewList.get(i9);
                    if (view3.getVisibility() != 8) {
                        view3.layout(getPaddingLeft(), (int) paddingTop, getPaddingLeft() + this.mMeasuredWidth, (int) (view3.getMeasuredHeight() + paddingTop));
                        paddingTop += view3.getMeasuredHeight();
                    }
                }
            }
        }
        onSizeChanged(0, 0, i3 - i, i4 - i2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0034, code lost:
        if (r15.mInfoVisFraction > 0.0f) goto L_0x0043;
     */
    /* JADX WARN: Removed duplicated region for block: B:101:0x009c A[EDGE_INSN: B:101:0x009c->B:49:0x009c ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0046  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x004e  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0050  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0061  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00ab  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00f1  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x00f3  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x00f6  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x015c  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0162  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0164  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0170  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0179  */
    @Override // android.widget.FrameLayout, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onMeasure(int r16, int r17) {
        /*
            Method dump skipped, instructions count: 425
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.BaseCardView.onMeasure(int, int):void");
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public final boolean shouldDelayChildPressedState() {
        return false;
    }

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [androidx.leanback.widget.BaseCardView$1] */
    @SuppressLint({"CustomViewStyleable"})
    public BaseCardView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mAnimationTrigger = new Runnable() { // from class: androidx.leanback.widget.BaseCardView.1
            @Override // java.lang.Runnable
            public final void run() {
                BaseCardView.this.animateInfoOffset(true);
            }
        };
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.lbBaseCardView, i, 0);
        try {
            this.mCardType = obtainStyledAttributes.getInteger(3, 0);
            Drawable drawable = obtainStyledAttributes.getDrawable(2);
            if (drawable != null) {
                setForeground(drawable);
            }
            Drawable drawable2 = obtainStyledAttributes.getDrawable(1);
            if (drawable2 != null) {
                setBackground(drawable2);
            }
            this.mInfoVisibility = obtainStyledAttributes.getInteger(5, 1);
            obtainStyledAttributes.getInteger(4, 2);
            int i2 = this.mInfoVisibility;
            this.mSelectedAnimationDelay = obtainStyledAttributes.getInteger(6, getResources().getInteger(2131492948));
            this.mSelectedAnimDuration = obtainStyledAttributes.getInteger(7, getResources().getInteger(2131492949));
            this.mActivatedAnimDuration = obtainStyledAttributes.getInteger(0, getResources().getInteger(2131492947));
            obtainStyledAttributes.recycle();
            this.mDelaySelectedAnim = true;
            this.mMainViewList = new ArrayList<>();
            this.mInfoViewList = new ArrayList<>();
            this.mExtraViewList = new ArrayList<>();
            float f = 0.0f;
            this.mInfoOffset = 0.0f;
            this.mInfoVisFraction = (this.mCardType == 2 && this.mInfoVisibility == 2 && !isSelected()) ? 0.0f : 1.0f;
            this.mInfoAlpha = (this.mCardType == 1 && this.mInfoVisibility == 2 && !isSelected()) ? f : 1.0f;
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    public final void cancelAnimations() {
        AnimationBase animationBase = this.mAnim;
        if (animationBase != null) {
            animationBase.cancel();
            this.mAnim = null;
            clearAnimation();
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    /* renamed from: generateDefaultLayoutParams  reason: collision with other method in class */
    public final FrameLayout.LayoutParams mo14generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    /* renamed from: generateLayoutParams  reason: collision with other method in class */
    public final FrameLayout.LayoutParams mo15generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    public final void setInfoViewVisibility(boolean z) {
        float f;
        int i;
        int i2 = this.mCardType;
        float f2 = 0.0f;
        if (i2 == 3) {
            if (z) {
                for (int i3 = 0; i3 < this.mInfoViewList.size(); i3++) {
                    this.mInfoViewList.get(i3).setVisibility(0);
                }
                return;
            }
            for (int i4 = 0; i4 < this.mInfoViewList.size(); i4++) {
                this.mInfoViewList.get(i4).setVisibility(8);
            }
            for (int i5 = 0; i5 < this.mExtraViewList.size(); i5++) {
                this.mExtraViewList.get(i5).setVisibility(8);
            }
            this.mInfoOffset = 0.0f;
        } else if (i2 == 2) {
            if (this.mInfoVisibility == 2) {
                cancelAnimations();
                if (z) {
                    for (int i6 = 0; i6 < this.mInfoViewList.size(); i6++) {
                        this.mInfoViewList.get(i6).setVisibility(0);
                    }
                }
                if (z) {
                    f2 = 1.0f;
                }
                if (this.mInfoVisFraction != f2) {
                    InfoHeightAnimation infoHeightAnimation = new InfoHeightAnimation(this.mInfoVisFraction, f2);
                    this.mAnim = infoHeightAnimation;
                    infoHeightAnimation.setDuration(this.mSelectedAnimDuration);
                    this.mAnim.setInterpolator(new AccelerateDecelerateInterpolator());
                    this.mAnim.setAnimationListener(new Animation.AnimationListener() { // from class: androidx.leanback.widget.BaseCardView.3
                        @Override // android.view.animation.Animation.AnimationListener
                        public final void onAnimationRepeat(Animation animation) {
                        }

                        @Override // android.view.animation.Animation.AnimationListener
                        public final void onAnimationStart(Animation animation) {
                        }

                        @Override // android.view.animation.Animation.AnimationListener
                        public final void onAnimationEnd(Animation animation) {
                            if (BaseCardView.this.mInfoVisFraction == 0.0f) {
                                for (int i7 = 0; i7 < BaseCardView.this.mInfoViewList.size(); i7++) {
                                    BaseCardView.this.mInfoViewList.get(i7).setVisibility(8);
                                }
                            }
                        }
                    });
                    startAnimation(this.mAnim);
                    return;
                }
                return;
            }
            for (int i7 = 0; i7 < this.mInfoViewList.size(); i7++) {
                View view = this.mInfoViewList.get(i7);
                if (z) {
                    i = 0;
                } else {
                    i = 8;
                }
                view.setVisibility(i);
            }
        } else if (i2 == 1) {
            cancelAnimations();
            if (z) {
                for (int i8 = 0; i8 < this.mInfoViewList.size(); i8++) {
                    this.mInfoViewList.get(i8).setVisibility(0);
                }
            }
            if (z) {
                f = 1.0f;
            } else {
                f = 0.0f;
            }
            if (f != this.mInfoAlpha) {
                float f3 = this.mInfoAlpha;
                if (z) {
                    f2 = 1.0f;
                }
                InfoAlphaAnimation infoAlphaAnimation = new InfoAlphaAnimation(f3, f2);
                this.mAnim = infoAlphaAnimation;
                infoAlphaAnimation.setDuration(this.mActivatedAnimDuration);
                this.mAnim.setInterpolator(new DecelerateInterpolator());
                this.mAnim.setAnimationListener(new Animation.AnimationListener() { // from class: androidx.leanback.widget.BaseCardView.4
                    @Override // android.view.animation.Animation.AnimationListener
                    public final void onAnimationRepeat(Animation animation) {
                    }

                    @Override // android.view.animation.Animation.AnimationListener
                    public final void onAnimationStart(Animation animation) {
                    }

                    @Override // android.view.animation.Animation.AnimationListener
                    public final void onAnimationEnd(Animation animation) {
                        if (BaseCardView.this.mInfoAlpha == 0.0d) {
                            for (int i9 = 0; i9 < BaseCardView.this.mInfoViewList.size(); i9++) {
                                BaseCardView.this.mInfoViewList.get(i9).setVisibility(8);
                            }
                        }
                    }
                });
                startAnimation(this.mAnim);
            }
        }
    }

    public final void animateInfoOffset(boolean z) {
        cancelAnimations();
        int i = 0;
        if (z) {
            int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(this.mMeasuredWidth, 1073741824);
            int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(0, 0);
            int i2 = 0;
            for (int i3 = 0; i3 < this.mExtraViewList.size(); i3++) {
                View view = this.mExtraViewList.get(i3);
                view.setVisibility(0);
                view.measure(makeMeasureSpec, makeMeasureSpec2);
                i2 = Math.max(i2, view.getMeasuredHeight());
            }
            i = i2;
        }
        InfoOffsetAnimation infoOffsetAnimation = new InfoOffsetAnimation(this.mInfoOffset, z ? i : 0.0f);
        this.mAnim = infoOffsetAnimation;
        infoOffsetAnimation.setDuration(this.mSelectedAnimDuration);
        this.mAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        this.mAnim.setAnimationListener(new Animation.AnimationListener() { // from class: androidx.leanback.widget.BaseCardView.2
            @Override // android.view.animation.Animation.AnimationListener
            public final void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public final void onAnimationStart(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public final void onAnimationEnd(Animation animation) {
                if (BaseCardView.this.mInfoOffset == 0.0f) {
                    for (int i4 = 0; i4 < BaseCardView.this.mExtraViewList.size(); i4++) {
                        BaseCardView.this.mExtraViewList.get(i4).setVisibility(8);
                    }
                }
            }
        });
        startAnimation(this.mAnim);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public final ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof LayoutParams) {
            return new LayoutParams((LayoutParams) layoutParams);
        }
        return new LayoutParams(layoutParams);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final int[] onCreateDrawableState(int i) {
        int[] onCreateDrawableState = super.onCreateDrawableState(i);
        int length = onCreateDrawableState.length;
        boolean z = false;
        boolean z2 = false;
        for (int i2 = 0; i2 < length; i2++) {
            if (onCreateDrawableState[i2] == 16842919) {
                z = true;
            }
            if (onCreateDrawableState[i2] == 16842910) {
                z2 = true;
            }
        }
        if (z && z2) {
            return View.PRESSED_ENABLED_STATE_SET;
        }
        if (z) {
            return LB_PRESSED_STATE_SET;
        }
        if (z2) {
            return View.ENABLED_STATE_SET;
        }
        return View.EMPTY_STATE_SET;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(this.mAnimationTrigger);
        cancelAnimations();
    }

    @Override // android.view.View
    public final void setActivated(boolean z) {
        boolean z2;
        int i;
        if (z != isActivated()) {
            super.setActivated(z);
            boolean z3 = true;
            if (this.mCardType != 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z2 && (i = this.mInfoVisibility) == 1) {
                if (i != 0) {
                    if (i == 1) {
                        z3 = isActivated();
                    } else if (i != 2) {
                        z3 = false;
                    } else {
                        z3 = isSelected();
                    }
                }
                setInfoViewVisibility(z3);
            }
        }
    }

    @Override // android.view.View
    public final void setSelected(boolean z) {
        if (z != isSelected()) {
            super.setSelected(z);
            boolean isSelected = isSelected();
            removeCallbacks(this.mAnimationTrigger);
            if (this.mCardType == 3) {
                if (!isSelected) {
                    animateInfoOffset(false);
                } else if (!this.mDelaySelectedAnim) {
                    post(this.mAnimationTrigger);
                    this.mDelaySelectedAnim = true;
                } else {
                    postDelayed(this.mAnimationTrigger, this.mSelectedAnimationDelay);
                }
            } else if (this.mInfoVisibility == 2) {
                setInfoViewVisibility(isSelected);
            }
        }
    }

    @Override // android.view.View
    public final String toString() {
        return super.toString();
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends FrameLayout.LayoutParams {
        @ViewDebug.ExportedProperty(category = "layout", mapping = {@ViewDebug.IntToString(from = 0, to = "MAIN"), @ViewDebug.IntToString(from = 1, to = "INFO"), @ViewDebug.IntToString(from = 2, to = "EXTRA")})
        public int viewType;

        @SuppressLint({"CustomViewStyleable"})
        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.viewType = 0;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.lbBaseCardView_Layout);
            this.viewType = obtainStyledAttributes.getInt(0, 0);
            obtainStyledAttributes.recycle();
        }

        public LayoutParams() {
            super(-2, -2);
            this.viewType = 0;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.viewType = 0;
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ViewGroup.MarginLayoutParams) layoutParams);
            this.viewType = 0;
            this.viewType = layoutParams.viewType;
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public final boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }
}
