package androidx.constraintlayout.motion.utils;

import androidx.constraintlayout.motion.widget.MotionInterpolator;
/* loaded from: classes.dex */
public final class StopLogic extends MotionInterpolator {
    public boolean mBackwards = false;
    public float mLastPosition;
    public int mNumberOfStages;
    public float mStage1Duration;
    public float mStage1EndPosition;
    public float mStage1Velocity;
    public float mStage2Duration;
    public float mStage2EndPosition;
    public float mStage2Velocity;
    public float mStage3Duration;
    public float mStage3EndPosition;
    public float mStage3Velocity;
    public float mStartPosition;

    public final void setup(float f, float f2, float f3, float f4, float f5) {
        if (f == 0.0f) {
            f = 1.0E-4f;
        }
        this.mStage1Velocity = f;
        float f6 = f / f3;
        float f7 = (f6 * f) / 2.0f;
        if (f < 0.0f) {
            float sqrt = (float) Math.sqrt((f2 - ((((-f) / f3) * f) / 2.0f)) * f3);
            if (sqrt < f4) {
                this.mNumberOfStages = 2;
                this.mStage1Velocity = f;
                this.mStage2Velocity = sqrt;
                this.mStage3Velocity = 0.0f;
                float f8 = (sqrt - f) / f3;
                this.mStage1Duration = f8;
                this.mStage2Duration = sqrt / f3;
                this.mStage1EndPosition = ((f + sqrt) * f8) / 2.0f;
                this.mStage2EndPosition = f2;
                this.mStage3EndPosition = f2;
                return;
            }
            this.mNumberOfStages = 3;
            this.mStage1Velocity = f;
            this.mStage2Velocity = f4;
            this.mStage3Velocity = f4;
            float f9 = (f4 - f) / f3;
            this.mStage1Duration = f9;
            float f10 = f4 / f3;
            this.mStage3Duration = f10;
            float f11 = ((f + f4) * f9) / 2.0f;
            float f12 = (f10 * f4) / 2.0f;
            this.mStage2Duration = ((f2 - f11) - f12) / f4;
            this.mStage1EndPosition = f11;
            this.mStage2EndPosition = f2 - f12;
            this.mStage3EndPosition = f2;
        } else if (f7 >= f2) {
            this.mNumberOfStages = 1;
            this.mStage1Velocity = f;
            this.mStage2Velocity = 0.0f;
            this.mStage1EndPosition = f2;
            this.mStage1Duration = (2.0f * f2) / f;
        } else {
            float f13 = f2 - f7;
            float f14 = f13 / f;
            if (f14 + f6 < f5) {
                this.mNumberOfStages = 2;
                this.mStage1Velocity = f;
                this.mStage2Velocity = f;
                this.mStage3Velocity = 0.0f;
                this.mStage1EndPosition = f13;
                this.mStage2EndPosition = f2;
                this.mStage1Duration = f14;
                this.mStage2Duration = f6;
                return;
            }
            float sqrt2 = (float) Math.sqrt(((f * f) / 2.0f) + (f3 * f2));
            float f15 = (sqrt2 - f) / f3;
            this.mStage1Duration = f15;
            float f16 = sqrt2 / f3;
            this.mStage2Duration = f16;
            if (sqrt2 < f4) {
                this.mNumberOfStages = 2;
                this.mStage1Velocity = f;
                this.mStage2Velocity = sqrt2;
                this.mStage3Velocity = 0.0f;
                this.mStage1Duration = f15;
                this.mStage2Duration = f16;
                this.mStage1EndPosition = ((f + sqrt2) * f15) / 2.0f;
                this.mStage2EndPosition = f2;
                return;
            }
            this.mNumberOfStages = 3;
            this.mStage1Velocity = f;
            this.mStage2Velocity = f4;
            this.mStage3Velocity = f4;
            float f17 = (f4 - f) / f3;
            this.mStage1Duration = f17;
            float f18 = f4 / f3;
            this.mStage3Duration = f18;
            float f19 = ((f + f4) * f17) / 2.0f;
            float f20 = (f18 * f4) / 2.0f;
            this.mStage2Duration = ((f2 - f19) - f20) / f4;
            this.mStage1EndPosition = f19;
            this.mStage2EndPosition = f2 - f20;
            this.mStage3EndPosition = f2;
        }
    }

    public final void config(float f, float f2, float f3, float f4, float f5, float f6) {
        boolean z;
        this.mStartPosition = f;
        if (f > f2) {
            z = true;
        } else {
            z = false;
        }
        this.mBackwards = z;
        if (z) {
            setup(-f3, f - f2, f5, f6, f4);
        } else {
            setup(f3, f2 - f, f5, f6, f4);
        }
    }

    @Override // android.animation.TimeInterpolator
    public final float getInterpolation(float f) {
        float f2;
        float f3 = this.mStage1Duration;
        if (f <= f3) {
            float f4 = this.mStage1Velocity;
            f2 = ((((this.mStage2Velocity - f4) * f) * f) / (f3 * 2.0f)) + (f4 * f);
        } else {
            int i = this.mNumberOfStages;
            if (i == 1) {
                f2 = this.mStage1EndPosition;
            } else {
                float f5 = f - f3;
                float f6 = this.mStage2Duration;
                if (f5 < f6) {
                    float f7 = this.mStage1EndPosition;
                    float f8 = this.mStage2Velocity;
                    f2 = ((((this.mStage3Velocity - f8) * f5) * f5) / (f6 * 2.0f)) + (f8 * f5) + f7;
                } else if (i == 2) {
                    f2 = this.mStage2EndPosition;
                } else {
                    float f9 = f5 - f6;
                    float f10 = this.mStage3Duration;
                    if (f9 < f10) {
                        float f11 = this.mStage2EndPosition;
                        float f12 = this.mStage3Velocity * f9;
                        f2 = (f11 + f12) - ((f12 * f9) / (f10 * 2.0f));
                    } else {
                        f2 = this.mStage3EndPosition;
                    }
                }
            }
        }
        this.mLastPosition = f;
        boolean z = this.mBackwards;
        float f13 = this.mStartPosition;
        if (z) {
            return f13 - f2;
        }
        return f13 + f2;
    }

    @Override // androidx.constraintlayout.motion.widget.MotionInterpolator
    public final float getVelocity$1() {
        float f;
        float f2;
        float f3 = this.mLastPosition;
        float f4 = this.mStage1Duration;
        if (f3 <= f4) {
            f = this.mStage1Velocity;
            f2 = this.mStage2Velocity;
        } else {
            int i = this.mNumberOfStages;
            if (i == 1) {
                return 0.0f;
            }
            f3 -= f4;
            f4 = this.mStage2Duration;
            if (f3 < f4) {
                f = this.mStage2Velocity;
                f2 = this.mStage3Velocity;
            } else if (i == 2) {
                return this.mStage2EndPosition;
            } else {
                float f5 = f3 - f4;
                float f6 = this.mStage3Duration;
                if (f5 >= f6) {
                    return this.mStage3EndPosition;
                }
                float f7 = this.mStage3Velocity;
                return f7 - ((f5 * f7) / f6);
            }
        }
        return (((f2 - f) * f3) / f4) + f;
    }
}
