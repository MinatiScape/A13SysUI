package com.airbnb.lottie.value;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.PointF;
import android.view.animation.Interpolator;
import com.airbnb.lottie.LottieComposition;
import java.util.Objects;
/* loaded from: classes.dex */
public class Keyframe<T> {
    public final LottieComposition composition;
    public Float endFrame;
    public float endProgress;
    public T endValue;
    public float endValueFloat;
    public int endValueInt;
    public final Interpolator interpolator;
    public PointF pathCp1;
    public PointF pathCp2;
    public final float startFrame;
    public float startProgress;
    public final T startValue;
    public float startValueFloat;
    public int startValueInt;

    public Keyframe(LottieComposition lottieComposition, T t, T t2, Interpolator interpolator, float f, Float f2) {
        this.startValueFloat = -3987645.8f;
        this.endValueFloat = -3987645.8f;
        this.startValueInt = 784923401;
        this.endValueInt = 784923401;
        this.startProgress = Float.MIN_VALUE;
        this.endProgress = Float.MIN_VALUE;
        this.pathCp1 = null;
        this.pathCp2 = null;
        this.composition = lottieComposition;
        this.startValue = t;
        this.endValue = t2;
        this.interpolator = interpolator;
        this.startFrame = f;
        this.endFrame = f2;
    }

    public final float getEndProgress() {
        if (this.composition == null) {
            return 1.0f;
        }
        if (this.endProgress == Float.MIN_VALUE) {
            if (this.endFrame == null) {
                this.endProgress = 1.0f;
            } else {
                float startProgress = getStartProgress();
                float floatValue = this.endFrame.floatValue() - this.startFrame;
                LottieComposition lottieComposition = this.composition;
                Objects.requireNonNull(lottieComposition);
                this.endProgress = (floatValue / (lottieComposition.endFrame - lottieComposition.startFrame)) + startProgress;
            }
        }
        return this.endProgress;
    }

    public final float getStartProgress() {
        LottieComposition lottieComposition = this.composition;
        if (lottieComposition == null) {
            return 0.0f;
        }
        if (this.startProgress == Float.MIN_VALUE) {
            float f = this.startFrame;
            Objects.requireNonNull(lottieComposition);
            float f2 = f - lottieComposition.startFrame;
            LottieComposition lottieComposition2 = this.composition;
            Objects.requireNonNull(lottieComposition2);
            this.startProgress = f2 / (lottieComposition2.endFrame - lottieComposition2.startFrame);
        }
        return this.startProgress;
    }

    public final boolean isStatic() {
        if (this.interpolator == null) {
            return true;
        }
        return false;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Keyframe{startValue=");
        m.append(this.startValue);
        m.append(", endValue=");
        m.append(this.endValue);
        m.append(", startFrame=");
        m.append(this.startFrame);
        m.append(", endFrame=");
        m.append(this.endFrame);
        m.append(", interpolator=");
        m.append(this.interpolator);
        m.append('}');
        return m.toString();
    }

    public Keyframe(T t) {
        this.startValueFloat = -3987645.8f;
        this.endValueFloat = -3987645.8f;
        this.startValueInt = 784923401;
        this.endValueInt = 784923401;
        this.startProgress = Float.MIN_VALUE;
        this.endProgress = Float.MIN_VALUE;
        this.pathCp1 = null;
        this.pathCp2 = null;
        this.composition = null;
        this.startValue = t;
        this.endValue = t;
        this.interpolator = null;
        this.startFrame = Float.MIN_VALUE;
        this.endFrame = Float.valueOf(Float.MAX_VALUE);
    }
}
