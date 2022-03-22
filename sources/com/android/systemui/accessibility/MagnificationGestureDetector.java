package com.android.systemui.accessibility;

import android.content.Context;
import android.graphics.PointF;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import com.android.systemui.wmshell.WMShell$7$$ExternalSyntheticLambda1;
/* loaded from: classes.dex */
public final class MagnificationGestureDetector {
    public final Handler mHandler;
    public final OnGestureListener mOnGestureListener;
    public int mTouchSlopSquare;
    public final PointF mPointerDown = new PointF();
    public final PointF mPointerLocation = new PointF(Float.NaN, Float.NaN);
    public boolean mDetectSingleTap = true;
    public boolean mDraggingDetected = false;
    public final WMShell$7$$ExternalSyntheticLambda1 mCancelTapGestureRunnable = new WMShell$7$$ExternalSyntheticLambda1(this, 1);

    /* loaded from: classes.dex */
    public interface OnGestureListener {
        void onDrag(float f, float f2);

        boolean onFinish();

        void onSingleTap();

        void onStart();
    }

    public final void stopSingleTapDetectionIfNeeded(float f, float f2) {
        boolean z;
        if (!this.mDraggingDetected) {
            PointF pointF = this.mPointerDown;
            if (Float.isNaN(pointF.x) || Float.isNaN(pointF.y)) {
                z = false;
            } else {
                z = true;
            }
            if (z) {
                PointF pointF2 = this.mPointerDown;
                int i = (int) (pointF2.x - f);
                int i2 = (int) (pointF2.y - f2);
                if ((i2 * i2) + (i * i) > this.mTouchSlopSquare) {
                    this.mDraggingDetected = true;
                    this.mHandler.removeCallbacks(this.mCancelTapGestureRunnable);
                    this.mDetectSingleTap = false;
                }
            }
        }
    }

    public MagnificationGestureDetector(Context context, Handler handler, OnGestureListener onGestureListener) {
        int scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.mTouchSlopSquare = scaledTouchSlop * scaledTouchSlop;
        this.mHandler = handler;
        this.mOnGestureListener = onGestureListener;
    }

    public final boolean onTouch(MotionEvent motionEvent) {
        boolean z;
        boolean z2;
        float rawX = motionEvent.getRawX();
        float rawY = motionEvent.getRawY();
        int actionMasked = motionEvent.getActionMasked();
        boolean z3 = true;
        if (actionMasked != 0) {
            if (actionMasked == 1) {
                stopSingleTapDetectionIfNeeded(rawX, rawY);
                if (this.mDetectSingleTap) {
                    this.mOnGestureListener.onSingleTap();
                    z = true;
                    boolean onFinish = z | this.mOnGestureListener.onFinish();
                    PointF pointF = this.mPointerDown;
                    pointF.x = Float.NaN;
                    pointF.y = Float.NaN;
                    PointF pointF2 = this.mPointerLocation;
                    pointF2.x = Float.NaN;
                    pointF2.y = Float.NaN;
                    this.mHandler.removeCallbacks(this.mCancelTapGestureRunnable);
                    this.mDetectSingleTap = true;
                    this.mDraggingDetected = false;
                    return onFinish;
                }
            } else if (actionMasked == 2) {
                stopSingleTapDetectionIfNeeded(rawX, rawY);
                if (!this.mDraggingDetected) {
                    z3 = false;
                } else {
                    PointF pointF3 = this.mPointerLocation;
                    if (Float.isNaN(pointF3.x) || Float.isNaN(pointF3.y)) {
                        z2 = false;
                    } else {
                        z2 = true;
                    }
                    if (!z2) {
                        this.mPointerLocation.set(this.mPointerDown);
                    }
                    PointF pointF4 = this.mPointerLocation;
                    pointF4.set(rawX, rawY);
                    this.mOnGestureListener.onDrag(rawX - pointF4.x, rawY - pointF4.y);
                }
            } else if (actionMasked != 3) {
                if (actionMasked != 5) {
                    return false;
                }
                this.mHandler.removeCallbacks(this.mCancelTapGestureRunnable);
                this.mDetectSingleTap = false;
                return false;
            }
            z = false;
            boolean onFinish2 = z | this.mOnGestureListener.onFinish();
            PointF pointF5 = this.mPointerDown;
            pointF5.x = Float.NaN;
            pointF5.y = Float.NaN;
            PointF pointF22 = this.mPointerLocation;
            pointF22.x = Float.NaN;
            pointF22.y = Float.NaN;
            this.mHandler.removeCallbacks(this.mCancelTapGestureRunnable);
            this.mDetectSingleTap = true;
            this.mDraggingDetected = false;
            return onFinish2;
        }
        this.mPointerDown.set(rawX, rawY);
        this.mHandler.postAtTime(this.mCancelTapGestureRunnable, motionEvent.getDownTime() + ViewConfiguration.getLongPressTimeout());
        this.mOnGestureListener.onStart();
        return false | z3;
    }
}
