package com.android.wm.shell.pip.phone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;
import android.util.Size;
import android.view.SurfaceControl;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.transition.TransitionPropagation;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda0;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda6;
import com.android.systemui.people.PeopleSpaceUtils$$ExternalSyntheticLambda2;
import com.android.systemui.scrim.ScrimView$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda19;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda3;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.FloatingContentCoordinator;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipUiEventLogger;
import com.android.wm.shell.pip.phone.PhonePipMenuController;
import java.util.Objects;
/* loaded from: classes.dex */
public final class PipTouchHandler {
    public final AccessibilityManager mAccessibilityManager;
    public int mBottomOffsetBufferPx;
    public final PipAccessibilityInteractionConnection mConnection;
    public final Context mContext;
    public int mDisplayRotation;
    public boolean mEnableResize;
    public int mExpandedShortestEdgeSize;
    public final FloatingContentCoordinator mFloatingContentCoordinator;
    public DefaultPipTouchGesture mGesture;
    public int mImeHeight;
    public int mImeOffset;
    public boolean mIsImeShowing;
    public boolean mIsShelfShowing;
    public final ShellExecutor mMainExecutor;
    public final PhonePipMenuController mMenuController;
    public float mMinimumSizePercent;
    public PipMotionHelper mMotionHelper;
    public int mMovementBoundsExtraOffsets;
    public boolean mMovementWithinDismiss;
    public final PipBoundsAlgorithm mPipBoundsAlgorithm;
    public final PipBoundsState mPipBoundsState;
    public final PipDismissTargetHandler mPipDismissTargetHandler;
    public PipResizeGestureHandler mPipResizeGestureHandler;
    public final PipTaskOrganizer mPipTaskOrganizer;
    public final PipUiEventLogger mPipUiEventLogger;
    public boolean mSendingHoverAccessibilityEvents;
    public int mShelfHeight;
    public float mStashVelocityThreshold;
    public final PipTouchState mTouchState;
    public boolean mEnableStash = true;
    public final Rect mInsetBounds = new Rect();
    public int mDeferResizeToNormalBoundsUntilRotation = -1;
    public int mMenuState = 0;
    public float mSavedSnapFraction = -1.0f;
    public final Rect mTmpBounds = new Rect();

    /* loaded from: classes.dex */
    public class DefaultPipTouchGesture extends TransitionPropagation {
        public boolean mShouldHideMenuAfterFling;
        public final Point mStartPosition = new Point();
        public final PointF mDelta = new PointF();

        public DefaultPipTouchGesture() {
        }

        /* JADX WARN: Code restructure failed: missing block: B:19:0x006c, code lost:
            if (r2.mStashedState == 2) goto L_0x006e;
         */
        /* JADX WARN: Code restructure failed: missing block: B:23:0x007f, code lost:
            if (r2.mStashedState != 1) goto L_0x0081;
         */
        /* JADX WARN: Code restructure failed: missing block: B:24:0x0081, code lost:
            r2 = true;
         */
        /* JADX WARN: Removed duplicated region for block: B:61:0x0168  */
        /* JADX WARN: Removed duplicated region for block: B:63:0x0171  */
        /* JADX WARN: Removed duplicated region for block: B:67:0x01c5  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final boolean onUp(com.android.wm.shell.pip.phone.PipTouchState r9) {
            /*
                Method dump skipped, instructions count: 616
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.pip.phone.PipTouchHandler.DefaultPipTouchGesture.onUp(com.android.wm.shell.pip.phone.PipTouchState):boolean");
        }

        public final void onDown(PipTouchState pipTouchState) {
            PipBoundsState pipBoundsState;
            boolean z;
            Objects.requireNonNull(pipTouchState);
            if (pipTouchState.mIsUserInteracting) {
                Rect possiblyMotionBounds = PipTouchHandler.this.getPossiblyMotionBounds();
                this.mDelta.set(0.0f, 0.0f);
                this.mStartPosition.set(possiblyMotionBounds.left, possiblyMotionBounds.top);
                PipTouchHandler pipTouchHandler = PipTouchHandler.this;
                float f = pipTouchState.mDownTouch.y;
                Objects.requireNonNull(pipTouchHandler.mPipBoundsState);
                if (f >= pipBoundsState.mMovementBounds.bottom) {
                    z = true;
                } else {
                    z = false;
                }
                pipTouchHandler.mMovementWithinDismiss = z;
                PipMotionHelper pipMotionHelper = PipTouchHandler.this.mMotionHelper;
                Objects.requireNonNull(pipMotionHelper);
                pipMotionHelper.mSpringingToTouch = false;
                PipTouchHandler pipTouchHandler2 = PipTouchHandler.this;
                PipDismissTargetHandler pipDismissTargetHandler = pipTouchHandler2.mPipDismissTargetHandler;
                PipTaskOrganizer pipTaskOrganizer = pipTouchHandler2.mPipTaskOrganizer;
                Objects.requireNonNull(pipTaskOrganizer);
                SurfaceControl surfaceControl = pipTaskOrganizer.mLeash;
                Objects.requireNonNull(pipDismissTargetHandler);
                pipDismissTargetHandler.mTaskLeash = surfaceControl;
                PipTouchHandler pipTouchHandler3 = PipTouchHandler.this;
                if (pipTouchHandler3.mMenuState != 0 && !pipTouchHandler3.mPipBoundsState.isStashed()) {
                    PhonePipMenuController phonePipMenuController = PipTouchHandler.this.mMenuController;
                    Objects.requireNonNull(phonePipMenuController);
                    if (phonePipMenuController.isMenuVisible()) {
                        PipMenuView pipMenuView = phonePipMenuController.mPipMenuView;
                        Objects.requireNonNull(pipMenuView);
                        pipMenuView.mMainExecutor.removeCallbacks(pipMenuView.mHideMenuRunnable);
                    }
                }
            }
        }

        public final boolean onMove(PipTouchState pipTouchState) {
            PipBoundsState pipBoundsState;
            Objects.requireNonNull(pipTouchState);
            boolean z = false;
            if (!pipTouchState.mIsUserInteracting) {
                return false;
            }
            if (pipTouchState.mStartedDragging) {
                PipTouchHandler pipTouchHandler = PipTouchHandler.this;
                pipTouchHandler.mSavedSnapFraction = -1.0f;
                pipTouchHandler.mPipDismissTargetHandler.showDismissTargetMaybe();
            }
            if (!pipTouchState.mIsDragging) {
                return false;
            }
            PointF pointF = pipTouchState.mLastDelta;
            Point point = this.mStartPosition;
            PointF pointF2 = this.mDelta;
            float f = pointF2.x;
            float f2 = point.x + f;
            float f3 = pointF2.y;
            float f4 = point.y + f3;
            float f5 = pointF.x + f2;
            float f6 = pointF.y + f4;
            pointF2.x = (f5 - f2) + f;
            pointF2.y = (f6 - f4) + f3;
            PipTouchHandler pipTouchHandler2 = PipTouchHandler.this;
            pipTouchHandler2.mTmpBounds.set(pipTouchHandler2.getPossiblyMotionBounds());
            PipTouchHandler.this.mTmpBounds.offsetTo((int) f5, (int) f6);
            PipTouchHandler pipTouchHandler3 = PipTouchHandler.this;
            pipTouchHandler3.mMotionHelper.movePip(pipTouchHandler3.mTmpBounds, true);
            PointF pointF3 = pipTouchState.mLastTouch;
            PipTouchHandler pipTouchHandler4 = PipTouchHandler.this;
            if (pipTouchHandler4.mMovementWithinDismiss) {
                float f7 = pointF3.y;
                Objects.requireNonNull(pipTouchHandler4.mPipBoundsState);
                if (f7 >= pipBoundsState.mMovementBounds.bottom) {
                    z = true;
                }
                pipTouchHandler4.mMovementWithinDismiss = z;
            }
            return true;
        }
    }

    /* loaded from: classes.dex */
    public class PipMenuListener implements PhonePipMenuController.Listener {
        public PipMenuListener() {
        }

        @Override // com.android.wm.shell.pip.phone.PhonePipMenuController.Listener
        public final void onEnterSplit() {
            PipMotionHelper pipMotionHelper = PipTouchHandler.this.mMotionHelper;
            Objects.requireNonNull(pipMotionHelper);
            pipMotionHelper.expandLeavePip(false, true);
        }

        @Override // com.android.wm.shell.pip.phone.PhonePipMenuController.Listener
        public final void onPipDismiss() {
            PipTouchState pipTouchState = PipTouchHandler.this.mTouchState;
            Objects.requireNonNull(pipTouchState);
            pipTouchState.mIsWaitingForDoubleTap = false;
            pipTouchState.mMainExecutor.removeCallbacks(pipTouchState.mDoubleTapTimeoutCallback);
            PipTouchHandler.this.mMotionHelper.dismissPip();
        }

        @Override // com.android.wm.shell.pip.phone.PhonePipMenuController.Listener
        public final void onPipExpand() {
            PipMotionHelper pipMotionHelper = PipTouchHandler.this.mMotionHelper;
            Objects.requireNonNull(pipMotionHelper);
            pipMotionHelper.expandLeavePip(false, false);
        }

        @Override // com.android.wm.shell.pip.phone.PhonePipMenuController.Listener
        public final void onPipMenuStateChangeFinish(int i) {
            boolean z;
            PipTouchHandler pipTouchHandler = PipTouchHandler.this;
            Objects.requireNonNull(pipTouchHandler);
            pipTouchHandler.mMenuState = i;
            pipTouchHandler.updateMovementBounds();
            if (i == 0) {
                z = true;
            } else {
                z = false;
            }
            pipTouchHandler.onRegistrationChanged(z);
            if (i == 0) {
                pipTouchHandler.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_HIDE_MENU);
            } else if (i == 1) {
                pipTouchHandler.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_SHOW_MENU);
            }
        }

        @Override // com.android.wm.shell.pip.phone.PhonePipMenuController.Listener
        public final void onPipMenuStateChangeStart(int i, boolean z, Runnable runnable) {
            int rotation;
            boolean z2;
            boolean z3;
            int i2;
            int i3;
            PipTouchHandler pipTouchHandler = PipTouchHandler.this;
            Objects.requireNonNull(pipTouchHandler);
            int i4 = pipTouchHandler.mMenuState;
            if (i4 == i && !z) {
                return;
            }
            if (i != 1 || i4 == 1) {
                if (i == 0 && i4 == 1) {
                    if (z) {
                        PipResizeGestureHandler pipResizeGestureHandler = pipTouchHandler.mPipResizeGestureHandler;
                        Objects.requireNonNull(pipResizeGestureHandler);
                        if (!pipResizeGestureHandler.mAllowGesture) {
                            if (pipTouchHandler.mDeferResizeToNormalBoundsUntilRotation == -1 && pipTouchHandler.mDisplayRotation != (rotation = pipTouchHandler.mContext.getDisplay().getRotation())) {
                                pipTouchHandler.mDeferResizeToNormalBoundsUntilRotation = rotation;
                            }
                            if (pipTouchHandler.mDeferResizeToNormalBoundsUntilRotation == -1) {
                                PipResizeGestureHandler pipResizeGestureHandler2 = pipTouchHandler.mPipResizeGestureHandler;
                                Objects.requireNonNull(pipResizeGestureHandler2);
                                pipTouchHandler.animateToUnexpandedState(pipResizeGestureHandler2.mUserResizeBounds);
                                return;
                            }
                            return;
                        }
                    }
                    pipTouchHandler.mSavedSnapFraction = -1.0f;
                }
            } else if (z) {
                PipResizeGestureHandler pipResizeGestureHandler3 = pipTouchHandler.mPipResizeGestureHandler;
                Rect bounds = pipTouchHandler.mPipBoundsState.getBounds();
                Objects.requireNonNull(pipResizeGestureHandler3);
                pipResizeGestureHandler3.mUserResizeBounds.set(bounds);
                Size estimatedMinMenuSize = pipTouchHandler.mMenuController.getEstimatedMinMenuSize();
                PipBoundsState pipBoundsState = pipTouchHandler.mPipBoundsState;
                Objects.requireNonNull(pipBoundsState);
                Rect rect = pipBoundsState.mNormalBounds;
                PipBoundsAlgorithm pipBoundsAlgorithm = pipTouchHandler.mPipBoundsAlgorithm;
                Objects.requireNonNull(pipBoundsAlgorithm);
                int i5 = 0;
                if (estimatedMinMenuSize != null && (rect.width() < estimatedMinMenuSize.getWidth() || rect.height() < estimatedMinMenuSize.getHeight())) {
                    Rect rect2 = new Rect();
                    if (estimatedMinMenuSize.getWidth() > rect.width()) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    if (estimatedMinMenuSize.getHeight() > rect.height()) {
                        z3 = true;
                    } else {
                        z3 = false;
                    }
                    if (!z2 || !z3) {
                        if (z2) {
                            i3 = estimatedMinMenuSize.getWidth();
                            PipBoundsState pipBoundsState2 = pipBoundsAlgorithm.mPipBoundsState;
                            Objects.requireNonNull(pipBoundsState2);
                            i2 = Math.round(i3 / pipBoundsState2.mAspectRatio);
                        } else {
                            i2 = estimatedMinMenuSize.getHeight();
                            PipBoundsState pipBoundsState3 = pipBoundsAlgorithm.mPipBoundsState;
                            Objects.requireNonNull(pipBoundsState3);
                            i3 = Math.round(i2 * pipBoundsState3.mAspectRatio);
                        }
                    } else if (estimatedMinMenuSize.getWidth() / rect.width() > estimatedMinMenuSize.getHeight() / rect.height()) {
                        i3 = estimatedMinMenuSize.getWidth();
                        PipBoundsState pipBoundsState4 = pipBoundsAlgorithm.mPipBoundsState;
                        Objects.requireNonNull(pipBoundsState4);
                        i2 = Math.round(i3 / pipBoundsState4.mAspectRatio);
                    } else {
                        i2 = estimatedMinMenuSize.getHeight();
                        PipBoundsState pipBoundsState5 = pipBoundsAlgorithm.mPipBoundsState;
                        Objects.requireNonNull(pipBoundsState5);
                        i3 = Math.round(i2 * pipBoundsState5.mAspectRatio);
                    }
                    rect2.set(0, 0, i3, i2);
                    PipBoundsState pipBoundsState6 = pipBoundsAlgorithm.mPipBoundsState;
                    Objects.requireNonNull(pipBoundsState6);
                    pipBoundsAlgorithm.transformBoundsToAspectRatio(rect2, pipBoundsState6.mAspectRatio, true, true);
                    rect = rect2;
                }
                Rect rect3 = new Rect();
                PipBoundsAlgorithm pipBoundsAlgorithm2 = pipTouchHandler.mPipBoundsAlgorithm;
                Rect rect4 = pipTouchHandler.mInsetBounds;
                if (pipTouchHandler.mIsImeShowing) {
                    i5 = pipTouchHandler.mImeHeight;
                }
                Objects.requireNonNull(pipBoundsAlgorithm2);
                PipBoundsAlgorithm.getMovementBounds(rect, rect4, rect3, i5);
                PipMotionHelper pipMotionHelper = pipTouchHandler.mMotionHelper;
                PipBoundsState pipBoundsState7 = pipTouchHandler.mPipBoundsState;
                Objects.requireNonNull(pipBoundsState7);
                pipTouchHandler.mSavedSnapFraction = pipMotionHelper.animateToExpandedState(rect, pipBoundsState7.mMovementBounds, rect3, runnable);
            }
        }

        @Override // com.android.wm.shell.pip.phone.PhonePipMenuController.Listener
        public final void onPipShowMenu() {
            PipTouchHandler pipTouchHandler = PipTouchHandler.this;
            PhonePipMenuController phonePipMenuController = pipTouchHandler.mMenuController;
            Rect bounds = pipTouchHandler.mPipBoundsState.getBounds();
            boolean willResizeMenu = PipTouchHandler.this.willResizeMenu();
            Objects.requireNonNull(PipTouchHandler.this);
            phonePipMenuController.showMenu(1, bounds, true, willResizeMenu);
        }
    }

    @SuppressLint({"InflateParams"})
    public PipTouchHandler(Context context, PhonePipMenuController phonePipMenuController, PipBoundsAlgorithm pipBoundsAlgorithm, PipBoundsState pipBoundsState, PipTaskOrganizer pipTaskOrganizer, PipMotionHelper pipMotionHelper, FloatingContentCoordinator floatingContentCoordinator, PipUiEventLogger pipUiEventLogger, ShellExecutor shellExecutor) {
        this.mContext = context;
        this.mMainExecutor = shellExecutor;
        this.mAccessibilityManager = (AccessibilityManager) context.getSystemService(AccessibilityManager.class);
        this.mPipBoundsAlgorithm = pipBoundsAlgorithm;
        this.mPipBoundsState = pipBoundsState;
        this.mPipTaskOrganizer = pipTaskOrganizer;
        this.mMenuController = phonePipMenuController;
        this.mPipUiEventLogger = pipUiEventLogger;
        this.mFloatingContentCoordinator = floatingContentCoordinator;
        PipMenuListener pipMenuListener = new PipMenuListener();
        Objects.requireNonNull(phonePipMenuController);
        if (!phonePipMenuController.mListeners.contains(pipMenuListener)) {
            phonePipMenuController.mListeners.add(pipMenuListener);
        }
        this.mGesture = new DefaultPipTouchGesture();
        this.mMotionHelper = pipMotionHelper;
        PipDismissTargetHandler pipDismissTargetHandler = new PipDismissTargetHandler(context, pipUiEventLogger, pipMotionHelper, shellExecutor);
        this.mPipDismissTargetHandler = pipDismissTargetHandler;
        this.mPipResizeGestureHandler = new PipResizeGestureHandler(context, pipBoundsAlgorithm, pipBoundsState, this.mMotionHelper, pipTaskOrganizer, pipDismissTargetHandler, new PeopleSpaceUtils$$ExternalSyntheticLambda2(this, 1), new StatusBar$$ExternalSyntheticLambda19(this, 8), pipUiEventLogger, phonePipMenuController, shellExecutor);
        this.mTouchState = new PipTouchState(ViewConfiguration.get(context), new CarrierTextManager$$ExternalSyntheticLambda0(this, 9), new TaskView$$ExternalSyntheticLambda3(phonePipMenuController, 9), shellExecutor);
        PipMotionHelper pipMotionHelper2 = this.mMotionHelper;
        Objects.requireNonNull(pipBoundsAlgorithm);
        this.mConnection = new PipAccessibilityInteractionConnection(context, pipBoundsState, pipMotionHelper2, pipTaskOrganizer, pipBoundsAlgorithm.mSnapAlgorithm, new PipTouchHandler$$ExternalSyntheticLambda2(this), new ScrimView$$ExternalSyntheticLambda0(this, 7), new KeyguardUpdateMonitor$$ExternalSyntheticLambda6(this, 10), shellExecutor);
    }

    public final void animateToUnStashedState() {
        boolean z;
        int i;
        int i2;
        Rect bounds = this.mPipBoundsState.getBounds();
        if (bounds.left < this.mPipBoundsState.getDisplayBounds().left) {
            z = true;
        } else {
            z = false;
        }
        Rect rect = new Rect(0, bounds.top, 0, bounds.bottom);
        if (z) {
            i = this.mInsetBounds.left;
        } else {
            i = this.mInsetBounds.right - bounds.width();
        }
        rect.left = i;
        if (z) {
            i2 = bounds.width() + this.mInsetBounds.left;
        } else {
            i2 = this.mInsetBounds.right;
        }
        rect.right = i2;
        PipMotionHelper pipMotionHelper = this.mMotionHelper;
        Objects.requireNonNull(pipMotionHelper);
        pipMotionHelper.mPipTaskOrganizer.scheduleAnimateResizePip(rect, 250, 8, null);
        PipBoundsState pipBoundsState = pipMotionHelper.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState);
        PipBoundsState.MotionBoundsState motionBoundsState = pipBoundsState.mMotionBoundsState;
        Objects.requireNonNull(motionBoundsState);
        motionBoundsState.mAnimatingToBounds.set(rect);
        pipMotionHelper.mFloatingContentCoordinator.onContentMoved(pipMotionHelper);
    }

    public final void animateToUnexpandedState(Rect rect) {
        int i;
        Rect rect2 = new Rect();
        PipBoundsAlgorithm pipBoundsAlgorithm = this.mPipBoundsAlgorithm;
        Rect rect3 = this.mInsetBounds;
        if (this.mIsImeShowing) {
            i = this.mImeHeight;
        } else {
            i = 0;
        }
        Objects.requireNonNull(pipBoundsAlgorithm);
        PipBoundsAlgorithm.getMovementBounds(rect, rect3, rect2, i);
        PipMotionHelper pipMotionHelper = this.mMotionHelper;
        float f = this.mSavedSnapFraction;
        PipBoundsState pipBoundsState = this.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState);
        pipMotionHelper.animateToUnexpandedState(rect, f, rect2, pipBoundsState.mMovementBounds, false);
        this.mSavedSnapFraction = -1.0f;
    }

    public final Rect getPossiblyMotionBounds() {
        PipBoundsState pipBoundsState = this.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState);
        PipBoundsState.MotionBoundsState motionBoundsState = pipBoundsState.mMotionBoundsState;
        Objects.requireNonNull(motionBoundsState);
        if (!(!motionBoundsState.mBoundsInMotion.isEmpty())) {
            return this.mPipBoundsState.getBounds();
        }
        PipBoundsState pipBoundsState2 = this.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState2);
        PipBoundsState.MotionBoundsState motionBoundsState2 = pipBoundsState2.mMotionBoundsState;
        Objects.requireNonNull(motionBoundsState2);
        return motionBoundsState2.mBoundsInMotion;
    }

    public final void onRegistrationChanged(boolean z) {
        if (z) {
            PipAccessibilityInteractionConnection pipAccessibilityInteractionConnection = this.mConnection;
            AccessibilityManager accessibilityManager = this.mAccessibilityManager;
            Objects.requireNonNull(pipAccessibilityInteractionConnection);
            accessibilityManager.setPictureInPictureActionReplacingConnection(pipAccessibilityInteractionConnection.mConnectionImpl);
        } else {
            this.mAccessibilityManager.setPictureInPictureActionReplacingConnection(null);
        }
        if (!z) {
            PipTouchState pipTouchState = this.mTouchState;
            Objects.requireNonNull(pipTouchState);
            if (pipTouchState.mIsUserInteracting) {
                PipDismissTargetHandler pipDismissTargetHandler = this.mPipDismissTargetHandler;
                Objects.requireNonNull(pipDismissTargetHandler);
                if (pipDismissTargetHandler.mTargetViewContainer.isAttachedToWindow()) {
                    pipDismissTargetHandler.mWindowManager.removeViewImmediate(pipDismissTargetHandler.mTargetViewContainer);
                }
            }
        }
    }

    public final void reloadResources() {
        Resources resources = this.mContext.getResources();
        this.mBottomOffsetBufferPx = resources.getDimensionPixelSize(2131166779);
        this.mExpandedShortestEdgeSize = resources.getDimensionPixelSize(2131166785);
        this.mImeOffset = resources.getDimensionPixelSize(2131166786);
        this.mMinimumSizePercent = resources.getFraction(2131361799, 1, 1);
        this.mPipDismissTargetHandler.updateMagneticTargetSize();
    }

    public final void sendAccessibilityHoverEvent(int i) {
        if (this.mAccessibilityManager.isEnabled()) {
            AccessibilityEvent obtain = AccessibilityEvent.obtain(i);
            obtain.setImportantForAccessibility(true);
            obtain.setSourceNodeId(AccessibilityNodeInfo.ROOT_NODE_ID);
            obtain.setWindowId(-3);
            this.mAccessibilityManager.sendAccessibilityEvent(obtain);
        }
    }

    public final void updateMovementBounds() {
        int i;
        PipBoundsState pipBoundsState;
        PipBoundsState pipBoundsState2;
        PipBoundsState pipBoundsState3;
        PipBoundsState pipBoundsState4;
        PipBoundsState pipBoundsState5;
        PipBoundsState pipBoundsState6;
        int i2;
        PipBoundsAlgorithm pipBoundsAlgorithm = this.mPipBoundsAlgorithm;
        Rect bounds = this.mPipBoundsState.getBounds();
        Rect rect = this.mInsetBounds;
        PipBoundsState pipBoundsState7 = this.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState7);
        Rect rect2 = pipBoundsState7.mMovementBounds;
        boolean z = false;
        if (this.mIsImeShowing) {
            i = this.mImeHeight;
        } else {
            i = 0;
        }
        Objects.requireNonNull(pipBoundsAlgorithm);
        PipBoundsAlgorithm.getMovementBounds(bounds, rect, rect2, i);
        PipMotionHelper pipMotionHelper = this.mMotionHelper;
        Objects.requireNonNull(pipMotionHelper);
        Objects.requireNonNull(pipMotionHelper.mPipBoundsState);
        Objects.requireNonNull(pipMotionHelper.mPipBoundsState);
        pipMotionHelper.mFlingConfigX = new PhysicsAnimator.FlingConfig(1.9f, pipBoundsState.mMovementBounds.left, pipBoundsState2.mMovementBounds.right, 0.0f);
        Objects.requireNonNull(pipMotionHelper.mPipBoundsState);
        Objects.requireNonNull(pipMotionHelper.mPipBoundsState);
        pipMotionHelper.mFlingConfigY = new PhysicsAnimator.FlingConfig(1.9f, pipBoundsState3.mMovementBounds.top, pipBoundsState4.mMovementBounds.bottom, 0.0f);
        PipBoundsState pipBoundsState8 = pipMotionHelper.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState8);
        DisplayLayout displayLayout = pipBoundsState8.mDisplayLayout;
        Objects.requireNonNull(displayLayout);
        Rect rect3 = displayLayout.mStableInsets;
        Objects.requireNonNull(pipMotionHelper.mPipBoundsState);
        int i3 = pipMotionHelper.mPipBoundsState.getDisplayBounds().right;
        Objects.requireNonNull(pipMotionHelper.mPipBoundsState);
        pipMotionHelper.mStashConfigX = new PhysicsAnimator.FlingConfig(1.9f, (pipBoundsState5.mStashOffset - pipMotionHelper.mPipBoundsState.getBounds().width()) + rect3.left, (i3 - pipBoundsState6.mStashOffset) - rect3.right, 0.0f);
        Rect rect4 = pipMotionHelper.mFloatingAllowedArea;
        PipBoundsState pipBoundsState9 = pipMotionHelper.mPipBoundsState;
        Objects.requireNonNull(pipBoundsState9);
        rect4.set(pipBoundsState9.mMovementBounds);
        Rect rect5 = pipMotionHelper.mFloatingAllowedArea;
        rect5.right = pipMotionHelper.getBounds().width() + rect5.right;
        Rect rect6 = pipMotionHelper.mFloatingAllowedArea;
        rect6.bottom = pipMotionHelper.getBounds().height() + rect6.bottom;
        if (this.mMenuState == 1) {
            z = true;
        }
        PipBoundsState pipBoundsState10 = this.mPipBoundsState;
        if (!z || !willResizeMenu()) {
            PipBoundsAlgorithm pipBoundsAlgorithm2 = this.mPipBoundsAlgorithm;
            Objects.requireNonNull(pipBoundsAlgorithm2);
            i2 = pipBoundsAlgorithm2.mDefaultMinSize;
        } else {
            i2 = this.mExpandedShortestEdgeSize;
        }
        Objects.requireNonNull(pipBoundsState10);
        pipBoundsState10.mMinEdgeSize = i2;
    }

    public final boolean willResizeMenu() {
        if (!this.mEnableResize) {
            return false;
        }
        Size estimatedMinMenuSize = this.mMenuController.getEstimatedMinMenuSize();
        if (estimatedMinMenuSize == null) {
            Log.wtf("PipTouchHandler", "Failed to get estimated menu size");
            return false;
        }
        Rect bounds = this.mPipBoundsState.getBounds();
        if (bounds.width() < estimatedMinMenuSize.getWidth() || bounds.height() < estimatedMinMenuSize.getHeight()) {
            return true;
        }
        return false;
    }

    @VisibleForTesting
    public void setPipMotionHelper(PipMotionHelper pipMotionHelper) {
        this.mMotionHelper = pipMotionHelper;
    }

    @VisibleForTesting
    public void setPipResizeGestureHandler(PipResizeGestureHandler pipResizeGestureHandler) {
        this.mPipResizeGestureHandler = pipResizeGestureHandler;
    }

    @VisibleForTesting
    public PipResizeGestureHandler getPipResizeGestureHandler() {
        return this.mPipResizeGestureHandler;
    }
}
