package com.airbnb.lottie.animation.content;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RectF;
import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.LPaint;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.FloatKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.IntegerKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.ValueCallbackKeyframeAnimation;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.content.ShapeTrimPath;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class BaseStrokeContent implements BaseKeyframeAnimation.AnimationListener, KeyPathElementContent, DrawingContent {
    public ValueCallbackKeyframeAnimation colorFilterAnimation;
    public final ArrayList dashPatternAnimations;
    public final FloatKeyframeAnimation dashPatternOffsetAnimation;
    public final float[] dashPatternValues;
    public final BaseLayer layer;
    public final LottieDrawable lottieDrawable;
    public final IntegerKeyframeAnimation opacityAnimation;
    public final LPaint paint;
    public final FloatKeyframeAnimation widthAnimation;
    public final PathMeasure pm = new PathMeasure();
    public final Path path = new Path();
    public final Path trimPathPath = new Path();
    public final RectF rect = new RectF();
    public final ArrayList pathGroups = new ArrayList();

    /* loaded from: classes.dex */
    public static final class PathGroup {
        public final ArrayList paths = new ArrayList();
        public final TrimPathContent trimPath;

        public PathGroup(TrimPathContent trimPathContent) {
            this.trimPath = trimPathContent;
        }
    }

    @Override // com.airbnb.lottie.model.KeyPathElement
    public <T> void addValueCallback(T t, LottieValueCallback<T> lottieValueCallback) {
        if (t == LottieProperty.OPACITY) {
            this.opacityAnimation.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.STROKE_WIDTH) {
            this.widthAnimation.setValueCallback(lottieValueCallback);
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
    public void draw(Canvas canvas, Matrix matrix, int i) {
        boolean z;
        IntegerKeyframeAnimation integerKeyframeAnimation;
        float f;
        float f2;
        float f3;
        float f4;
        float[] fArr = Utils.points;
        boolean z2 = false;
        fArr[0] = 0.0f;
        fArr[1] = 0.0f;
        fArr[2] = 37394.73f;
        fArr[3] = 39575.234f;
        matrix.mapPoints(fArr);
        if (fArr[0] == fArr[2] || fArr[1] == fArr[3]) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            L.endSection();
            return;
        }
        Objects.requireNonNull(this.opacityAnimation);
        float intValue = (i / 255.0f) * integerKeyframeAnimation.getIntValue(integerKeyframeAnimation.getCurrentKeyframe(), integerKeyframeAnimation.getInterpolatedCurrentKeyframeProgress());
        float f5 = 100.0f;
        LPaint lPaint = this.paint;
        PointF pointF = MiscUtils.pathFromDataCurrentPoint;
        lPaint.setAlpha(Math.max(0, Math.min(255, (int) ((intValue / 100.0f) * 255.0f))));
        this.paint.setStrokeWidth(Utils.getScale(matrix) * this.widthAnimation.getFloatValue());
        if (this.paint.getStrokeWidth() <= 0.0f) {
            L.endSection();
            return;
        }
        float f6 = 1.0f;
        if (this.dashPatternAnimations.isEmpty()) {
            L.endSection();
        } else {
            float scale = Utils.getScale(matrix);
            for (int i2 = 0; i2 < this.dashPatternAnimations.size(); i2++) {
                this.dashPatternValues[i2] = ((Float) ((BaseKeyframeAnimation) this.dashPatternAnimations.get(i2)).getValue()).floatValue();
                if (i2 % 2 == 0) {
                    float[] fArr2 = this.dashPatternValues;
                    if (fArr2[i2] < 1.0f) {
                        fArr2[i2] = 1.0f;
                    }
                } else {
                    float[] fArr3 = this.dashPatternValues;
                    if (fArr3[i2] < 0.1f) {
                        fArr3[i2] = 0.1f;
                    }
                }
                float[] fArr4 = this.dashPatternValues;
                fArr4[i2] = fArr4[i2] * scale;
            }
            FloatKeyframeAnimation floatKeyframeAnimation = this.dashPatternOffsetAnimation;
            if (floatKeyframeAnimation == null) {
                f4 = 0.0f;
            } else {
                f4 = floatKeyframeAnimation.getValue().floatValue() * scale;
            }
            this.paint.setPathEffect(new DashPathEffect(this.dashPatternValues, f4));
            L.endSection();
        }
        ValueCallbackKeyframeAnimation valueCallbackKeyframeAnimation = this.colorFilterAnimation;
        if (valueCallbackKeyframeAnimation != null) {
            this.paint.setColorFilter((ColorFilter) valueCallbackKeyframeAnimation.getValue());
        }
        int i3 = 0;
        while (i3 < this.pathGroups.size()) {
            PathGroup pathGroup = (PathGroup) this.pathGroups.get(i3);
            if (pathGroup.trimPath != null) {
                this.path.reset();
                int size = pathGroup.paths.size();
                while (true) {
                    size--;
                    if (size < 0) {
                        break;
                    }
                    this.path.addPath(((PathContent) pathGroup.paths.get(size)).getPath(), matrix);
                }
                this.pm.setPath(this.path, z2);
                float length = this.pm.getLength();
                while (this.pm.nextContour()) {
                    length += this.pm.getLength();
                }
                TrimPathContent trimPathContent = pathGroup.trimPath;
                Objects.requireNonNull(trimPathContent);
                float floatValue = (trimPathContent.offsetAnimation.getValue().floatValue() * length) / 360.0f;
                TrimPathContent trimPathContent2 = pathGroup.trimPath;
                Objects.requireNonNull(trimPathContent2);
                float floatValue2 = ((trimPathContent2.startAnimation.getValue().floatValue() * length) / f5) + floatValue;
                TrimPathContent trimPathContent3 = pathGroup.trimPath;
                Objects.requireNonNull(trimPathContent3);
                float floatValue3 = ((trimPathContent3.endAnimation.getValue().floatValue() * length) / f5) + floatValue;
                int size2 = pathGroup.paths.size() - 1;
                float f7 = 0.0f;
                while (size2 >= 0) {
                    this.trimPathPath.set(((PathContent) pathGroup.paths.get(size2)).getPath());
                    this.trimPathPath.transform(matrix);
                    this.pm.setPath(this.trimPathPath, z2);
                    float length2 = this.pm.getLength();
                    if (floatValue3 > length) {
                        float f8 = floatValue3 - length;
                        if (f8 < f7 + length2 && f7 < f8) {
                            if (floatValue2 > length) {
                                f3 = (floatValue2 - length) / length2;
                            } else {
                                f3 = 0.0f;
                            }
                            Utils.applyTrimPathIfNeeded(this.trimPathPath, f3, Math.min(f8 / length2, f6), 0.0f);
                            canvas.drawPath(this.trimPathPath, this.paint);
                            f7 += length2;
                            size2--;
                            z2 = false;
                            f6 = 1.0f;
                        }
                    }
                    float f9 = f7 + length2;
                    if (f9 >= floatValue2 && f7 <= floatValue3) {
                        if (f9 > floatValue3 || floatValue2 >= f7) {
                            if (floatValue2 < f7) {
                                f = 0.0f;
                            } else {
                                f = (floatValue2 - f7) / length2;
                            }
                            if (floatValue3 > f9) {
                                f2 = 1.0f;
                            } else {
                                f2 = (floatValue3 - f7) / length2;
                            }
                            Utils.applyTrimPathIfNeeded(this.trimPathPath, f, f2, 0.0f);
                            canvas.drawPath(this.trimPathPath, this.paint);
                        } else {
                            canvas.drawPath(this.trimPathPath, this.paint);
                        }
                    }
                    f7 += length2;
                    size2--;
                    z2 = false;
                    f6 = 1.0f;
                }
                L.endSection();
            } else {
                this.path.reset();
                for (int size3 = pathGroup.paths.size() - 1; size3 >= 0; size3--) {
                    this.path.addPath(((PathContent) pathGroup.paths.get(size3)).getPath(), matrix);
                }
                L.endSection();
                canvas.drawPath(this.path, this.paint);
                L.endSection();
            }
            i3++;
            z2 = false;
            f6 = 1.0f;
            f5 = 100.0f;
        }
        L.endSection();
    }

    @Override // com.airbnb.lottie.animation.content.DrawingContent
    public final void getBounds(RectF rectF, Matrix matrix, boolean z) {
        this.path.reset();
        for (int i = 0; i < this.pathGroups.size(); i++) {
            PathGroup pathGroup = (PathGroup) this.pathGroups.get(i);
            for (int i2 = 0; i2 < pathGroup.paths.size(); i2++) {
                this.path.addPath(((PathContent) pathGroup.paths.get(i2)).getPath(), matrix);
            }
        }
        this.path.computeBounds(this.rect, false);
        float floatValue = this.widthAnimation.getFloatValue();
        RectF rectF2 = this.rect;
        float f = floatValue / 2.0f;
        rectF2.set(rectF2.left - f, rectF2.top - f, rectF2.right + f, rectF2.bottom + f);
        rectF.set(this.rect);
        rectF.set(rectF.left - 1.0f, rectF.top - 1.0f, rectF.right + 1.0f, rectF.bottom + 1.0f);
        L.endSection();
    }

    @Override // com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation.AnimationListener
    public final void onValueChanged() {
        this.lottieDrawable.invalidateSelf();
    }

    @Override // com.airbnb.lottie.animation.content.Content
    public final void setContents(List<Content> list, List<Content> list2) {
        ShapeTrimPath.Type type = ShapeTrimPath.Type.INDIVIDUALLY;
        ArrayList arrayList = (ArrayList) list;
        PathGroup pathGroup = null;
        TrimPathContent trimPathContent = null;
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            Content content = (Content) arrayList.get(size);
            if (content instanceof TrimPathContent) {
                TrimPathContent trimPathContent2 = (TrimPathContent) content;
                Objects.requireNonNull(trimPathContent2);
                if (trimPathContent2.type == type) {
                    trimPathContent = trimPathContent2;
                }
            }
        }
        if (trimPathContent != null) {
            trimPathContent.addListener(this);
        }
        int size2 = list2.size();
        while (true) {
            size2--;
            if (size2 < 0) {
                break;
            }
            Content content2 = list2.get(size2);
            if (content2 instanceof TrimPathContent) {
                TrimPathContent trimPathContent3 = (TrimPathContent) content2;
                Objects.requireNonNull(trimPathContent3);
                if (trimPathContent3.type == type) {
                    if (pathGroup != null) {
                        this.pathGroups.add(pathGroup);
                    }
                    PathGroup pathGroup2 = new PathGroup(trimPathContent3);
                    trimPathContent3.addListener(this);
                    pathGroup = pathGroup2;
                }
            }
            if (content2 instanceof PathContent) {
                if (pathGroup == null) {
                    pathGroup = new PathGroup(trimPathContent);
                }
                pathGroup.paths.add((PathContent) content2);
            }
        }
        if (pathGroup != null) {
            this.pathGroups.add(pathGroup);
        }
    }

    public BaseStrokeContent(LottieDrawable lottieDrawable, BaseLayer baseLayer, Paint.Cap cap, Paint.Join join, float f, AnimatableIntegerValue animatableIntegerValue, AnimatableFloatValue animatableFloatValue, List<AnimatableFloatValue> list, AnimatableFloatValue animatableFloatValue2) {
        LPaint lPaint = new LPaint(1);
        this.paint = lPaint;
        this.lottieDrawable = lottieDrawable;
        this.layer = baseLayer;
        lPaint.setStyle(Paint.Style.STROKE);
        lPaint.setStrokeCap(cap);
        lPaint.setStrokeJoin(join);
        lPaint.setStrokeMiter(f);
        this.opacityAnimation = (IntegerKeyframeAnimation) animatableIntegerValue.createAnimation();
        this.widthAnimation = (FloatKeyframeAnimation) animatableFloatValue.createAnimation();
        if (animatableFloatValue2 == null) {
            this.dashPatternOffsetAnimation = null;
        } else {
            this.dashPatternOffsetAnimation = (FloatKeyframeAnimation) animatableFloatValue2.createAnimation();
        }
        this.dashPatternAnimations = new ArrayList(list.size());
        this.dashPatternValues = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            this.dashPatternAnimations.add(list.get(i).createAnimation());
        }
        baseLayer.addAnimation(this.opacityAnimation);
        baseLayer.addAnimation(this.widthAnimation);
        for (int i2 = 0; i2 < this.dashPatternAnimations.size(); i2++) {
            baseLayer.addAnimation((BaseKeyframeAnimation) this.dashPatternAnimations.get(i2));
        }
        FloatKeyframeAnimation floatKeyframeAnimation = this.dashPatternOffsetAnimation;
        if (floatKeyframeAnimation != null) {
            baseLayer.addAnimation(floatKeyframeAnimation);
        }
        this.opacityAnimation.addUpdateListener(this);
        this.widthAnimation.addUpdateListener(this);
        for (int i3 = 0; i3 < list.size(); i3++) {
            ((BaseKeyframeAnimation) this.dashPatternAnimations.get(i3)).addUpdateListener(this);
        }
        FloatKeyframeAnimation floatKeyframeAnimation2 = this.dashPatternOffsetAnimation;
        if (floatKeyframeAnimation2 != null) {
            floatKeyframeAnimation2.addUpdateListener(this);
        }
    }

    @Override // com.airbnb.lottie.model.KeyPathElement
    public final void resolveKeyPath(KeyPath keyPath, int i, ArrayList arrayList, KeyPath keyPath2) {
        MiscUtils.resolveKeyPath(keyPath, i, arrayList, keyPath2, this);
    }
}
