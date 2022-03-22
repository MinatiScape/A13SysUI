package com.android.systemui.accessibility;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.RemoteException;
import android.util.Log;
import android.view.accessibility.IRemoteMagnificationAnimationCallback;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
import com.android.internal.annotations.VisibleForTesting;
/* loaded from: classes.dex */
public final class WindowMagnificationAnimationController implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
    public static final boolean DEBUG = Log.isLoggable("WindowMagnificationAnimationController", 3);
    public IRemoteMagnificationAnimationCallback mAnimationCallback;
    public final Context mContext;
    public WindowMagnificationController mController;
    public final ValueAnimator mValueAnimator;
    public final AnimationSpec mStartSpec = new AnimationSpec();
    public final AnimationSpec mEndSpec = new AnimationSpec();
    public float mMagnificationFrameOffsetRatioX = 0.0f;
    public float mMagnificationFrameOffsetRatioY = 0.0f;
    public boolean mEndAnimationCanceled = false;
    public int mState = 0;

    /* loaded from: classes.dex */
    public static class AnimationSpec {
        public float mScale = Float.NaN;
        public float mCenterX = Float.NaN;
        public float mCenterY = Float.NaN;

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || AnimationSpec.class != obj.getClass()) {
                return false;
            }
            AnimationSpec animationSpec = (AnimationSpec) obj;
            return this.mScale == animationSpec.mScale && this.mCenterX == animationSpec.mCenterX && this.mCenterY == animationSpec.mCenterY;
        }

        public final int hashCode() {
            int i;
            int i2;
            float f = this.mScale;
            int i3 = 0;
            if (f != 0.0f) {
                i = Float.floatToIntBits(f);
            } else {
                i = 0;
            }
            int i4 = i * 31;
            float f2 = this.mCenterX;
            if (f2 != 0.0f) {
                i2 = Float.floatToIntBits(f2);
            } else {
                i2 = 0;
            }
            int i5 = (i4 + i2) * 31;
            float f3 = this.mCenterY;
            if (f3 != 0.0f) {
                i3 = Float.floatToIntBits(f3);
            }
            return i5 + i3;
        }

        public final void set(float f, float f2, float f3) {
            this.mScale = f;
            this.mCenterX = f2;
            this.mCenterY = f3;
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("AnimationSpec{mScale=");
            m.append(this.mScale);
            m.append(", mCenterX=");
            m.append(this.mCenterX);
            m.append(", mCenterY=");
            m.append(this.mCenterY);
            m.append('}');
            return m.toString();
        }
    }

    @Override // android.animation.Animator.AnimatorListener
    public final void onAnimationCancel(Animator animator) {
        this.mEndAnimationCanceled = true;
    }

    @Override // android.animation.Animator.AnimatorListener
    public final void onAnimationEnd(Animator animator) {
    }

    @Override // android.animation.Animator.AnimatorListener
    public final void onAnimationRepeat(Animator animator) {
    }

    @Override // android.animation.Animator.AnimatorListener
    public final void onAnimationStart(Animator animator) {
        this.mEndAnimationCanceled = false;
    }

    @Override // android.animation.Animator.AnimatorListener
    public final void onAnimationEnd(Animator animator, boolean z) {
        WindowMagnificationController windowMagnificationController;
        float f;
        if (!this.mEndAnimationCanceled && (windowMagnificationController = this.mController) != null) {
            if (windowMagnificationController.isWindowVisible()) {
                f = windowMagnificationController.mScale;
            } else {
                f = Float.NaN;
            }
            if (Float.isNaN(f)) {
                setState(0);
            } else {
                setState(1);
            }
            sendAnimationCallback(true);
        }
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        if (this.mController != null) {
            float animatedFraction = valueAnimator.getAnimatedFraction();
            AnimationSpec animationSpec = this.mStartSpec;
            float f = animationSpec.mScale;
            AnimationSpec animationSpec2 = this.mEndSpec;
            float m = MotionController$$ExternalSyntheticOutline0.m(animationSpec2.mScale, f, animatedFraction, f);
            float f2 = animationSpec.mCenterX;
            float m2 = MotionController$$ExternalSyntheticOutline0.m(animationSpec2.mCenterX, f2, animatedFraction, f2);
            float f3 = animationSpec.mCenterY;
            this.mController.enableWindowMagnificationInternal(m, m2, MotionController$$ExternalSyntheticOutline0.m(animationSpec2.mCenterY, f3, animatedFraction, f3), this.mMagnificationFrameOffsetRatioX, this.mMagnificationFrameOffsetRatioY);
        }
    }

    public final void sendAnimationCallback(boolean z) {
        IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback = this.mAnimationCallback;
        if (iRemoteMagnificationAnimationCallback != null) {
            try {
                iRemoteMagnificationAnimationCallback.onResult(z);
                if (DEBUG) {
                    Log.d("WindowMagnificationAnimationController", "sendAnimationCallback success = " + z);
                }
            } catch (RemoteException e) {
                Log.w("WindowMagnificationAnimationController", "sendAnimationCallback failed : " + e);
            }
            this.mAnimationCallback = null;
        }
    }

    public final void setState(int i) {
        if (DEBUG) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("setState from ");
            m.append(this.mState);
            m.append(" to ");
            m.append(i);
            Log.d("WindowMagnificationAnimationController", m.toString());
        }
        this.mState = i;
    }

    @VisibleForTesting
    public WindowMagnificationAnimationController(Context context, ValueAnimator valueAnimator) {
        this.mContext = context;
        this.mValueAnimator = valueAnimator;
        valueAnimator.addUpdateListener(this);
        valueAnimator.addListener(this);
    }
}
