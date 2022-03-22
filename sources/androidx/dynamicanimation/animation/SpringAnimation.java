package androidx.dynamicanimation.animation;

import android.util.AndroidRuntimeException;
import androidx.dynamicanimation.animation.DynamicAnimation;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SpringAnimation extends DynamicAnimation<SpringAnimation> {
    public SpringForce mSpring = null;
    public float mPendingPosition = Float.MAX_VALUE;
    public boolean mEndRequested = false;

    public final void animateToFinalPosition(float f) {
        if (this.mRunning) {
            this.mPendingPosition = f;
            return;
        }
        if (this.mSpring == null) {
            this.mSpring = new SpringForce(f);
        }
        SpringForce springForce = this.mSpring;
        Objects.requireNonNull(springForce);
        springForce.mFinalPosition = f;
        start();
    }

    public final void skipToEnd() {
        boolean z;
        if (this.mSpring.mDampingRatio > 0.0d) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            AnimationHandler animationHandler = getAnimationHandler();
            Objects.requireNonNull(animationHandler);
            if (!animationHandler.mScheduler.isCurrentThread()) {
                throw new AndroidRuntimeException("Animations may only be started on the same thread as the animation handler");
            } else if (this.mRunning) {
                this.mEndRequested = true;
            }
        } else {
            throw new UnsupportedOperationException("Spring animations can only come to an end when there is damping");
        }
    }

    @Override // androidx.dynamicanimation.animation.DynamicAnimation
    public final void start() {
        SpringForce springForce = this.mSpring;
        if (springForce != null) {
            double d = (float) springForce.mFinalPosition;
            if (d > this.mMaxValue) {
                throw new UnsupportedOperationException("Final position of the spring cannot be greater than the max value.");
            } else if (d >= this.mMinValue) {
                Objects.requireNonNull(springForce);
                double abs = Math.abs(this.mMinVisibleChange * 0.75f);
                springForce.mValueThreshold = abs;
                springForce.mVelocityThreshold = abs * 62.5d;
                super.start();
            } else {
                throw new UnsupportedOperationException("Final position of the spring cannot be less than the min value.");
            }
        } else {
            throw new UnsupportedOperationException("Incomplete SpringAnimation: Either final position or a spring force needs to be set.");
        }
    }

    @Override // androidx.dynamicanimation.animation.DynamicAnimation
    public final boolean updateValueAndVelocity(long j) {
        float min;
        boolean z;
        if (this.mEndRequested) {
            float f = this.mPendingPosition;
            if (f != Float.MAX_VALUE) {
                SpringForce springForce = this.mSpring;
                Objects.requireNonNull(springForce);
                springForce.mFinalPosition = f;
                this.mPendingPosition = Float.MAX_VALUE;
            }
            SpringForce springForce2 = this.mSpring;
            Objects.requireNonNull(springForce2);
            this.mValue = (float) springForce2.mFinalPosition;
            this.mVelocity = 0.0f;
            this.mEndRequested = false;
            return true;
        }
        if (this.mPendingPosition != Float.MAX_VALUE) {
            long j2 = j / 2;
            DynamicAnimation.MassState updateValues = this.mSpring.updateValues(this.mValue, this.mVelocity, j2);
            SpringForce springForce3 = this.mSpring;
            float f2 = this.mPendingPosition;
            Objects.requireNonNull(springForce3);
            springForce3.mFinalPosition = f2;
            this.mPendingPosition = Float.MAX_VALUE;
            DynamicAnimation.MassState updateValues2 = this.mSpring.updateValues(updateValues.mValue, updateValues.mVelocity, j2);
            this.mValue = updateValues2.mValue;
            this.mVelocity = updateValues2.mVelocity;
        } else {
            DynamicAnimation.MassState updateValues3 = this.mSpring.updateValues(this.mValue, this.mVelocity, j);
            this.mValue = updateValues3.mValue;
            this.mVelocity = updateValues3.mVelocity;
        }
        float max = Math.max(this.mValue, this.mMinValue);
        this.mValue = max;
        this.mValue = Math.min(max, this.mMaxValue);
        float f3 = this.mVelocity;
        SpringForce springForce4 = this.mSpring;
        Objects.requireNonNull(springForce4);
        if (Math.abs(f3) >= springForce4.mVelocityThreshold || Math.abs(min - ((float) springForce4.mFinalPosition)) >= springForce4.mValueThreshold) {
            z = false;
        } else {
            z = true;
        }
        if (!z) {
            return false;
        }
        SpringForce springForce5 = this.mSpring;
        Objects.requireNonNull(springForce5);
        this.mValue = (float) springForce5.mFinalPosition;
        this.mVelocity = 0.0f;
        return true;
    }

    public <K> SpringAnimation(K k, FloatPropertyCompat<K> floatPropertyCompat) {
        super(k, floatPropertyCompat);
    }

    @Override // androidx.dynamicanimation.animation.DynamicAnimation
    public final void cancel() {
        super.cancel();
        float f = this.mPendingPosition;
        if (f != Float.MAX_VALUE) {
            SpringForce springForce = this.mSpring;
            if (springForce == null) {
                this.mSpring = new SpringForce(f);
            } else {
                Objects.requireNonNull(springForce);
                springForce.mFinalPosition = f;
            }
            this.mPendingPosition = Float.MAX_VALUE;
        }
    }
}
