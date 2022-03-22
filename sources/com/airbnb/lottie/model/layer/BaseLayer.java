package com.airbnb.lottie.model.layer;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import androidx.collection.ArraySet;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline1;
import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.PerformanceTracker;
import com.airbnb.lottie.animation.LPaint;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.animation.content.DrawingContent;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.FloatKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.MaskKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.TransformKeyframeAnimation;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.KeyPathElement;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.model.content.Mask;
import com.airbnb.lottie.model.layer.Layer;
import com.airbnb.lottie.utils.MeanCalculator;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.value.ScaleXY;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class BaseLayer implements DrawingContent, BaseKeyframeAnimation.AnimationListener, KeyPathElement {
    public final String drawTraceName;
    public final Layer layerModel;
    public final LottieDrawable lottieDrawable;
    public MaskKeyframeAnimation mask;
    public BaseLayer matteLayer;
    public final LPaint mattePaint;
    public BaseLayer parentLayer;
    public List<BaseLayer> parentLayers;
    public final TransformKeyframeAnimation transform;
    public boolean visible;
    public final Path path = new Path();
    public final Matrix matrix = new Matrix();
    public final LPaint contentPaint = new LPaint(1);
    public final LPaint dstInPaint = new LPaint(PorterDuff.Mode.DST_IN, 0);
    public final LPaint dstOutPaint = new LPaint(PorterDuff.Mode.DST_OUT, 0);
    public final LPaint clearPaint = new LPaint(PorterDuff.Mode.CLEAR);
    public final RectF rect = new RectF();
    public final RectF maskBoundsRect = new RectF();
    public final RectF matteBoundsRect = new RectF();
    public final RectF tempMaskBoundsRect = new RectF();
    public final Matrix boundsMatrix = new Matrix();
    public final ArrayList animations = new ArrayList();

    public abstract void drawLayer(Canvas canvas, Matrix matrix, int i);

    public void resolveChildKeyPath(KeyPath keyPath, int i, ArrayList arrayList, KeyPath keyPath2) {
    }

    @Override // com.airbnb.lottie.animation.content.Content
    public final void setContents(List<Content> list, List<Content> list2) {
    }

    public final void addAnimation(BaseKeyframeAnimation<?, ?> baseKeyframeAnimation) {
        if (baseKeyframeAnimation != null) {
            this.animations.add(baseKeyframeAnimation);
        }
    }

    @Override // com.airbnb.lottie.model.KeyPathElement
    public <T> void addValueCallback(T t, LottieValueCallback<T> lottieValueCallback) {
        this.transform.applyValueCallback(t, lottieValueCallback);
    }

    public final void buildParentLayerListIfNeeded() {
        if (this.parentLayers == null) {
            if (this.parentLayer == null) {
                this.parentLayers = Collections.emptyList();
                return;
            }
            this.parentLayers = new ArrayList();
            for (BaseLayer baseLayer = this.parentLayer; baseLayer != null; baseLayer = baseLayer.parentLayer) {
                this.parentLayers.add(baseLayer);
            }
        }
    }

    public final void clearCanvas(Canvas canvas) {
        RectF rectF = this.rect;
        canvas.drawRect(rectF.left - 1.0f, rectF.top - 1.0f, rectF.right + 1.0f, rectF.bottom + 1.0f, this.clearPaint);
        L.endSection();
    }

    /* JADX WARN: Removed duplicated region for block: B:118:0x03de A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0146  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x014e  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x026d  */
    @Override // com.airbnb.lottie.animation.content.DrawingContent
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void draw(android.graphics.Canvas r17, android.graphics.Matrix r18, int r19) {
        /*
            Method dump skipped, instructions count: 1055
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.model.layer.BaseLayer.draw(android.graphics.Canvas, android.graphics.Matrix, int):void");
    }

    @Override // com.airbnb.lottie.animation.content.DrawingContent
    public void getBounds(RectF rectF, Matrix matrix, boolean z) {
        this.rect.set(0.0f, 0.0f, 0.0f, 0.0f);
        buildParentLayerListIfNeeded();
        this.boundsMatrix.set(matrix);
        if (z) {
            List<BaseLayer> list = this.parentLayers;
            if (list != null) {
                int size = list.size();
                while (true) {
                    size--;
                    if (size < 0) {
                        break;
                    }
                    this.boundsMatrix.preConcat(this.parentLayers.get(size).transform.getMatrix());
                }
            } else {
                BaseLayer baseLayer = this.parentLayer;
                if (baseLayer != null) {
                    this.boundsMatrix.preConcat(baseLayer.transform.getMatrix());
                }
            }
        }
        this.boundsMatrix.preConcat(this.transform.getMatrix());
    }

    @Override // com.airbnb.lottie.animation.content.Content
    public final String getName() {
        Layer layer = this.layerModel;
        Objects.requireNonNull(layer);
        return layer.layerName;
    }

    public final boolean hasMasksOnThisLayer() {
        MaskKeyframeAnimation maskKeyframeAnimation = this.mask;
        if (maskKeyframeAnimation == null || maskKeyframeAnimation.maskAnimations.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override // com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation.AnimationListener
    public final void onValueChanged() {
        this.lottieDrawable.invalidateSelf();
    }

    public final void recordRenderTime() {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        LottieComposition lottieComposition = lottieDrawable.composition;
        Objects.requireNonNull(lottieComposition);
        PerformanceTracker performanceTracker = lottieComposition.performanceTracker;
        Layer layer = this.layerModel;
        Objects.requireNonNull(layer);
        String str = layer.layerName;
        Objects.requireNonNull(performanceTracker);
        if (performanceTracker.enabled) {
            MeanCalculator meanCalculator = (MeanCalculator) performanceTracker.layerRenderTimes.get(str);
            if (meanCalculator == null) {
                meanCalculator = new MeanCalculator();
                performanceTracker.layerRenderTimes.put(str, meanCalculator);
            }
            int i = meanCalculator.n + 1;
            meanCalculator.n = i;
            if (i == Integer.MAX_VALUE) {
                meanCalculator.n = i / 2;
            }
            if (str.equals("__container")) {
                ArraySet arraySet = performanceTracker.frameListeners;
                Objects.requireNonNull(arraySet);
                ArraySet.ElementIterator elementIterator = new ArraySet.ElementIterator();
                while (elementIterator.hasNext()) {
                    ((PerformanceTracker.FrameListener) elementIterator.next()).onFrameRendered();
                }
            }
        }
    }

    public final void removeAnimation(BaseKeyframeAnimation<?, ?> baseKeyframeAnimation) {
        this.animations.remove(baseKeyframeAnimation);
    }

    public void setProgress(float f) {
        TransformKeyframeAnimation transformKeyframeAnimation = this.transform;
        Objects.requireNonNull(transformKeyframeAnimation);
        BaseKeyframeAnimation<Integer, Integer> baseKeyframeAnimation = transformKeyframeAnimation.opacity;
        if (baseKeyframeAnimation != null) {
            baseKeyframeAnimation.setProgress(f);
        }
        BaseKeyframeAnimation<?, Float> baseKeyframeAnimation2 = transformKeyframeAnimation.startOpacity;
        if (baseKeyframeAnimation2 != null) {
            baseKeyframeAnimation2.setProgress(f);
        }
        BaseKeyframeAnimation<?, Float> baseKeyframeAnimation3 = transformKeyframeAnimation.endOpacity;
        if (baseKeyframeAnimation3 != null) {
            baseKeyframeAnimation3.setProgress(f);
        }
        BaseKeyframeAnimation<PointF, PointF> baseKeyframeAnimation4 = transformKeyframeAnimation.anchorPoint;
        if (baseKeyframeAnimation4 != null) {
            baseKeyframeAnimation4.setProgress(f);
        }
        BaseKeyframeAnimation<?, PointF> baseKeyframeAnimation5 = transformKeyframeAnimation.position;
        if (baseKeyframeAnimation5 != null) {
            baseKeyframeAnimation5.setProgress(f);
        }
        BaseKeyframeAnimation<ScaleXY, ScaleXY> baseKeyframeAnimation6 = transformKeyframeAnimation.scale;
        if (baseKeyframeAnimation6 != null) {
            baseKeyframeAnimation6.setProgress(f);
        }
        BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation7 = transformKeyframeAnimation.rotation;
        if (baseKeyframeAnimation7 != null) {
            baseKeyframeAnimation7.setProgress(f);
        }
        FloatKeyframeAnimation floatKeyframeAnimation = transformKeyframeAnimation.skew;
        if (floatKeyframeAnimation != null) {
            floatKeyframeAnimation.setProgress(f);
        }
        FloatKeyframeAnimation floatKeyframeAnimation2 = transformKeyframeAnimation.skewAngle;
        if (floatKeyframeAnimation2 != null) {
            floatKeyframeAnimation2.setProgress(f);
        }
        if (this.mask != null) {
            int i = 0;
            while (true) {
                MaskKeyframeAnimation maskKeyframeAnimation = this.mask;
                Objects.requireNonNull(maskKeyframeAnimation);
                if (i >= maskKeyframeAnimation.maskAnimations.size()) {
                    break;
                }
                MaskKeyframeAnimation maskKeyframeAnimation2 = this.mask;
                Objects.requireNonNull(maskKeyframeAnimation2);
                ((BaseKeyframeAnimation) maskKeyframeAnimation2.maskAnimations.get(i)).setProgress(f);
                i++;
            }
        }
        Layer layer = this.layerModel;
        Objects.requireNonNull(layer);
        if (layer.timeStretch != 0.0f) {
            Layer layer2 = this.layerModel;
            Objects.requireNonNull(layer2);
            f /= layer2.timeStretch;
        }
        BaseLayer baseLayer = this.matteLayer;
        if (baseLayer != null) {
            Layer layer3 = baseLayer.layerModel;
            Objects.requireNonNull(layer3);
            this.matteLayer.setProgress(layer3.timeStretch * f);
        }
        for (int i2 = 0; i2 < this.animations.size(); i2++) {
            ((BaseKeyframeAnimation) this.animations.get(i2)).setProgress(f);
        }
    }

    public BaseLayer(LottieDrawable lottieDrawable, Layer layer) {
        boolean z = true;
        LPaint lPaint = new LPaint(1);
        this.mattePaint = lPaint;
        this.visible = true;
        this.lottieDrawable = lottieDrawable;
        this.layerModel = layer;
        this.drawTraceName = MotionController$$ExternalSyntheticOutline1.m(new StringBuilder(), layer.layerName, "#draw");
        if (layer.matteType == Layer.MatteType.INVERT) {
            lPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        } else {
            lPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        }
        AnimatableTransform animatableTransform = layer.transform;
        Objects.requireNonNull(animatableTransform);
        TransformKeyframeAnimation transformKeyframeAnimation = new TransformKeyframeAnimation(animatableTransform);
        this.transform = transformKeyframeAnimation;
        transformKeyframeAnimation.addListener(this);
        List<Mask> list = layer.masks;
        if (list != null && !list.isEmpty()) {
            MaskKeyframeAnimation maskKeyframeAnimation = new MaskKeyframeAnimation(layer.masks);
            this.mask = maskKeyframeAnimation;
            Iterator it = maskKeyframeAnimation.maskAnimations.iterator();
            while (it.hasNext()) {
                ((BaseKeyframeAnimation) it.next()).addUpdateListener(this);
            }
            MaskKeyframeAnimation maskKeyframeAnimation2 = this.mask;
            Objects.requireNonNull(maskKeyframeAnimation2);
            Iterator it2 = maskKeyframeAnimation2.opacityAnimations.iterator();
            while (it2.hasNext()) {
                BaseKeyframeAnimation<?, ?> baseKeyframeAnimation = (BaseKeyframeAnimation) it2.next();
                addAnimation(baseKeyframeAnimation);
                baseKeyframeAnimation.addUpdateListener(this);
            }
        }
        Layer layer2 = this.layerModel;
        Objects.requireNonNull(layer2);
        if (!layer2.inOutKeyframes.isEmpty()) {
            Layer layer3 = this.layerModel;
            Objects.requireNonNull(layer3);
            final FloatKeyframeAnimation floatKeyframeAnimation = new FloatKeyframeAnimation(layer3.inOutKeyframes);
            floatKeyframeAnimation.isDiscrete = true;
            floatKeyframeAnimation.addUpdateListener(new BaseKeyframeAnimation.AnimationListener() { // from class: com.airbnb.lottie.model.layer.BaseLayer.1
                @Override // com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation.AnimationListener
                public final void onValueChanged() {
                    boolean z2;
                    BaseLayer baseLayer = BaseLayer.this;
                    if (floatKeyframeAnimation.getFloatValue() == 1.0f) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    Objects.requireNonNull(baseLayer);
                    if (z2 != baseLayer.visible) {
                        baseLayer.visible = z2;
                        baseLayer.lottieDrawable.invalidateSelf();
                    }
                }
            });
            z = floatKeyframeAnimation.getValue().floatValue() != 1.0f ? false : z;
            if (z != this.visible) {
                this.visible = z;
                this.lottieDrawable.invalidateSelf();
            }
            addAnimation(floatKeyframeAnimation);
        } else if (true != this.visible) {
            this.visible = true;
            this.lottieDrawable.invalidateSelf();
        }
    }

    @Override // com.airbnb.lottie.model.KeyPathElement
    public final void resolveKeyPath(KeyPath keyPath, int i, ArrayList arrayList, KeyPath keyPath2) {
        if (keyPath.matches(getName(), i)) {
            if (!"__container".equals(getName())) {
                String name = getName();
                Objects.requireNonNull(keyPath2);
                KeyPath keyPath3 = new KeyPath(keyPath2);
                keyPath3.keys.add(name);
                if (keyPath.fullyResolvesTo(getName(), i)) {
                    KeyPath keyPath4 = new KeyPath(keyPath3);
                    keyPath4.resolvedElement = this;
                    arrayList.add(keyPath4);
                }
                keyPath2 = keyPath3;
            }
            if (keyPath.propagateToChildren(getName(), i)) {
                resolveChildKeyPath(keyPath, keyPath.incrementDepthBy(getName(), i) + i, arrayList, keyPath2);
            }
        }
    }
}
