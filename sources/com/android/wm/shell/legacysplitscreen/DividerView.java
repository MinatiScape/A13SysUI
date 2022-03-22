package com.android.wm.shell.legacysplitscreen;

import android.animation.AnimationHandler;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Region;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.PointerIcon;
import android.view.SurfaceControl;
import android.view.SurfaceControlViewHost;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.WindowManagerGlobal;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.policy.DividerSnapAlgorithm;
import com.android.internal.policy.DockedDividerUtils;
import com.android.systemui.qs.tiles.CastTile$$ExternalSyntheticLambda1;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda19;
import com.android.systemui.theme.ThemeOverlayApplier$$ExternalSyntheticLambda1;
import com.android.wm.shell.animation.FlingAnimationUtils;
import com.android.wm.shell.animation.Interpolators;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.SystemWindows;
import com.android.wm.shell.common.split.DividerHandleView;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public class DividerView extends FrameLayout implements View.OnTouchListener, ViewTreeObserver.OnComputeInternalInsetsListener {
    public static final PathInterpolator IME_ADJUST_INTERPOLATOR = new PathInterpolator(0.2f, 0.0f, 0.1f, 1.0f);
    public boolean mAdjustedForIme;
    public View mBackground;
    public boolean mBackgroundLifted;
    public DividerCallbacks mCallback;
    public ValueAnimator mCurrentAnimator;
    public final Display mDefaultDisplay;
    public int mDividerInsets;
    public int mDividerPositionX;
    public int mDividerPositionY;
    public int mDividerSize;
    public int mDockSide;
    public final Rect mDockedInsetRect;
    public final Rect mDockedRect;
    public boolean mDockedStackMinimized;
    public final Rect mDockedTaskRect;
    public boolean mExitAnimationRunning;
    public int mExitStartPosition;
    public boolean mFirstLayout;
    public FlingAnimationUtils mFlingAnimationUtils;
    public DividerHandleView mHandle;
    public final AnonymousClass1 mHandleDelegate;
    public boolean mHomeStackResizable;
    public DividerImeController mImeController;
    public boolean mIsInMinimizeInteraction;
    public final Rect mLastResizeRect;
    public MinimizedDockShadow mMinimizedShadow;
    public boolean mMoving;
    public final Rect mOtherInsetRect;
    public final Rect mOtherRect;
    public final Rect mOtherTaskRect;
    public boolean mRemoved;
    public final AnonymousClass2 mResetBackgroundRunnable;
    public AnimationHandler mSfVsyncAnimationHandler;
    public DividerSnapAlgorithm.SnapTarget mSnapTargetBeforeMinimized;
    public LegacySplitDisplayLayout mSplitLayout;
    public LegacySplitScreenController mSplitScreenController;
    public int mStartPosition;
    public int mStartX;
    public int mStartY;
    public DividerState mState;
    public boolean mSurfaceHidden;
    public LegacySplitScreenTaskListener mTiles;
    public final Matrix mTmpMatrix;
    public final Rect mTmpRect;
    public final float[] mTmpValues;
    public int mTouchElevation;
    public int mTouchSlop;
    public StatusBar$$ExternalSyntheticLambda19 mUpdateEmbeddedMatrix;
    public VelocityTracker mVelocityTracker;
    public DividerWindowManager mWindowManager;
    public WindowManagerProxy mWindowManagerProxy;

    /* renamed from: com.android.wm.shell.legacysplitscreen.DividerView$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass3 extends AnimatorListenerAdapter {
        public static final /* synthetic */ int $r8$clinit = 0;
        public boolean mCancelled;
        public final /* synthetic */ Consumer val$endAction;
        public final /* synthetic */ long val$endDelay = 0;

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationCancel(Animator animator) {
            this.mCancelled = true;
        }

        public AnonymousClass3(ThemeOverlayApplier$$ExternalSyntheticLambda1 themeOverlayApplier$$ExternalSyntheticLambda1) {
            this.val$endAction = themeOverlayApplier$$ExternalSyntheticLambda1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationEnd(Animator animator) {
            long j = this.val$endDelay;
            if (j == 0) {
                j = 0;
            }
            if (j == 0) {
                this.val$endAction.accept(Boolean.valueOf(this.mCancelled));
                return;
            }
            DividerView.this.getHandler().postDelayed(new CastTile$$ExternalSyntheticLambda1(this.val$endAction, Boolean.valueOf(this.mCancelled), 4), j);
        }
    }

    /* loaded from: classes.dex */
    public interface DividerCallbacks {
    }

    public DividerView(Context context) {
        this(context, null);
    }

    public static boolean dockSideTopLeft(int i) {
        return i == 2 || i == 1;
    }

    public final void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
        internalInsetsInfo.setTouchableInsets(3);
        internalInsetsInfo.touchableRegion.set(this.mHandle.getLeft(), this.mHandle.getTop(), this.mHandle.getRight(), this.mHandle.getBottom());
        internalInsetsInfo.touchableRegion.op(this.mBackground.getLeft(), this.mBackground.getTop(), this.mBackground.getRight(), this.mBackground.getBottom(), Region.Op.UNION);
    }

    public final void stopDragging(int i, float f) {
        int i2 = 0;
        this.mHandle.setTouching(false, true);
        DividerSnapAlgorithm.SnapTarget calculateSnapTarget = getSnapAlgorithm().calculateSnapTarget(i, f);
        if (calculateSnapTarget == this.mSplitLayout.getSnapAlgorithm().getDismissStartTarget()) {
            MetricsLogger.action(((FrameLayout) this).mContext, 390, dockSideTopLeft(this.mDockSide) ? 1 : 0);
        } else if (calculateSnapTarget == this.mSplitLayout.getSnapAlgorithm().getDismissEndTarget()) {
            Context context = ((FrameLayout) this).mContext;
            int i3 = this.mDockSide;
            if (i3 == 4 || i3 == 3) {
                i2 = 1;
            }
            MetricsLogger.action(context, 390, i2);
        } else if (calculateSnapTarget == this.mSplitLayout.getSnapAlgorithm().getMiddleTarget()) {
            MetricsLogger.action(((FrameLayout) this).mContext, 389, 0);
        } else {
            int i4 = 2;
            if (calculateSnapTarget == this.mSplitLayout.getSnapAlgorithm().getFirstSplitTarget()) {
                Context context2 = ((FrameLayout) this).mContext;
                if (dockSideTopLeft(this.mDockSide)) {
                    i4 = 1;
                }
                MetricsLogger.action(context2, 389, i4);
            } else if (calculateSnapTarget == this.mSplitLayout.getSnapAlgorithm().getLastSplitTarget()) {
                Context context3 = ((FrameLayout) this).mContext;
                if (!dockSideTopLeft(this.mDockSide)) {
                    i4 = 1;
                }
                MetricsLogger.action(context3, 389, i4);
            }
        }
        ValueAnimator flingAnimator = getFlingAnimator(i, calculateSnapTarget);
        this.mFlingAnimationUtils.apply(flingAnimator, i, calculateSnapTarget.position, f);
        flingAnimator.start();
        this.mWindowManager.setSlippery(true);
        releaseBackground();
    }

    public DividerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public final void applyDismissingParallax(Rect rect, int i, int i2, int i3) {
        DividerSnapAlgorithm.SnapTarget snapTarget;
        boolean z;
        float min = Math.min(1.0f, Math.max(0.0f, this.mSplitLayout.getSnapAlgorithm().calculateDismissingFraction(i2)));
        boolean z2 = false;
        DividerSnapAlgorithm.SnapTarget snapTarget2 = null;
        if (i2 > this.mSplitLayout.getSnapAlgorithm().getLastSplitTarget().position || !dockSideTopLeft(i)) {
            if (i2 >= this.mSplitLayout.getSnapAlgorithm().getLastSplitTarget().position) {
                if (i == 4 || i == 3) {
                    z = true;
                } else {
                    z = false;
                }
                if (z) {
                    snapTarget2 = this.mSplitLayout.getSnapAlgorithm().getDismissEndTarget();
                    DividerSnapAlgorithm.SnapTarget lastSplitTarget = this.mSplitLayout.getSnapAlgorithm().getLastSplitTarget();
                    snapTarget = lastSplitTarget;
                    i3 = lastSplitTarget.position;
                }
            }
            i3 = 0;
            snapTarget = null;
        } else {
            snapTarget2 = this.mSplitLayout.getSnapAlgorithm().getDismissStartTarget();
            snapTarget = this.mSplitLayout.getSnapAlgorithm().getFirstSplitTarget();
        }
        if (snapTarget2 != null && min > 0.0f) {
            if (i == 2 || i == 1 ? i2 < snapTarget.position : i2 > snapTarget.position) {
                z2 = true;
            }
            if (z2) {
                float interpolation = Interpolators.SLOWDOWN_INTERPOLATOR.getInterpolation(min) / 3.5f;
                if (i == 2) {
                    interpolation /= 2.0f;
                }
                int i4 = (int) ((interpolation * (snapTarget2.position - snapTarget.position)) + i3);
                int width = rect.width();
                int height = rect.height();
                if (i == 1) {
                    rect.left = i4 - width;
                    rect.right = i4;
                } else if (i == 2) {
                    rect.top = i4 - height;
                    rect.bottom = i4;
                } else if (i == 3) {
                    int i5 = this.mDividerSize;
                    rect.left = i4 + i5;
                    rect.right = i4 + width + i5;
                } else if (i == 4) {
                    int i6 = this.mDividerSize;
                    rect.top = i4 + i6;
                    rect.bottom = i4 + height + i6;
                }
            }
        }
    }

    public final void calculateBoundsForPosition(int i, int i2, Rect rect) {
        DisplayLayout displayLayout = this.mSplitLayout.mDisplayLayout;
        Objects.requireNonNull(displayLayout);
        int i3 = displayLayout.mWidth;
        DisplayLayout displayLayout2 = this.mSplitLayout.mDisplayLayout;
        Objects.requireNonNull(displayLayout2);
        DockedDividerUtils.calculateBoundsForPosition(i, i2, rect, i3, displayLayout2.mHeight, this.mDividerSize);
    }

    public final ValueAnimator getFlingAnimator(int i, final DividerSnapAlgorithm.SnapTarget snapTarget) {
        final boolean z;
        ValueAnimator valueAnimator = this.mCurrentAnimator;
        if (valueAnimator != null) {
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            updateDockSide();
        }
        if (snapTarget.flag == 0) {
            z = true;
        } else {
            z = false;
        }
        ValueAnimator ofInt = ValueAnimator.ofInt(i, snapTarget.position);
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.wm.shell.legacysplitscreen.DividerView$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                int i2;
                DividerView dividerView = DividerView.this;
                boolean z2 = z;
                DividerSnapAlgorithm.SnapTarget snapTarget2 = snapTarget;
                PathInterpolator pathInterpolator = DividerView.IME_ADJUST_INTERPOLATOR;
                Objects.requireNonNull(dividerView);
                int intValue = ((Integer) valueAnimator2.getAnimatedValue()).intValue();
                if (!z2 || valueAnimator2.getAnimatedFraction() != 1.0f) {
                    i2 = snapTarget2.taskPosition;
                } else {
                    i2 = Integer.MAX_VALUE;
                }
                dividerView.resizeStackSurfaces(intValue, i2, snapTarget2, null);
            }
        });
        ofInt.addListener(new AnonymousClass3(new ThemeOverlayApplier$$ExternalSyntheticLambda1(this, snapTarget, 1)));
        this.mCurrentAnimator = ofInt;
        ofInt.setAnimationHandler(this.mSfVsyncAnimationHandler);
        return ofInt;
    }

    public final DividerSnapAlgorithm getSnapAlgorithm() {
        if (this.mDockedStackMinimized) {
            return this.mSplitLayout.getMinimizedSnapAlgorithm(this.mHomeStackResizable);
        }
        return this.mSplitLayout.getSnapAlgorithm();
    }

    public final void notifySplitScreenBoundsChanged() {
        Rect rect;
        LegacySplitDisplayLayout legacySplitDisplayLayout = this.mSplitLayout;
        if (legacySplitDisplayLayout.mPrimary != null && (rect = legacySplitDisplayLayout.mSecondary) != null) {
            this.mOtherTaskRect.set(rect);
            this.mTmpRect.set(this.mHandle.getLeft(), this.mHandle.getTop(), this.mHandle.getRight(), this.mHandle.getBottom());
            if (isHorizontalDivision()) {
                this.mTmpRect.offsetTo(this.mHandle.getLeft(), this.mDividerPositionY);
            } else {
                this.mTmpRect.offsetTo(this.mDividerPositionX, this.mHandle.getTop());
            }
            WindowManagerProxy windowManagerProxy = this.mWindowManagerProxy;
            Rect rect2 = this.mTmpRect;
            Objects.requireNonNull(windowManagerProxy);
            try {
                synchronized (windowManagerProxy.mDockedRect) {
                    windowManagerProxy.mTouchableRegion.set(rect2);
                }
                WindowManagerGlobal.getWindowManagerService().setDockedTaskDividerTouchRegion(windowManagerProxy.mTouchableRegion);
            } catch (RemoteException e) {
                Log.w("WindowManagerProxy", "Failed to set touchable region: " + e);
            }
            Rect rect3 = this.mTmpRect;
            DisplayLayout displayLayout = this.mSplitLayout.mDisplayLayout;
            Objects.requireNonNull(displayLayout);
            rect3.set(displayLayout.mStableInsets);
            int primarySplitSide = this.mSplitLayout.getPrimarySplitSide();
            if (primarySplitSide == 1) {
                this.mTmpRect.left = 0;
            } else if (primarySplitSide == 2) {
                this.mTmpRect.top = 0;
            } else if (primarySplitSide == 3) {
                this.mTmpRect.right = 0;
            }
            LegacySplitScreenController legacySplitScreenController = this.mSplitScreenController;
            final Rect rect4 = this.mOtherTaskRect;
            final Rect rect5 = this.mTmpRect;
            Objects.requireNonNull(legacySplitScreenController);
            synchronized (legacySplitScreenController.mBoundsChangedListeners) {
                legacySplitScreenController.mBoundsChangedListeners.removeIf(new Predicate() { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenController$$ExternalSyntheticLambda1
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        Rect rect6 = rect4;
                        Rect rect7 = rect5;
                        BiConsumer biConsumer = (BiConsumer) ((WeakReference) obj).get();
                        if (biConsumer != null) {
                            biConsumer.accept(rect6, rect7);
                        }
                        if (biConsumer == null) {
                            return true;
                        }
                        return false;
                    }
                });
            }
        }
    }

    public final void releaseBackground() {
        if (this.mBackgroundLifted) {
            ViewPropertyAnimator animate = this.mBackground.animate();
            PathInterpolator pathInterpolator = Interpolators.FAST_OUT_SLOW_IN;
            animate.setInterpolator(pathInterpolator).setDuration(200L).translationZ(0.0f).scaleX(1.0f).scaleY(1.0f).start();
            this.mHandle.animate().setInterpolator(pathInterpolator).setDuration(200L).translationZ(0.0f).start();
            this.mBackgroundLifted = false;
        }
    }

    public final void repositionSnapTargetBeforeMinimized() {
        int i;
        float f = this.mState.mRatioPositionBeforeMinimized;
        if (isHorizontalDivision()) {
            DisplayLayout displayLayout = this.mSplitLayout.mDisplayLayout;
            Objects.requireNonNull(displayLayout);
            i = displayLayout.mHeight;
        } else {
            DisplayLayout displayLayout2 = this.mSplitLayout.mDisplayLayout;
            Objects.requireNonNull(displayLayout2);
            i = displayLayout2.mWidth;
        }
        this.mSnapTargetBeforeMinimized = this.mSplitLayout.getSnapAlgorithm().calculateNonDismissingSnapTarget((int) (f * i));
    }

    public final void resetBackground() {
        View view = this.mBackground;
        view.setPivotX(view.getWidth() / 2);
        View view2 = this.mBackground;
        view2.setPivotY(view2.getHeight() / 2);
        this.mBackground.setScaleX(1.0f);
        this.mBackground.setScaleY(1.0f);
        this.mMinimizedShadow.setAlpha(0.0f);
    }

    public final void resizeSplitSurfaces(SurfaceControl.Transaction transaction, Rect rect, Rect rect2, Rect rect3, Rect rect4) {
        int i;
        if (rect2 == null) {
            rect2 = rect;
        }
        if (rect4 == null) {
            rect4 = rect3;
        }
        if (this.mSplitLayout.getPrimarySplitSide() == 3) {
            i = rect3.right;
        } else {
            i = rect.right;
        }
        this.mDividerPositionX = i;
        this.mDividerPositionY = rect.bottom;
        transaction.setPosition(this.mTiles.mPrimarySurface, rect2.left, rect2.top);
        Rect rect5 = new Rect(rect);
        rect5.offsetTo(-Math.min(rect2.left - rect.left, 0), -Math.min(rect2.top - rect.top, 0));
        transaction.setWindowCrop(this.mTiles.mPrimarySurface, rect5);
        transaction.setPosition(this.mTiles.mSecondarySurface, rect4.left, rect4.top);
        rect5.set(rect3);
        rect5.offsetTo(-(rect4.left - rect3.left), -(rect4.top - rect3.top));
        transaction.setWindowCrop(this.mTiles.mSecondarySurface, rect5);
        SurfaceControl viewSurface = this.mWindowManager.mSystemWindows.getViewSurface(this);
        if (viewSurface != null) {
            if (isHorizontalDivision()) {
                transaction.setPosition(viewSurface, 0.0f, this.mDividerPositionY - this.mDividerInsets);
            } else {
                transaction.setPosition(viewSurface, this.mDividerPositionX - this.mDividerInsets, 0.0f);
            }
        }
        if (getViewRootImpl() != null) {
            getHandler().removeCallbacks(this.mUpdateEmbeddedMatrix);
            getHandler().post(this.mUpdateEmbeddedMatrix);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:59:0x0225, code lost:
        if (r0 != false) goto L_0x0227;
     */
    /* JADX WARN: Removed duplicated region for block: B:64:0x022f  */
    /* JADX WARN: Removed duplicated region for block: B:67:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void resizeStackSurfaces(int r17, int r18, com.android.internal.policy.DividerSnapAlgorithm.SnapTarget r19, android.view.SurfaceControl.Transaction r20) {
        /*
            Method dump skipped, instructions count: 568
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.legacysplitscreen.DividerView.resizeStackSurfaces(int, int, com.android.internal.policy.DividerSnapAlgorithm$SnapTarget, android.view.SurfaceControl$Transaction):void");
    }

    public final int restrictDismissingTaskPosition(int i, int i2, DividerSnapAlgorithm.SnapTarget snapTarget) {
        boolean z = true;
        if (snapTarget.flag == 1 && dockSideTopLeft(i2)) {
            return Math.max(this.mSplitLayout.getSnapAlgorithm().getFirstSplitTarget().position, this.mStartPosition);
        }
        if (snapTarget.flag == 2) {
            if (!(i2 == 4 || i2 == 3)) {
                z = false;
            }
            if (z) {
                return Math.min(this.mSplitLayout.getSnapAlgorithm().getLastSplitTarget().position, this.mStartPosition);
            }
        }
        return i;
    }

    public final void saveSnapTargetBeforeMinimized(DividerSnapAlgorithm.SnapTarget snapTarget) {
        int i;
        this.mSnapTargetBeforeMinimized = snapTarget;
        DividerState dividerState = this.mState;
        float f = snapTarget.position;
        if (isHorizontalDivision()) {
            DisplayLayout displayLayout = this.mSplitLayout.mDisplayLayout;
            Objects.requireNonNull(displayLayout);
            i = displayLayout.mHeight;
        } else {
            DisplayLayout displayLayout2 = this.mSplitLayout.mDisplayLayout;
            Objects.requireNonNull(displayLayout2);
            i = displayLayout2.mWidth;
        }
        dividerState.mRatioPositionBeforeMinimized = f / i;
    }

    public final void setAdjustedForIme(boolean z, long j) {
        float f;
        if (this.mAdjustedForIme != z) {
            updateDockSide();
            ViewPropertyAnimator animate = this.mHandle.animate();
            PathInterpolator pathInterpolator = IME_ADJUST_INTERPOLATOR;
            ViewPropertyAnimator duration = animate.setInterpolator(pathInterpolator).setDuration(j);
            float f2 = 1.0f;
            if (z) {
                f = 0.0f;
            } else {
                f = 1.0f;
            }
            duration.alpha(f).start();
            if (this.mDockSide == 2) {
                this.mBackground.setPivotY(0.0f);
                ViewPropertyAnimator animate2 = this.mBackground.animate();
                if (z) {
                    f2 = 0.5f;
                }
                animate2.scaleY(f2);
            }
            if (!z) {
                this.mBackground.animate().withEndAction(this.mResetBackgroundRunnable);
            }
            this.mBackground.animate().setInterpolator(pathInterpolator).setDuration(j).start();
            this.mAdjustedForIme = z;
        }
    }

    public final void setMinimizedDockStack(boolean z, boolean z2, SurfaceControl.Transaction transaction) {
        float f;
        this.mHomeStackResizable = z2;
        updateDockSide();
        if (!z) {
            resetBackground();
        }
        MinimizedDockShadow minimizedDockShadow = this.mMinimizedShadow;
        if (z) {
            f = 1.0f;
        } else {
            f = 0.0f;
        }
        minimizedDockShadow.setAlpha(f);
        if (this.mDockedStackMinimized != z) {
            this.mDockedStackMinimized = z;
            DisplayLayout displayLayout = this.mSplitLayout.mDisplayLayout;
            Objects.requireNonNull(displayLayout);
            if (displayLayout.mRotation != this.mDefaultDisplay.getRotation()) {
                repositionSnapTargetBeforeMinimized();
            }
            if (this.mIsInMinimizeInteraction != z || this.mCurrentAnimator != null) {
                ValueAnimator valueAnimator = this.mCurrentAnimator;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                }
                if (z) {
                    requestLayout();
                    this.mIsInMinimizeInteraction = true;
                    DividerSnapAlgorithm.SnapTarget middleTarget = this.mSplitLayout.getMinimizedSnapAlgorithm(this.mHomeStackResizable).getMiddleTarget();
                    int i = middleTarget.position;
                    resizeStackSurfaces(i, i, middleTarget, transaction);
                    return;
                }
                DividerSnapAlgorithm.SnapTarget snapTarget = this.mSnapTargetBeforeMinimized;
                int i2 = snapTarget.position;
                resizeStackSurfaces(i2, i2, snapTarget, transaction);
                this.mIsInMinimizeInteraction = false;
            }
        }
    }

    public final void setResizeDimLayer(SurfaceControl.Transaction transaction, boolean z, float f) {
        SurfaceControl surfaceControl;
        LegacySplitScreenTaskListener legacySplitScreenTaskListener = this.mTiles;
        if (z) {
            surfaceControl = legacySplitScreenTaskListener.mPrimaryDim;
        } else {
            surfaceControl = legacySplitScreenTaskListener.mSecondaryDim;
        }
        if (f <= 0.001f) {
            transaction.hide(surfaceControl);
            return;
        }
        transaction.setAlpha(surfaceControl, f);
        transaction.show(surfaceControl);
    }

    public final boolean startDragging(boolean z, boolean z2) {
        ValueAnimator valueAnimator = this.mCurrentAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        if (z2) {
            this.mHandle.setTouching(true, z);
        }
        this.mDockSide = this.mSplitLayout.getPrimarySplitSide();
        Objects.requireNonNull(this.mWindowManagerProxy);
        WindowManagerProxy.setResizing(true);
        if (z2) {
            this.mWindowManager.setSlippery(false);
            if (!this.mBackgroundLifted) {
                if (isHorizontalDivision()) {
                    this.mBackground.animate().scaleY(1.4f);
                } else {
                    this.mBackground.animate().scaleX(1.4f);
                }
                ViewPropertyAnimator animate = this.mBackground.animate();
                PathInterpolator pathInterpolator = Interpolators.TOUCH_RESPONSE;
                animate.setInterpolator(pathInterpolator).setDuration(150L).translationZ(this.mTouchElevation).start();
                this.mHandle.animate().setInterpolator(pathInterpolator).setDuration(150L).translationZ(this.mTouchElevation).start();
                this.mBackgroundLifted = true;
            }
        }
        DividerCallbacks dividerCallbacks = this.mCallback;
        if (dividerCallbacks != null) {
            ForcedResizableInfoActivityController forcedResizableInfoActivityController = (ForcedResizableInfoActivityController) dividerCallbacks;
            forcedResizableInfoActivityController.mDividerDragging = true;
            forcedResizableInfoActivityController.mMainExecutor.removeCallbacks(forcedResizableInfoActivityController.mTimeoutRunnable);
        }
        if (getVisibility() == 0) {
            return true;
        }
        return false;
    }

    public final void updateDockSide() {
        int primarySplitSide = this.mSplitLayout.getPrimarySplitSide();
        this.mDockSide = primarySplitSide;
        MinimizedDockShadow minimizedDockShadow = this.mMinimizedShadow;
        Objects.requireNonNull(minimizedDockShadow);
        if (primarySplitSide != minimizedDockShadow.mDockSide) {
            minimizedDockShadow.mDockSide = primarySplitSide;
            minimizedDockShadow.updatePaint(minimizedDockShadow.getLeft(), minimizedDockShadow.getTop(), minimizedDockShadow.getRight(), minimizedDockShadow.getBottom());
            minimizedDockShadow.invalidate();
        }
    }

    public DividerView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public static void alignTopLeft(Rect rect, Rect rect2) {
        int width = rect2.width();
        int height = rect2.height();
        int i = rect.left;
        int i2 = rect.top;
        rect2.set(i, i2, width + i, height + i2);
    }

    public final int getCurrentPosition() {
        if (isHorizontalDivision()) {
            return this.mDividerPositionY;
        }
        return this.mDividerPositionX;
    }

    public final boolean isHorizontalDivision() {
        if (getResources().getConfiguration().orientation == 1) {
            return true;
        }
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mDockSide != -1 && !this.mIsInMinimizeInteraction) {
            saveSnapTargetBeforeMinimized(this.mSnapTargetBeforeMinimized);
        }
        this.mFirstLayout = true;
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        boolean z;
        int i;
        super.onFinishInflate();
        this.mHandle = (DividerHandleView) findViewById(2131427863);
        this.mBackground = findViewById(2131427862);
        this.mMinimizedShadow = (MinimizedDockShadow) findViewById(2131428372);
        this.mHandle.setOnTouchListener(this);
        int dimensionPixelSize = getResources().getDimensionPixelSize(17105203);
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(17105202);
        this.mDividerInsets = dimensionPixelSize2;
        this.mDividerSize = dimensionPixelSize - (dimensionPixelSize2 * 2);
        this.mTouchElevation = getResources().getDimensionPixelSize(2131165677);
        getResources().getInteger(2131492977);
        this.mTouchSlop = ViewConfiguration.get(((FrameLayout) this).mContext).getScaledTouchSlop();
        this.mFlingAnimationUtils = new FlingAnimationUtils(getResources().getDisplayMetrics(), 0.3f);
        if (getResources().getConfiguration().orientation == 2) {
            z = true;
        } else {
            z = false;
        }
        DividerHandleView dividerHandleView = this.mHandle;
        Context context = getContext();
        if (z) {
            i = 1014;
        } else {
            i = 1015;
        }
        dividerHandleView.setPointerIcon(PointerIcon.getSystemIcon(context, i));
        getViewTreeObserver().addOnComputeInternalInsetsListener(this);
        this.mHandle.setAccessibilityDelegate(this.mHandleDelegate);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        int right;
        Rect rect;
        super.onLayout(z, i, i2, i3, i4);
        int i6 = 0;
        if (this.mFirstLayout) {
            this.mSplitLayout.resizeSplits(this.mSplitLayout.getSnapAlgorithm().getMiddleTarget().position);
            SurfaceControl.Transaction transaction = this.mTiles.getTransaction();
            if (this.mDockedStackMinimized) {
                int i7 = this.mSplitLayout.getMinimizedSnapAlgorithm(this.mHomeStackResizable).getMiddleTarget().position;
                calculateBoundsForPosition(i7, this.mDockSide, this.mDockedRect);
                calculateBoundsForPosition(i7, DockedDividerUtils.invertDockSide(this.mDockSide), this.mOtherRect);
                this.mDividerPositionY = i7;
                this.mDividerPositionX = i7;
                Rect rect2 = this.mDockedRect;
                LegacySplitDisplayLayout legacySplitDisplayLayout = this.mSplitLayout;
                resizeSplitSurfaces(transaction, rect2, legacySplitDisplayLayout.mPrimary, this.mOtherRect, legacySplitDisplayLayout.mSecondary);
            } else {
                LegacySplitDisplayLayout legacySplitDisplayLayout2 = this.mSplitLayout;
                resizeSplitSurfaces(transaction, legacySplitDisplayLayout2.mPrimary, null, legacySplitDisplayLayout2.mSecondary, null);
            }
            setResizeDimLayer(transaction, true, 0.0f);
            setResizeDimLayer(transaction, false, 0.0f);
            transaction.apply();
            this.mTiles.releaseTransaction(transaction);
            if (isHorizontalDivision()) {
                int i8 = this.mDividerInsets;
                DisplayLayout displayLayout = this.mSplitLayout.mDisplayLayout;
                Objects.requireNonNull(displayLayout);
                rect = new Rect(0, i8, displayLayout.mWidth, this.mDividerInsets + this.mDividerSize);
            } else {
                int i9 = this.mDividerInsets;
                DisplayLayout displayLayout2 = this.mSplitLayout.mDisplayLayout;
                Objects.requireNonNull(displayLayout2);
                rect = new Rect(i9, 0, this.mDividerSize + i9, displayLayout2.mHeight);
            }
            Region region = new Region(rect);
            region.union(new Rect(this.mHandle.getLeft(), this.mHandle.getTop(), this.mHandle.getRight(), this.mHandle.getBottom()));
            DividerWindowManager dividerWindowManager = this.mWindowManager;
            Objects.requireNonNull(dividerWindowManager);
            View view = dividerWindowManager.mView;
            if (view != null) {
                SystemWindows systemWindows = dividerWindowManager.mSystemWindows;
                Objects.requireNonNull(systemWindows);
                SurfaceControlViewHost surfaceControlViewHost = systemWindows.mViewRoots.get(view);
                if (surfaceControlViewHost != null) {
                    SystemWindows.SysUiWindowManager windowlessWM = surfaceControlViewHost.getWindowlessWM();
                    if (windowlessWM instanceof SystemWindows.SysUiWindowManager) {
                        windowlessWM.setTouchableRegionForWindow(view, region);
                    }
                }
            }
            this.mFirstLayout = false;
        }
        int i10 = this.mDockSide;
        if (i10 == 2) {
            i5 = this.mBackground.getTop();
        } else {
            if (i10 == 1) {
                right = this.mBackground.getLeft();
            } else if (i10 == 3) {
                right = this.mBackground.getRight() - this.mMinimizedShadow.getWidth();
            } else {
                i5 = 0;
            }
            i6 = right;
            i5 = 0;
        }
        MinimizedDockShadow minimizedDockShadow = this.mMinimizedShadow;
        minimizedDockShadow.layout(i6, i5, minimizedDockShadow.getMeasuredWidth() + i6, this.mMinimizedShadow.getMeasuredHeight() + i5);
        if (z) {
            notifySplitScreenBoundsChanged();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x001b, code lost:
        if (r6 != 3) goto L_0x00ec;
     */
    @Override // android.view.View.OnTouchListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean onTouch(android.view.View r6, android.view.MotionEvent r7) {
        /*
            Method dump skipped, instructions count: 296
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.legacysplitscreen.DividerView.onTouch(android.view.View, android.view.MotionEvent):boolean");
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.wm.shell.legacysplitscreen.DividerView$1] */
    /* JADX WARN: Type inference failed for: r2v2, types: [com.android.wm.shell.legacysplitscreen.DividerView$2] */
    public DividerView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mDockedRect = new Rect();
        this.mDockedTaskRect = new Rect();
        this.mOtherTaskRect = new Rect();
        this.mOtherRect = new Rect();
        this.mDockedInsetRect = new Rect();
        this.mOtherInsetRect = new Rect();
        this.mLastResizeRect = new Rect();
        this.mTmpRect = new Rect();
        this.mFirstLayout = true;
        this.mTmpMatrix = new Matrix();
        this.mTmpValues = new float[9];
        this.mSurfaceHidden = false;
        this.mHandleDelegate = new View.AccessibilityDelegate() { // from class: com.android.wm.shell.legacysplitscreen.DividerView.1
            @Override // android.view.View.AccessibilityDelegate
            public final boolean performAccessibilityAction(View view, int i3, Bundle bundle) {
                DividerSnapAlgorithm.SnapTarget snapTarget;
                int currentPosition = DividerView.this.getCurrentPosition();
                DividerSnapAlgorithm snapAlgorithm = DividerView.this.mSplitLayout.getSnapAlgorithm();
                if (i3 == 2131427438) {
                    snapTarget = snapAlgorithm.getDismissEndTarget();
                } else if (i3 == 2131427437) {
                    snapTarget = snapAlgorithm.getLastSplitTarget();
                } else if (i3 == 2131427436) {
                    snapTarget = snapAlgorithm.getMiddleTarget();
                } else if (i3 == 2131427435) {
                    snapTarget = snapAlgorithm.getFirstSplitTarget();
                } else if (i3 == 2131427434) {
                    snapTarget = snapAlgorithm.getDismissStartTarget();
                } else {
                    snapTarget = null;
                }
                if (snapTarget == null) {
                    return super.performAccessibilityAction(view, i3, bundle);
                }
                DividerView.this.startDragging(true, false);
                DividerView dividerView = DividerView.this;
                PathInterpolator pathInterpolator = Interpolators.FAST_OUT_SLOW_IN;
                Objects.requireNonNull(dividerView);
                dividerView.stopDragging(currentPosition, snapTarget, 250L, 0L, pathInterpolator);
                return true;
            }

            @Override // android.view.View.AccessibilityDelegate
            public final void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                DividerSnapAlgorithm snapAlgorithm = DividerView.this.getSnapAlgorithm();
                if (DividerView.this.isHorizontalDivision()) {
                    accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(2131427438, ((FrameLayout) DividerView.this).mContext.getString(2131951666)));
                    if (snapAlgorithm.isFirstSplitTargetAvailable()) {
                        accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(2131427437, ((FrameLayout) DividerView.this).mContext.getString(2131951665)));
                    }
                    if (snapAlgorithm.showMiddleSplitTargetForAccessibility()) {
                        accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(2131427436, ((FrameLayout) DividerView.this).mContext.getString(2131951664)));
                    }
                    if (snapAlgorithm.isLastSplitTargetAvailable()) {
                        accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(2131427435, ((FrameLayout) DividerView.this).mContext.getString(2131951663)));
                    }
                    accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(2131427434, ((FrameLayout) DividerView.this).mContext.getString(2131951657)));
                    return;
                }
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(2131427438, ((FrameLayout) DividerView.this).mContext.getString(2131951661)));
                if (snapAlgorithm.isFirstSplitTargetAvailable()) {
                    accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(2131427437, ((FrameLayout) DividerView.this).mContext.getString(2131951660)));
                }
                if (snapAlgorithm.showMiddleSplitTargetForAccessibility()) {
                    accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(2131427436, ((FrameLayout) DividerView.this).mContext.getString(2131951659)));
                }
                if (snapAlgorithm.isLastSplitTargetAvailable()) {
                    accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(2131427435, ((FrameLayout) DividerView.this).mContext.getString(2131951658)));
                }
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(2131427434, ((FrameLayout) DividerView.this).mContext.getString(2131951662)));
            }
        };
        this.mResetBackgroundRunnable = new Runnable() { // from class: com.android.wm.shell.legacysplitscreen.DividerView.2
            @Override // java.lang.Runnable
            public final void run() {
                DividerView dividerView = DividerView.this;
                PathInterpolator pathInterpolator = DividerView.IME_ADJUST_INTERPOLATOR;
                dividerView.resetBackground();
            }
        };
        this.mUpdateEmbeddedMatrix = new StatusBar$$ExternalSyntheticLambda19(this, 6);
        this.mDefaultDisplay = ((DisplayManager) ((FrameLayout) this).mContext.getSystemService("display")).getDisplay(0);
    }

    public final void stopDragging(int i, DividerSnapAlgorithm.SnapTarget snapTarget, long j, long j2, PathInterpolator pathInterpolator) {
        this.mHandle.setTouching(false, true);
        ValueAnimator flingAnimator = getFlingAnimator(i, snapTarget);
        flingAnimator.setDuration(j);
        flingAnimator.setStartDelay(j2);
        flingAnimator.setInterpolator(pathInterpolator);
        flingAnimator.start();
        this.mWindowManager.setSlippery(true);
        releaseBackground();
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }
}
