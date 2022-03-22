package com.android.wm.shell.pip.phone;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Region;
import android.hardware.input.InputManager;
import android.os.Looper;
import android.util.Log;
import android.view.BatchedInputEventReceiver;
import android.view.Choreographer;
import android.view.InputChannel;
import android.view.InputEvent;
import android.view.InputMonitor;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import com.android.internal.policy.TaskResizingAlgorithm;
import com.android.systemui.ScreenDecorations$$ExternalSyntheticLambda1;
import com.android.systemui.people.PeopleSpaceUtils$$ExternalSyntheticLambda2;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda19;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda33;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.magnetictarget.MagnetizedObject;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipSnapAlgorithm;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipUiEventLogger;
import java.util.Objects;
import java.util.function.Function;
/* loaded from: classes.dex */
public final class PipResizeGestureHandler {
    public boolean mAllowGesture;
    public final Context mContext;
    public int mCtrlType;
    public int mDelta;
    public final int mDisplayId;
    public boolean mEnableDragCornerResize;
    public boolean mEnablePinchResize;
    public PipResizeInputEventReceiver mInputEventReceiver;
    public InputMonitor mInputMonitor;
    public boolean mIsAttached;
    public boolean mIsEnabled;
    public boolean mIsSysUiStateValid;
    public final ShellExecutor mMainExecutor;
    public final PipMotionHelper mMotionHelper;
    public final Function<Rect, Rect> mMovementBoundsSupplier;
    public int mOhmOffset;
    public final PhonePipMenuController mPhonePipMenuController;
    public final PipBoundsAlgorithm mPipBoundsAlgorithm;
    public final PipBoundsState mPipBoundsState;
    public final PipDismissTargetHandler mPipDismissTargetHandler;
    public final PipTaskOrganizer mPipTaskOrganizer;
    public final PipUiEventLogger mPipUiEventLogger;
    public boolean mThresholdCrossed;
    public float mTouchSlop;
    public final Runnable mUpdateMovementBoundsRunnable;
    public final Region mTmpRegion = new Region();
    public final PointF mDownPoint = new PointF();
    public final PointF mDownSecondPoint = new PointF();
    public final PointF mLastPoint = new PointF();
    public final PointF mLastSecondPoint = new PointF();
    public final Point mMaxSize = new Point();
    public final Point mMinSize = new Point();
    public final Rect mLastResizeBounds = new Rect();
    public final Rect mUserResizeBounds = new Rect();
    public final Rect mDownBounds = new Rect();
    public final Rect mDragCornerSize = new Rect();
    public final Rect mTmpTopLeftCorner = new Rect();
    public final Rect mTmpTopRightCorner = new Rect();
    public final Rect mTmpBottomLeftCorner = new Rect();
    public final Rect mTmpBottomRightCorner = new Rect();
    public final Rect mDisplayBounds = new Rect();
    public boolean mOngoingPinchToResize = false;
    public float mAngle = 0.0f;
    public int mFirstIndex = -1;
    public int mSecondIndex = -1;
    public final PipPinchResizingAlgorithm mPinchResizingAlgorithm = new PipPinchResizingAlgorithm();

    /* loaded from: classes.dex */
    public class PipResizeInputEventReceiver extends BatchedInputEventReceiver {
        public PipResizeInputEventReceiver(InputChannel inputChannel, Looper looper) {
            super(inputChannel, looper, Choreographer.getSfInstance());
        }

        public final void onInputEvent(InputEvent inputEvent) {
            PipResizeGestureHandler.this.onInputEvent(inputEvent);
            finishInputEvent(inputEvent, true);
        }
    }

    public final void finishResize() {
        int i;
        if (!this.mLastResizeBounds.isEmpty()) {
            StatusBar$$ExternalSyntheticLambda33 statusBar$$ExternalSyntheticLambda33 = new StatusBar$$ExternalSyntheticLambda33(this, 4);
            if (this.mOngoingPinchToResize) {
                Rect rect = new Rect(this.mLastResizeBounds);
                if (this.mLastResizeBounds.width() >= this.mMaxSize.x * 0.9f || this.mLastResizeBounds.height() >= this.mMaxSize.y * 0.9f) {
                    Rect rect2 = this.mLastResizeBounds;
                    Point point = this.mMaxSize;
                    int i2 = point.x;
                    int i3 = point.y;
                    int centerX = rect2.centerX() - (i2 / 2);
                    int centerY = rect2.centerY() - (i3 / 2);
                    rect2.set(centerX, centerY, i2 + centerX, i3 + centerY);
                }
                Rect rect3 = this.mLastResizeBounds;
                int i4 = rect3.left;
                PipBoundsAlgorithm pipBoundsAlgorithm = this.mPipBoundsAlgorithm;
                Objects.requireNonNull(pipBoundsAlgorithm);
                Rect movementBounds = pipBoundsAlgorithm.getMovementBounds(rect3, true);
                if (Math.abs(i4 - movementBounds.left) < Math.abs(movementBounds.right - i4)) {
                    i = movementBounds.left;
                } else {
                    i = movementBounds.right;
                }
                Rect rect4 = this.mLastResizeBounds;
                rect4.offsetTo(i, rect4.top);
                PipBoundsAlgorithm pipBoundsAlgorithm2 = this.mPipBoundsAlgorithm;
                Rect rect5 = this.mLastResizeBounds;
                Objects.requireNonNull(pipBoundsAlgorithm2);
                PipSnapAlgorithm pipSnapAlgorithm = pipBoundsAlgorithm2.mSnapAlgorithm;
                Objects.requireNonNull(pipSnapAlgorithm);
                float snapFraction = pipSnapAlgorithm.getSnapFraction(rect5, movementBounds, 0);
                PipBoundsAlgorithm pipBoundsAlgorithm3 = this.mPipBoundsAlgorithm;
                Rect rect6 = this.mLastResizeBounds;
                Objects.requireNonNull(pipBoundsAlgorithm3);
                Rect movementBounds2 = pipBoundsAlgorithm3.getMovementBounds(rect6, true);
                Objects.requireNonNull(pipBoundsAlgorithm3.mSnapAlgorithm);
                PipSnapAlgorithm.applySnapFraction(rect6, movementBounds2, snapFraction);
                PipTaskOrganizer pipTaskOrganizer = this.mPipTaskOrganizer;
                Rect rect7 = this.mLastResizeBounds;
                float f = this.mAngle;
                Objects.requireNonNull(pipTaskOrganizer);
                if (pipTaskOrganizer.mWaitForFixedRotation) {
                    Log.d("PipTaskOrganizer", "skip scheduleAnimateResizePip, entering pip deferred");
                } else {
                    pipTaskOrganizer.scheduleAnimateResizePip(rect, rect7, f, null, 6, 250, statusBar$$ExternalSyntheticLambda33);
                }
            } else {
                this.mPipTaskOrganizer.scheduleFinishResizePip(this.mLastResizeBounds, 7, statusBar$$ExternalSyntheticLambda33);
            }
            float width = (this.mLastResizeBounds.width() / this.mMinSize.x) / 2.0f;
            PipDismissTargetHandler pipDismissTargetHandler = this.mPipDismissTargetHandler;
            Objects.requireNonNull(pipDismissTargetHandler);
            pipDismissTargetHandler.mMagneticFieldRadiusPercent = width;
            MagnetizedObject.MagneticTarget magneticTarget = pipDismissTargetHandler.mMagneticTarget;
            Objects.requireNonNull(magneticTarget);
            magneticTarget.magneticFieldRadiusPx = (int) (width * pipDismissTargetHandler.mTargetSize * 1.25f);
            this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_RESIZE);
            return;
        }
        this.mCtrlType = 0;
        this.mAngle = 0.0f;
        this.mOngoingPinchToResize = false;
        this.mAllowGesture = false;
        this.mThresholdCrossed = false;
    }

    public final boolean isWithinDragResizeRegion(int i, int i2) {
        if (!this.mEnableDragCornerResize) {
            return false;
        }
        Rect bounds = this.mPipBoundsState.getBounds();
        Rect rect = this.mDragCornerSize;
        int i3 = this.mDelta;
        rect.set(0, 0, i3, i3);
        this.mTmpTopLeftCorner.set(this.mDragCornerSize);
        this.mTmpTopRightCorner.set(this.mDragCornerSize);
        this.mTmpBottomLeftCorner.set(this.mDragCornerSize);
        this.mTmpBottomRightCorner.set(this.mDragCornerSize);
        Rect rect2 = this.mTmpTopLeftCorner;
        int i4 = bounds.left;
        int i5 = this.mDelta;
        rect2.offset(i4 - (i5 / 2), bounds.top - (i5 / 2));
        Rect rect3 = this.mTmpTopRightCorner;
        int i6 = bounds.right;
        int i7 = this.mDelta;
        rect3.offset(i6 - (i7 / 2), bounds.top - (i7 / 2));
        Rect rect4 = this.mTmpBottomLeftCorner;
        int i8 = bounds.left;
        int i9 = this.mDelta;
        rect4.offset(i8 - (i9 / 2), bounds.bottom - (i9 / 2));
        Rect rect5 = this.mTmpBottomRightCorner;
        int i10 = bounds.right;
        int i11 = this.mDelta;
        rect5.offset(i10 - (i11 / 2), bounds.bottom - (i11 / 2));
        this.mTmpRegion.setEmpty();
        this.mTmpRegion.op(this.mTmpTopLeftCorner, Region.Op.UNION);
        this.mTmpRegion.op(this.mTmpTopRightCorner, Region.Op.UNION);
        this.mTmpRegion.op(this.mTmpBottomLeftCorner, Region.Op.UNION);
        this.mTmpRegion.op(this.mTmpBottomRightCorner, Region.Op.UNION);
        return this.mTmpRegion.contains(i, i2);
    }

    public void onInputEvent(InputEvent inputEvent) {
        boolean z;
        if ((this.mEnableDragCornerResize || this.mEnablePinchResize) && !this.mPipBoundsState.isStashed() && (inputEvent instanceof MotionEvent)) {
            MotionEvent motionEvent = (MotionEvent) inputEvent;
            int actionMasked = motionEvent.getActionMasked();
            Rect bounds = this.mPipBoundsState.getBounds();
            if ((actionMasked == 1 || actionMasked == 3) && !bounds.contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY()) && this.mPhonePipMenuController.isMenuVisible()) {
                this.mPhonePipMenuController.hideMenu();
            }
            if (this.mEnablePinchResize && this.mOngoingPinchToResize) {
                onPinchResize(motionEvent);
            } else if (this.mEnableDragCornerResize) {
                int actionMasked2 = motionEvent.getActionMasked();
                float x = motionEvent.getX();
                float y = motionEvent.getY() - this.mOhmOffset;
                boolean z2 = false;
                if (actionMasked2 == 0) {
                    this.mLastResizeBounds.setEmpty();
                    if (this.mIsSysUiStateValid && isWithinDragResizeRegion((int) x, (int) y)) {
                        z2 = true;
                    }
                    this.mAllowGesture = z2;
                    if (z2) {
                        int i = (int) x;
                        int i2 = (int) y;
                        Rect bounds2 = this.mPipBoundsState.getBounds();
                        Rect apply = this.mMovementBoundsSupplier.apply(bounds2);
                        this.mDisplayBounds.set(apply.left, apply.top, bounds2.width() + apply.right, bounds2.height() + apply.bottom);
                        if (this.mTmpTopLeftCorner.contains(i, i2)) {
                            int i3 = bounds2.top;
                            Rect rect = this.mDisplayBounds;
                            if (!(i3 == rect.top || bounds2.left == rect.left)) {
                                this.mCtrlType = this.mCtrlType | 1 | 4;
                            }
                        }
                        if (this.mTmpTopRightCorner.contains(i, i2)) {
                            int i4 = bounds2.top;
                            Rect rect2 = this.mDisplayBounds;
                            if (!(i4 == rect2.top || bounds2.right == rect2.right)) {
                                this.mCtrlType = this.mCtrlType | 2 | 4;
                            }
                        }
                        if (this.mTmpBottomRightCorner.contains(i, i2)) {
                            int i5 = bounds2.bottom;
                            Rect rect3 = this.mDisplayBounds;
                            if (!(i5 == rect3.bottom || bounds2.right == rect3.right)) {
                                this.mCtrlType = 2 | this.mCtrlType | 8;
                            }
                        }
                        if (this.mTmpBottomLeftCorner.contains(i, i2)) {
                            int i6 = bounds2.bottom;
                            Rect rect4 = this.mDisplayBounds;
                            if (!(i6 == rect4.bottom || bounds2.left == rect4.left)) {
                                this.mCtrlType = this.mCtrlType | 1 | 8;
                            }
                        }
                        this.mDownPoint.set(x, y);
                        this.mDownBounds.set(this.mPipBoundsState.getBounds());
                    }
                } else if (this.mAllowGesture) {
                    if (actionMasked2 != 1) {
                        if (actionMasked2 == 2) {
                            if (!this.mThresholdCrossed) {
                                PointF pointF = this.mDownPoint;
                                if (Math.hypot(x - pointF.x, y - pointF.y) > this.mTouchSlop) {
                                    this.mThresholdCrossed = true;
                                    this.mDownPoint.set(x, y);
                                    this.mInputMonitor.pilferPointers();
                                }
                            }
                            if (this.mThresholdCrossed) {
                                if (this.mPhonePipMenuController.isMenuVisible()) {
                                    this.mPhonePipMenuController.hideMenu(0);
                                }
                                Rect bounds3 = this.mPipBoundsState.getBounds();
                                Rect rect5 = this.mLastResizeBounds;
                                PointF pointF2 = this.mDownPoint;
                                float f = pointF2.x;
                                float f2 = pointF2.y;
                                int i7 = this.mCtrlType;
                                Point point = this.mMinSize;
                                int i8 = point.x;
                                int i9 = point.y;
                                Point point2 = this.mMaxSize;
                                if (this.mDownBounds.width() > this.mDownBounds.height()) {
                                    z = true;
                                } else {
                                    z = false;
                                }
                                rect5.set(TaskResizingAlgorithm.resizeDrag(x, y, f, f2, bounds3, i7, i8, i9, point2, true, z));
                                PipBoundsAlgorithm pipBoundsAlgorithm = this.mPipBoundsAlgorithm;
                                Rect rect6 = this.mLastResizeBounds;
                                PipBoundsState pipBoundsState = this.mPipBoundsState;
                                Objects.requireNonNull(pipBoundsState);
                                pipBoundsAlgorithm.transformBoundsToAspectRatio(rect6, pipBoundsState.mAspectRatio, false, true);
                                PipTaskOrganizer pipTaskOrganizer = this.mPipTaskOrganizer;
                                Rect rect7 = this.mDownBounds;
                                Rect rect8 = this.mLastResizeBounds;
                                Objects.requireNonNull(pipTaskOrganizer);
                                pipTaskOrganizer.scheduleUserResizePip(rect7, rect8, 0.0f, null);
                                PipBoundsState pipBoundsState2 = this.mPipBoundsState;
                                Objects.requireNonNull(pipBoundsState2);
                                pipBoundsState2.mHasUserResizedPip = true;
                                return;
                            }
                            return;
                        } else if (actionMasked2 != 3) {
                            if (actionMasked2 == 5) {
                                this.mAllowGesture = false;
                                return;
                            }
                            return;
                        }
                    }
                    finishResize();
                }
            }
        }
    }

    public void pilferPointers() {
        this.mInputMonitor.pilferPointers();
    }

    public final void reloadResources() {
        Resources resources = this.mContext.getResources();
        this.mDelta = resources.getDimensionPixelSize(2131166798);
        this.mEnableDragCornerResize = resources.getBoolean(2131034150);
        this.mTouchSlop = ViewConfiguration.get(this.mContext).getScaledTouchSlop();
    }

    public final void updateIsEnabled() {
        boolean z = this.mIsAttached;
        if (z != this.mIsEnabled) {
            this.mIsEnabled = z;
            PipResizeInputEventReceiver pipResizeInputEventReceiver = this.mInputEventReceiver;
            if (pipResizeInputEventReceiver != null) {
                pipResizeInputEventReceiver.dispose();
                this.mInputEventReceiver = null;
            }
            InputMonitor inputMonitor = this.mInputMonitor;
            if (inputMonitor != null) {
                inputMonitor.dispose();
                this.mInputMonitor = null;
            }
            if (this.mIsEnabled) {
                this.mInputMonitor = InputManager.getInstance().monitorGestureInput("pip-resize", this.mDisplayId);
                try {
                    this.mMainExecutor.executeBlocking$1(new ScreenDecorations$$ExternalSyntheticLambda1(this, 10));
                } catch (InterruptedException e) {
                    throw new RuntimeException("Failed to create input event receiver", e);
                }
            }
        }
    }

    public void updateMaxSize(int i, int i2) {
        this.mMaxSize.set(i, i2);
    }

    public void updateMinSize(int i, int i2) {
        this.mMinSize.set(i, i2);
    }

    public PipResizeGestureHandler(Context context, PipBoundsAlgorithm pipBoundsAlgorithm, PipBoundsState pipBoundsState, PipMotionHelper pipMotionHelper, PipTaskOrganizer pipTaskOrganizer, PipDismissTargetHandler pipDismissTargetHandler, PeopleSpaceUtils$$ExternalSyntheticLambda2 peopleSpaceUtils$$ExternalSyntheticLambda2, StatusBar$$ExternalSyntheticLambda19 statusBar$$ExternalSyntheticLambda19, PipUiEventLogger pipUiEventLogger, PhonePipMenuController phonePipMenuController, ShellExecutor shellExecutor) {
        this.mContext = context;
        this.mDisplayId = context.getDisplayId();
        this.mMainExecutor = shellExecutor;
        this.mPipBoundsAlgorithm = pipBoundsAlgorithm;
        this.mPipBoundsState = pipBoundsState;
        this.mMotionHelper = pipMotionHelper;
        this.mPipTaskOrganizer = pipTaskOrganizer;
        this.mPipDismissTargetHandler = pipDismissTargetHandler;
        this.mMovementBoundsSupplier = peopleSpaceUtils$$ExternalSyntheticLambda2;
        this.mUpdateMovementBoundsRunnable = statusBar$$ExternalSyntheticLambda19;
        this.mPhonePipMenuController = phonePipMenuController;
        this.mPipUiEventLogger = pipUiEventLogger;
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x00f0, code lost:
        if (((float) java.lang.Math.hypot(r0.x - r15.x, r0.y - r15.y)) > r14.mTouchSlop) goto L_0x00f2;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onPinchResize(android.view.MotionEvent r15) {
        /*
            Method dump skipped, instructions count: 636
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.pip.phone.PipResizeGestureHandler.onPinchResize(android.view.MotionEvent):void");
    }

    public Rect getLastResizeBounds() {
        return this.mLastResizeBounds;
    }
}
