package com.airbnb.lottie.animation.content;

import android.graphics.Path;
import android.graphics.PointF;
import androidx.appcompat.app.LayoutIncludeDetector;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.FloatKeyframeAnimation;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.content.PolystarShape;
import com.airbnb.lottie.model.content.ShapeTrimPath;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class PolystarContent implements PathContent, BaseKeyframeAnimation.AnimationListener, KeyPathElementContent {
    public final boolean hidden;
    public final FloatKeyframeAnimation innerRadiusAnimation;
    public final FloatKeyframeAnimation innerRoundednessAnimation;
    public boolean isPathValid;
    public final LottieDrawable lottieDrawable;
    public final String name;
    public final FloatKeyframeAnimation outerRadiusAnimation;
    public final FloatKeyframeAnimation outerRoundednessAnimation;
    public final FloatKeyframeAnimation pointsAnimation;
    public final BaseKeyframeAnimation<?, PointF> positionAnimation;
    public final FloatKeyframeAnimation rotationAnimation;
    public final PolystarShape.Type type;
    public final Path path = new Path();
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
        FloatKeyframeAnimation floatKeyframeAnimation;
        FloatKeyframeAnimation floatKeyframeAnimation2;
        if (t == LottieProperty.POLYSTAR_POINTS) {
            this.pointsAnimation.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.POLYSTAR_ROTATION) {
            this.rotationAnimation.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.POSITION) {
            this.positionAnimation.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.POLYSTAR_INNER_RADIUS && (floatKeyframeAnimation2 = this.innerRadiusAnimation) != null) {
            floatKeyframeAnimation2.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.POLYSTAR_OUTER_RADIUS) {
            this.outerRadiusAnimation.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.POLYSTAR_INNER_ROUNDEDNESS && (floatKeyframeAnimation = this.innerRoundednessAnimation) != null) {
            floatKeyframeAnimation.setValueCallback(lottieValueCallback);
        } else if (t == LottieProperty.POLYSTAR_OUTER_ROUNDEDNESS) {
            this.outerRoundednessAnimation.setValueCallback(lottieValueCallback);
        }
    }

    @Override // com.airbnb.lottie.animation.content.PathContent
    public final Path getPath() {
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        double d;
        float f7;
        float f8;
        float f9;
        float f10;
        float f11;
        float f12;
        double d2;
        float f13;
        float f14;
        float f15;
        float f16;
        float f17;
        float f18;
        double d3;
        double d4;
        double d5;
        if (this.isPathValid) {
            return this.path;
        }
        this.path.reset();
        if (this.hidden) {
            this.isPathValid = true;
            return this.path;
        }
        int ordinal = this.type.ordinal();
        double d6 = 0.0d;
        if (ordinal == 0) {
            float floatValue = this.pointsAnimation.getValue().floatValue();
            FloatKeyframeAnimation floatKeyframeAnimation = this.rotationAnimation;
            if (floatKeyframeAnimation != null) {
                d6 = floatKeyframeAnimation.getValue().floatValue();
            }
            double radians = Math.toRadians(d6 - 90.0d);
            double d7 = floatValue;
            float f19 = (float) (6.283185307179586d / d7);
            float f20 = f19 / 2.0f;
            float f21 = floatValue - ((int) floatValue);
            int i = (f21 > 0.0f ? 1 : (f21 == 0.0f ? 0 : -1));
            if (i != 0) {
                radians += (1.0f - f21) * f20;
            }
            float floatValue2 = this.outerRadiusAnimation.getValue().floatValue();
            float floatValue3 = this.innerRadiusAnimation.getValue().floatValue();
            FloatKeyframeAnimation floatKeyframeAnimation2 = this.innerRoundednessAnimation;
            if (floatKeyframeAnimation2 != null) {
                f = floatKeyframeAnimation2.getValue().floatValue() / 100.0f;
            } else {
                f = 0.0f;
            }
            FloatKeyframeAnimation floatKeyframeAnimation3 = this.outerRoundednessAnimation;
            if (floatKeyframeAnimation3 != null) {
                f2 = floatKeyframeAnimation3.getValue().floatValue() / 100.0f;
            } else {
                f2 = 0.0f;
            }
            if (i != 0) {
                f7 = MotionController$$ExternalSyntheticOutline0.m(floatValue2, floatValue3, f21, floatValue3);
                double d8 = f7;
                f4 = floatValue3;
                f3 = f;
                f6 = (float) (Math.cos(radians) * d8);
                f5 = (float) (d8 * Math.sin(radians));
                this.path.moveTo(f6, f5);
                d = radians + ((f19 * f21) / 2.0f);
            } else {
                f4 = floatValue3;
                f3 = f;
                double d9 = floatValue2;
                float cos = (float) (Math.cos(radians) * d9);
                f5 = (float) (Math.sin(radians) * d9);
                this.path.moveTo(cos, f5);
                d = radians + f20;
                f6 = cos;
                f7 = 0.0f;
            }
            double ceil = Math.ceil(d7) * 2.0d;
            int i2 = 0;
            boolean z = false;
            while (true) {
                double d10 = i2;
                if (d10 >= ceil) {
                    break;
                }
                if (z) {
                    f8 = floatValue2;
                } else {
                    f8 = f4;
                }
                int i3 = (f7 > 0.0f ? 1 : (f7 == 0.0f ? 0 : -1));
                if (i3 == 0 || d10 != ceil - 2.0d) {
                    f9 = f19;
                    f10 = f20;
                } else {
                    f9 = f19;
                    f10 = (f19 * f21) / 2.0f;
                }
                if (i3 == 0 || d10 != ceil - 1.0d) {
                    f11 = f7;
                    f7 = f8;
                    f12 = f10;
                } else {
                    f12 = f10;
                    f11 = f7;
                }
                double d11 = f7;
                float cos2 = (float) (Math.cos(d) * d11);
                float sin = (float) (d11 * Math.sin(d));
                if (f3 == 0.0f && f2 == 0.0f) {
                    this.path.lineTo(cos2, sin);
                    f13 = sin;
                    d2 = d;
                    f14 = f2;
                } else {
                    d2 = d;
                    double atan2 = (float) (Math.atan2(f5, f6) - 1.5707963267948966d);
                    float cos3 = (float) Math.cos(atan2);
                    float sin2 = (float) Math.sin(atan2);
                    f13 = sin;
                    f14 = f2;
                    double atan22 = (float) (Math.atan2(sin, cos2) - 1.5707963267948966d);
                    float cos4 = (float) Math.cos(atan22);
                    float sin3 = (float) Math.sin(atan22);
                    if (z) {
                        f15 = f3;
                    } else {
                        f15 = f14;
                    }
                    if (z) {
                        f16 = f14;
                    } else {
                        f16 = f3;
                    }
                    if (z) {
                        f17 = f4;
                    } else {
                        f17 = floatValue2;
                    }
                    if (z) {
                        f18 = floatValue2;
                    } else {
                        f18 = f4;
                    }
                    float f22 = f17 * f15 * 0.47829f;
                    float f23 = cos3 * f22;
                    float f24 = f22 * sin2;
                    float f25 = f18 * f16 * 0.47829f;
                    float f26 = cos4 * f25;
                    float f27 = f25 * sin3;
                    if (i != 0) {
                        if (i2 == 0) {
                            f23 *= f21;
                            f24 *= f21;
                        } else if (d10 == ceil - 1.0d) {
                            f26 *= f21;
                            f27 *= f21;
                        }
                    }
                    this.path.cubicTo(f6 - f23, f5 - f24, cos2 + f26, f13 + f27, cos2, f13);
                }
                d = d2 + f12;
                z = !z;
                i2++;
                f6 = cos2;
                f7 = f11;
                f19 = f9;
                f5 = f13;
                f2 = f14;
            }
            PointF value = this.positionAnimation.getValue();
            this.path.offset(value.x, value.y);
            this.path.close();
        } else if (ordinal == 1) {
            int floor = (int) Math.floor(this.pointsAnimation.getValue().floatValue());
            FloatKeyframeAnimation floatKeyframeAnimation4 = this.rotationAnimation;
            if (floatKeyframeAnimation4 != null) {
                d6 = floatKeyframeAnimation4.getValue().floatValue();
            }
            double radians2 = Math.toRadians(d6 - 90.0d);
            double d12 = floor;
            float floatValue4 = this.outerRoundednessAnimation.getValue().floatValue() / 100.0f;
            float floatValue5 = this.outerRadiusAnimation.getValue().floatValue();
            double d13 = floatValue5;
            float cos5 = (float) (Math.cos(radians2) * d13);
            float sin4 = (float) (Math.sin(radians2) * d13);
            this.path.moveTo(cos5, sin4);
            double d14 = (float) (6.283185307179586d / d12);
            double d15 = radians2 + d14;
            double ceil2 = Math.ceil(d12);
            int i4 = 0;
            while (i4 < ceil2) {
                float cos6 = (float) (Math.cos(d15) * d13);
                float sin5 = (float) (Math.sin(d15) * d13);
                if (floatValue4 != 0.0f) {
                    d5 = d13;
                    d4 = d15;
                    double atan23 = (float) (Math.atan2(sin4, cos5) - 1.5707963267948966d);
                    float cos7 = (float) Math.cos(atan23);
                    d3 = d14;
                    double atan24 = (float) (Math.atan2(sin5, cos6) - 1.5707963267948966d);
                    float f28 = floatValue5 * floatValue4 * 0.25f;
                    this.path.cubicTo(cos5 - (cos7 * f28), sin4 - (((float) Math.sin(atan23)) * f28), cos6 + (((float) Math.cos(atan24)) * f28), sin5 + (f28 * ((float) Math.sin(atan24))), cos6, sin5);
                } else {
                    d4 = d15;
                    d5 = d13;
                    d3 = d14;
                    this.path.lineTo(cos6, sin5);
                }
                d15 = d4 + d3;
                i4++;
                sin4 = sin5;
                cos5 = cos6;
                ceil2 = ceil2;
                d13 = d5;
                d14 = d3;
            }
            PointF value2 = this.positionAnimation.getValue();
            this.path.offset(value2.x, value2.y);
            this.path.close();
        }
        this.path.close();
        this.trimPaths.apply(this.path);
        this.isPathValid = true;
        return this.path;
    }

    public PolystarContent(LottieDrawable lottieDrawable, BaseLayer baseLayer, PolystarShape polystarShape) {
        this.lottieDrawable = lottieDrawable;
        Objects.requireNonNull(polystarShape);
        this.name = polystarShape.name;
        PolystarShape.Type type = polystarShape.type;
        this.type = type;
        this.hidden = polystarShape.hidden;
        BaseKeyframeAnimation<?, ?> createAnimation = polystarShape.points.createAnimation();
        this.pointsAnimation = (FloatKeyframeAnimation) createAnimation;
        BaseKeyframeAnimation<PointF, PointF> createAnimation2 = polystarShape.position.createAnimation();
        this.positionAnimation = createAnimation2;
        BaseKeyframeAnimation<?, ?> createAnimation3 = polystarShape.rotation.createAnimation();
        this.rotationAnimation = (FloatKeyframeAnimation) createAnimation3;
        BaseKeyframeAnimation<?, ?> createAnimation4 = polystarShape.outerRadius.createAnimation();
        this.outerRadiusAnimation = (FloatKeyframeAnimation) createAnimation4;
        BaseKeyframeAnimation<?, ?> createAnimation5 = polystarShape.outerRoundedness.createAnimation();
        this.outerRoundednessAnimation = (FloatKeyframeAnimation) createAnimation5;
        PolystarShape.Type type2 = PolystarShape.Type.STAR;
        if (type == type2) {
            this.innerRadiusAnimation = (FloatKeyframeAnimation) polystarShape.innerRadius.createAnimation();
            this.innerRoundednessAnimation = (FloatKeyframeAnimation) polystarShape.innerRoundedness.createAnimation();
        } else {
            this.innerRadiusAnimation = null;
            this.innerRoundednessAnimation = null;
        }
        baseLayer.addAnimation(createAnimation);
        baseLayer.addAnimation(createAnimation2);
        baseLayer.addAnimation(createAnimation3);
        baseLayer.addAnimation(createAnimation4);
        baseLayer.addAnimation(createAnimation5);
        if (type == type2) {
            baseLayer.addAnimation(this.innerRadiusAnimation);
            baseLayer.addAnimation(this.innerRoundednessAnimation);
        }
        createAnimation.addUpdateListener(this);
        createAnimation2.addUpdateListener(this);
        createAnimation3.addUpdateListener(this);
        createAnimation4.addUpdateListener(this);
        createAnimation5.addUpdateListener(this);
        if (type == type2) {
            this.innerRadiusAnimation.addUpdateListener(this);
            this.innerRoundednessAnimation.addUpdateListener(this);
        }
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
