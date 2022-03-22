package com.airbnb.lottie.animation.content;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import androidx.collection.LongSparseArray;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.keyframe.GradientColorKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.PointKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.airbnb.lottie.model.content.GradientColor;
import com.airbnb.lottie.model.content.GradientType;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.Objects;
/* loaded from: classes.dex */
public final class GradientStrokeContent extends BaseStrokeContent {
    public final RectF boundsRect;
    public final int cacheSteps;
    public final GradientColorKeyframeAnimation colorAnimation;
    public ValueCallbackKeyframeAnimation colorCallbackAnimation;
    public final PointKeyframeAnimation endPointAnimation;
    public final boolean hidden;
    public final LongSparseArray<LinearGradient> linearGradientCache;
    public final String name;
    public final LongSparseArray<RadialGradient> radialGradientCache;
    public final PointKeyframeAnimation startPointAnimation;
    public final GradientType type;

    public final int[] applyDynamicColorsIfNeeded(int[] iArr) {
        ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation = this.colorCallbackAnimation;
        if (valueCallbackKeyframeAnimation != null) {
            Integer[] numArr = (Integer[]) valueCallbackKeyframeAnimation.getValue();
            int i = 0;
            if (iArr.length == numArr.length) {
                while (i < iArr.length) {
                    iArr[i] = numArr[i].intValue();
                    i++;
                }
            } else {
                iArr = new int[numArr.length];
                while (i < numArr.length) {
                    iArr[i] = numArr[i].intValue();
                    i++;
                }
            }
        }
        return iArr;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.airbnb.lottie.animation.content.BaseStrokeContent, com.airbnb.lottie.animation.content.DrawingContent
    public final void draw(Canvas canvas, Matrix matrix, int i) {
        Shader shader;
        float f;
        float f2;
        if (!this.hidden) {
            getBounds(this.boundsRect, matrix, false);
            if (this.type == GradientType.LINEAR) {
                int gradientHash = getGradientHash();
                LongSparseArray<LinearGradient> longSparseArray = this.linearGradientCache;
                long j = gradientHash;
                Objects.requireNonNull(longSparseArray);
                shader = (LinearGradient) longSparseArray.get(j, null);
                if (shader == null) {
                    PointF value = this.startPointAnimation.getValue();
                    PointF value2 = this.endPointAnimation.getValue();
                    GradientColor value3 = this.colorAnimation.getValue();
                    Objects.requireNonNull(value3);
                    shader = new LinearGradient(value.x, value.y, value2.x, value2.y, applyDynamicColorsIfNeeded(value3.colors), value3.positions, Shader.TileMode.CLAMP);
                    this.linearGradientCache.put(j, shader);
                }
            } else {
                int gradientHash2 = getGradientHash();
                LongSparseArray<RadialGradient> longSparseArray2 = this.radialGradientCache;
                long j2 = gradientHash2;
                Objects.requireNonNull(longSparseArray2);
                shader = (RadialGradient) longSparseArray2.get(j2, null);
                if (shader == null) {
                    PointF value4 = this.startPointAnimation.getValue();
                    PointF value5 = this.endPointAnimation.getValue();
                    GradientColor value6 = this.colorAnimation.getValue();
                    Objects.requireNonNull(value6);
                    int[] applyDynamicColorsIfNeeded = applyDynamicColorsIfNeeded(value6.colors);
                    float[] fArr = value6.positions;
                    shader = new RadialGradient(value4.x, value4.y, (float) Math.hypot(value5.x - f, value5.y - f2), applyDynamicColorsIfNeeded, fArr, Shader.TileMode.CLAMP);
                    this.radialGradientCache.put(j2, shader);
                }
            }
            shader.setLocalMatrix(matrix);
            this.paint.setShader(shader);
            super.draw(canvas, matrix, i);
        }
    }

    public final int getGradientHash() {
        int i;
        PointKeyframeAnimation pointKeyframeAnimation = this.startPointAnimation;
        Objects.requireNonNull(pointKeyframeAnimation);
        int round = Math.round(pointKeyframeAnimation.progress * this.cacheSteps);
        PointKeyframeAnimation pointKeyframeAnimation2 = this.endPointAnimation;
        Objects.requireNonNull(pointKeyframeAnimation2);
        int round2 = Math.round(pointKeyframeAnimation2.progress * this.cacheSteps);
        GradientColorKeyframeAnimation gradientColorKeyframeAnimation = this.colorAnimation;
        Objects.requireNonNull(gradientColorKeyframeAnimation);
        int round3 = Math.round(gradientColorKeyframeAnimation.progress * this.cacheSteps);
        if (round != 0) {
            i = round * 527;
        } else {
            i = 17;
        }
        if (round2 != 0) {
            i = i * 31 * round2;
        }
        if (round3 != 0) {
            return i * 31 * round3;
        }
        return i;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public GradientStrokeContent(com.airbnb.lottie.LottieDrawable r13, com.airbnb.lottie.model.layer.BaseLayer r14, com.airbnb.lottie.model.content.GradientStroke r15) {
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
            com.airbnb.lottie.model.animatable.AnimatableFloatValue r11 = r15.dashOffset
            r2 = r12
            r3 = r13
            r4 = r14
            r2.<init>(r3, r4, r5, r6, r7, r8, r9, r10, r11)
            androidx.collection.LongSparseArray r0 = new androidx.collection.LongSparseArray
            r0.<init>()
            r12.linearGradientCache = r0
            androidx.collection.LongSparseArray r0 = new androidx.collection.LongSparseArray
            r0.<init>()
            r12.radialGradientCache = r0
            android.graphics.RectF r0 = new android.graphics.RectF
            r0.<init>()
            r12.boundsRect = r0
            java.lang.String r0 = r15.name
            r12.name = r0
            com.airbnb.lottie.model.content.GradientType r0 = r15.gradientType
            r12.type = r0
            boolean r0 = r15.hidden
            r12.hidden = r0
            java.util.Objects.requireNonNull(r13)
            com.airbnb.lottie.LottieComposition r13 = r13.composition
            float r13 = r13.getDuration()
            r0 = 1107296256(0x42000000, float:32.0)
            float r13 = r13 / r0
            int r13 = (int) r13
            r12.cacheSteps = r13
            com.airbnb.lottie.model.animatable.AnimatableGradientColorValue r13 = r15.gradientColor
            com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation r13 = r13.createAnimation()
            r0 = r13
            com.airbnb.lottie.animation.keyframe.GradientColorKeyframeAnimation r0 = (com.airbnb.lottie.animation.keyframe.GradientColorKeyframeAnimation) r0
            r12.colorAnimation = r0
            r13.addUpdateListener(r12)
            r14.addAnimation(r13)
            com.airbnb.lottie.model.animatable.AnimatablePointValue r13 = r15.startPoint
            com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation r13 = r13.createAnimation()
            r0 = r13
            com.airbnb.lottie.animation.keyframe.PointKeyframeAnimation r0 = (com.airbnb.lottie.animation.keyframe.PointKeyframeAnimation) r0
            r12.startPointAnimation = r0
            r13.addUpdateListener(r12)
            r14.addAnimation(r13)
            com.airbnb.lottie.model.animatable.AnimatablePointValue r13 = r15.endPoint
            com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation r13 = r13.createAnimation()
            r15 = r13
            com.airbnb.lottie.animation.keyframe.PointKeyframeAnimation r15 = (com.airbnb.lottie.animation.keyframe.PointKeyframeAnimation) r15
            r12.endPointAnimation = r15
            r13.addUpdateListener(r12)
            r14.addAnimation(r13)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.animation.content.GradientStrokeContent.<init>(com.airbnb.lottie.LottieDrawable, com.airbnb.lottie.model.layer.BaseLayer, com.airbnb.lottie.model.content.GradientStroke):void");
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.airbnb.lottie.animation.content.BaseStrokeContent, com.airbnb.lottie.model.KeyPathElement
    public final <T> void addValueCallback(T t, LottieValueCallback<T> lottieValueCallback) {
        super.addValueCallback(t, lottieValueCallback);
        if (t != LottieProperty.GRADIENT_COLOR) {
            return;
        }
        if (lottieValueCallback == null) {
            ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation = this.colorCallbackAnimation;
            if (valueCallbackKeyframeAnimation != null) {
                this.layer.removeAnimation(valueCallbackKeyframeAnimation);
            }
            this.colorCallbackAnimation = null;
            return;
        }
        ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation2 = new ValueCallbackKeyframeAnimation(lottieValueCallback, null);
        this.colorCallbackAnimation = valueCallbackKeyframeAnimation2;
        valueCallbackKeyframeAnimation2.addUpdateListener(this);
        this.layer.addAnimation(this.colorCallbackAnimation);
    }

    @Override // com.airbnb.lottie.animation.content.Content
    public final String getName() {
        return this.name;
    }
}
