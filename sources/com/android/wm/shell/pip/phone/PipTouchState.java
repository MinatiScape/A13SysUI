package com.android.wm.shell.pip.phone;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda0;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda3;
import com.android.wm.shell.common.ShellExecutor;
/* loaded from: classes.dex */
public final class PipTouchState {
    @VisibleForTesting
    public static final long DOUBLE_TAP_TIMEOUT = 200;
    public int mActivePointerId;
    public final Runnable mDoubleTapTimeoutCallback;
    public final Runnable mHoverExitTimeoutCallback;
    public final ShellExecutor mMainExecutor;
    public VelocityTracker mVelocityTracker;
    public final ViewConfiguration mViewConfig;
    public long mDownTouchTime = 0;
    public long mLastDownTouchTime = 0;
    public long mUpTouchTime = 0;
    public final PointF mDownTouch = new PointF();
    public final PointF mDownDelta = new PointF();
    public final PointF mLastTouch = new PointF();
    public final PointF mLastDelta = new PointF();
    public final PointF mVelocity = new PointF();
    public boolean mAllowTouches = true;
    public boolean mIsUserInteracting = false;
    public boolean mIsDoubleTap = false;
    public boolean mIsWaitingForDoubleTap = false;
    public boolean mIsDragging = false;
    public boolean mPreviouslyDragging = false;
    public boolean mStartedDragging = false;
    public boolean mAllowDraggingOffscreen = false;
    public int mLastTouchDisplayId = -1;

    public final void reset() {
        this.mAllowDraggingOffscreen = false;
        this.mIsDragging = false;
        this.mStartedDragging = false;
        this.mIsUserInteracting = false;
        this.mLastTouchDisplayId = -1;
    }

    public final void addMovementToVelocityTracker(MotionEvent motionEvent) {
        if (this.mVelocityTracker != null) {
            float rawX = motionEvent.getRawX() - motionEvent.getX();
            float rawY = motionEvent.getRawY() - motionEvent.getY();
            motionEvent.offsetLocation(rawX, rawY);
            this.mVelocityTracker.addMovement(motionEvent);
            motionEvent.offsetLocation(-rawX, -rawY);
        }
    }

    @VisibleForTesting
    public long getDoubleTapTimeoutCallbackDelay() {
        if (this.mIsWaitingForDoubleTap) {
            return Math.max(0L, 200 - (this.mUpTouchTime - this.mDownTouchTime));
        }
        return -1L;
    }

    @VisibleForTesting
    public void scheduleHoverExitTimeoutCallback() {
        this.mMainExecutor.removeCallbacks(this.mHoverExitTimeoutCallback);
        this.mMainExecutor.executeDelayed(this.mHoverExitTimeoutCallback, 50L);
    }

    public PipTouchState(ViewConfiguration viewConfiguration, CarrierTextManager$$ExternalSyntheticLambda0 carrierTextManager$$ExternalSyntheticLambda0, TaskView$$ExternalSyntheticLambda3 taskView$$ExternalSyntheticLambda3, ShellExecutor shellExecutor) {
        this.mViewConfig = viewConfiguration;
        this.mDoubleTapTimeoutCallback = carrierTextManager$$ExternalSyntheticLambda0;
        this.mHoverExitTimeoutCallback = taskView$$ExternalSyntheticLambda3;
        this.mMainExecutor = shellExecutor;
    }

    public final void onTouchEvent(MotionEvent motionEvent) {
        Object[] objArr;
        this.mLastTouchDisplayId = motionEvent.getDisplayId();
        int actionMasked = motionEvent.getActionMasked();
        boolean z = false;
        int i = 0;
        z = false;
        z = false;
        boolean z2 = true;
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked != 2) {
                    if (actionMasked != 3) {
                        if (actionMasked != 6) {
                            if (actionMasked == 11) {
                                this.mMainExecutor.removeCallbacks(this.mHoverExitTimeoutCallback);
                                return;
                            }
                            return;
                        } else if (this.mIsUserInteracting) {
                            addMovementToVelocityTracker(motionEvent);
                            int actionIndex = motionEvent.getActionIndex();
                            if (motionEvent.getPointerId(actionIndex) == this.mActivePointerId) {
                                if (actionIndex == 0) {
                                    i = 1;
                                }
                                this.mActivePointerId = motionEvent.getPointerId(i);
                                this.mLastTouch.set(motionEvent.getRawX(i), motionEvent.getRawY(i));
                                return;
                            }
                            return;
                        } else {
                            return;
                        }
                    }
                } else if (this.mIsUserInteracting) {
                    addMovementToVelocityTracker(motionEvent);
                    int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (findPointerIndex == -1) {
                        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Invalid active pointer id on MOVE: ");
                        m.append(this.mActivePointerId);
                        Log.e("PipTouchState", m.toString());
                        return;
                    }
                    float rawX = motionEvent.getRawX(findPointerIndex);
                    float rawY = motionEvent.getRawY(findPointerIndex);
                    PointF pointF = this.mLastDelta;
                    PointF pointF2 = this.mLastTouch;
                    pointF.set(rawX - pointF2.x, rawY - pointF2.y);
                    PointF pointF3 = this.mDownDelta;
                    PointF pointF4 = this.mDownTouch;
                    pointF3.set(rawX - pointF4.x, rawY - pointF4.y);
                    if (this.mDownDelta.length() > this.mViewConfig.getScaledTouchSlop()) {
                        objArr = 1;
                    } else {
                        objArr = null;
                    }
                    if (this.mIsDragging) {
                        this.mStartedDragging = false;
                    } else if (objArr != null) {
                        this.mIsDragging = true;
                        this.mStartedDragging = true;
                    }
                    this.mLastTouch.set(rawX, rawY);
                    return;
                } else {
                    return;
                }
            } else if (this.mIsUserInteracting) {
                addMovementToVelocityTracker(motionEvent);
                this.mVelocityTracker.computeCurrentVelocity(1000, this.mViewConfig.getScaledMaximumFlingVelocity());
                this.mVelocity.set(this.mVelocityTracker.getXVelocity(), this.mVelocityTracker.getYVelocity());
                int findPointerIndex2 = motionEvent.findPointerIndex(this.mActivePointerId);
                if (findPointerIndex2 == -1) {
                    StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Invalid active pointer id on UP: ");
                    m2.append(this.mActivePointerId);
                    Log.e("PipTouchState", m2.toString());
                    return;
                }
                this.mUpTouchTime = motionEvent.getEventTime();
                this.mLastTouch.set(motionEvent.getRawX(findPointerIndex2), motionEvent.getRawY(findPointerIndex2));
                boolean z3 = this.mIsDragging;
                this.mPreviouslyDragging = z3;
                if (!this.mIsDoubleTap && !z3 && this.mUpTouchTime - this.mDownTouchTime < 200) {
                    z = true;
                }
                this.mIsWaitingForDoubleTap = z;
            } else {
                return;
            }
            VelocityTracker velocityTracker = this.mVelocityTracker;
            if (velocityTracker != null) {
                velocityTracker.recycle();
                this.mVelocityTracker = null;
            }
        } else if (this.mAllowTouches) {
            VelocityTracker velocityTracker2 = this.mVelocityTracker;
            if (velocityTracker2 == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            } else {
                velocityTracker2.clear();
            }
            addMovementToVelocityTracker(motionEvent);
            this.mActivePointerId = motionEvent.getPointerId(0);
            this.mLastTouch.set(motionEvent.getRawX(), motionEvent.getRawY());
            this.mDownTouch.set(this.mLastTouch);
            this.mAllowDraggingOffscreen = true;
            this.mIsUserInteracting = true;
            long eventTime = motionEvent.getEventTime();
            this.mDownTouchTime = eventTime;
            if (this.mPreviouslyDragging || eventTime - this.mLastDownTouchTime >= 200) {
                z2 = false;
            }
            this.mIsDoubleTap = z2;
            this.mIsWaitingForDoubleTap = false;
            this.mIsDragging = false;
            this.mLastDownTouchTime = eventTime;
            Runnable runnable = this.mDoubleTapTimeoutCallback;
            if (runnable != null) {
                this.mMainExecutor.removeCallbacks(runnable);
            }
        }
    }
}
