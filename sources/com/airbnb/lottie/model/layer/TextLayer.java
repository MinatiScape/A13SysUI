package com.airbnb.lottie.model.layer;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import androidx.collection.LongSparseArray;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.TextKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.airbnb.lottie.model.animatable.AnimatableColorValue;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableTextFrame;
import com.airbnb.lottie.model.animatable.AnimatableTextProperties;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.HashMap;
import java.util.Objects;
/* loaded from: classes.dex */
public final class TextLayer extends BaseLayer {
    public BaseKeyframeAnimation<Integer, Integer> colorAnimation;
    public final LottieComposition composition;
    public final LottieDrawable lottieDrawable;
    public BaseKeyframeAnimation<Integer, Integer> strokeColorAnimation;
    public BaseKeyframeAnimation<Float, Float> strokeWidthAnimation;
    public final TextKeyframeAnimation textAnimation;
    public ValueCallbackKeyframeAnimation textSizeAnimation;
    public BaseKeyframeAnimation<Float, Float> trackingAnimation;
    public final StringBuilder stringBuilder = new StringBuilder(2);
    public final RectF rectF = new RectF();
    public final Matrix matrix = new Matrix();
    public final AnonymousClass1 fillPaint = new Paint() { // from class: com.airbnb.lottie.model.layer.TextLayer.1
        {
            setStyle(Paint.Style.FILL);
        }
    };
    public final AnonymousClass2 strokePaint = new Paint() { // from class: com.airbnb.lottie.model.layer.TextLayer.2
        {
            setStyle(Paint.Style.STROKE);
        }
    };
    public final HashMap contentsForCharacter = new HashMap();
    public final LongSparseArray<String> codePointCache = new LongSparseArray<>();

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:12:0x003e  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0042  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00ea  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x02a4  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0022  */
    /* JADX WARN: Type inference failed for: r3v5, types: [T, java.lang.String] */
    /* JADX WARN: Type inference failed for: r9v0, types: [T, java.lang.Object, java.lang.String] */
    /* JADX WARN: Unknown variable types count: 2 */
    @Override // com.airbnb.lottie.model.layer.BaseLayer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void drawLayer(android.graphics.Canvas r20, android.graphics.Matrix r21, int r22) {
        /*
            Method dump skipped, instructions count: 1249
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.model.layer.TextLayer.drawLayer(android.graphics.Canvas, android.graphics.Matrix, int):void");
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [com.airbnb.lottie.model.layer.TextLayer$1] */
    /* JADX WARN: Type inference failed for: r0v4, types: [com.airbnb.lottie.model.layer.TextLayer$2] */
    public TextLayer(LottieDrawable lottieDrawable, Layer layer) {
        super(lottieDrawable, layer);
        AnimatableFloatValue animatableFloatValue;
        AnimatableFloatValue animatableFloatValue2;
        AnimatableColorValue animatableColorValue;
        AnimatableColorValue animatableColorValue2;
        this.lottieDrawable = lottieDrawable;
        this.composition = layer.composition;
        AnimatableTextFrame animatableTextFrame = layer.text;
        Objects.requireNonNull(animatableTextFrame);
        TextKeyframeAnimation textKeyframeAnimation = new TextKeyframeAnimation(animatableTextFrame.keyframes);
        this.textAnimation = textKeyframeAnimation;
        textKeyframeAnimation.addUpdateListener(this);
        addAnimation(textKeyframeAnimation);
        AnimatableTextProperties animatableTextProperties = layer.textProperties;
        if (!(animatableTextProperties == null || (animatableColorValue2 = animatableTextProperties.color) == null)) {
            BaseKeyframeAnimation<Integer, Integer> createAnimation = animatableColorValue2.createAnimation();
            this.colorAnimation = createAnimation;
            createAnimation.addUpdateListener(this);
            addAnimation(this.colorAnimation);
        }
        if (!(animatableTextProperties == null || (animatableColorValue = animatableTextProperties.stroke) == null)) {
            BaseKeyframeAnimation<Integer, Integer> createAnimation2 = animatableColorValue.createAnimation();
            this.strokeColorAnimation = createAnimation2;
            createAnimation2.addUpdateListener(this);
            addAnimation(this.strokeColorAnimation);
        }
        if (!(animatableTextProperties == null || (animatableFloatValue2 = animatableTextProperties.strokeWidth) == null)) {
            BaseKeyframeAnimation<Float, Float> createAnimation3 = animatableFloatValue2.createAnimation();
            this.strokeWidthAnimation = createAnimation3;
            createAnimation3.addUpdateListener(this);
            addAnimation(this.strokeWidthAnimation);
        }
        if (animatableTextProperties != null && (animatableFloatValue = animatableTextProperties.tracking) != null) {
            BaseKeyframeAnimation<Float, Float> createAnimation4 = animatableFloatValue.createAnimation();
            this.trackingAnimation = createAnimation4;
            createAnimation4.addUpdateListener(this);
            addAnimation(this.trackingAnimation);
        }
    }

    public static void drawCharacter(String str, Paint paint, Canvas canvas) {
        if (paint.getColor() != 0) {
            if (paint.getStyle() != Paint.Style.STROKE || paint.getStrokeWidth() != 0.0f) {
                canvas.drawText(str, 0, str.length(), 0.0f, 0.0f, paint);
            }
        }
    }

    public static void drawGlyph(Path path, Paint paint, Canvas canvas) {
        if (paint.getColor() != 0) {
            if (paint.getStyle() != Paint.Style.STROKE || paint.getStrokeWidth() != 0.0f) {
                canvas.drawPath(path, paint);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.airbnb.lottie.model.layer.BaseLayer, com.airbnb.lottie.model.KeyPathElement
    public final <T> void addValueCallback(T t, LottieValueCallback<T> lottieValueCallback) {
        super.addValueCallback(t, lottieValueCallback);
        if (t == LottieProperty.COLOR) {
            BaseKeyframeAnimation<Integer, Integer> baseKeyframeAnimation = this.colorAnimation;
            if (baseKeyframeAnimation != null) {
                baseKeyframeAnimation.setValueCallback(lottieValueCallback);
            } else if (lottieValueCallback == 0) {
                if (baseKeyframeAnimation != null) {
                    removeAnimation(baseKeyframeAnimation);
                }
                this.colorAnimation = null;
            } else {
                ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation = new ValueCallbackKeyframeAnimation(lottieValueCallback, null);
                this.colorAnimation = valueCallbackKeyframeAnimation;
                valueCallbackKeyframeAnimation.addUpdateListener(this);
                addAnimation(this.colorAnimation);
            }
        } else if (t == LottieProperty.STROKE_COLOR) {
            BaseKeyframeAnimation<Integer, Integer> baseKeyframeAnimation2 = this.strokeColorAnimation;
            if (baseKeyframeAnimation2 != null) {
                baseKeyframeAnimation2.setValueCallback(lottieValueCallback);
            } else if (lottieValueCallback == 0) {
                if (baseKeyframeAnimation2 != null) {
                    removeAnimation(baseKeyframeAnimation2);
                }
                this.strokeColorAnimation = null;
            } else {
                ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation2 = new ValueCallbackKeyframeAnimation(lottieValueCallback, null);
                this.strokeColorAnimation = valueCallbackKeyframeAnimation2;
                valueCallbackKeyframeAnimation2.addUpdateListener(this);
                addAnimation(this.strokeColorAnimation);
            }
        } else if (t == LottieProperty.STROKE_WIDTH) {
            BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation3 = this.strokeWidthAnimation;
            if (baseKeyframeAnimation3 != null) {
                baseKeyframeAnimation3.setValueCallback(lottieValueCallback);
            } else if (lottieValueCallback == 0) {
                if (baseKeyframeAnimation3 != null) {
                    removeAnimation(baseKeyframeAnimation3);
                }
                this.strokeWidthAnimation = null;
            } else {
                ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation3 = new ValueCallbackKeyframeAnimation(lottieValueCallback, null);
                this.strokeWidthAnimation = valueCallbackKeyframeAnimation3;
                valueCallbackKeyframeAnimation3.addUpdateListener(this);
                addAnimation(this.strokeWidthAnimation);
            }
        } else if (t == LottieProperty.TEXT_TRACKING) {
            BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation4 = this.trackingAnimation;
            if (baseKeyframeAnimation4 != null) {
                baseKeyframeAnimation4.setValueCallback(lottieValueCallback);
            } else if (lottieValueCallback == 0) {
                if (baseKeyframeAnimation4 != null) {
                    removeAnimation(baseKeyframeAnimation4);
                }
                this.trackingAnimation = null;
            } else {
                ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation4 = new ValueCallbackKeyframeAnimation(lottieValueCallback, null);
                this.trackingAnimation = valueCallbackKeyframeAnimation4;
                valueCallbackKeyframeAnimation4.addUpdateListener(this);
                addAnimation(this.trackingAnimation);
            }
        } else if (t != LottieProperty.TEXT_SIZE) {
        } else {
            if (lottieValueCallback == 0) {
                ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation5 = this.textSizeAnimation;
                if (valueCallbackKeyframeAnimation5 != null) {
                    removeAnimation(valueCallbackKeyframeAnimation5);
                }
                this.textSizeAnimation = null;
                return;
            }
            ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation6 = new ValueCallbackKeyframeAnimation(lottieValueCallback, null);
            this.textSizeAnimation = valueCallbackKeyframeAnimation6;
            valueCallbackKeyframeAnimation6.addUpdateListener(this);
            addAnimation(this.textSizeAnimation);
        }
    }

    @Override // com.airbnb.lottie.model.layer.BaseLayer, com.airbnb.lottie.animation.content.DrawingContent
    public final void getBounds(RectF rectF, Matrix matrix, boolean z) {
        super.getBounds(rectF, matrix, z);
        LottieComposition lottieComposition = this.composition;
        Objects.requireNonNull(lottieComposition);
        LottieComposition lottieComposition2 = this.composition;
        Objects.requireNonNull(lottieComposition2);
        rectF.set(0.0f, 0.0f, lottieComposition.bounds.width(), lottieComposition2.bounds.height());
    }
}
