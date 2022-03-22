package com.airbnb.lottie.model.layer;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import androidx.collection.LongSparseArray;
import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.utils.Logger;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class CompositionLayer extends BaseLayer {
    public Boolean hasMasks;
    public Boolean hasMatte;
    public BaseKeyframeAnimation<Float, Float> timeRemapping;
    public final ArrayList layers = new ArrayList();
    public final RectF rect = new RectF();
    public final RectF newClipRect = new RectF();
    public Paint layerPaint = new Paint();

    @Override // com.airbnb.lottie.model.layer.BaseLayer
    public final void resolveChildKeyPath(KeyPath keyPath, int i, ArrayList arrayList, KeyPath keyPath2) {
        for (int i2 = 0; i2 < this.layers.size(); i2++) {
            ((BaseLayer) this.layers.get(i2)).resolveKeyPath(keyPath, i, arrayList, keyPath2);
        }
    }

    @Override // com.airbnb.lottie.model.layer.BaseLayer
    public final void drawLayer(Canvas canvas, Matrix matrix, int i) {
        Layer layer;
        Layer layer2;
        boolean z;
        boolean z2;
        RectF rectF = this.newClipRect;
        Objects.requireNonNull(this.layerModel);
        Objects.requireNonNull(this.layerModel);
        rectF.set(0.0f, 0.0f, layer.preCompWidth, layer2.preCompHeight);
        matrix.mapRect(this.newClipRect);
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        if (!lottieDrawable.isApplyingOpacityToLayersEnabled || this.layers.size() <= 1 || i == 255) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            this.layerPaint.setAlpha(i);
            RectF rectF2 = this.newClipRect;
            Paint paint = this.layerPaint;
            PathMeasure pathMeasure = Utils.pathMeasure;
            canvas.saveLayer(rectF2, paint);
            L.endSection();
        } else {
            canvas.save();
        }
        if (z) {
            i = 255;
        }
        for (int size = this.layers.size() - 1; size >= 0; size--) {
            if (!this.newClipRect.isEmpty()) {
                z2 = canvas.clipRect(this.newClipRect);
            } else {
                z2 = true;
            }
            if (z2) {
                ((BaseLayer) this.layers.get(size)).draw(canvas, matrix, i);
            }
        }
        canvas.restore();
        L.endSection();
    }

    public final boolean hasMasks() {
        if (this.hasMasks == null) {
            for (int size = this.layers.size() - 1; size >= 0; size--) {
                BaseLayer baseLayer = (BaseLayer) this.layers.get(size);
                if (baseLayer instanceof ShapeLayer) {
                    if (baseLayer.hasMasksOnThisLayer()) {
                        this.hasMasks = Boolean.TRUE;
                        return true;
                    }
                } else if ((baseLayer instanceof CompositionLayer) && ((CompositionLayer) baseLayer).hasMasks()) {
                    this.hasMasks = Boolean.TRUE;
                    return true;
                }
            }
            this.hasMasks = Boolean.FALSE;
        }
        return this.hasMasks.booleanValue();
    }

    public CompositionLayer(LottieDrawable lottieDrawable, Layer layer, List<Layer> list, LottieComposition lottieComposition) {
        super(lottieDrawable, layer);
        int i;
        BaseLayer baseLayer;
        AnimatableFloatValue animatableFloatValue = layer.timeRemapping;
        if (animatableFloatValue != null) {
            BaseKeyframeAnimation<Float, Float> createAnimation = animatableFloatValue.createAnimation();
            this.timeRemapping = createAnimation;
            addAnimation(createAnimation);
            this.timeRemapping.addUpdateListener(this);
        } else {
            this.timeRemapping = null;
        }
        Objects.requireNonNull(lottieComposition);
        LongSparseArray longSparseArray = new LongSparseArray(lottieComposition.layers.size());
        int size = list.size() - 1;
        BaseLayer baseLayer2 = null;
        while (true) {
            if (size >= 0) {
                Layer layer2 = list.get(size);
                Objects.requireNonNull(layer2);
                int ordinal = layer2.layerType.ordinal();
                if (ordinal == 0) {
                    baseLayer = new CompositionLayer(lottieDrawable, layer2, lottieComposition.precomps.get(layer2.refId), lottieComposition);
                } else if (ordinal == 1) {
                    baseLayer = new SolidLayer(lottieDrawable, layer2);
                } else if (ordinal == 2) {
                    baseLayer = new ImageLayer(lottieDrawable, layer2);
                } else if (ordinal == 3) {
                    baseLayer = new NullLayer(lottieDrawable, layer2);
                } else if (ordinal == 4) {
                    baseLayer = new ShapeLayer(lottieDrawable, layer2);
                } else if (ordinal != 5) {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Unknown layer type ");
                    m.append(layer2.layerType);
                    Logger.warning(m.toString());
                    baseLayer = null;
                } else {
                    baseLayer = new TextLayer(lottieDrawable, layer2);
                }
                if (baseLayer != null) {
                    Layer layer3 = baseLayer.layerModel;
                    Objects.requireNonNull(layer3);
                    longSparseArray.put(layer3.layerId, baseLayer);
                    if (baseLayer2 != null) {
                        baseLayer2.matteLayer = baseLayer;
                        baseLayer2 = null;
                    } else {
                        this.layers.add(0, baseLayer);
                        int ordinal2 = layer2.matteType.ordinal();
                        if (ordinal2 == 1 || ordinal2 == 2) {
                            baseLayer2 = baseLayer;
                        }
                    }
                }
                size--;
            }
        }
        for (i = 0; i < longSparseArray.size(); i++) {
            if (longSparseArray.mGarbage) {
                longSparseArray.gc();
            }
            BaseLayer baseLayer3 = (BaseLayer) longSparseArray.get(longSparseArray.mKeys[i], null);
            if (baseLayer3 != null) {
                Layer layer4 = baseLayer3.layerModel;
                Objects.requireNonNull(layer4);
                BaseLayer baseLayer4 = (BaseLayer) longSparseArray.get(layer4.parentId, null);
                if (baseLayer4 != null) {
                    baseLayer3.parentLayer = baseLayer4;
                }
            }
        }
    }

    @Override // com.airbnb.lottie.model.layer.BaseLayer, com.airbnb.lottie.model.KeyPathElement
    public final <T> void addValueCallback(T t, LottieValueCallback<T> lottieValueCallback) {
        super.addValueCallback(t, lottieValueCallback);
        if (t != LottieProperty.TIME_REMAP) {
            return;
        }
        if (lottieValueCallback == null) {
            this.timeRemapping = null;
            return;
        }
        ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation = new ValueCallbackKeyframeAnimation(lottieValueCallback, null);
        this.timeRemapping = valueCallbackKeyframeAnimation;
        addAnimation(valueCallbackKeyframeAnimation);
    }

    @Override // com.airbnb.lottie.model.layer.BaseLayer, com.airbnb.lottie.animation.content.DrawingContent
    public final void getBounds(RectF rectF, Matrix matrix, boolean z) {
        super.getBounds(rectF, matrix, z);
        for (int size = this.layers.size() - 1; size >= 0; size--) {
            this.rect.set(0.0f, 0.0f, 0.0f, 0.0f);
            ((BaseLayer) this.layers.get(size)).getBounds(this.rect, this.boundsMatrix, true);
            rectF.union(this.rect);
        }
    }

    @Override // com.airbnb.lottie.model.layer.BaseLayer
    public final void setProgress(float f) {
        super.setProgress(f);
        if (this.timeRemapping != null) {
            LottieDrawable lottieDrawable = this.lottieDrawable;
            Objects.requireNonNull(lottieDrawable);
            LottieComposition lottieComposition = lottieDrawable.composition;
            Objects.requireNonNull(lottieComposition);
            Layer layer = this.layerModel;
            Objects.requireNonNull(layer);
            LottieComposition lottieComposition2 = layer.composition;
            Objects.requireNonNull(lottieComposition2);
            float f2 = lottieComposition2.startFrame;
            float floatValue = this.timeRemapping.getValue().floatValue();
            Layer layer2 = this.layerModel;
            Objects.requireNonNull(layer2);
            LottieComposition lottieComposition3 = layer2.composition;
            Objects.requireNonNull(lottieComposition3);
            f = ((floatValue * lottieComposition3.frameRate) - f2) / ((lottieComposition.endFrame - lottieComposition.startFrame) + 0.01f);
        }
        Layer layer3 = this.layerModel;
        Objects.requireNonNull(layer3);
        if (layer3.timeStretch != 0.0f) {
            Layer layer4 = this.layerModel;
            Objects.requireNonNull(layer4);
            f /= layer4.timeStretch;
        }
        if (this.timeRemapping == null) {
            Layer layer5 = this.layerModel;
            Objects.requireNonNull(layer5);
            float f3 = layer5.startFrame;
            LottieComposition lottieComposition4 = layer5.composition;
            Objects.requireNonNull(lottieComposition4);
            f -= f3 / (lottieComposition4.endFrame - lottieComposition4.startFrame);
        }
        int size = this.layers.size();
        while (true) {
            size--;
            if (size >= 0) {
                ((BaseLayer) this.layers.get(size)).setProgress(f);
            } else {
                return;
            }
        }
    }
}
