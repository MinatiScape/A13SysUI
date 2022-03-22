package com.airbnb.lottie.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.view.Choreographer;
import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieComposition;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class LottieValueAnimator extends BaseLottieAnimator implements Choreographer.FrameCallback {
    public LottieComposition composition;
    public float speed = 1.0f;
    public boolean speedReversedForRepeatMode = false;
    public long lastFrameTimeNs = 0;
    public float frame = 0.0f;
    public int repeatCount = 0;
    public float minFrame = -2.14748365E9f;
    public float maxFrame = 2.14748365E9f;
    public boolean running = false;

    @Override // android.animation.ValueAnimator, android.animation.Animator
    public final void cancel() {
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((Animator.AnimatorListener) it.next()).onAnimationCancel(this);
        }
        removeFrameCallback(true);
    }

    @Override // android.view.Choreographer.FrameCallback
    public final void doFrame(long j) {
        boolean z;
        float f;
        float f2;
        if (this.running) {
            removeFrameCallback(false);
            Choreographer.getInstance().postFrameCallback(this);
        }
        LottieComposition lottieComposition = this.composition;
        if (lottieComposition != null && this.running) {
            long j2 = this.lastFrameTimeNs;
            long j3 = 0;
            if (j2 != 0) {
                j3 = j - j2;
            }
            float abs = ((float) j3) / ((1.0E9f / lottieComposition.frameRate) / Math.abs(this.speed));
            float f3 = this.frame;
            if (isReversed()) {
                abs = -abs;
            }
            float f4 = f3 + abs;
            this.frame = f4;
            float minFrame = getMinFrame();
            float maxFrame = getMaxFrame();
            PointF pointF = MiscUtils.pathFromDataCurrentPoint;
            if (f4 < minFrame || f4 > maxFrame) {
                z = false;
            } else {
                z = true;
            }
            boolean z2 = !z;
            this.frame = MiscUtils.clamp(this.frame, getMinFrame(), getMaxFrame());
            this.lastFrameTimeNs = j;
            Iterator it = this.updateListeners.iterator();
            while (it.hasNext()) {
                ((ValueAnimator.AnimatorUpdateListener) it.next()).onAnimationUpdate(this);
            }
            if (z2) {
                if (getRepeatCount() == -1 || this.repeatCount < getRepeatCount()) {
                    Iterator it2 = this.listeners.iterator();
                    while (it2.hasNext()) {
                        ((Animator.AnimatorListener) it2.next()).onAnimationRepeat(this);
                    }
                    this.repeatCount++;
                    if (getRepeatMode() == 2) {
                        this.speedReversedForRepeatMode = !this.speedReversedForRepeatMode;
                        this.speed = -this.speed;
                    } else {
                        if (isReversed()) {
                            f = getMaxFrame();
                        } else {
                            f = getMinFrame();
                        }
                        this.frame = f;
                    }
                    this.lastFrameTimeNs = j;
                } else {
                    if (this.speed < 0.0f) {
                        f2 = getMinFrame();
                    } else {
                        f2 = getMaxFrame();
                    }
                    this.frame = f2;
                    removeFrameCallback(true);
                    boolean isReversed = isReversed();
                    Iterator it3 = this.listeners.iterator();
                    while (it3.hasNext()) {
                        ((Animator.AnimatorListener) it3.next()).onAnimationEnd(this, isReversed);
                    }
                }
            }
            if (this.composition != null) {
                float f5 = this.frame;
                if (f5 < this.minFrame || f5 > this.maxFrame) {
                    throw new IllegalStateException(String.format("Frame must be [%f,%f]. It is %f", Float.valueOf(this.minFrame), Float.valueOf(this.maxFrame), Float.valueOf(this.frame)));
                }
            }
            L.endSection();
        }
    }

    @Override // android.animation.ValueAnimator
    public final float getAnimatedFraction() {
        float minFrame;
        float maxFrame;
        float minFrame2;
        if (this.composition == null) {
            return 0.0f;
        }
        if (isReversed()) {
            minFrame = getMaxFrame() - this.frame;
            maxFrame = getMaxFrame();
            minFrame2 = getMinFrame();
        } else {
            minFrame = this.frame - getMinFrame();
            maxFrame = getMaxFrame();
            minFrame2 = getMinFrame();
        }
        return minFrame / (maxFrame - minFrame2);
    }

    public final float getAnimatedValueAbsolute() {
        LottieComposition lottieComposition = this.composition;
        if (lottieComposition == null) {
            return 0.0f;
        }
        float f = this.frame;
        Objects.requireNonNull(lottieComposition);
        float f2 = f - lottieComposition.startFrame;
        LottieComposition lottieComposition2 = this.composition;
        Objects.requireNonNull(lottieComposition2);
        float f3 = lottieComposition2.endFrame;
        LottieComposition lottieComposition3 = this.composition;
        Objects.requireNonNull(lottieComposition3);
        return f2 / (f3 - lottieComposition3.startFrame);
    }

    @Override // android.animation.ValueAnimator, android.animation.Animator
    public final long getDuration() {
        LottieComposition lottieComposition = this.composition;
        if (lottieComposition == null) {
            return 0L;
        }
        return lottieComposition.getDuration();
    }

    public final float getMaxFrame() {
        LottieComposition lottieComposition = this.composition;
        if (lottieComposition == null) {
            return 0.0f;
        }
        float f = this.maxFrame;
        if (f != 2.14748365E9f) {
            return f;
        }
        Objects.requireNonNull(lottieComposition);
        return lottieComposition.endFrame;
    }

    public final float getMinFrame() {
        LottieComposition lottieComposition = this.composition;
        if (lottieComposition == null) {
            return 0.0f;
        }
        float f = this.minFrame;
        if (f != -2.14748365E9f) {
            return f;
        }
        Objects.requireNonNull(lottieComposition);
        return lottieComposition.startFrame;
    }

    public final boolean isReversed() {
        if (this.speed < 0.0f) {
            return true;
        }
        return false;
    }

    public final void setFrame(float f) {
        if (this.frame != f) {
            this.frame = MiscUtils.clamp(f, getMinFrame(), getMaxFrame());
            this.lastFrameTimeNs = 0L;
            Iterator it = this.updateListeners.iterator();
            while (it.hasNext()) {
                ((ValueAnimator.AnimatorUpdateListener) it.next()).onAnimationUpdate(this);
            }
        }
    }

    public final void setMinAndMaxFrames(float f, float f2) {
        float f3;
        float f4;
        if (f <= f2) {
            LottieComposition lottieComposition = this.composition;
            if (lottieComposition == null) {
                f3 = -3.4028235E38f;
            } else {
                Objects.requireNonNull(lottieComposition);
                f3 = lottieComposition.startFrame;
            }
            LottieComposition lottieComposition2 = this.composition;
            if (lottieComposition2 == null) {
                f4 = Float.MAX_VALUE;
            } else {
                Objects.requireNonNull(lottieComposition2);
                f4 = lottieComposition2.endFrame;
            }
            this.minFrame = MiscUtils.clamp(f, f3, f4);
            this.maxFrame = MiscUtils.clamp(f2, f3, f4);
            setFrame((int) MiscUtils.clamp(this.frame, f, f2));
            return;
        }
        throw new IllegalArgumentException(String.format("minFrame (%s) must be <= maxFrame (%s)", Float.valueOf(f), Float.valueOf(f2)));
    }

    @Override // android.animation.ValueAnimator
    public final Object getAnimatedValue() {
        return Float.valueOf(getAnimatedValueAbsolute());
    }

    public final void removeFrameCallback(boolean z) {
        Choreographer.getInstance().removeFrameCallback(this);
        if (z) {
            this.running = false;
        }
    }

    @Override // android.animation.ValueAnimator
    public final void setRepeatMode(int i) {
        super.setRepeatMode(i);
        if (i != 2 && this.speedReversedForRepeatMode) {
            this.speedReversedForRepeatMode = false;
            this.speed = -this.speed;
        }
    }

    @Override // android.animation.ValueAnimator, android.animation.Animator
    public final boolean isRunning() {
        return this.running;
    }
}
