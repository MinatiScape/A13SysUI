package com.google.android.systemui.assist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.os.Trace;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda0;
import com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda2;
import com.android.settingslib.Utils;
import com.android.systemui.Dependency;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.navigationbar.buttons.ButtonInterface;
import com.android.systemui.navigationbar.buttons.KeyButtonDrawable;
import com.android.systemui.navigationbar.buttons.KeyButtonView;
import com.android.systemui.recents.OverviewProxyService;
import com.google.android.systemui.elmyra.feedback.FeedbackEffect;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public class OpaLayout extends FrameLayout implements ButtonInterface, FeedbackEffect {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final PathInterpolator HOME_DISAPPEAR_INTERPOLATOR;
    public final ArrayList<View> mAnimatedViews;
    public int mAnimationState;
    public View mBlue;
    public View mBottom;
    public final ArraySet<Animator> mCurrentAnimators;
    public boolean mDelayTouchFeedback;
    public final LockIconViewController$$ExternalSyntheticLambda0 mDiamondAnimation;
    public boolean mDiamondAnimationDelayed;
    public final PathInterpolator mDiamondInterpolator;
    public long mGestureAnimationSetDuration;
    public AnimatorSet mGestureAnimatorSet;
    public AnimatorSet mGestureLineSet;
    public int mGestureState;
    public View mGreen;
    public ImageView mHalo;
    public KeyButtonView mHome;
    public int mHomeDiameter;
    public boolean mIsPressed;
    public boolean mIsVertical;
    public View mLeft;
    public boolean mOpaEnabled;
    public boolean mOpaEnabledNeedsUpdate;
    public final AnonymousClass2 mOverviewProxyListener;
    public OverviewProxyService mOverviewProxyService;
    public View mRed;
    public Resources mResources;
    public final AnonymousClass1 mRetract;
    public View mRight;
    public long mStartTime;
    public View mTop;
    public int mTouchDownX;
    public int mTouchDownY;
    public ImageView mWhite;
    public ImageView mWhiteCutout;
    public boolean mWindowVisible;
    public View mYellow;

    /* renamed from: com.google.android.systemui.assist.OpaLayout$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements Runnable {
        public AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            OpaLayout opaLayout = OpaLayout.this;
            int i = OpaLayout.$r8$clinit;
            opaLayout.cancelCurrentAnimation("retract");
            OpaLayout.this.startRetractAnimation();
        }
    }

    public OpaLayout(Context context) {
        this(context, null);
    }

    public static ObjectAnimator getPropertyAnimator(View view, Property property, float f, int i, PathInterpolator pathInterpolator) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, property, f);
        ofFloat.setDuration(i);
        ofFloat.setInterpolator(pathInterpolator);
        return ofFloat;
    }

    public OpaLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @Override // com.android.systemui.navigationbar.buttons.ButtonInterface
    public final void abortCurrentGesture() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("OpaLayout.abortCurrentGesture: animState=");
        m.append(this.mAnimationState);
        Trace.beginSection(m.toString());
        Trace.endSection();
        this.mHome.abortCurrentGesture();
        this.mIsPressed = false;
        this.mDiamondAnimationDelayed = false;
        removeCallbacks(this.mDiamondAnimation);
        cancelLongPress();
        int i = this.mAnimationState;
        if (i == 3 || i == 1) {
            this.mRetract.run();
        }
    }

    public final void cancelCurrentAnimation(String str) {
        Trace.beginSection("OpaLayout.cancelCurrentAnimation: reason=" + str);
        Trace.endSection();
        if (!this.mCurrentAnimators.isEmpty()) {
            for (int size = this.mCurrentAnimators.size() - 1; size >= 0; size--) {
                Animator valueAt = this.mCurrentAnimators.valueAt(size);
                valueAt.removeAllListeners();
                valueAt.cancel();
            }
            this.mCurrentAnimators.clear();
            this.mAnimationState = 0;
        }
        AnimatorSet animatorSet = this.mGestureAnimatorSet;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.mGestureState = 0;
        }
    }

    public final void endCurrentAnimation(String str) {
        Trace.beginSection("OpaLayout.endCurrentAnimation: reason=" + str);
        if (!this.mCurrentAnimators.isEmpty()) {
            for (int size = this.mCurrentAnimators.size() - 1; size >= 0; size--) {
                Animator valueAt = this.mCurrentAnimators.valueAt(size);
                valueAt.removeAllListeners();
                valueAt.end();
            }
            this.mCurrentAnimators.clear();
        }
        this.mAnimationState = 0;
    }

    public final boolean getOpaEnabled() {
        if (this.mOpaEnabledNeedsUpdate) {
            AssistManagerGoogle assistManagerGoogle = (AssistManagerGoogle) Dependency.get(AssistManager.class);
            Objects.requireNonNull(assistManagerGoogle);
            OpaEnabledReceiver opaEnabledReceiver = assistManagerGoogle.mOpaEnabledReceiver;
            Objects.requireNonNull(opaEnabledReceiver);
            opaEnabledReceiver.dispatchOpaEnabledState(opaEnabledReceiver.mContext);
            if (this.mOpaEnabledNeedsUpdate) {
                Log.w("OpaLayout", "mOpaEnabledNeedsUpdate not cleared by AssistManagerGoogle!");
            }
        }
        return this.mOpaEnabled;
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public final void onProgress(float f, int i) {
        AnimatorSet animatorSet;
        if (this.mGestureState != 2 && allowAnimations()) {
            if (this.mAnimationState == 2) {
                endCurrentAnimation("progress=" + f);
            }
            if (this.mAnimationState == 0) {
                if (this.mGestureAnimatorSet == null) {
                    AnimatorSet animatorSet2 = this.mGestureLineSet;
                    if (animatorSet2 != null) {
                        animatorSet2.removeAllListeners();
                        this.mGestureLineSet.cancel();
                        animatorSet = this.mGestureLineSet;
                    } else {
                        this.mGestureLineSet = new AnimatorSet();
                        ImageView imageView = this.mWhite;
                        PathInterpolator pathInterpolator = OpaUtils.INTERPOLATOR_40_OUT;
                        ObjectAnimator scaleObjectAnimator = OpaUtils.getScaleObjectAnimator(imageView, 0.0f, 100, pathInterpolator);
                        ObjectAnimator scaleObjectAnimator2 = OpaUtils.getScaleObjectAnimator(this.mWhiteCutout, 0.0f, 100, pathInterpolator);
                        ObjectAnimator scaleObjectAnimator3 = OpaUtils.getScaleObjectAnimator(this.mHalo, 0.0f, 100, pathInterpolator);
                        scaleObjectAnimator.setStartDelay(50L);
                        scaleObjectAnimator2.setStartDelay(50L);
                        this.mGestureLineSet.play(scaleObjectAnimator).with(scaleObjectAnimator2).with(scaleObjectAnimator3);
                        View view = this.mTop;
                        PathInterpolator pathInterpolator2 = Interpolators.FAST_OUT_SLOW_IN;
                        AnimatorSet.Builder with = this.mGestureLineSet.play(OpaUtils.getScaleObjectAnimator(view, 0.8f, 200, pathInterpolator2)).with(scaleObjectAnimator);
                        View view2 = this.mRed;
                        LinearInterpolator linearInterpolator = Interpolators.LINEAR;
                        with.with(OpaUtils.getAlphaObjectAnimator(view2, 130, linearInterpolator)).with(OpaUtils.getAlphaObjectAnimator(this.mYellow, 130, linearInterpolator)).with(OpaUtils.getAlphaObjectAnimator(this.mBlue, 113, linearInterpolator)).with(OpaUtils.getAlphaObjectAnimator(this.mGreen, 113, linearInterpolator)).with(OpaUtils.getScaleObjectAnimator(this.mBottom, 0.8f, 200, pathInterpolator2)).with(OpaUtils.getScaleObjectAnimator(this.mLeft, 0.8f, 200, pathInterpolator2)).with(OpaUtils.getScaleObjectAnimator(this.mRight, 0.8f, 200, pathInterpolator2));
                        if (this.mIsVertical) {
                            View view3 = this.mRed;
                            PathInterpolator pathInterpolator3 = OpaUtils.INTERPOLATOR_40_40;
                            ObjectAnimator translationObjectAnimatorY = OpaUtils.getTranslationObjectAnimatorY(view3, pathInterpolator3, this.mResources.getDimensionPixelOffset(2131166719), this.mRed.getY() + 0.0f);
                            translationObjectAnimatorY.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.OpaLayout.7
                                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                public final void onAnimationEnd(Animator animator) {
                                    OpaLayout.m166$$Nest$mstartCollapseAnimation(OpaLayout.this);
                                }
                            });
                            this.mGestureLineSet.play(translationObjectAnimatorY).with(scaleObjectAnimator3).with(OpaUtils.getTranslationObjectAnimatorY(this.mBlue, pathInterpolator3, this.mResources.getDimensionPixelOffset(2131166718), this.mBlue.getY() + this.mResources.getDimensionPixelOffset(2131166713))).with(OpaUtils.getTranslationObjectAnimatorY(this.mYellow, pathInterpolator3, -this.mResources.getDimensionPixelOffset(2131166719), this.mYellow.getY() + 0.0f)).with(OpaUtils.getTranslationObjectAnimatorY(this.mGreen, pathInterpolator3, -this.mResources.getDimensionPixelOffset(2131166718), this.mGreen.getY() + (-this.mResources.getDimensionPixelOffset(2131166713))));
                        } else {
                            View view4 = this.mRed;
                            PathInterpolator pathInterpolator4 = OpaUtils.INTERPOLATOR_40_40;
                            ObjectAnimator translationObjectAnimatorX = OpaUtils.getTranslationObjectAnimatorX(view4, pathInterpolator4, -this.mResources.getDimensionPixelOffset(2131166719), this.mRed.getX() + 0.0f, 350);
                            translationObjectAnimatorX.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.OpaLayout.8
                                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                public final void onAnimationEnd(Animator animator) {
                                    OpaLayout.m166$$Nest$mstartCollapseAnimation(OpaLayout.this);
                                }
                            });
                            this.mGestureLineSet.play(translationObjectAnimatorX).with(scaleObjectAnimator).with(OpaUtils.getTranslationObjectAnimatorX(this.mBlue, pathInterpolator4, -this.mResources.getDimensionPixelOffset(2131166718), this.mBlue.getX() + (-this.mResources.getDimensionPixelOffset(2131166713)), 350)).with(OpaUtils.getTranslationObjectAnimatorX(this.mYellow, pathInterpolator4, this.mResources.getDimensionPixelOffset(2131166719), this.mYellow.getX() + 0.0f, 350)).with(OpaUtils.getTranslationObjectAnimatorX(this.mGreen, pathInterpolator4, this.mResources.getDimensionPixelOffset(2131166718), this.mGreen.getX() + this.mResources.getDimensionPixelOffset(2131166713), 350));
                        }
                        animatorSet = this.mGestureLineSet;
                    }
                    this.mGestureAnimatorSet = animatorSet;
                    this.mGestureAnimationSetDuration = animatorSet.getTotalDuration();
                }
                this.mGestureAnimatorSet.setCurrentPlayTime(((float) (this.mGestureAnimationSetDuration - 1)) * f);
                if (f == 0.0f) {
                    this.mGestureState = 0;
                } else {
                    this.mGestureState = 1;
                }
            }
        }
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public final void onRelease() {
        if (this.mAnimationState == 0 && this.mGestureState == 1) {
            AnimatorSet animatorSet = this.mGestureAnimatorSet;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            this.mGestureState = 0;
            startRetractAnimation();
        }
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public final void onResolve(GestureSensor.DetectionProperties detectionProperties) {
        AnimatorSet animatorSet;
        if (this.mAnimationState == 0) {
            if (this.mGestureState != 1 || (animatorSet = this.mGestureAnimatorSet) == null || animatorSet.isStarted()) {
                skipToStartingValue();
                return;
            }
            this.mGestureAnimatorSet.start();
            this.mGestureState = 2;
        }
    }

    @Override // com.android.systemui.navigationbar.buttons.ButtonInterface
    public final void setDarkIntensity(float f) {
        if (this.mWhite.getDrawable() instanceof KeyButtonDrawable) {
            ((KeyButtonDrawable) this.mWhite.getDrawable()).setDarkIntensity(f);
        }
        ((KeyButtonDrawable) this.mHalo.getDrawable()).setDarkIntensity(f);
        this.mWhite.invalidate();
        this.mHalo.invalidate();
        this.mHome.setDarkIntensity(f);
    }

    @Override // com.android.systemui.navigationbar.buttons.ButtonInterface
    public final void setImageDrawable(Drawable drawable) {
        this.mWhite.setImageDrawable(drawable);
        this.mWhiteCutout.setImageDrawable(drawable);
    }

    @Override // android.view.View
    public final void setOnLongClickListener(final View.OnLongClickListener onLongClickListener) {
        if (onLongClickListener == null) {
            this.mHome.setLongClickable(false);
            return;
        }
        this.mHome.setLongClickable(true);
        this.mHome.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.google.android.systemui.assist.OpaLayout$$ExternalSyntheticLambda0
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                OpaLayout opaLayout = OpaLayout.this;
                View.OnLongClickListener onLongClickListener2 = onLongClickListener;
                int i = OpaLayout.$r8$clinit;
                Objects.requireNonNull(opaLayout);
                return onLongClickListener2.onLongClick(opaLayout.mHome);
            }
        });
    }

    @Override // android.view.View
    public final void setOnTouchListener(View.OnTouchListener onTouchListener) {
        this.mHome.setOnTouchListener(onTouchListener);
    }

    @Override // com.android.systemui.navigationbar.buttons.ButtonInterface
    public final void setVertical(boolean z) {
        AnimatorSet animatorSet;
        if (!(this.mIsVertical == z || (animatorSet = this.mGestureAnimatorSet) == null)) {
            animatorSet.cancel();
            this.mGestureAnimatorSet = null;
            skipToStartingValue();
        }
        this.mIsVertical = z;
        Objects.requireNonNull(this.mHome);
        if (this.mIsVertical) {
            this.mTop = this.mGreen;
            this.mBottom = this.mBlue;
            this.mRight = this.mYellow;
            this.mLeft = this.mRed;
            return;
        }
        this.mTop = this.mRed;
        this.mBottom = this.mYellow;
        this.mLeft = this.mBlue;
        this.mRight = this.mGreen;
    }

    public final void skipToStartingValue() {
        int size = this.mAnimatedViews.size();
        for (int i = 0; i < size; i++) {
            View view = this.mAnimatedViews.get(i);
            view.setScaleY(1.0f);
            view.setScaleX(1.0f);
            view.setTranslationY(0.0f);
            view.setTranslationX(0.0f);
            view.setAlpha(0.0f);
        }
        this.mHalo.setAlpha(1.0f);
        this.mWhite.setAlpha(1.0f);
        this.mWhiteCutout.setAlpha(1.0f);
        this.mAnimationState = 0;
        this.mGestureState = 0;
    }

    public final void updateOpaLayout() {
        boolean z;
        int i;
        int i2;
        ImageView.ScaleType scaleType;
        boolean shouldShowSwipeUpUI = this.mOverviewProxyService.shouldShowSwipeUpUI();
        boolean z2 = true;
        if (!this.mOpaEnabled || shouldShowSwipeUpUI) {
            z = false;
        } else {
            z = true;
        }
        ImageView imageView = this.mHalo;
        if (z) {
            i = 0;
        } else {
            i = 4;
        }
        imageView.setVisibility(i);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mHalo.getLayoutParams();
        if (z || shouldShowSwipeUpUI) {
            z2 = false;
        }
        if (z2) {
            i2 = this.mHomeDiameter;
        } else {
            i2 = -1;
        }
        layoutParams.width = i2;
        layoutParams.height = i2;
        this.mWhite.setLayoutParams(layoutParams);
        this.mWhiteCutout.setLayoutParams(layoutParams);
        if (z2) {
            scaleType = ImageView.ScaleType.FIT_CENTER;
        } else {
            scaleType = ImageView.ScaleType.CENTER;
        }
        this.mWhite.setScaleType(scaleType);
        this.mWhiteCutout.setScaleType(scaleType);
    }

    /* renamed from: -$$Nest$mstartCollapseAnimation  reason: not valid java name */
    public static void m166$$Nest$mstartCollapseAnimation(OpaLayout opaLayout) {
        ObjectAnimator objectAnimator;
        ObjectAnimator objectAnimator2;
        ObjectAnimator objectAnimator3;
        ObjectAnimator objectAnimator4;
        Objects.requireNonNull(opaLayout);
        if (opaLayout.allowAnimations()) {
            opaLayout.mCurrentAnimators.clear();
            ArraySet<Animator> arraySet = opaLayout.mCurrentAnimators;
            ArraySet<? extends Animator> arraySet2 = new ArraySet<>();
            if (opaLayout.mIsVertical) {
                objectAnimator = getPropertyAnimator(opaLayout.mRed, FrameLayout.TRANSLATION_Y, 0.0f, 133, OpaUtils.INTERPOLATOR_40_OUT);
            } else {
                objectAnimator = getPropertyAnimator(opaLayout.mRed, FrameLayout.TRANSLATION_X, 0.0f, 133, OpaUtils.INTERPOLATOR_40_OUT);
            }
            arraySet2.add(objectAnimator);
            View view = opaLayout.mRed;
            Property property = FrameLayout.SCALE_X;
            PathInterpolator pathInterpolator = OpaUtils.INTERPOLATOR_40_OUT;
            arraySet2.add(getPropertyAnimator(view, property, 1.0f, 200, pathInterpolator));
            arraySet2.add(getPropertyAnimator(opaLayout.mRed, FrameLayout.SCALE_Y, 1.0f, 200, pathInterpolator));
            if (opaLayout.mIsVertical) {
                objectAnimator2 = getPropertyAnimator(opaLayout.mBlue, FrameLayout.TRANSLATION_Y, 0.0f, 150, pathInterpolator);
            } else {
                objectAnimator2 = getPropertyAnimator(opaLayout.mBlue, FrameLayout.TRANSLATION_X, 0.0f, 150, pathInterpolator);
            }
            arraySet2.add(objectAnimator2);
            arraySet2.add(getPropertyAnimator(opaLayout.mBlue, FrameLayout.SCALE_X, 1.0f, 200, pathInterpolator));
            arraySet2.add(getPropertyAnimator(opaLayout.mBlue, FrameLayout.SCALE_Y, 1.0f, 200, pathInterpolator));
            if (opaLayout.mIsVertical) {
                objectAnimator3 = getPropertyAnimator(opaLayout.mYellow, FrameLayout.TRANSLATION_Y, 0.0f, 133, pathInterpolator);
            } else {
                objectAnimator3 = getPropertyAnimator(opaLayout.mYellow, FrameLayout.TRANSLATION_X, 0.0f, 133, pathInterpolator);
            }
            arraySet2.add(objectAnimator3);
            arraySet2.add(getPropertyAnimator(opaLayout.mYellow, FrameLayout.SCALE_X, 1.0f, 200, pathInterpolator));
            arraySet2.add(getPropertyAnimator(opaLayout.mYellow, FrameLayout.SCALE_Y, 1.0f, 200, pathInterpolator));
            if (opaLayout.mIsVertical) {
                objectAnimator4 = getPropertyAnimator(opaLayout.mGreen, FrameLayout.TRANSLATION_Y, 0.0f, 150, pathInterpolator);
            } else {
                objectAnimator4 = getPropertyAnimator(opaLayout.mGreen, FrameLayout.TRANSLATION_X, 0.0f, 150, pathInterpolator);
            }
            arraySet2.add(objectAnimator4);
            arraySet2.add(getPropertyAnimator(opaLayout.mGreen, FrameLayout.SCALE_X, 1.0f, 200, pathInterpolator));
            arraySet2.add(getPropertyAnimator(opaLayout.mGreen, FrameLayout.SCALE_Y, 1.0f, 200, pathInterpolator));
            ImageView imageView = opaLayout.mWhite;
            Property property2 = FrameLayout.SCALE_X;
            PathInterpolator pathInterpolator2 = Interpolators.FAST_OUT_SLOW_IN;
            ObjectAnimator propertyAnimator = getPropertyAnimator(imageView, property2, 1.0f, 150, pathInterpolator2);
            ObjectAnimator propertyAnimator2 = getPropertyAnimator(opaLayout.mWhite, FrameLayout.SCALE_Y, 1.0f, 150, pathInterpolator2);
            ObjectAnimator propertyAnimator3 = getPropertyAnimator(opaLayout.mWhiteCutout, FrameLayout.SCALE_X, 1.0f, 150, pathInterpolator2);
            ObjectAnimator propertyAnimator4 = getPropertyAnimator(opaLayout.mWhiteCutout, FrameLayout.SCALE_Y, 1.0f, 150, pathInterpolator2);
            ObjectAnimator propertyAnimator5 = getPropertyAnimator(opaLayout.mHalo, FrameLayout.SCALE_X, 1.0f, 150, pathInterpolator2);
            ObjectAnimator propertyAnimator6 = getPropertyAnimator(opaLayout.mHalo, FrameLayout.SCALE_Y, 1.0f, 150, pathInterpolator2);
            ObjectAnimator propertyAnimator7 = getPropertyAnimator(opaLayout.mHalo, FrameLayout.ALPHA, 1.0f, 150, pathInterpolator2);
            propertyAnimator.setStartDelay(33L);
            propertyAnimator2.setStartDelay(33L);
            propertyAnimator3.setStartDelay(33L);
            propertyAnimator4.setStartDelay(33L);
            propertyAnimator5.setStartDelay(33L);
            propertyAnimator6.setStartDelay(33L);
            propertyAnimator7.setStartDelay(33L);
            arraySet2.add(propertyAnimator);
            arraySet2.add(propertyAnimator2);
            arraySet2.add(propertyAnimator3);
            arraySet2.add(propertyAnimator4);
            arraySet2.add(propertyAnimator5);
            arraySet2.add(propertyAnimator6);
            arraySet2.add(propertyAnimator7);
            getLongestAnim(arraySet2).addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.OpaLayout.5
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationCancel(Animator animator) {
                    Trace.beginSection("OpaLayout.cancel.collapse");
                    Trace.endSection();
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    Trace.beginSection("OpaLayout.end.collapse");
                    Trace.endSection();
                    OpaLayout.this.mCurrentAnimators.clear();
                    OpaLayout.this.skipToStartingValue();
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationStart(Animator animator) {
                    Trace.beginSection("OpaLayout.start.collapse");
                    Trace.endSection();
                }
            });
            arraySet.addAll(arraySet2);
            opaLayout.mAnimationState = 3;
            opaLayout.startAll(opaLayout.mCurrentAnimators);
            return;
        }
        opaLayout.skipToStartingValue();
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [com.google.android.systemui.assist.OpaLayout$2] */
    public OpaLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.HOME_DISAPPEAR_INTERPOLATOR = new PathInterpolator(0.65f, 0.0f, 1.0f, 1.0f);
        this.mDiamondInterpolator = new PathInterpolator(0.2f, 0.0f, 0.2f, 1.0f);
        this.mCurrentAnimators = new ArraySet<>();
        this.mAnimatedViews = new ArrayList<>();
        this.mAnimationState = 0;
        this.mGestureState = 0;
        this.mRetract = new AnonymousClass1();
        this.mOverviewProxyListener = new OverviewProxyService.OverviewProxyListener() { // from class: com.google.android.systemui.assist.OpaLayout.2
            @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
            public final void onConnectionChanged(boolean z) {
                OpaLayout.this.updateOpaLayout();
            }
        };
        this.mDiamondAnimation = new LockIconViewController$$ExternalSyntheticLambda0(this, 5);
    }

    public static Animator getLongestAnim(ArraySet arraySet) {
        long j = Long.MIN_VALUE;
        Animator animator = null;
        for (int size = arraySet.size() - 1; size >= 0; size--) {
            Animator animator2 = (Animator) arraySet.valueAt(size);
            if (animator2.getTotalDuration() > j) {
                j = animator2.getTotalDuration();
                animator = animator2;
            }
        }
        return animator;
    }

    public final boolean allowAnimations() {
        if (!isAttachedToWindow() || !this.mWindowVisible) {
            return false;
        }
        return true;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mOverviewProxyService.addCallback((OverviewProxyService.OverviewProxyListener) this.mOverviewProxyListener);
        this.mOpaEnabledNeedsUpdate = true;
        post(new LockIconViewController$$ExternalSyntheticLambda2(this, 11));
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateOpaLayout();
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mOverviewProxyService.removeCallback((OverviewProxyService.OverviewProxyListener) this.mOverviewProxyListener);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mResources = getResources();
        this.mBlue = findViewById(2131427582);
        this.mRed = findViewById(2131428678);
        this.mYellow = findViewById(2131429287);
        this.mGreen = findViewById(2131428038);
        this.mWhite = (ImageView) findViewById(2131429246);
        this.mWhiteCutout = (ImageView) findViewById(2131429247);
        this.mHalo = (ImageView) findViewById(2131428076);
        this.mHome = (KeyButtonView) findViewById(2131428096);
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getContext(), 2132017486);
        ContextThemeWrapper contextThemeWrapper2 = new ContextThemeWrapper(getContext(), 2132017485);
        ImageView imageView = this.mHalo;
        KeyButtonDrawable.AnonymousClass1 r3 = KeyButtonDrawable.KEY_DRAWABLE_ROTATE;
        imageView.setImageDrawable(KeyButtonDrawable.create(contextThemeWrapper, Utils.getColorAttrDefaultColor(contextThemeWrapper, 2130969739), Utils.getColorAttrDefaultColor(contextThemeWrapper2, 2130969739), 2131231727, true));
        this.mHomeDiameter = this.mResources.getDimensionPixelSize(2131166714);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        this.mWhiteCutout.setLayerType(2, paint);
        this.mAnimatedViews.add(this.mBlue);
        this.mAnimatedViews.add(this.mRed);
        this.mAnimatedViews.add(this.mYellow);
        this.mAnimatedViews.add(this.mGreen);
        this.mAnimatedViews.add(this.mWhite);
        this.mAnimatedViews.add(this.mWhiteCutout);
        this.mAnimatedViews.add(this.mHalo);
        this.mOverviewProxyService = (OverviewProxyService) Dependency.get(OverviewProxyService.class);
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0020, code lost:
        if (r0 != 3) goto L_0x00e5;
     */
    @Override // android.view.ViewGroup
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean onInterceptTouchEvent(android.view.MotionEvent r9) {
        /*
            Method dump skipped, instructions count: 230
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.assist.OpaLayout.onInterceptTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // android.view.View
    public final void onWindowVisibilityChanged(int i) {
        boolean z;
        super.onWindowVisibilityChanged(i);
        if (i == 0) {
            z = true;
        } else {
            z = false;
        }
        this.mWindowVisible = z;
        if (i == 0) {
            updateOpaLayout();
            return;
        }
        cancelCurrentAnimation("winVis=" + i);
        skipToStartingValue();
    }

    @Override // android.view.View
    public final void setAccessibilityDelegate(View.AccessibilityDelegate accessibilityDelegate) {
        super.setAccessibilityDelegate(accessibilityDelegate);
        this.mHome.setAccessibilityDelegate(accessibilityDelegate);
    }

    public final void startAll(ArraySet<Animator> arraySet) {
        for (int size = arraySet.size() - 1; size >= 0; size--) {
            arraySet.valueAt(size).start();
        }
        for (int size2 = this.mAnimatedViews.size() - 1; size2 >= 0; size2--) {
            this.mAnimatedViews.get(size2).invalidate();
        }
    }

    public final void startDiamondAnimation() {
        if (allowAnimations()) {
            this.mCurrentAnimators.clear();
            int size = this.mAnimatedViews.size();
            for (int i = 0; i < size; i++) {
                this.mAnimatedViews.get(i).setAlpha(1.0f);
            }
            ArraySet<Animator> arraySet = this.mCurrentAnimators;
            ArraySet<? extends Animator> arraySet2 = new ArraySet<>();
            View view = this.mTop;
            Property property = View.Y;
            float y = view.getY();
            Resources resources = this.mResources;
            PathInterpolator pathInterpolator = OpaUtils.INTERPOLATOR_40_40;
            arraySet2.add(getPropertyAnimator(view, property, (-resources.getDimensionPixelOffset(2131166713)) + y, 200, this.mDiamondInterpolator));
            View view2 = this.mTop;
            Property property2 = FrameLayout.SCALE_X;
            PathInterpolator pathInterpolator2 = Interpolators.FAST_OUT_SLOW_IN;
            arraySet2.add(getPropertyAnimator(view2, property2, 0.8f, 200, pathInterpolator2));
            arraySet2.add(getPropertyAnimator(this.mTop, FrameLayout.SCALE_Y, 0.8f, 200, pathInterpolator2));
            View view3 = this.mBottom;
            arraySet2.add(getPropertyAnimator(view3, View.Y, this.mResources.getDimensionPixelOffset(2131166713) + view3.getY(), 200, this.mDiamondInterpolator));
            arraySet2.add(getPropertyAnimator(this.mBottom, FrameLayout.SCALE_X, 0.8f, 200, pathInterpolator2));
            arraySet2.add(getPropertyAnimator(this.mBottom, FrameLayout.SCALE_Y, 0.8f, 200, pathInterpolator2));
            View view4 = this.mLeft;
            arraySet2.add(getPropertyAnimator(view4, View.X, (-this.mResources.getDimensionPixelOffset(2131166713)) + view4.getX(), 200, this.mDiamondInterpolator));
            arraySet2.add(getPropertyAnimator(this.mLeft, FrameLayout.SCALE_X, 0.8f, 200, pathInterpolator2));
            arraySet2.add(getPropertyAnimator(this.mLeft, FrameLayout.SCALE_Y, 0.8f, 200, pathInterpolator2));
            View view5 = this.mRight;
            arraySet2.add(getPropertyAnimator(view5, View.X, this.mResources.getDimensionPixelOffset(2131166713) + view5.getX(), 200, this.mDiamondInterpolator));
            arraySet2.add(getPropertyAnimator(this.mRight, FrameLayout.SCALE_X, 0.8f, 200, pathInterpolator2));
            arraySet2.add(getPropertyAnimator(this.mRight, FrameLayout.SCALE_Y, 0.8f, 200, pathInterpolator2));
            arraySet2.add(getPropertyAnimator(this.mWhite, FrameLayout.SCALE_X, 0.625f, 200, pathInterpolator2));
            arraySet2.add(getPropertyAnimator(this.mWhite, FrameLayout.SCALE_Y, 0.625f, 200, pathInterpolator2));
            arraySet2.add(getPropertyAnimator(this.mWhiteCutout, FrameLayout.SCALE_X, 0.625f, 200, pathInterpolator2));
            arraySet2.add(getPropertyAnimator(this.mWhiteCutout, FrameLayout.SCALE_Y, 0.625f, 200, pathInterpolator2));
            arraySet2.add(getPropertyAnimator(this.mHalo, FrameLayout.SCALE_X, 0.47619048f, 100, pathInterpolator2));
            arraySet2.add(getPropertyAnimator(this.mHalo, FrameLayout.SCALE_Y, 0.47619048f, 100, pathInterpolator2));
            arraySet2.add(getPropertyAnimator(this.mHalo, View.ALPHA, 0.0f, 100, pathInterpolator2));
            getLongestAnim(arraySet2).addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.OpaLayout.3
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationCancel(Animator animator) {
                    Trace.beginSection("OpaLayout.cancel.diamond");
                    Trace.endSection();
                    OpaLayout.this.mCurrentAnimators.clear();
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    Trace.beginSection("OpaLayout.end.diamond");
                    Trace.endSection();
                    final OpaLayout opaLayout = OpaLayout.this;
                    int i2 = OpaLayout.$r8$clinit;
                    Objects.requireNonNull(opaLayout);
                    if (opaLayout.allowAnimations()) {
                        opaLayout.mCurrentAnimators.clear();
                        ArraySet<Animator> arraySet3 = opaLayout.mCurrentAnimators;
                        ArraySet<? extends Animator> arraySet4 = new ArraySet<>();
                        if (opaLayout.mIsVertical) {
                            View view6 = opaLayout.mRed;
                            Property property3 = View.Y;
                            float y2 = view6.getY();
                            Resources resources2 = opaLayout.mResources;
                            PathInterpolator pathInterpolator3 = OpaUtils.INTERPOLATOR_40_40;
                            float dimensionPixelOffset = resources2.getDimensionPixelOffset(2131166719) + y2;
                            PathInterpolator pathInterpolator4 = Interpolators.FAST_OUT_SLOW_IN;
                            arraySet4.add(OpaLayout.getPropertyAnimator(view6, property3, dimensionPixelOffset, 225, pathInterpolator4));
                            View view7 = opaLayout.mRed;
                            arraySet4.add(OpaLayout.getPropertyAnimator(view7, View.X, opaLayout.mResources.getDimensionPixelOffset(2131166720) + view7.getX(), 133, pathInterpolator4));
                            View view8 = opaLayout.mBlue;
                            arraySet4.add(OpaLayout.getPropertyAnimator(view8, View.Y, opaLayout.mResources.getDimensionPixelOffset(2131166718) + view8.getY(), 225, pathInterpolator4));
                            View view9 = opaLayout.mYellow;
                            arraySet4.add(OpaLayout.getPropertyAnimator(view9, View.Y, (-opaLayout.mResources.getDimensionPixelOffset(2131166719)) + view9.getY(), 225, pathInterpolator4));
                            View view10 = opaLayout.mYellow;
                            arraySet4.add(OpaLayout.getPropertyAnimator(view10, View.X, (-opaLayout.mResources.getDimensionPixelOffset(2131166720)) + view10.getX(), 133, pathInterpolator4));
                            View view11 = opaLayout.mGreen;
                            arraySet4.add(OpaLayout.getPropertyAnimator(view11, View.Y, (-opaLayout.mResources.getDimensionPixelOffset(2131166718)) + view11.getY(), 225, pathInterpolator4));
                        } else {
                            View view12 = opaLayout.mRed;
                            Property property4 = View.X;
                            float x = view12.getX();
                            Resources resources3 = opaLayout.mResources;
                            PathInterpolator pathInterpolator5 = OpaUtils.INTERPOLATOR_40_40;
                            float f = (-resources3.getDimensionPixelOffset(2131166719)) + x;
                            PathInterpolator pathInterpolator6 = Interpolators.FAST_OUT_SLOW_IN;
                            arraySet4.add(OpaLayout.getPropertyAnimator(view12, property4, f, 225, pathInterpolator6));
                            View view13 = opaLayout.mRed;
                            arraySet4.add(OpaLayout.getPropertyAnimator(view13, View.Y, opaLayout.mResources.getDimensionPixelOffset(2131166720) + view13.getY(), 133, pathInterpolator6));
                            View view14 = opaLayout.mBlue;
                            arraySet4.add(OpaLayout.getPropertyAnimator(view14, View.X, (-opaLayout.mResources.getDimensionPixelOffset(2131166718)) + view14.getX(), 225, pathInterpolator6));
                            View view15 = opaLayout.mYellow;
                            arraySet4.add(OpaLayout.getPropertyAnimator(view15, View.X, opaLayout.mResources.getDimensionPixelOffset(2131166719) + view15.getX(), 225, pathInterpolator6));
                            View view16 = opaLayout.mYellow;
                            arraySet4.add(OpaLayout.getPropertyAnimator(view16, View.Y, (-opaLayout.mResources.getDimensionPixelOffset(2131166720)) + view16.getY(), 133, pathInterpolator6));
                            View view17 = opaLayout.mGreen;
                            arraySet4.add(OpaLayout.getPropertyAnimator(view17, View.X, opaLayout.mResources.getDimensionPixelOffset(2131166718) + view17.getX(), 225, pathInterpolator6));
                        }
                        arraySet4.add(OpaLayout.getPropertyAnimator(opaLayout.mWhite, FrameLayout.SCALE_X, 0.0f, 83, opaLayout.HOME_DISAPPEAR_INTERPOLATOR));
                        arraySet4.add(OpaLayout.getPropertyAnimator(opaLayout.mWhite, FrameLayout.SCALE_Y, 0.0f, 83, opaLayout.HOME_DISAPPEAR_INTERPOLATOR));
                        arraySet4.add(OpaLayout.getPropertyAnimator(opaLayout.mWhiteCutout, FrameLayout.SCALE_X, 0.0f, 83, opaLayout.HOME_DISAPPEAR_INTERPOLATOR));
                        arraySet4.add(OpaLayout.getPropertyAnimator(opaLayout.mWhiteCutout, FrameLayout.SCALE_Y, 0.0f, 83, opaLayout.HOME_DISAPPEAR_INTERPOLATOR));
                        arraySet4.add(OpaLayout.getPropertyAnimator(opaLayout.mHalo, FrameLayout.SCALE_X, 0.0f, 83, opaLayout.HOME_DISAPPEAR_INTERPOLATOR));
                        arraySet4.add(OpaLayout.getPropertyAnimator(opaLayout.mHalo, FrameLayout.SCALE_Y, 0.0f, 83, opaLayout.HOME_DISAPPEAR_INTERPOLATOR));
                        OpaLayout.getLongestAnim(arraySet4).addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.OpaLayout.6
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public final void onAnimationCancel(Animator animator2) {
                                Trace.beginSection("OpaLayout.cancel.line");
                                Trace.endSection();
                                OpaLayout.this.mCurrentAnimators.clear();
                            }

                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public final void onAnimationEnd(Animator animator2) {
                                Trace.beginSection("OpaLayout.end.line");
                                Trace.endSection();
                                OpaLayout.m166$$Nest$mstartCollapseAnimation(OpaLayout.this);
                            }

                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public final void onAnimationStart(Animator animator2) {
                                Trace.beginSection("OpaLayout.start.line");
                                Trace.endSection();
                            }
                        });
                        arraySet3.addAll(arraySet4);
                        opaLayout.mAnimationState = 3;
                        opaLayout.startAll(opaLayout.mCurrentAnimators);
                        return;
                    }
                    opaLayout.skipToStartingValue();
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationStart(Animator animator) {
                    Trace.beginSection("OpaLayout.start.diamond");
                    Trace.endSection();
                }
            });
            arraySet.addAll(arraySet2);
            this.mAnimationState = 1;
            startAll(this.mCurrentAnimators);
            return;
        }
        skipToStartingValue();
    }

    public final void startRetractAnimation() {
        if (allowAnimations()) {
            this.mCurrentAnimators.clear();
            ArraySet<Animator> arraySet = this.mCurrentAnimators;
            ArraySet<? extends Animator> arraySet2 = new ArraySet<>();
            View view = this.mRed;
            Property property = FrameLayout.TRANSLATION_X;
            PathInterpolator pathInterpolator = OpaUtils.INTERPOLATOR_40_OUT;
            arraySet2.add(getPropertyAnimator(view, property, 0.0f, 190, pathInterpolator));
            arraySet2.add(getPropertyAnimator(this.mRed, FrameLayout.TRANSLATION_Y, 0.0f, 190, pathInterpolator));
            arraySet2.add(getPropertyAnimator(this.mRed, FrameLayout.SCALE_X, 1.0f, 190, pathInterpolator));
            arraySet2.add(getPropertyAnimator(this.mRed, FrameLayout.SCALE_Y, 1.0f, 190, pathInterpolator));
            arraySet2.add(getPropertyAnimator(this.mBlue, FrameLayout.TRANSLATION_X, 0.0f, 190, pathInterpolator));
            arraySet2.add(getPropertyAnimator(this.mBlue, FrameLayout.TRANSLATION_Y, 0.0f, 190, pathInterpolator));
            arraySet2.add(getPropertyAnimator(this.mBlue, FrameLayout.SCALE_X, 1.0f, 190, pathInterpolator));
            arraySet2.add(getPropertyAnimator(this.mBlue, FrameLayout.SCALE_Y, 1.0f, 190, pathInterpolator));
            arraySet2.add(getPropertyAnimator(this.mGreen, FrameLayout.TRANSLATION_X, 0.0f, 190, pathInterpolator));
            arraySet2.add(getPropertyAnimator(this.mGreen, FrameLayout.TRANSLATION_Y, 0.0f, 190, pathInterpolator));
            arraySet2.add(getPropertyAnimator(this.mGreen, FrameLayout.SCALE_X, 1.0f, 190, pathInterpolator));
            arraySet2.add(getPropertyAnimator(this.mGreen, FrameLayout.SCALE_Y, 1.0f, 190, pathInterpolator));
            arraySet2.add(getPropertyAnimator(this.mYellow, FrameLayout.TRANSLATION_X, 0.0f, 190, pathInterpolator));
            arraySet2.add(getPropertyAnimator(this.mYellow, FrameLayout.TRANSLATION_Y, 0.0f, 190, pathInterpolator));
            arraySet2.add(getPropertyAnimator(this.mYellow, FrameLayout.SCALE_X, 1.0f, 190, pathInterpolator));
            arraySet2.add(getPropertyAnimator(this.mYellow, FrameLayout.SCALE_Y, 1.0f, 190, pathInterpolator));
            arraySet2.add(getPropertyAnimator(this.mWhite, FrameLayout.SCALE_X, 1.0f, 190, pathInterpolator));
            arraySet2.add(getPropertyAnimator(this.mWhite, FrameLayout.SCALE_Y, 1.0f, 190, pathInterpolator));
            arraySet2.add(getPropertyAnimator(this.mWhiteCutout, FrameLayout.SCALE_X, 1.0f, 190, pathInterpolator));
            arraySet2.add(getPropertyAnimator(this.mWhiteCutout, FrameLayout.SCALE_Y, 1.0f, 190, pathInterpolator));
            ImageView imageView = this.mHalo;
            Property property2 = FrameLayout.SCALE_X;
            PathInterpolator pathInterpolator2 = Interpolators.FAST_OUT_SLOW_IN;
            arraySet2.add(getPropertyAnimator(imageView, property2, 1.0f, 190, pathInterpolator2));
            arraySet2.add(getPropertyAnimator(this.mHalo, FrameLayout.SCALE_Y, 1.0f, 190, pathInterpolator2));
            arraySet2.add(getPropertyAnimator(this.mHalo, FrameLayout.ALPHA, 1.0f, 190, pathInterpolator2));
            getLongestAnim(arraySet2).addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.OpaLayout.4
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationCancel(Animator animator) {
                    Trace.beginSection("OpaLayout.cancel.retract");
                    Trace.endSection();
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    Trace.beginSection("OpaLayout.end.retract");
                    Trace.endSection();
                    OpaLayout.this.mCurrentAnimators.clear();
                    OpaLayout.this.skipToStartingValue();
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationStart(Animator animator) {
                    Trace.beginSection("OpaLayout.start.retract");
                    Trace.endSection();
                }
            });
            arraySet.addAll(arraySet2);
            this.mAnimationState = 2;
            startAll(this.mCurrentAnimators);
            return;
        }
        skipToStartingValue();
    }

    public OpaLayout(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }
}
