package com.android.systemui.statusbar.notification.row;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.util.MathUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.util.ArrayUtils;
import com.android.settingslib.Utils;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.R$array;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.notification.FakeShadowView;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class ActivatableNotificationView extends ExpandableOutlineView {
    public static final /* synthetic */ int $r8$clinit = 0;
    public boolean mActivated;
    public float mAnimationTranslationY;
    public float mAppearAnimationTranslation;
    public ValueAnimator mAppearAnimator;
    public ValueAnimator mBackgroundColorAnimator;
    public NotificationBackgroundView mBackgroundNormal;
    public PathInterpolator mCurrentAppearInterpolator;
    public int mCurrentBackgroundTint;
    public boolean mDismissed;
    public boolean mDrawingAppearAnimation;
    public FakeShadowView mFakeShadow;
    public boolean mIsBelowSpeedBump;
    public boolean mIsHeadsUpAnimation;
    public long mLastActionUpTime;
    public int mNormalColor;
    public int mNormalRippleColor;
    public OnActivatedListener mOnActivatedListener;
    public float mOverrideAmount;
    public int mOverrideTint;
    public boolean mRefocusOnDismiss;
    public boolean mShadowHidden;
    public int mStartTint;
    public Point mTargetPoint;
    public int mTargetTint;
    public int mTintedRippleColor;
    public Gefingerpoken mTouchHandler;
    public int mBgTint = 0;
    public float mAppearAnimationFraction = -1.0f;
    public final PathInterpolator mSlowOutFastInInterpolator = new PathInterpolator(0.8f, 0.0f, 0.6f, 1.0f);

    /* loaded from: classes.dex */
    public interface OnActivatedListener {
        void onActivationReset(ActivatableNotificationView activatableNotificationView);
    }

    public abstract View getContentView();

    public void onAppearAnimationFinished(boolean z) {
    }

    public void onTap() {
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final void performAddAnimation(long j, long j2, boolean z) {
        float f;
        enableAppearDrawing(true);
        this.mIsHeadsUpAnimation = z;
        if (this.mDrawingAppearAnimation) {
            if (z) {
                f = 0.0f;
            } else {
                f = -1.0f;
            }
            startAppearAnimation(true, f, j, j2, null, null);
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public long performRemoveAnimation(long j, long j2, float f, boolean z, float f2, Runnable runnable, AnimatorListenerAdapter animatorListenerAdapter) {
        enableAppearDrawing(true);
        this.mIsHeadsUpAnimation = z;
        if (this.mDrawingAppearAnimation) {
            startAppearAnimation(false, f, j2, j, runnable, animatorListenerAdapter);
            return 0L;
        } else if (runnable == null) {
            return 0L;
        } else {
            runnable.run();
            return 0L;
        }
    }

    public void resetAllContentAlphas() {
    }

    public void updateBackgroundTint() {
        updateBackgroundTint(false);
    }

    static {
        new PathInterpolator(0.6f, 0.0f, 0.5f, 1.0f);
        new PathInterpolator(0.0f, 0.0f, 0.5f, 1.0f);
    }

    private void updateColors() {
        this.mNormalColor = Utils.getColorAttrDefaultColor(((FrameLayout) this).mContext, 17956909);
        this.mTintedRippleColor = ((FrameLayout) this).mContext.getColor(2131100487);
        this.mNormalRippleColor = ((FrameLayout) this).mContext.getColor(2131100488);
    }

    public final int calculateBgColor(boolean z, boolean z2) {
        int i;
        if (z2 && this.mOverrideTint != 0) {
            return R$array.interpolateColors(calculateBgColor(z, false), this.mOverrideTint, this.mOverrideAmount);
        }
        if (!z || (i = this.mBgTint) == 0) {
            return this.mNormalColor;
        }
        return i;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableOutlineView
    public boolean childNeedsClipping(View view) {
        if (!(view instanceof NotificationBackgroundView) || !isClippingNeeded()) {
            return false;
        }
        return true;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        if (this.mDrawingAppearAnimation) {
            canvas.save();
            canvas.translate(0.0f, this.mAppearAnimationTranslation);
        }
        super.dispatchDraw(canvas);
        if (this.mDrawingAppearAnimation) {
            canvas.restore();
        }
    }

    public final void enableAppearDrawing(boolean z) {
        if (z != this.mDrawingAppearAnimation) {
            this.mDrawingAppearAnimation = z;
            if (!z) {
                setContentAlpha(1.0f);
                this.mAppearAnimationFraction = -1.0f;
                this.mCustomOutline = false;
                applyRoundness();
            }
            invalidate();
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableOutlineView
    public final float getCurrentBackgroundRadiusBottom() {
        float f;
        float f2 = this.mAppearAnimationFraction;
        if (f2 >= 0.0f) {
            f = this.mCurrentAppearInterpolator.getInterpolation(f2);
        } else {
            f = 1.0f;
        }
        return MathUtils.lerp(0.0f, this.mCurrentBottomRoundness * this.mOutlineRadius, f);
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableOutlineView
    public final float getCurrentBackgroundRadiusTop() {
        float f;
        float f2 = this.mAppearAnimationFraction;
        if (f2 >= 0.0f) {
            f = this.mCurrentAppearInterpolator.getInterpolation(f2);
        } else {
            f = 1.0f;
        }
        return MathUtils.lerp(0.0f, this.mCurrentTopRoundness * this.mOutlineRadius, f);
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        Gefingerpoken gefingerpoken = this.mTouchHandler;
        if (gefingerpoken == null || !gefingerpoken.onInterceptTouchEvent(motionEvent)) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        return true;
    }

    public void setBackgroundTintColor(int i) {
        if (i != this.mCurrentBackgroundTint) {
            this.mCurrentBackgroundTint = i;
            if (i == this.mNormalColor) {
                i = 0;
            }
            NotificationBackgroundView notificationBackgroundView = this.mBackgroundNormal;
            Objects.requireNonNull(notificationBackgroundView);
            if (i != 0) {
                notificationBackgroundView.mBackground.setColorFilter(i, PorterDuff.Mode.SRC_ATOP);
            } else {
                notificationBackgroundView.mBackground.clearColorFilter();
            }
            notificationBackgroundView.mTintColor = i;
            notificationBackgroundView.invalidate();
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final void setBelowSpeedBump(boolean z) {
        if (z != this.mIsBelowSpeedBump) {
            this.mIsBelowSpeedBump = z;
            updateBackgroundTint();
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public void setFakeShadowIntensity(float f, float f2, int i, int i2) {
        boolean z;
        View view;
        boolean z2 = this.mShadowHidden;
        if (f == 0.0f) {
            z = true;
        } else {
            z = false;
        }
        this.mShadowHidden = z;
        if (!z || !z2) {
            FakeShadowView fakeShadowView = this.mFakeShadow;
            float translationZ = (getTranslationZ() + 0.1f) * f;
            Objects.requireNonNull(fakeShadowView);
            if (translationZ == 0.0f) {
                fakeShadowView.mFakeShadow.setVisibility(4);
                return;
            }
            fakeShadowView.mFakeShadow.setVisibility(0);
            fakeShadowView.mFakeShadow.setTranslationZ(Math.max(fakeShadowView.mShadowMinHeight, translationZ));
            fakeShadowView.mFakeShadow.setTranslationX(i2);
            fakeShadowView.mFakeShadow.setTranslationY(i - view.getHeight());
            if (f2 != fakeShadowView.mOutlineAlpha) {
                fakeShadowView.mOutlineAlpha = f2;
                fakeShadowView.mFakeShadow.invalidateOutline();
            }
        }
    }

    public final void setOverrideTintColor(int i, float f) {
        this.mOverrideTint = i;
        this.mOverrideAmount = f;
        setBackgroundTintColor(calculateBgColor(true, true));
    }

    public final void startAppearAnimation(final boolean z, float f, long j, long j2, final Runnable runnable, AnimatorListenerAdapter animatorListenerAdapter) {
        this.mAnimationTranslationY = f * this.mActualHeight;
        ValueAnimator valueAnimator = this.mAppearAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.mAppearAnimator = null;
        }
        float f2 = 0.0f;
        if (this.mAppearAnimationFraction == -1.0f) {
            if (z) {
                this.mAppearAnimationFraction = 0.0f;
                this.mAppearAnimationTranslation = this.mAnimationTranslationY;
            } else {
                this.mAppearAnimationFraction = 1.0f;
                this.mAppearAnimationTranslation = 0.0f;
            }
        }
        if (z) {
            this.mCurrentAppearInterpolator = Interpolators.FAST_OUT_SLOW_IN;
            f2 = 1.0f;
        } else {
            this.mCurrentAppearInterpolator = this.mSlowOutFastInInterpolator;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.mAppearAnimationFraction, f2);
        this.mAppearAnimator = ofFloat;
        ofFloat.setInterpolator(Interpolators.LINEAR);
        this.mAppearAnimator.setDuration(Math.abs(this.mAppearAnimationFraction - f2) * ((float) j2));
        this.mAppearAnimator.addUpdateListener(new ActivatableNotificationView$$ExternalSyntheticLambda0(this, 0));
        if (animatorListenerAdapter != null) {
            this.mAppearAnimator.addListener(animatorListenerAdapter);
        }
        if (j > 0) {
            setContentAlpha(Interpolators.ALPHA_IN.getInterpolation((MathUtils.constrain(this.mAppearAnimationFraction, 0.4f, 1.0f) - 0.4f) / 0.6f));
            updateAppearRect();
            this.mAppearAnimator.setStartDelay(j);
        }
        this.mAppearAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.notification.row.ActivatableNotificationView.2
            public boolean mWasCancelled;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationCancel(Animator animator) {
                this.mWasCancelled = true;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationStart(Animator animator) {
                this.mWasCancelled = false;
                InteractionJankMonitor.getInstance().begin(InteractionJankMonitor.Configuration.Builder.withView(ActivatableNotificationView.m95$$Nest$mgetCujType(ActivatableNotificationView.this, z), ActivatableNotificationView.this));
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                Runnable runnable2 = runnable;
                if (runnable2 != null) {
                    runnable2.run();
                }
                if (!this.mWasCancelled) {
                    ActivatableNotificationView activatableNotificationView = ActivatableNotificationView.this;
                    int i = ActivatableNotificationView.$r8$clinit;
                    activatableNotificationView.enableAppearDrawing(false);
                    ActivatableNotificationView.this.onAppearAnimationFinished(z);
                    InteractionJankMonitor.getInstance().end(ActivatableNotificationView.m95$$Nest$mgetCujType(ActivatableNotificationView.this, z));
                    return;
                }
                InteractionJankMonitor.getInstance().cancel(ActivatableNotificationView.m95$$Nest$mgetCujType(ActivatableNotificationView.this, z));
            }
        });
        this.mAppearAnimator.start();
    }

    public final void updateAppearRect() {
        int i;
        int i2;
        float interpolation = this.mCurrentAppearInterpolator.getInterpolation(this.mAppearAnimationFraction);
        float f = (1.0f - interpolation) * this.mAnimationTranslationY;
        this.mAppearAnimationTranslation = f;
        float f2 = this.mActualHeight;
        float f3 = interpolation * f2;
        if (this.mTargetPoint != null) {
            int width = getWidth();
            float f4 = 1.0f - this.mAppearAnimationFraction;
            Point point = this.mTargetPoint;
            int i3 = point.x;
            float f5 = this.mAnimationTranslationY;
            setOutlineRect(i3 * f4, MotionController$$ExternalSyntheticOutline0.m(f5, point.y, f4, f5), width - ((width - i3) * f4), f2 - ((i - i2) * f4));
            return;
        }
        setOutlineRect(0.0f, f, getWidth(), f3 + this.mAppearAnimationTranslation);
    }

    public final void updateBackgroundTint(boolean z) {
        int i;
        ValueAnimator valueAnimator = this.mBackgroundColorAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        if (this.mBgTint != 0) {
            i = this.mTintedRippleColor;
        } else {
            i = this.mNormalRippleColor;
        }
        NotificationBackgroundView notificationBackgroundView = this.mBackgroundNormal;
        Objects.requireNonNull(notificationBackgroundView);
        Drawable drawable = notificationBackgroundView.mBackground;
        if (drawable instanceof RippleDrawable) {
            ((RippleDrawable) drawable).setColor(ColorStateList.valueOf(i));
        }
        int calculateBgColor = calculateBgColor(true, true);
        if (!z) {
            setBackgroundTintColor(calculateBgColor);
            return;
        }
        int i2 = this.mCurrentBackgroundTint;
        if (calculateBgColor != i2) {
            this.mStartTint = i2;
            this.mTargetTint = calculateBgColor;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.mBackgroundColorAnimator = ofFloat;
            ofFloat.addUpdateListener(new ScreenshotView$$ExternalSyntheticLambda0(this, 1));
            this.mBackgroundColorAnimator.setDuration(360L);
            this.mBackgroundColorAnimator.setInterpolator(Interpolators.LINEAR);
            this.mBackgroundColorAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.notification.row.ActivatableNotificationView.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    ActivatableNotificationView.this.mBackgroundColorAnimator = null;
                }
            });
            this.mBackgroundColorAnimator.start();
        }
    }

    /* renamed from: -$$Nest$mgetCujType  reason: not valid java name */
    public static int m95$$Nest$mgetCujType(ActivatableNotificationView activatableNotificationView, boolean z) {
        Objects.requireNonNull(activatableNotificationView);
        if (activatableNotificationView.mIsHeadsUpAnimation) {
            if (z) {
                return 12;
            }
            return 13;
        } else if (z) {
            return 14;
        } else {
            return 15;
        }
    }

    public ActivatableNotificationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        new PathInterpolator(0.8f, 0.0f, 1.0f, 1.0f);
        setClipChildren(false);
        setClipToPadding(false);
        updateColors();
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableOutlineView
    public void applyRoundness() {
        boolean z;
        super.applyRoundness();
        float currentBackgroundRadiusTop = getCurrentBackgroundRadiusTop();
        float currentBackgroundRadiusBottom = getCurrentBackgroundRadiusBottom();
        NotificationBackgroundView notificationBackgroundView = this.mBackgroundNormal;
        Objects.requireNonNull(notificationBackgroundView);
        float[] fArr = notificationBackgroundView.mCornerRadii;
        if (currentBackgroundRadiusTop != fArr[0] || currentBackgroundRadiusBottom != fArr[4]) {
            if (currentBackgroundRadiusBottom != 0.0f) {
                z = true;
            } else {
                z = false;
            }
            notificationBackgroundView.mBottomIsRounded = z;
            fArr[0] = currentBackgroundRadiusTop;
            fArr[1] = currentBackgroundRadiusTop;
            fArr[2] = currentBackgroundRadiusTop;
            fArr[3] = currentBackgroundRadiusTop;
            fArr[4] = currentBackgroundRadiusBottom;
            fArr[5] = currentBackgroundRadiusBottom;
            fArr[6] = currentBackgroundRadiusBottom;
            fArr[7] = currentBackgroundRadiusBottom;
            if (!notificationBackgroundView.mDontModifyCorners) {
                Drawable drawable = notificationBackgroundView.mBackground;
                if (drawable instanceof LayerDrawable) {
                    ((GradientDrawable) ((LayerDrawable) drawable).getDrawable(0)).setCornerRadii(notificationBackgroundView.mCornerRadii);
                }
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void drawableStateChanged() {
        super.drawableStateChanged();
        NotificationBackgroundView notificationBackgroundView = this.mBackgroundNormal;
        int[] drawableState = getDrawableState();
        Objects.requireNonNull(notificationBackgroundView);
        Drawable drawable = notificationBackgroundView.mBackground;
        if (drawable != null && drawable.isStateful()) {
            if (!notificationBackgroundView.mIsPressedAllowed) {
                drawableState = ArrayUtils.removeInt(drawableState, 16842919);
            }
            notificationBackgroundView.mBackground.setState(drawableState);
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public int getHeadsUpHeightWithoutHeader() {
        return getHeight();
    }

    @Override // android.view.View
    public void onFinishInflate() {
        boolean z;
        super.onFinishInflate();
        this.mBackgroundNormal = (NotificationBackgroundView) findViewById(2131427541);
        FakeShadowView fakeShadowView = (FakeShadowView) findViewById(2131427957);
        this.mFakeShadow = fakeShadowView;
        if (fakeShadowView.getVisibility() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.mShadowHidden = z;
        this.mBackgroundNormal.setCustomBackground$1();
        updateBackgroundTint();
        if (0.7f != this.mOutlineAlpha) {
            this.mOutlineAlpha = 0.7f;
            applyRoundness();
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        setPivotX(getWidth() / 2);
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableOutlineView, com.android.systemui.statusbar.notification.row.ExpandableView
    public void setActualHeight(int i, boolean z) {
        super.setActualHeight(i, z);
        setPivotY(i / 2);
        NotificationBackgroundView notificationBackgroundView = this.mBackgroundNormal;
        Objects.requireNonNull(notificationBackgroundView);
        if (!notificationBackgroundView.mExpandAnimationRunning) {
            notificationBackgroundView.mActualHeight = i;
            notificationBackgroundView.invalidate();
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableOutlineView, com.android.systemui.statusbar.notification.row.ExpandableView
    public void setClipBottomAmount(int i) {
        super.setClipBottomAmount(i);
        NotificationBackgroundView notificationBackgroundView = this.mBackgroundNormal;
        Objects.requireNonNull(notificationBackgroundView);
        notificationBackgroundView.mClipBottomAmount = i;
        notificationBackgroundView.invalidate();
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableOutlineView, com.android.systemui.statusbar.notification.row.ExpandableView
    public void setClipTopAmount(int i) {
        super.setClipTopAmount(i);
        NotificationBackgroundView notificationBackgroundView = this.mBackgroundNormal;
        Objects.requireNonNull(notificationBackgroundView);
        notificationBackgroundView.mClipTopAmount = i;
        notificationBackgroundView.invalidate();
    }

    public final void setContentAlpha(float f) {
        int i;
        View contentView = getContentView();
        if (contentView.hasOverlappingRendering()) {
            if (f == 0.0f || f == 1.0f) {
                i = 0;
            } else {
                i = 2;
            }
            contentView.setLayerType(i, null);
        }
        contentView.setAlpha(f);
        if (f == 1.0f) {
            resetAllContentAlphas();
        }
    }

    public final void updateBackgroundColors() {
        updateColors();
        this.mBackgroundNormal.setCustomBackground$1();
        updateBackgroundTint();
    }
}
