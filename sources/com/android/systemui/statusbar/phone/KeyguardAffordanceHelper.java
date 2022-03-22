package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.CanvasProperty;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import android.view.RenderNodeAnimator;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewConfiguration;
import android.view.animation.PathInterpolator;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.KeyguardAffordanceView;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.wm.shell.animation.FlingAnimationUtils;
import java.util.Objects;
/* loaded from: classes.dex */
public final class KeyguardAffordanceHelper {
    public final Callback mCallback;
    public final Context mContext;
    public final FalsingManager mFalsingManager;
    public FlingAnimationUtils mFlingAnimationUtils;
    public int mHintGrowAmount;
    public float mInitialTouchX;
    public float mInitialTouchY;
    public KeyguardAffordanceView mLeftIcon;
    public int mMinBackgroundRadius;
    public int mMinFlingVelocity;
    public int mMinTranslationAmount;
    public boolean mMotionCancelled;
    public KeyguardAffordanceView mRightIcon;
    public Animator mSwipeAnimator;
    public boolean mSwipingInProgress;
    public View mTargetedView;
    public int mTouchSlop;
    public boolean mTouchSlopExeeded;
    public int mTouchTargetSize;
    public float mTranslation;
    public float mTranslationOnDown;
    public VelocityTracker mVelocityTracker;
    public AnonymousClass1 mFlingEndListener = new AnonymousClass1();
    public AnonymousClass2 mAnimationEndRunnable = new AnonymousClass2();

    /* renamed from: com.android.systemui.statusbar.phone.KeyguardAffordanceHelper$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends AnimatorListenerAdapter {
        public AnonymousClass1() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationEnd(Animator animator) {
            KeyguardAffordanceHelper keyguardAffordanceHelper = KeyguardAffordanceHelper.this;
            keyguardAffordanceHelper.mSwipeAnimator = null;
            keyguardAffordanceHelper.mSwipingInProgress = false;
            keyguardAffordanceHelper.mTargetedView = null;
        }
    }

    /* renamed from: com.android.systemui.statusbar.phone.KeyguardAffordanceHelper$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 implements Runnable {
        public AnonymousClass2() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            NotificationPanelViewController.KeyguardAffordanceHelperCallback keyguardAffordanceHelperCallback = (NotificationPanelViewController.KeyguardAffordanceHelperCallback) KeyguardAffordanceHelper.this.mCallback;
            Objects.requireNonNull(keyguardAffordanceHelperCallback);
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            notificationPanelViewController.mIsLaunchTransitionRunning = false;
            notificationPanelViewController.mIsLaunchTransitionFinished = true;
            Runnable runnable = notificationPanelViewController.mLaunchAnimationEndRunnable;
            if (runnable != null) {
                runnable.run();
                NotificationPanelViewController.this.mLaunchAnimationEndRunnable = null;
            }
            StatusBar statusBar = NotificationPanelViewController.this.mStatusBar;
            Objects.requireNonNull(statusBar);
            StatusBarKeyguardViewManager statusBarKeyguardViewManager = statusBar.mStatusBarKeyguardViewManager;
            Objects.requireNonNull(statusBarKeyguardViewManager);
            statusBarKeyguardViewManager.mViewMediatorCallback.readyForKeyguardDone();
        }
    }

    /* loaded from: classes.dex */
    public interface Callback {
    }

    public KeyguardAffordanceHelper(NotificationPanelViewController.KeyguardAffordanceHelperCallback keyguardAffordanceHelperCallback, Context context, FalsingManager falsingManager) {
        this.mContext = context;
        this.mCallback = keyguardAffordanceHelperCallback;
        initIcons();
        KeyguardAffordanceView keyguardAffordanceView = this.mLeftIcon;
        Objects.requireNonNull(keyguardAffordanceView);
        updateIcon(keyguardAffordanceView, 0.0f, keyguardAffordanceView.mRestingAlpha, false, false, true, false);
        KeyguardAffordanceView keyguardAffordanceView2 = this.mRightIcon;
        Objects.requireNonNull(keyguardAffordanceView2);
        updateIcon(keyguardAffordanceView2, 0.0f, keyguardAffordanceView2.mRestingAlpha, false, false, true, false);
        this.mFalsingManager = falsingManager;
        initDimens();
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x005f  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0085  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0087  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0093 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x009c  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x009f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00ab  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void endMotion(boolean r9, float r10, float r11) {
        /*
            Method dump skipped, instructions count: 188
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.KeyguardAffordanceHelper.endMotion(boolean, float, float):void");
    }

    public final void fling(float f, boolean z, boolean z2) {
        float f2;
        final KeyguardAffordanceView keyguardAffordanceView;
        RenderNodeAnimator renderNodeAnimator;
        if (z2) {
            f2 = -((NotificationPanelViewController.KeyguardAffordanceHelperCallback) this.mCallback).getMaxTranslationDistance();
        } else {
            f2 = ((NotificationPanelViewController.KeyguardAffordanceHelperCallback) this.mCallback).getMaxTranslationDistance();
        }
        if (z) {
            f2 = 0.0f;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.mTranslation, f2);
        this.mFlingAnimationUtils.apply(ofFloat, this.mTranslation, f2, f);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.phone.KeyguardAffordanceHelper.6
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                KeyguardAffordanceHelper.this.mTranslation = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            }
        });
        ofFloat.addListener(this.mFlingEndListener);
        if (!z) {
            float f3 = 0.375f * f;
            final AnonymousClass2 r14 = this.mAnimationEndRunnable;
            if (z2) {
                keyguardAffordanceView = this.mRightIcon;
            } else {
                keyguardAffordanceView = this.mLeftIcon;
            }
            Objects.requireNonNull(keyguardAffordanceView);
            KeyguardAffordanceView.cancelAnimator(keyguardAffordanceView.mCircleAnimator);
            KeyguardAffordanceView.cancelAnimator(keyguardAffordanceView.mPreviewClipper);
            keyguardAffordanceView.mFinishing = true;
            keyguardAffordanceView.mCircleStartRadius = keyguardAffordanceView.mCircleRadius;
            final float maxCircleSize = keyguardAffordanceView.getMaxCircleSize();
            if (keyguardAffordanceView.mSupportHardware) {
                keyguardAffordanceView.mHwCenterX = CanvasProperty.createFloat(keyguardAffordanceView.mCenterX);
                keyguardAffordanceView.mHwCenterY = CanvasProperty.createFloat(keyguardAffordanceView.mCenterY);
                keyguardAffordanceView.mHwCirclePaint = CanvasProperty.createPaint(keyguardAffordanceView.mCirclePaint);
                keyguardAffordanceView.mHwCircleRadius = CanvasProperty.createFloat(keyguardAffordanceView.mCircleRadius);
                renderNodeAnimator = new RenderNodeAnimator(keyguardAffordanceView.mHwCircleRadius, maxCircleSize);
                renderNodeAnimator.setTarget(keyguardAffordanceView);
                if (keyguardAffordanceView.mCircleRadius == 0.0f && keyguardAffordanceView.mPreviewView == null) {
                    Paint paint = new Paint(keyguardAffordanceView.mCirclePaint);
                    paint.setColor(keyguardAffordanceView.mCircleColor);
                    paint.setAlpha(0);
                    keyguardAffordanceView.mHwCirclePaint = CanvasProperty.createPaint(paint);
                    RenderNodeAnimator renderNodeAnimator2 = new RenderNodeAnimator(keyguardAffordanceView.mHwCirclePaint, 1, 255.0f);
                    renderNodeAnimator2.setTarget(keyguardAffordanceView);
                    renderNodeAnimator2.setInterpolator(Interpolators.ALPHA_IN);
                    renderNodeAnimator2.setDuration(250L);
                    renderNodeAnimator2.start();
                }
            } else {
                renderNodeAnimator = keyguardAffordanceView.getAnimatorToRadius(maxCircleSize);
            }
            keyguardAffordanceView.mFlingAnimationUtils.applyDismissing(renderNodeAnimator, keyguardAffordanceView.mCircleRadius, maxCircleSize, f3, maxCircleSize);
            renderNodeAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.KeyguardAffordanceView.5
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    r14.run();
                    KeyguardAffordanceView keyguardAffordanceView2 = keyguardAffordanceView;
                    keyguardAffordanceView2.mFinishing = false;
                    keyguardAffordanceView2.mCircleRadius = maxCircleSize;
                    keyguardAffordanceView2.invalidate();
                }
            });
            renderNodeAnimator.start();
            keyguardAffordanceView.setImageAlpha(0.0f, true);
            View view = keyguardAffordanceView.mPreviewView;
            if (view != null) {
                view.setVisibility(0);
                Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(keyguardAffordanceView.mPreviewView, keyguardAffordanceView.getLeft() + keyguardAffordanceView.mCenterX, keyguardAffordanceView.getTop() + keyguardAffordanceView.mCenterY, keyguardAffordanceView.mCircleRadius, maxCircleSize);
                keyguardAffordanceView.mPreviewClipper = createCircularReveal;
                keyguardAffordanceView.mFlingAnimationUtils.applyDismissing(createCircularReveal, keyguardAffordanceView.mCircleRadius, maxCircleSize, f3, maxCircleSize);
                keyguardAffordanceView.mPreviewClipper.addListener(keyguardAffordanceView.mClipEndListener);
                keyguardAffordanceView.mPreviewClipper.start();
                if (keyguardAffordanceView.mSupportHardware) {
                    long duration = renderNodeAnimator.getDuration();
                    RenderNodeAnimator renderNodeAnimator3 = new RenderNodeAnimator(keyguardAffordanceView.mHwCirclePaint, 1, 0.0f);
                    renderNodeAnimator3.setDuration(duration);
                    renderNodeAnimator3.setInterpolator(Interpolators.ALPHA_OUT);
                    renderNodeAnimator3.setTarget(keyguardAffordanceView);
                    renderNodeAnimator3.start();
                }
            }
            ((NotificationPanelViewController.KeyguardAffordanceHelperCallback) this.mCallback).onAnimationToSideStarted(z2, this.mTranslation, f);
        } else {
            reset(true);
        }
        ofFloat.start();
        this.mSwipeAnimator = ofFloat;
        if (z) {
            NotificationPanelViewController.KeyguardAffordanceHelperCallback keyguardAffordanceHelperCallback = (NotificationPanelViewController.KeyguardAffordanceHelperCallback) this.mCallback;
            Objects.requireNonNull(keyguardAffordanceHelperCallback);
            NotificationPanelViewController.this.mFalsingCollector.onAffordanceSwipingAborted();
            NotificationPanelViewController.this.mKeyguardBottomArea.unbindCameraPrewarmService(false);
        }
    }

    public final ValueAnimator getAnimatorToRadius(final boolean z, int i) {
        final KeyguardAffordanceView keyguardAffordanceView;
        if (z) {
            keyguardAffordanceView = this.mRightIcon;
        } else {
            keyguardAffordanceView = this.mLeftIcon;
        }
        Objects.requireNonNull(keyguardAffordanceView);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(keyguardAffordanceView.mCircleRadius, i);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.phone.KeyguardAffordanceHelper.5
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                KeyguardAffordanceHelper keyguardAffordanceHelper;
                float f;
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                KeyguardAffordanceView keyguardAffordanceView2 = keyguardAffordanceView;
                Objects.requireNonNull(keyguardAffordanceView2);
                KeyguardAffordanceView.cancelAnimator(keyguardAffordanceView2.mCircleAnimator);
                keyguardAffordanceView2.setCircleRadius(floatValue, false, true);
                Objects.requireNonNull(KeyguardAffordanceHelper.this);
                float f2 = (floatValue - keyguardAffordanceHelper.mMinBackgroundRadius) / 0.25f;
                if (f2 > 0.0f) {
                    f = f2 + keyguardAffordanceHelper.mTouchSlop;
                } else {
                    f = 0.0f;
                }
                KeyguardAffordanceHelper keyguardAffordanceHelper2 = KeyguardAffordanceHelper.this;
                if (z) {
                    f = -f;
                }
                keyguardAffordanceHelper2.mTranslation = f;
                KeyguardAffordanceView keyguardAffordanceView3 = keyguardAffordanceView;
                float abs = Math.abs(f) / keyguardAffordanceHelper2.getMinTranslationAmount();
                float max = Math.max(0.0f, 1.0f - abs);
                KeyguardAffordanceView keyguardAffordanceView4 = keyguardAffordanceHelper2.mRightIcon;
                if (keyguardAffordanceView3 == keyguardAffordanceView4) {
                    keyguardAffordanceView4 = keyguardAffordanceHelper2.mLeftIcon;
                }
                Objects.requireNonNull(keyguardAffordanceView3);
                KeyguardAffordanceHelper.updateIconAlpha(keyguardAffordanceView3, (keyguardAffordanceView3.mRestingAlpha * max) + abs, false);
                Objects.requireNonNull(keyguardAffordanceView4);
                KeyguardAffordanceHelper.updateIconAlpha(keyguardAffordanceView4, max * keyguardAffordanceView4.mRestingAlpha, false);
            }
        });
        return ofFloat;
    }

    public final int getMinTranslationAmount() {
        float f;
        NotificationPanelViewController.KeyguardAffordanceHelperCallback keyguardAffordanceHelperCallback = (NotificationPanelViewController.KeyguardAffordanceHelperCallback) this.mCallback;
        Objects.requireNonNull(keyguardAffordanceHelperCallback);
        StatusBar statusBar = NotificationPanelViewController.this.mStatusBar;
        Objects.requireNonNull(statusBar);
        if (statusBar.mWakeUpComingFromTouch) {
            f = 1.5f;
        } else {
            f = 1.0f;
        }
        return (int) (this.mMinTranslationAmount * f);
    }

    public final void initDimens() {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(this.mContext);
        this.mTouchSlop = viewConfiguration.getScaledPagingTouchSlop();
        this.mMinFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mMinTranslationAmount = this.mContext.getResources().getDimensionPixelSize(2131165856);
        this.mMinBackgroundRadius = this.mContext.getResources().getDimensionPixelSize(2131165836);
        this.mTouchTargetSize = this.mContext.getResources().getDimensionPixelSize(2131165837);
        this.mHintGrowAmount = this.mContext.getResources().getDimensionPixelSize(2131165804);
        this.mFlingAnimationUtils = new FlingAnimationUtils(this.mContext.getResources().getDisplayMetrics(), 0.4f);
    }

    public final void initIcons() {
        this.mLeftIcon = ((NotificationPanelViewController.KeyguardAffordanceHelperCallback) this.mCallback).getLeftIcon();
        this.mRightIcon = ((NotificationPanelViewController.KeyguardAffordanceHelperCallback) this.mCallback).getRightIcon();
        updatePreviews();
    }

    public final void reset(boolean z) {
        Animator animator = this.mSwipeAnimator;
        if (animator != null) {
            animator.cancel();
        }
        setTranslation(0.0f, true, z);
        this.mMotionCancelled = true;
        if (this.mSwipingInProgress) {
            NotificationPanelViewController.KeyguardAffordanceHelperCallback keyguardAffordanceHelperCallback = (NotificationPanelViewController.KeyguardAffordanceHelperCallback) this.mCallback;
            Objects.requireNonNull(keyguardAffordanceHelperCallback);
            NotificationPanelViewController.this.mFalsingCollector.onAffordanceSwipingAborted();
            NotificationPanelViewController.this.mKeyguardBottomArea.unbindCameraPrewarmService(false);
            this.mSwipingInProgress = false;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:49:0x00a0  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00b0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void setTranslation(float r20, boolean r21, boolean r22) {
        /*
            Method dump skipped, instructions count: 217
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.KeyguardAffordanceHelper.setTranslation(float, boolean, boolean):void");
    }

    public final void startSwiping(KeyguardAffordanceView keyguardAffordanceView) {
        boolean z;
        Bundle bundle;
        String string;
        Callback callback = this.mCallback;
        if (keyguardAffordanceView == this.mRightIcon) {
            z = true;
        } else {
            z = false;
        }
        NotificationPanelViewController.KeyguardAffordanceHelperCallback keyguardAffordanceHelperCallback = (NotificationPanelViewController.KeyguardAffordanceHelperCallback) callback;
        Objects.requireNonNull(keyguardAffordanceHelperCallback);
        NotificationPanelViewController.this.mFalsingCollector.onAffordanceSwipingStarted();
        if (NotificationPanelViewController.this.mView.getLayoutDirection() == 1) {
            if (!z) {
                z = true;
            } else {
                z = false;
            }
        }
        if (z) {
            KeyguardBottomAreaView keyguardBottomAreaView = NotificationPanelViewController.this.mKeyguardBottomArea;
            Objects.requireNonNull(keyguardBottomAreaView);
            ActivityInfo targetActivityInfo = keyguardBottomAreaView.mActivityIntentHelper.getTargetActivityInfo(keyguardBottomAreaView.mRightButton.getIntent(), KeyguardUpdateMonitor.getCurrentUser(), true);
            if (!(targetActivityInfo == null || (bundle = targetActivityInfo.metaData) == null || (string = bundle.getString("android.media.still_image_camera_preview_service")) == null)) {
                Intent intent = new Intent();
                intent.setClassName(targetActivityInfo.packageName, string);
                intent.setAction("android.service.media.CameraPrewarmService.ACTION_PREWARM");
                try {
                    if (keyguardBottomAreaView.getContext().bindServiceAsUser(intent, keyguardBottomAreaView.mPrewarmConnection, 67108865, new UserHandle(-2))) {
                        keyguardBottomAreaView.mPrewarmBound = true;
                    }
                } catch (SecurityException e) {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Unable to bind to prewarm service package=");
                    m.append(targetActivityInfo.packageName);
                    m.append(" class=");
                    m.append(string);
                    Log.w("StatusBar/KeyguardBottomAreaView", m.toString(), e);
                }
            }
        }
        NotificationPanelViewController.this.mView.requestDisallowInterceptTouchEvent(true);
        NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
        notificationPanelViewController.mOnlyAffordanceInThisMotion = true;
        notificationPanelViewController.mQsTracking = false;
        this.mSwipingInProgress = true;
        this.mTargetedView = keyguardAffordanceView;
    }

    public final void updatePreviews() {
        KeyguardPreviewContainer keyguardPreviewContainer;
        KeyguardPreviewContainer keyguardPreviewContainer2;
        int i;
        KeyguardAffordanceView keyguardAffordanceView = this.mLeftIcon;
        NotificationPanelViewController.KeyguardAffordanceHelperCallback keyguardAffordanceHelperCallback = (NotificationPanelViewController.KeyguardAffordanceHelperCallback) this.mCallback;
        Objects.requireNonNull(keyguardAffordanceHelperCallback);
        if (NotificationPanelViewController.this.mView.getLayoutDirection() == 1) {
            KeyguardBottomAreaView keyguardBottomAreaView = NotificationPanelViewController.this.mKeyguardBottomArea;
            Objects.requireNonNull(keyguardBottomAreaView);
            keyguardPreviewContainer = keyguardBottomAreaView.mCameraPreview;
        } else {
            KeyguardBottomAreaView keyguardBottomAreaView2 = NotificationPanelViewController.this.mKeyguardBottomArea;
            Objects.requireNonNull(keyguardBottomAreaView2);
            keyguardPreviewContainer = keyguardBottomAreaView2.mLeftPreview;
        }
        Objects.requireNonNull(keyguardAffordanceView);
        View view = keyguardAffordanceView.mPreviewView;
        int i2 = 4;
        if (view != keyguardPreviewContainer) {
            keyguardAffordanceView.mPreviewView = keyguardPreviewContainer;
            if (keyguardPreviewContainer != null) {
                if (keyguardAffordanceView.mLaunchingAffordance) {
                    i = view.getVisibility();
                } else {
                    i = 4;
                }
                keyguardPreviewContainer.setVisibility(i);
            }
        }
        KeyguardAffordanceView keyguardAffordanceView2 = this.mRightIcon;
        NotificationPanelViewController.KeyguardAffordanceHelperCallback keyguardAffordanceHelperCallback2 = (NotificationPanelViewController.KeyguardAffordanceHelperCallback) this.mCallback;
        Objects.requireNonNull(keyguardAffordanceHelperCallback2);
        if (NotificationPanelViewController.this.mView.getLayoutDirection() == 1) {
            KeyguardBottomAreaView keyguardBottomAreaView3 = NotificationPanelViewController.this.mKeyguardBottomArea;
            Objects.requireNonNull(keyguardBottomAreaView3);
            keyguardPreviewContainer2 = keyguardBottomAreaView3.mLeftPreview;
        } else {
            KeyguardBottomAreaView keyguardBottomAreaView4 = NotificationPanelViewController.this.mKeyguardBottomArea;
            Objects.requireNonNull(keyguardBottomAreaView4);
            keyguardPreviewContainer2 = keyguardBottomAreaView4.mCameraPreview;
        }
        Objects.requireNonNull(keyguardAffordanceView2);
        View view2 = keyguardAffordanceView2.mPreviewView;
        if (view2 != keyguardPreviewContainer2) {
            keyguardAffordanceView2.mPreviewView = keyguardPreviewContainer2;
            if (keyguardPreviewContainer2 != null) {
                if (keyguardAffordanceView2.mLaunchingAffordance) {
                    i2 = view2.getVisibility();
                }
                keyguardPreviewContainer2.setVisibility(i2);
            }
        }
    }

    public static void updateIcon(KeyguardAffordanceView keyguardAffordanceView, float f, float f2, boolean z, boolean z2, boolean z3, boolean z4) {
        if (keyguardAffordanceView.getVisibility() == 0 || z3) {
            if (z4) {
                KeyguardAffordanceView.cancelAnimator(keyguardAffordanceView.mCircleAnimator);
                keyguardAffordanceView.setCircleRadius(f, false, true);
            } else {
                keyguardAffordanceView.setCircleRadius(f, z2, false);
            }
            updateIconAlpha(keyguardAffordanceView, f2, z);
        }
    }

    public static void updateIconAlpha(final KeyguardAffordanceView keyguardAffordanceView, float f, boolean z) {
        PathInterpolator pathInterpolator;
        Objects.requireNonNull(keyguardAffordanceView);
        float min = Math.min(((f / keyguardAffordanceView.mRestingAlpha) * 0.2f) + 0.8f, 1.5f);
        keyguardAffordanceView.setImageAlpha(Math.min(1.0f, f), z);
        KeyguardAffordanceView.cancelAnimator(keyguardAffordanceView.mScaleAnimator);
        if (!z) {
            keyguardAffordanceView.mImageScale = min;
            keyguardAffordanceView.invalidate();
            return;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(keyguardAffordanceView.mImageScale, min);
        keyguardAffordanceView.mScaleAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.KeyguardAffordanceView.8
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                keyguardAffordanceView.mImageScale = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                keyguardAffordanceView.invalidate();
            }
        });
        ofFloat.addListener(keyguardAffordanceView.mScaleEndListener);
        if (min == 0.0f) {
            pathInterpolator = Interpolators.FAST_OUT_LINEAR_IN;
        } else {
            pathInterpolator = Interpolators.LINEAR_OUT_SLOW_IN;
        }
        ofFloat.setInterpolator(pathInterpolator);
        ofFloat.setDuration(Math.min(1.0f, Math.abs(keyguardAffordanceView.mImageScale - min) / 0.19999999f) * 200.0f);
        ofFloat.start();
    }

    public final boolean isOnIcon(KeyguardAffordanceView keyguardAffordanceView, float f, float f2) {
        if (Math.hypot(f - ((keyguardAffordanceView.getWidth() / 2.0f) + keyguardAffordanceView.getX()), f2 - ((keyguardAffordanceView.getHeight() / 2.0f) + keyguardAffordanceView.getY())) <= this.mTouchTargetSize / 2) {
            return true;
        }
        return false;
    }
}
