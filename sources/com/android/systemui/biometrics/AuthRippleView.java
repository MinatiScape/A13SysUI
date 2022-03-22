package com.android.systemui.biometrics;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RuntimeShader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.PathInterpolator;
import com.android.internal.graphics.ColorUtils;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.charging.DwellRippleShader;
import com.android.systemui.statusbar.charging.RippleShader;
import java.util.Objects;
/* compiled from: AuthRippleView.kt */
/* loaded from: classes.dex */
public final class AuthRippleView extends View {
    public long alphaInDuration;
    public boolean drawDwell;
    public boolean drawRipple;
    public final Paint dwellPaint;
    public AnimatorSet dwellPulseOutAnimator;
    public float dwellRadius;
    public final DwellRippleShader dwellShader;
    public float radius;
    public AnimatorSet retractAnimator;
    public final Paint ripplePaint;
    public final RippleShader rippleShader;
    public boolean unlockedRippleInProgress;
    public final PathInterpolator retractInterpolator = new PathInterpolator(0.05f, 0.93f, 0.1f, 1.0f);
    public final long dwellPulseDuration = 100;
    public final long dwellExpandDuration = 1900;
    public int lockScreenColorVal = -1;
    public final long retractDuration = 400;
    public PointF dwellOrigin = new PointF();
    public PointF origin = new PointF();

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        if (this.drawDwell) {
            float f = 1;
            DwellRippleShader dwellRippleShader = this.dwellShader;
            Objects.requireNonNull(dwellRippleShader);
            DwellRippleShader dwellRippleShader2 = this.dwellShader;
            Objects.requireNonNull(dwellRippleShader2);
            float f2 = (f - dwellRippleShader2.progress) * (f - dwellRippleShader.progress);
            DwellRippleShader dwellRippleShader3 = this.dwellShader;
            Objects.requireNonNull(dwellRippleShader3);
            float f3 = (f - ((f - dwellRippleShader3.progress) * f2)) * this.dwellRadius * 2.0f;
            if (canvas != null) {
                PointF pointF = this.dwellOrigin;
                canvas.drawCircle(pointF.x, pointF.y, f3, this.dwellPaint);
            }
        }
        if (this.drawRipple) {
            float f4 = 1;
            RippleShader rippleShader = this.rippleShader;
            Objects.requireNonNull(rippleShader);
            RippleShader rippleShader2 = this.rippleShader;
            Objects.requireNonNull(rippleShader2);
            float f5 = (f4 - rippleShader2.progress) * (f4 - rippleShader.progress);
            RippleShader rippleShader3 = this.rippleShader;
            Objects.requireNonNull(rippleShader3);
            float f6 = (f4 - ((f4 - rippleShader3.progress) * f5)) * this.radius * 2.0f;
            if (canvas != null) {
                PointF pointF2 = this.origin;
                canvas.drawCircle(pointF2.x, pointF2.y, f6, this.ripplePaint);
            }
        }
    }

    public final void retractRipple() {
        boolean z;
        boolean z2;
        AnimatorSet animatorSet = this.retractAnimator;
        if (animatorSet != null && animatorSet.isRunning()) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            AnimatorSet animatorSet2 = this.dwellPulseOutAnimator;
            if (animatorSet2 != null && animatorSet2.isRunning()) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z2) {
                DwellRippleShader dwellRippleShader = this.dwellShader;
                Objects.requireNonNull(dwellRippleShader);
                ValueAnimator ofFloat = ValueAnimator.ofFloat(dwellRippleShader.progress, 0.0f);
                ofFloat.setInterpolator(this.retractInterpolator);
                ofFloat.setDuration(this.retractDuration);
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.AuthRippleView$retractRipple$retractRippleAnimator$1$1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        long currentPlayTime = valueAnimator.getCurrentPlayTime();
                        DwellRippleShader dwellRippleShader2 = AuthRippleView.this.dwellShader;
                        Object animatedValue = valueAnimator.getAnimatedValue();
                        Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                        dwellRippleShader2.setProgress(((Float) animatedValue).floatValue());
                        AuthRippleView.this.dwellShader.setTime((float) currentPlayTime);
                        AuthRippleView.this.invalidate();
                    }
                });
                ValueAnimator ofInt = ValueAnimator.ofInt(255, 0);
                ofInt.setInterpolator(Interpolators.LINEAR);
                ofInt.setDuration(this.retractDuration);
                ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.AuthRippleView$retractRipple$retractAlphaAnimator$1$1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        DwellRippleShader dwellRippleShader2 = AuthRippleView.this.dwellShader;
                        Objects.requireNonNull(dwellRippleShader2);
                        int i = dwellRippleShader2.color;
                        Object animatedValue = valueAnimator.getAnimatedValue();
                        Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Int");
                        int alphaComponent = ColorUtils.setAlphaComponent(i, ((Integer) animatedValue).intValue());
                        dwellRippleShader2.color = alphaComponent;
                        dwellRippleShader2.setColorUniform("in_color", alphaComponent);
                        AuthRippleView.this.invalidate();
                    }
                });
                AnimatorSet animatorSet3 = new AnimatorSet();
                animatorSet3.playTogether(ofFloat, ofInt);
                animatorSet3.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.biometrics.AuthRippleView$retractRipple$1$1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator) {
                        AuthRippleView authRippleView = AuthRippleView.this;
                        authRippleView.drawDwell = false;
                        DwellRippleShader dwellRippleShader2 = authRippleView.dwellShader;
                        Objects.requireNonNull(dwellRippleShader2);
                        int alphaComponent = ColorUtils.setAlphaComponent(dwellRippleShader2.color, 255);
                        dwellRippleShader2.color = alphaComponent;
                        dwellRippleShader2.setColorUniform("in_color", alphaComponent);
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationStart(Animator animator) {
                        AnimatorSet animatorSet4 = AuthRippleView.this.dwellPulseOutAnimator;
                        if (animatorSet4 != null) {
                            animatorSet4.cancel();
                        }
                        AuthRippleView.this.drawDwell = true;
                    }
                });
                animatorSet3.start();
                this.retractAnimator = animatorSet3;
            }
        }
    }

    public final void setFingerprintSensorLocation(PointF pointF, float f) {
        RippleShader rippleShader = this.rippleShader;
        Objects.requireNonNull(rippleShader);
        rippleShader.setFloatUniform("in_origin", pointF.x, pointF.y);
        this.origin = pointF;
        float f2 = pointF.x;
        int i = 0;
        float[] fArr = {pointF.y, getWidth() - pointF.x, getHeight() - pointF.y};
        while (i < 3) {
            float f3 = fArr[i];
            i++;
            f2 = Math.max(f2, f3);
        }
        RippleShader rippleShader2 = this.rippleShader;
        Objects.requireNonNull(rippleShader2);
        rippleShader2.radius = f2;
        rippleShader2.setFloatUniform("in_maxRadius", f2);
        this.radius = f2;
        DwellRippleShader dwellRippleShader = this.dwellShader;
        Objects.requireNonNull(dwellRippleShader);
        dwellRippleShader.setFloatUniform("in_origin", pointF.x, pointF.y);
        this.dwellOrigin = pointF;
        float f4 = f * 1.5f;
        DwellRippleShader dwellRippleShader2 = this.dwellShader;
        Objects.requireNonNull(dwellRippleShader2);
        dwellRippleShader2.maxRadius = f4;
        this.dwellRadius = f4;
    }

    public final void setSensorLocation(PointF pointF) {
        RippleShader rippleShader = this.rippleShader;
        Objects.requireNonNull(rippleShader);
        rippleShader.setFloatUniform("in_origin", pointF.x, pointF.y);
        this.origin = pointF;
        float f = pointF.x;
        int i = 0;
        float[] fArr = {pointF.y, getWidth() - pointF.x, getHeight() - pointF.y};
        while (i < 3) {
            float f2 = fArr[i];
            i++;
            f = Math.max(f, f2);
        }
        RippleShader rippleShader2 = this.rippleShader;
        Objects.requireNonNull(rippleShader2);
        rippleShader2.radius = f;
        rippleShader2.setFloatUniform("in_maxRadius", f);
        this.radius = f;
    }

    public AuthRippleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        RuntimeShader dwellRippleShader = new DwellRippleShader();
        this.dwellShader = dwellRippleShader;
        Paint paint = new Paint();
        this.dwellPaint = paint;
        RuntimeShader rippleShader = new RippleShader();
        this.rippleShader = rippleShader;
        Paint paint2 = new Paint();
        this.ripplePaint = paint2;
        rippleShader.setColor(-1);
        rippleShader.setProgress(0.0f);
        rippleShader.setFloatUniform("in_sparkle_strength", 0.4f);
        paint2.setShader(rippleShader);
        dwellRippleShader.color = -1;
        dwellRippleShader.setColorUniform("in_color", -1);
        dwellRippleShader.setProgress(0.0f);
        dwellRippleShader.setFloatUniform("in_distortion_strength", 0.4f);
        paint.setShader(dwellRippleShader);
        setVisibility(8);
    }
}
