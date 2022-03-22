package com.android.systemui.statusbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import java.util.function.Consumer;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: LightRevealScrim.kt */
/* loaded from: classes.dex */
public final class LightRevealScrim extends View {
    public final Paint gradientPaint;
    public boolean isScrimOpaque;
    public Consumer<Boolean> isScrimOpaqueChangedListener;
    public float revealGradientEndColorAlpha;
    public float revealGradientHeight;
    public float revealGradientWidth;
    public float startColorAlpha;
    public float revealAmount = 1.0f;
    public LightRevealEffect revealEffect = LiftReveal.INSTANCE;
    public PointF revealGradientCenter = new PointF();
    public int revealGradientEndColor = -16777216;
    public float interpolatedRevealAmount = 1.0f;
    public final Matrix shaderGradientMatrix = new Matrix();

    public final void setRevealGradientBounds(float f, float f2, float f3, float f4) {
        float f5 = f3 - f;
        this.revealGradientWidth = f5;
        float f6 = f4 - f2;
        this.revealGradientHeight = f6;
        PointF pointF = this.revealGradientCenter;
        pointF.x = (f5 / 2.0f) + f;
        pointF.y = (f6 / 2.0f) + f2;
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        boolean z;
        if (canvas != null && this.revealGradientWidth > 0.0f && this.revealGradientHeight > 0.0f) {
            if (this.revealAmount == 0.0f) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                float f = this.startColorAlpha;
                if (f > 0.0f) {
                    int i = this.revealGradientEndColor;
                    canvas.drawColor(Color.argb((int) (f * 255), Color.red(i), Color.green(i), Color.blue(i)));
                }
                Matrix matrix = this.shaderGradientMatrix;
                matrix.setScale(this.revealGradientWidth, this.revealGradientHeight, 0.0f, 0.0f);
                PointF pointF = this.revealGradientCenter;
                matrix.postTranslate(pointF.x, pointF.y);
                this.gradientPaint.getShader().setLocalMatrix(matrix);
                canvas.drawRect(0.0f, 0.0f, getWidth(), getHeight(), this.gradientPaint);
                return;
            }
        }
        if (this.revealAmount < 1.0f && canvas != null) {
            canvas.drawColor(this.revealGradientEndColor);
        }
    }

    public final void setRevealAmount(float f) {
        boolean z;
        if (this.revealAmount == f) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            this.revealAmount = f;
            this.revealEffect.setRevealAmountOnScrim(f, this);
            updateScrimOpaque();
            invalidate();
        }
    }

    public final void setRevealEffect(LightRevealEffect lightRevealEffect) {
        if (!Intrinsics.areEqual(this.revealEffect, lightRevealEffect)) {
            this.revealEffect = lightRevealEffect;
            lightRevealEffect.setRevealAmountOnScrim(this.revealAmount, this);
            invalidate();
        }
    }

    public final void setRevealGradientEndColorAlpha(float f) {
        boolean z;
        if (this.revealGradientEndColorAlpha == f) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            this.revealGradientEndColorAlpha = f;
            Paint paint = this.gradientPaint;
            int i = this.revealGradientEndColor;
            paint.setColorFilter(new PorterDuffColorFilter(Color.argb((int) (this.revealGradientEndColorAlpha * 255), Color.red(i), Color.green(i), Color.blue(i)), PorterDuff.Mode.MULTIPLY));
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0021, code lost:
        if (getVisibility() == 0) goto L_0x0025;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateScrimOpaque() {
        /*
            r4 = this;
            float r0 = r4.revealAmount
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            r1 = 1
            r2 = 0
            if (r0 != 0) goto L_0x000b
            r0 = r1
            goto L_0x000c
        L_0x000b:
            r0 = r2
        L_0x000c:
            if (r0 == 0) goto L_0x0024
            float r0 = r4.getAlpha()
            r3 = 1065353216(0x3f800000, float:1.0)
            int r0 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r0 != 0) goto L_0x001a
            r0 = r1
            goto L_0x001b
        L_0x001a:
            r0 = r2
        L_0x001b:
            if (r0 == 0) goto L_0x0024
            int r0 = r4.getVisibility()
            if (r0 != 0) goto L_0x0024
            goto L_0x0025
        L_0x0024:
            r1 = r2
        L_0x0025:
            boolean r0 = r4.isScrimOpaque
            if (r0 == r1) goto L_0x0038
            r4.isScrimOpaque = r1
            java.util.function.Consumer<java.lang.Boolean> r4 = r4.isScrimOpaqueChangedListener
            if (r4 == 0) goto L_0x0030
            goto L_0x0031
        L_0x0030:
            r4 = 0
        L_0x0031:
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r1)
            r4.accept(r0)
        L_0x0038:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.LightRevealScrim.updateScrimOpaque():void");
    }

    public LightRevealScrim(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Paint paint = new Paint();
        paint.setShader(new RadialGradient(0.0f, 0.0f, 1.0f, new int[]{0, -1}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        this.gradientPaint = paint;
        this.revealEffect.setRevealAmountOnScrim(this.revealAmount, this);
        int i = this.revealGradientEndColor;
        paint.setColorFilter(new PorterDuffColorFilter(Color.argb((int) (this.revealGradientEndColorAlpha * 255), Color.red(i), Color.green(i), Color.blue(i)), PorterDuff.Mode.MULTIPLY));
        invalidate();
    }

    @Override // android.view.View
    public final void setAlpha(float f) {
        super.setAlpha(f);
        updateScrimOpaque();
    }

    @Override // android.view.View
    public final void setVisibility(int i) {
        super.setVisibility(i);
        updateScrimOpaque();
    }
}
