package com.airbnb.lottie.animation.content;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.FloatKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.TransformKeyframeAnimation;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.model.content.Repeater;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class RepeaterContent implements DrawingContent, PathContent, GreedyContent, BaseKeyframeAnimation.AnimationListener, KeyPathElementContent {
    public ContentGroup contentGroup;
    public final FloatKeyframeAnimation copies;
    public final boolean hidden;
    public final BaseLayer layer;
    public final LottieDrawable lottieDrawable;
    public final String name;
    public final FloatKeyframeAnimation offset;
    public final TransformKeyframeAnimation transform;
    public final Matrix matrix = new Matrix();
    public final Path path = new Path();

    @Override // com.airbnb.lottie.animation.content.GreedyContent
    public final void absorbContent(ListIterator<Content> listIterator) {
        if (this.contentGroup == null) {
            while (listIterator.hasPrevious() && listIterator.previous() != this) {
            }
            ArrayList arrayList = new ArrayList();
            while (listIterator.hasPrevious()) {
                arrayList.add(listIterator.previous());
                listIterator.remove();
            }
            Collections.reverse(arrayList);
            this.contentGroup = new ContentGroup(this.lottieDrawable, this.layer, "Repeater", this.hidden, arrayList, null);
        }
    }

    @Override // com.airbnb.lottie.model.KeyPathElement
    public final <T> void addValueCallback(T t, LottieValueCallback<T> lottieValueCallback) {
        if (!this.transform.applyValueCallback(t, lottieValueCallback)) {
            if (t == LottieProperty.REPEATER_COPIES) {
                this.copies.setValueCallback(lottieValueCallback);
            } else if (t == LottieProperty.REPEATER_OFFSET) {
                this.offset.setValueCallback(lottieValueCallback);
            }
        }
    }

    @Override // com.airbnb.lottie.animation.content.DrawingContent
    public final void draw(Canvas canvas, Matrix matrix, int i) {
        float floatValue = this.copies.getValue().floatValue();
        float floatValue2 = this.offset.getValue().floatValue();
        TransformKeyframeAnimation transformKeyframeAnimation = this.transform;
        Objects.requireNonNull(transformKeyframeAnimation);
        float floatValue3 = transformKeyframeAnimation.startOpacity.getValue().floatValue() / 100.0f;
        TransformKeyframeAnimation transformKeyframeAnimation2 = this.transform;
        Objects.requireNonNull(transformKeyframeAnimation2);
        float floatValue4 = transformKeyframeAnimation2.endOpacity.getValue().floatValue() / 100.0f;
        int i2 = (int) floatValue;
        while (true) {
            i2--;
            if (i2 >= 0) {
                this.matrix.set(matrix);
                float f = i2;
                this.matrix.preConcat(this.transform.getMatrixForRepeater(f + floatValue2));
                PointF pointF = MiscUtils.pathFromDataCurrentPoint;
                this.contentGroup.draw(canvas, this.matrix, (int) ((((floatValue4 - floatValue3) * (f / floatValue)) + floatValue3) * i));
            } else {
                return;
            }
        }
    }

    @Override // com.airbnb.lottie.animation.content.DrawingContent
    public final void getBounds(RectF rectF, Matrix matrix, boolean z) {
        this.contentGroup.getBounds(rectF, matrix, z);
    }

    @Override // com.airbnb.lottie.animation.content.PathContent
    public final Path getPath() {
        Path path = this.contentGroup.getPath();
        this.path.reset();
        float floatValue = this.copies.getValue().floatValue();
        float floatValue2 = this.offset.getValue().floatValue();
        int i = (int) floatValue;
        while (true) {
            i--;
            if (i < 0) {
                return this.path;
            }
            this.matrix.set(this.transform.getMatrixForRepeater(i + floatValue2));
            this.path.addPath(path, this.matrix);
        }
    }

    @Override // com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation.AnimationListener
    public final void onValueChanged() {
        this.lottieDrawable.invalidateSelf();
    }

    @Override // com.airbnb.lottie.animation.content.Content
    public final void setContents(List<Content> list, List<Content> list2) {
        this.contentGroup.setContents(list, list2);
    }

    public RepeaterContent(LottieDrawable lottieDrawable, BaseLayer baseLayer, Repeater repeater) {
        this.lottieDrawable = lottieDrawable;
        this.layer = baseLayer;
        Objects.requireNonNull(repeater);
        this.name = repeater.name;
        this.hidden = repeater.hidden;
        BaseKeyframeAnimation<Float, Float> createAnimation = repeater.copies.createAnimation();
        this.copies = (FloatKeyframeAnimation) createAnimation;
        baseLayer.addAnimation(createAnimation);
        createAnimation.addUpdateListener(this);
        BaseKeyframeAnimation<Float, Float> createAnimation2 = repeater.offset.createAnimation();
        this.offset = (FloatKeyframeAnimation) createAnimation2;
        baseLayer.addAnimation(createAnimation2);
        createAnimation2.addUpdateListener(this);
        AnimatableTransform animatableTransform = repeater.transform;
        Objects.requireNonNull(animatableTransform);
        TransformKeyframeAnimation transformKeyframeAnimation = new TransformKeyframeAnimation(animatableTransform);
        this.transform = transformKeyframeAnimation;
        transformKeyframeAnimation.addAnimationsToLayer(baseLayer);
        transformKeyframeAnimation.addListener(this);
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
