package com.airbnb.lottie.animation.content;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.LPaint;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.ColorKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.IntegerKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.content.ShapeFill;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class FillContent implements DrawingContent, BaseKeyframeAnimation.AnimationListener, KeyPathElementContent {
    public final ColorKeyframeAnimation colorAnimation;
    public ValueCallbackKeyframeAnimation colorFilterAnimation;
    public final boolean hidden;
    public final BaseLayer layer;
    public final LottieDrawable lottieDrawable;
    public final String name;
    public final IntegerKeyframeAnimation opacityAnimation;
    public final Path path;
    public final LPaint paint = new LPaint(1);
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

    @Override // com.airbnb.lottie.model.KeyPathElement
    public final <T> void addValueCallback(T t, LottieValueCallback<T> lottieValueCallback) {
        if (t == LottieProperty.COLOR) {
            this.colorAnimation.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.OPACITY) {
            this.opacityAnimation.setValueCallback(lottieValueCallback);
        } else if (t != LottieProperty.COLOR_FILTER) {
        } else {
            if (lottieValueCallback == null) {
                this.colorFilterAnimation = null;
                return;
            }
            ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation = new ValueCallbackKeyframeAnimation(lottieValueCallback, null);
            this.colorFilterAnimation = valueCallbackKeyframeAnimation;
            valueCallbackKeyframeAnimation.addUpdateListener(this);
            this.layer.addAnimation(this.colorFilterAnimation);
        }
    }

    @Override // com.airbnb.lottie.animation.content.DrawingContent
    public final void draw(Canvas canvas, Matrix matrix, int i) {
        if (!this.hidden) {
            LPaint lPaint = this.paint;
            ColorKeyframeAnimation colorKeyframeAnimation = this.colorAnimation;
            Objects.requireNonNull(colorKeyframeAnimation);
            lPaint.setColor(colorKeyframeAnimation.getIntValue(colorKeyframeAnimation.getCurrentKeyframe(), colorKeyframeAnimation.getInterpolatedCurrentKeyframeProgress()));
            LPaint lPaint2 = this.paint;
            PointF pointF = MiscUtils.pathFromDataCurrentPoint;
            lPaint2.setAlpha(Math.max(0, Math.min(255, (int) ((((i / 255.0f) * this.opacityAnimation.getValue().intValue()) / 100.0f) * 255.0f))));
            ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation = this.colorFilterAnimation;
            if (valueCallbackKeyframeAnimation != null) {
                this.paint.setColorFilter((ColorFilter) valueCallbackKeyframeAnimation.getValue());
            }
            this.path.reset();
            for (int i2 = 0; i2 < this.paths.size(); i2++) {
                this.path.addPath(((PathContent) this.paths.get(i2)).getPath(), matrix);
            }
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

    @Override // com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation.AnimationListener
    public final void onValueChanged() {
        this.lottieDrawable.invalidateSelf();
    }

    public FillContent(LottieDrawable lottieDrawable, BaseLayer baseLayer, ShapeFill shapeFill) {
        Path path = new Path();
        this.path = path;
        this.layer = baseLayer;
        Objects.requireNonNull(shapeFill);
        this.name = shapeFill.name;
        this.hidden = shapeFill.hidden;
        this.lottieDrawable = lottieDrawable;
        if (shapeFill.color == null || shapeFill.opacity == null) {
            this.colorAnimation = null;
            this.opacityAnimation = null;
            return;
        }
        path.setFillType(shapeFill.fillType);
        BaseKeyframeAnimation<Integer, Integer> createAnimation = shapeFill.color.createAnimation();
        this.colorAnimation = (ColorKeyframeAnimation) createAnimation;
        createAnimation.addUpdateListener(this);
        baseLayer.addAnimation(createAnimation);
        BaseKeyframeAnimation<Integer, Integer> createAnimation2 = shapeFill.opacity.createAnimation();
        this.opacityAnimation = (IntegerKeyframeAnimation) createAnimation2;
        createAnimation2.addUpdateListener(this);
        baseLayer.addAnimation(createAnimation2);
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
