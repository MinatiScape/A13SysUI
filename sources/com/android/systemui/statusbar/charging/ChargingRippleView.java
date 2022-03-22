package com.android.systemui.statusbar.charging;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RuntimeShader;
import android.util.AttributeSet;
import android.view.View;
import java.util.Objects;
/* compiled from: ChargingRippleView.kt */
/* loaded from: classes.dex */
public final class ChargingRippleView extends View {
    public float radius;
    public boolean rippleInProgress;
    public final Paint ripplePaint;
    public final RippleShader rippleShader;
    public PointF origin = new PointF();
    public long duration = 1750;

    @Override // android.view.View
    public final void onAttachedToWindow() {
        RippleShader rippleShader = this.rippleShader;
        float f = getResources().getDisplayMetrics().density;
        Objects.requireNonNull(rippleShader);
        rippleShader.setFloatUniform("in_pixelDensity", f);
        super.onAttachedToWindow();
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        RippleShader rippleShader = this.rippleShader;
        float f = getResources().getDisplayMetrics().density;
        Objects.requireNonNull(rippleShader);
        rippleShader.setFloatUniform("in_pixelDensity", f);
        super.onConfigurationChanged(configuration);
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        if (canvas != null && canvas.isHardwareAccelerated()) {
            float f = 1;
            RippleShader rippleShader = this.rippleShader;
            Objects.requireNonNull(rippleShader);
            RippleShader rippleShader2 = this.rippleShader;
            Objects.requireNonNull(rippleShader2);
            float f2 = (f - rippleShader2.progress) * (f - rippleShader.progress);
            RippleShader rippleShader3 = this.rippleShader;
            Objects.requireNonNull(rippleShader3);
            float f3 = (f - ((f - rippleShader3.progress) * f2)) * this.radius * 2;
            PointF pointF = this.origin;
            canvas.drawCircle(pointF.x, pointF.y, f3, this.ripplePaint);
        }
    }

    public final void startRipple(final Runnable runnable) {
        if (!this.rippleInProgress) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat.setDuration(this.duration);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.charging.ChargingRippleView$startRipple$1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    long currentPlayTime = valueAnimator.getCurrentPlayTime();
                    Object animatedValue = valueAnimator.getAnimatedValue();
                    Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                    float floatValue = ((Float) animatedValue).floatValue();
                    ChargingRippleView.this.rippleShader.setProgress(floatValue);
                    RippleShader rippleShader = ChargingRippleView.this.rippleShader;
                    float f = 1 - floatValue;
                    Objects.requireNonNull(rippleShader);
                    float f2 = 75;
                    rippleShader.setFloatUniform("in_distort_radial", rippleShader.progress * f2 * f);
                    rippleShader.setFloatUniform("in_distort_xy", f2 * f);
                    RippleShader rippleShader2 = ChargingRippleView.this.rippleShader;
                    Objects.requireNonNull(rippleShader2);
                    rippleShader2.setFloatUniform("in_time", (float) currentPlayTime);
                    ChargingRippleView.this.invalidate();
                }
            });
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.charging.ChargingRippleView$startRipple$2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    ChargingRippleView chargingRippleView = ChargingRippleView.this;
                    Objects.requireNonNull(chargingRippleView);
                    chargingRippleView.rippleInProgress = false;
                    Runnable runnable2 = runnable;
                    if (runnable2 != null) {
                        runnable2.run();
                    }
                }
            });
            ofFloat.start();
            this.rippleInProgress = true;
        }
    }

    public ChargingRippleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        RuntimeShader rippleShader = new RippleShader();
        this.rippleShader = rippleShader;
        Paint paint = new Paint();
        this.ripplePaint = paint;
        rippleShader.setColor(-1);
        rippleShader.setProgress(0.0f);
        rippleShader.setFloatUniform("in_sparkle_strength", 0.3f);
        paint.setShader(rippleShader);
    }
}
