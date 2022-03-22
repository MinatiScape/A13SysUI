package com.airbnb.lottie.animation.content;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import androidx.appcompat.app.LayoutIncludeDetector;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.FloatKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.PointKeyframeAnimation;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.content.RectangleShape;
import com.airbnb.lottie.model.content.ShapeTrimPath;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class RectangleContent implements BaseKeyframeAnimation.AnimationListener, KeyPathElementContent, PathContent {
    public final FloatKeyframeAnimation cornerRadiusAnimation;
    public final boolean hidden;
    public boolean isPathValid;
    public final LottieDrawable lottieDrawable;
    public final String name;
    public final BaseKeyframeAnimation<?, PointF> positionAnimation;
    public final PointKeyframeAnimation sizeAnimation;
    public final Path path = new Path();
    public final RectF rect = new RectF();
    public LayoutIncludeDetector trimPaths = new LayoutIncludeDetector();

    @Override // com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation.AnimationListener
    public final void onValueChanged() {
        this.isPathValid = false;
        this.lottieDrawable.invalidateSelf();
    }

    @Override // com.airbnb.lottie.animation.content.Content
    public final void setContents(List<Content> list, List<Content> list2) {
        int i = 0;
        while (true) {
            ArrayList arrayList = (ArrayList) list;
            if (i < arrayList.size()) {
                Content content = (Content) arrayList.get(i);
                if (content instanceof TrimPathContent) {
                    TrimPathContent trimPathContent = (TrimPathContent) content;
                    Objects.requireNonNull(trimPathContent);
                    if (trimPathContent.type == ShapeTrimPath.Type.SIMULTANEOUSLY) {
                        LayoutIncludeDetector layoutIncludeDetector = this.trimPaths;
                        Objects.requireNonNull(layoutIncludeDetector);
                        ((List) layoutIncludeDetector.mXmlParserStack).add(trimPathContent);
                        trimPathContent.addListener(this);
                    }
                }
                i++;
            } else {
                return;
            }
        }
    }

    @Override // com.airbnb.lottie.model.KeyPathElement
    public final <T> void addValueCallback(T t, LottieValueCallback<T> lottieValueCallback) {
        if (t == LottieProperty.RECTANGLE_SIZE) {
            this.sizeAnimation.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.POSITION) {
            this.positionAnimation.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.CORNER_RADIUS) {
            this.cornerRadiusAnimation.setValueCallback(lottieValueCallback);
        }
    }

    @Override // com.airbnb.lottie.animation.content.PathContent
    public final Path getPath() {
        float f;
        if (this.isPathValid) {
            return this.path;
        }
        this.path.reset();
        if (this.hidden) {
            this.isPathValid = true;
            return this.path;
        }
        PointF value = this.sizeAnimation.getValue();
        float f2 = value.x / 2.0f;
        float f3 = value.y / 2.0f;
        FloatKeyframeAnimation floatKeyframeAnimation = this.cornerRadiusAnimation;
        if (floatKeyframeAnimation == null) {
            f = 0.0f;
        } else {
            f = floatKeyframeAnimation.getFloatValue();
        }
        float min = Math.min(f2, f3);
        if (f > min) {
            f = min;
        }
        PointF value2 = this.positionAnimation.getValue();
        this.path.moveTo(value2.x + f2, (value2.y - f3) + f);
        this.path.lineTo(value2.x + f2, (value2.y + f3) - f);
        int i = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
        if (i > 0) {
            RectF rectF = this.rect;
            float f4 = value2.x + f2;
            float f5 = f * 2.0f;
            float f6 = value2.y + f3;
            rectF.set(f4 - f5, f6 - f5, f4, f6);
            this.path.arcTo(this.rect, 0.0f, 90.0f, false);
        }
        this.path.lineTo((value2.x - f2) + f, value2.y + f3);
        if (i > 0) {
            RectF rectF2 = this.rect;
            float f7 = value2.x - f2;
            float f8 = value2.y + f3;
            float f9 = f * 2.0f;
            rectF2.set(f7, f8 - f9, f9 + f7, f8);
            this.path.arcTo(this.rect, 90.0f, 90.0f, false);
        }
        this.path.lineTo(value2.x - f2, (value2.y - f3) + f);
        if (i > 0) {
            RectF rectF3 = this.rect;
            float f10 = value2.x - f2;
            float f11 = value2.y - f3;
            float f12 = f * 2.0f;
            rectF3.set(f10, f11, f10 + f12, f12 + f11);
            this.path.arcTo(this.rect, 180.0f, 90.0f, false);
        }
        this.path.lineTo((value2.x + f2) - f, value2.y - f3);
        if (i > 0) {
            RectF rectF4 = this.rect;
            float f13 = value2.x + f2;
            float f14 = f * 2.0f;
            float f15 = value2.y - f3;
            rectF4.set(f13 - f14, f15, f13, f14 + f15);
            this.path.arcTo(this.rect, 270.0f, 90.0f, false);
        }
        this.path.close();
        this.trimPaths.apply(this.path);
        this.isPathValid = true;
        return this.path;
    }

    public RectangleContent(LottieDrawable lottieDrawable, BaseLayer baseLayer, RectangleShape rectangleShape) {
        Objects.requireNonNull(rectangleShape);
        this.name = rectangleShape.name;
        this.hidden = rectangleShape.hidden;
        this.lottieDrawable = lottieDrawable;
        BaseKeyframeAnimation<PointF, PointF> createAnimation = rectangleShape.position.createAnimation();
        this.positionAnimation = createAnimation;
        BaseKeyframeAnimation<?, ?> createAnimation2 = rectangleShape.size.createAnimation();
        this.sizeAnimation = (PointKeyframeAnimation) createAnimation2;
        BaseKeyframeAnimation<?, ?> createAnimation3 = rectangleShape.cornerRadius.createAnimation();
        this.cornerRadiusAnimation = (FloatKeyframeAnimation) createAnimation3;
        baseLayer.addAnimation(createAnimation);
        baseLayer.addAnimation(createAnimation2);
        baseLayer.addAnimation(createAnimation3);
        createAnimation.addUpdateListener(this);
        createAnimation2.addUpdateListener(this);
        createAnimation3.addUpdateListener(this);
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
