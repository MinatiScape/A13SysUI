package com.airbnb.lottie.animation.content;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.LPaint;
import com.airbnb.lottie.animation.keyframe.ColorKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.Objects;
/* loaded from: classes.dex */
public final class StrokeContent extends BaseStrokeContent {
    public final ColorKeyframeAnimation colorAnimation;
    public ValueCallbackKeyframeAnimation colorFilterAnimation;
    public final boolean hidden;
    public final BaseLayer layer;
    public final String name;

    @Override // com.airbnb.lottie.animation.content.BaseStrokeContent, com.airbnb.lottie.animation.content.DrawingContent
    public final void draw(Canvas canvas, Matrix matrix, int i) {
        if (!this.hidden) {
            LPaint lPaint = this.paint;
            ColorKeyframeAnimation colorKeyframeAnimation = this.colorAnimation;
            Objects.requireNonNull(colorKeyframeAnimation);
            lPaint.setColor(colorKeyframeAnimation.getIntValue(colorKeyframeAnimation.getCurrentKeyframe(), colorKeyframeAnimation.getInterpolatedCurrentKeyframeProgress()));
            ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation = this.colorFilterAnimation;
            if (valueCallbackKeyframeAnimation != null) {
                this.paint.setColorFilter((ColorFilter) valueCallbackKeyframeAnimation.getValue());
            }
            super.draw(canvas, matrix, i);
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public StrokeContent(com.airbnb.lottie.LottieDrawable r13, com.airbnb.lottie.model.layer.BaseLayer r14, com.airbnb.lottie.model.content.ShapeStroke r15) {
        /*
            r12 = this;
            java.util.Objects.requireNonNull(r15)
            com.airbnb.lottie.model.content.ShapeStroke$LineCapType r0 = r15.capType
            java.util.Objects.requireNonNull(r0)
            int r0 = r0.ordinal()
            r1 = 1
            if (r0 == 0) goto L_0x0017
            if (r0 == r1) goto L_0x0014
            android.graphics.Paint$Cap r0 = android.graphics.Paint.Cap.SQUARE
            goto L_0x0019
        L_0x0014:
            android.graphics.Paint$Cap r0 = android.graphics.Paint.Cap.ROUND
            goto L_0x0019
        L_0x0017:
            android.graphics.Paint$Cap r0 = android.graphics.Paint.Cap.BUTT
        L_0x0019:
            r5 = r0
            com.airbnb.lottie.model.content.ShapeStroke$LineJoinType r0 = r15.joinType
            java.util.Objects.requireNonNull(r0)
            int r0 = r0.ordinal()
            if (r0 == 0) goto L_0x0032
            if (r0 == r1) goto L_0x002f
            r1 = 2
            if (r0 == r1) goto L_0x002c
            r0 = 0
            goto L_0x0034
        L_0x002c:
            android.graphics.Paint$Join r0 = android.graphics.Paint.Join.BEVEL
            goto L_0x0034
        L_0x002f:
            android.graphics.Paint$Join r0 = android.graphics.Paint.Join.ROUND
            goto L_0x0034
        L_0x0032:
            android.graphics.Paint$Join r0 = android.graphics.Paint.Join.MITER
        L_0x0034:
            r6 = r0
            float r7 = r15.miterLimit
            com.airbnb.lottie.model.animatable.AnimatableIntegerValue r8 = r15.opacity
            com.airbnb.lottie.model.animatable.AnimatableFloatValue r9 = r15.width
            java.util.List<com.airbnb.lottie.model.animatable.AnimatableFloatValue> r10 = r15.lineDashPattern
            com.airbnb.lottie.model.animatable.AnimatableFloatValue r11 = r15.offset
            r2 = r12
            r3 = r13
            r4 = r14
            r2.<init>(r3, r4, r5, r6, r7, r8, r9, r10, r11)
            r12.layer = r14
            java.lang.String r13 = r15.name
            r12.name = r13
            boolean r13 = r15.hidden
            r12.hidden = r13
            com.airbnb.lottie.model.animatable.AnimatableColorValue r13 = r15.color
            com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation r13 = r13.createAnimation()
            r15 = r13
            com.airbnb.lottie.animation.keyframe.ColorKeyframeAnimation r15 = (com.airbnb.lottie.animation.keyframe.ColorKeyframeAnimation) r15
            r12.colorAnimation = r15
            r13.addUpdateListener(r12)
            r14.addAnimation(r13)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.animation.content.StrokeContent.<init>(com.airbnb.lottie.LottieDrawable, com.airbnb.lottie.model.layer.BaseLayer, com.airbnb.lottie.model.content.ShapeStroke):void");
    }

    @Override // com.airbnb.lottie.animation.content.BaseStrokeContent, com.airbnb.lottie.model.KeyPathElement
    public final <T> void addValueCallback(T t, LottieValueCallback<T> lottieValueCallback) {
        super.addValueCallback(t, lottieValueCallback);
        if (t == LottieProperty.STROKE_COLOR) {
            this.colorAnimation.setValueCallback(lottieValueCallback);
        } else if (t != LottieProperty.COLOR_FILTER) {
        } else {
            if (lottieValueCallback == null) {
                this.colorFilterAnimation = null;
                return;
            }
            ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation = new ValueCallbackKeyframeAnimation(lottieValueCallback, null);
            this.colorFilterAnimation = valueCallbackKeyframeAnimation;
            valueCallbackKeyframeAnimation.addUpdateListener(this);
            this.layer.addAnimation(this.colorAnimation);
        }
    }

    @Override // com.airbnb.lottie.animation.content.Content
    public final String getName() {
        return this.name;
    }
}
