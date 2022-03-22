package com.airbnb.lottie.animation.content;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import androidx.collection.LongSparseArray;
import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.LPaint;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.GradientColorKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.IntegerKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.PointKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.content.GradientColor;
import com.airbnb.lottie.model.content.GradientFill;
import com.airbnb.lottie.model.content.GradientType;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class GradientFillContent implements DrawingContent, BaseKeyframeAnimation.AnimationListener, KeyPathElementContent {
    public final int cacheSteps;
    public final GradientColorKeyframeAnimation colorAnimation;
    public ValueCallbackKeyframeAnimation colorCallbackAnimation;
    public ValueCallbackKeyframeAnimation colorFilterAnimation;
    public final PointKeyframeAnimation endPointAnimation;
    public final boolean hidden;
    public final BaseLayer layer;
    public final LottieDrawable lottieDrawable;
    public final String name;
    public final IntegerKeyframeAnimation opacityAnimation;
    public final Path path;
    public final PointKeyframeAnimation startPointAnimation;
    public final GradientType type;
    public final LongSparseArray<LinearGradient> linearGradientCache = new LongSparseArray<>();
    public final LongSparseArray<RadialGradient> radialGradientCache = new LongSparseArray<>();
    public final LPaint paint = new LPaint(1);
    public final RectF boundsRect = new RectF();
    public final ArrayList paths = new ArrayList();

    @Override // com.airbnb.lottie.animation.content.Content
    public final void setContents(List<Content> list, List<Content> list2) {
        for (int i = 0; i < list2.size(); i++) {
            Content content = list2.get(i);
            if (content instanceof PathContent) {
                this.paths.add((PathContent) content);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.airbnb.lottie.model.KeyPathElement
    public final <T> void addValueCallback(T t, LottieValueCallback<T> lottieValueCallback) {
        if (t == LottieProperty.OPACITY) {
            this.opacityAnimation.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.COLOR_FILTER) {
            if (lottieValueCallback == null) {
                this.colorFilterAnimation = null;
                return;
            }
            ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation = new ValueCallbackKeyframeAnimation(lottieValueCallback, null);
            this.colorFilterAnimation = valueCallbackKeyframeAnimation;
            valueCallbackKeyframeAnimation.addUpdateListener(this);
            this.layer.addAnimation(this.colorFilterAnimation);
        } else if (t != LottieProperty.GRADIENT_COLOR) {
        } else {
            if (lottieValueCallback == null) {
                ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation2 = this.colorCallbackAnimation;
                if (valueCallbackKeyframeAnimation2 != null) {
                    this.layer.removeAnimation(valueCallbackKeyframeAnimation2);
                }
                this.colorCallbackAnimation = null;
                return;
            }
            ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation3 = new ValueCallbackKeyframeAnimation(lottieValueCallback, null);
            this.colorCallbackAnimation = valueCallbackKeyframeAnimation3;
            valueCallbackKeyframeAnimation3.addUpdateListener(this);
            this.layer.addAnimation(this.colorCallbackAnimation);
        }
    }

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
    @Override // com.airbnb.lottie.animation.content.DrawingContent
    public final void draw(Canvas canvas, Matrix matrix, int i) {
        Shader shader;
        if (!this.hidden) {
            this.path.reset();
            for (int i2 = 0; i2 < this.paths.size(); i2++) {
                this.path.addPath(((PathContent) this.paths.get(i2)).getPath(), matrix);
            }
            this.path.computeBounds(this.boundsRect, false);
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
                    LinearGradient linearGradient = new LinearGradient(value.x, value.y, value2.x, value2.y, applyDynamicColorsIfNeeded(value3.colors), value3.positions, Shader.TileMode.CLAMP);
                    this.linearGradientCache.put(j, linearGradient);
                    shader = linearGradient;
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
                    float f = value4.x;
                    float f2 = value4.y;
                    float hypot = (float) Math.hypot(value5.x - f, value5.y - f2);
                    if (hypot <= 0.0f) {
                        hypot = 0.001f;
                    }
                    shader = new RadialGradient(f, f2, hypot, applyDynamicColorsIfNeeded, fArr, Shader.TileMode.CLAMP);
                    this.radialGradientCache.put(j2, shader);
                }
            }
            shader.setLocalMatrix(matrix);
            this.paint.setShader(shader);
            ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation = this.colorFilterAnimation;
            if (valueCallbackKeyframeAnimation != null) {
                this.paint.setColorFilter((ColorFilter) valueCallbackKeyframeAnimation.getValue());
            }
            LPaint lPaint = this.paint;
            PointF pointF = MiscUtils.pathFromDataCurrentPoint;
            lPaint.setAlpha(Math.max(0, Math.min(255, (int) ((((i / 255.0f) * this.opacityAnimation.getValue().intValue()) / 100.0f) * 255.0f))));
            canvas.drawPath(this.path, this.paint);
            L.endSection();
        }
    }

    @Override // com.airbnb.lottie.animation.content.DrawingContent
    public final void getBounds(RectF rectF, Matrix matrix, boolean z) {
        this.path.reset();
        for (int i = 0; i < this.paths.size(); i++) {
            this.path.addPath(((PathContent) this.paths.get(i)).getPath(), matrix);
        }
        this.path.computeBounds(rectF, false);
        rectF.set(rectF.left - 1.0f, rectF.top - 1.0f, rectF.right + 1.0f, rectF.bottom + 1.0f);
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

    @Override // com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation.AnimationListener
    public final void onValueChanged() {
        this.lottieDrawable.invalidateSelf();
    }

    public GradientFillContent(LottieDrawable lottieDrawable, BaseLayer baseLayer, GradientFill gradientFill) {
        Path path = new Path();
        this.path = path;
        this.layer = baseLayer;
        Objects.requireNonNull(gradientFill);
        this.name = gradientFill.name;
        this.hidden = gradientFill.hidden;
        this.lottieDrawable = lottieDrawable;
        this.type = gradientFill.gradientType;
        path.setFillType(gradientFill.fillType);
        Objects.requireNonNull(lottieDrawable);
        this.cacheSteps = (int) (lottieDrawable.composition.getDuration() / 32.0f);
        BaseKeyframeAnimation<GradientColor, GradientColor> createAnimation = gradientFill.gradientColor.createAnimation();
        this.colorAnimation = (GradientColorKeyframeAnimation) createAnimation;
        createAnimation.addUpdateListener(this);
        baseLayer.addAnimation(createAnimation);
        BaseKeyframeAnimation<Integer, Integer> createAnimation2 = gradientFill.opacity.createAnimation();
        this.opacityAnimation = (IntegerKeyframeAnimation) createAnimation2;
        createAnimation2.addUpdateListener(this);
        baseLayer.addAnimation(createAnimation2);
        BaseKeyframeAnimation<PointF, PointF> createAnimation3 = gradientFill.startPoint.createAnimation();
        this.startPointAnimation = (PointKeyframeAnimation) createAnimation3;
        createAnimation3.addUpdateListener(this);
        baseLayer.addAnimation(createAnimation3);
        BaseKeyframeAnimation<PointF, PointF> createAnimation4 = gradientFill.endPoint.createAnimation();
        this.endPointAnimation = (PointKeyframeAnimation) createAnimation4;
        createAnimation4.addUpdateListener(this);
        baseLayer.addAnimation(createAnimation4);
    }

    @Override // com.airbnb.lottie.model.KeyPathElement
    public final void resolveKeyPath(KeyPath keyPath, int i, ArrayList arrayList, KeyPath keyPath2) {
        MiscUtils.resolveKeyPath(keyPath, i, arrayList, keyPath2, this);
    }

    @Override // com.airbnb.lottie.animation.content.Content
    public final String getName() {
        return this.name;
    }
}
