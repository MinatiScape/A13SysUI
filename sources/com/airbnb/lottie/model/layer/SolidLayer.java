package com.airbnb.lottie.model.layer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.LPaint;
import com.airbnb.lottie.animation.keyframe.TransformKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SolidLayer extends BaseLayer {
    public ValueCallbackKeyframeAnimation colorFilterAnimation;
    public final Layer layerModel;
    public final LPaint paint;
    public final RectF rect = new RectF();
    public final float[] points = new float[8];
    public final Path path = new Path();

    @Override // com.airbnb.lottie.model.layer.BaseLayer
    public final void drawLayer(Canvas canvas, Matrix matrix, int i) {
        int i2;
        Layer layer = this.layerModel;
        Objects.requireNonNull(layer);
        int alpha = Color.alpha(layer.solidColor);
        if (alpha != 0) {
            TransformKeyframeAnimation transformKeyframeAnimation = this.transform;
            Objects.requireNonNull(transformKeyframeAnimation);
            if (transformKeyframeAnimation.opacity == null) {
                i2 = 100;
            } else {
                TransformKeyframeAnimation transformKeyframeAnimation2 = this.transform;
                Objects.requireNonNull(transformKeyframeAnimation2);
                i2 = transformKeyframeAnimation2.opacity.getValue().intValue();
            }
            int i3 = (int) ((((alpha / 255.0f) * i2) / 100.0f) * (i / 255.0f) * 255.0f);
            this.paint.setAlpha(i3);
            ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation = this.colorFilterAnimation;
            if (valueCallbackKeyframeAnimation != null) {
                this.paint.setColorFilter((ColorFilter) valueCallbackKeyframeAnimation.getValue());
            }
            if (i3 > 0) {
                float[] fArr = this.points;
                fArr[0] = 0.0f;
                fArr[1] = 0.0f;
                Layer layer2 = this.layerModel;
                Objects.requireNonNull(layer2);
                fArr[2] = layer2.solidWidth;
                float[] fArr2 = this.points;
                fArr2[3] = 0.0f;
                Layer layer3 = this.layerModel;
                Objects.requireNonNull(layer3);
                fArr2[4] = layer3.solidWidth;
                float[] fArr3 = this.points;
                Layer layer4 = this.layerModel;
                Objects.requireNonNull(layer4);
                fArr3[5] = layer4.solidHeight;
                float[] fArr4 = this.points;
                fArr4[6] = 0.0f;
                Layer layer5 = this.layerModel;
                Objects.requireNonNull(layer5);
                fArr4[7] = layer5.solidHeight;
                matrix.mapPoints(this.points);
                this.path.reset();
                Path path = this.path;
                float[] fArr5 = this.points;
                path.moveTo(fArr5[0], fArr5[1]);
                Path path2 = this.path;
                float[] fArr6 = this.points;
                path2.lineTo(fArr6[2], fArr6[3]);
                Path path3 = this.path;
                float[] fArr7 = this.points;
                path3.lineTo(fArr7[4], fArr7[5]);
                Path path4 = this.path;
                float[] fArr8 = this.points;
                path4.lineTo(fArr8[6], fArr8[7]);
                Path path5 = this.path;
                float[] fArr9 = this.points;
                path5.lineTo(fArr9[0], fArr9[1]);
                this.path.close();
                canvas.drawPath(this.path, this.paint);
            }
        }
    }

    public SolidLayer(LottieDrawable lottieDrawable, Layer layer) {
        super(lottieDrawable, layer);
        LPaint lPaint = new LPaint();
        this.paint = lPaint;
        this.layerModel = layer;
        lPaint.setAlpha(0);
        lPaint.setStyle(Paint.Style.FILL);
        lPaint.setColor(layer.solidColor);
    }

    @Override // com.airbnb.lottie.model.layer.BaseLayer, com.airbnb.lottie.model.KeyPathElement
    public final <T> void addValueCallback(T t, LottieValueCallback<T> lottieValueCallback) {
        super.addValueCallback(t, lottieValueCallback);
        if (t != LottieProperty.COLOR_FILTER) {
            return;
        }
        if (lottieValueCallback == null) {
            this.colorFilterAnimation = null;
        } else {
            this.colorFilterAnimation = new ValueCallbackKeyframeAnimation(lottieValueCallback, null);
        }
    }

    @Override // com.airbnb.lottie.model.layer.BaseLayer, com.airbnb.lottie.animation.content.DrawingContent
    public final void getBounds(RectF rectF, Matrix matrix, boolean z) {
        super.getBounds(rectF, matrix, z);
        RectF rectF2 = this.rect;
        Layer layer = this.layerModel;
        Objects.requireNonNull(layer);
        Layer layer2 = this.layerModel;
        Objects.requireNonNull(layer2);
        rectF2.set(0.0f, 0.0f, layer.solidWidth, layer2.solidHeight);
        this.boundsMatrix.mapRect(this.rect);
        rectF.set(this.rect);
    }
}
