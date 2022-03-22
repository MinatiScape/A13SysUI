package com.airbnb.lottie.animation.keyframe;

import android.graphics.Matrix;
import android.graphics.PointF;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.animatable.AnimatableScaleValue;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.model.animatable.AnimatableValue;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.value.ScaleXY;
import com.android.systemui.qs.external.QSTileServiceWrapper;
import java.util.Collections;
import java.util.Objects;
/* loaded from: classes.dex */
public final class TransformKeyframeAnimation {
    public BaseKeyframeAnimation<PointF, PointF> anchorPoint;
    public BaseKeyframeAnimation<?, Float> endOpacity;
    public final Matrix matrix = new Matrix();
    public BaseKeyframeAnimation<Integer, Integer> opacity;
    public BaseKeyframeAnimation<?, PointF> position;
    public BaseKeyframeAnimation<Float, Float> rotation;
    public BaseKeyframeAnimation<ScaleXY, ScaleXY> scale;
    public FloatKeyframeAnimation skew;
    public FloatKeyframeAnimation skewAngle;
    public final Matrix skewMatrix1;
    public final Matrix skewMatrix2;
    public final Matrix skewMatrix3;
    public final float[] skewValues;
    public BaseKeyframeAnimation<?, Float> startOpacity;

    public final void addAnimationsToLayer(BaseLayer baseLayer) {
        baseLayer.addAnimation(this.opacity);
        baseLayer.addAnimation(this.startOpacity);
        baseLayer.addAnimation(this.endOpacity);
        baseLayer.addAnimation(this.anchorPoint);
        baseLayer.addAnimation(this.position);
        baseLayer.addAnimation(this.scale);
        baseLayer.addAnimation(this.rotation);
        baseLayer.addAnimation(this.skew);
        baseLayer.addAnimation(this.skewAngle);
    }

    public final void addListener(BaseKeyframeAnimation.AnimationListener animationListener) {
        BaseKeyframeAnimation<Integer, Integer> baseKeyframeAnimation = this.opacity;
        if (baseKeyframeAnimation != null) {
            baseKeyframeAnimation.addUpdateListener(animationListener);
        }
        BaseKeyframeAnimation<?, Float> baseKeyframeAnimation2 = this.startOpacity;
        if (baseKeyframeAnimation2 != null) {
            baseKeyframeAnimation2.addUpdateListener(animationListener);
        }
        BaseKeyframeAnimation<?, Float> baseKeyframeAnimation3 = this.endOpacity;
        if (baseKeyframeAnimation3 != null) {
            baseKeyframeAnimation3.addUpdateListener(animationListener);
        }
        BaseKeyframeAnimation<PointF, PointF> baseKeyframeAnimation4 = this.anchorPoint;
        if (baseKeyframeAnimation4 != null) {
            baseKeyframeAnimation4.addUpdateListener(animationListener);
        }
        BaseKeyframeAnimation<?, PointF> baseKeyframeAnimation5 = this.position;
        if (baseKeyframeAnimation5 != null) {
            baseKeyframeAnimation5.addUpdateListener(animationListener);
        }
        BaseKeyframeAnimation<ScaleXY, ScaleXY> baseKeyframeAnimation6 = this.scale;
        if (baseKeyframeAnimation6 != null) {
            baseKeyframeAnimation6.addUpdateListener(animationListener);
        }
        BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation7 = this.rotation;
        if (baseKeyframeAnimation7 != null) {
            baseKeyframeAnimation7.addUpdateListener(animationListener);
        }
        FloatKeyframeAnimation floatKeyframeAnimation = this.skew;
        if (floatKeyframeAnimation != null) {
            floatKeyframeAnimation.addUpdateListener(animationListener);
        }
        FloatKeyframeAnimation floatKeyframeAnimation2 = this.skewAngle;
        if (floatKeyframeAnimation2 != null) {
            floatKeyframeAnimation2.addUpdateListener(animationListener);
        }
    }

    public final <T> boolean applyValueCallback(T t, LottieValueCallback<T> lottieValueCallback) {
        FloatKeyframeAnimation floatKeyframeAnimation;
        FloatKeyframeAnimation floatKeyframeAnimation2;
        BaseKeyframeAnimation<?, Float> baseKeyframeAnimation;
        BaseKeyframeAnimation<?, Float> baseKeyframeAnimation2;
        if (t == LottieProperty.TRANSFORM_ANCHOR_POINT) {
            BaseKeyframeAnimation<PointF, PointF> baseKeyframeAnimation3 = this.anchorPoint;
            if (baseKeyframeAnimation3 == null) {
                this.anchorPoint = new ValueCallbackKeyframeAnimation(lottieValueCallback, new PointF());
                return true;
            }
            baseKeyframeAnimation3.setValueCallback(lottieValueCallback);
            return true;
        } else if (t == LottieProperty.TRANSFORM_POSITION) {
            BaseKeyframeAnimation<?, PointF> baseKeyframeAnimation4 = this.position;
            if (baseKeyframeAnimation4 == null) {
                this.position = new ValueCallbackKeyframeAnimation(lottieValueCallback, new PointF());
                return true;
            }
            baseKeyframeAnimation4.setValueCallback(lottieValueCallback);
            return true;
        } else if (t == LottieProperty.TRANSFORM_SCALE) {
            BaseKeyframeAnimation<ScaleXY, ScaleXY> baseKeyframeAnimation5 = this.scale;
            if (baseKeyframeAnimation5 == null) {
                this.scale = new ValueCallbackKeyframeAnimation(lottieValueCallback, new ScaleXY());
                return true;
            }
            baseKeyframeAnimation5.setValueCallback(lottieValueCallback);
            return true;
        } else if (t == LottieProperty.TRANSFORM_ROTATION) {
            BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation6 = this.rotation;
            if (baseKeyframeAnimation6 == null) {
                this.rotation = new ValueCallbackKeyframeAnimation(lottieValueCallback, Float.valueOf(0.0f));
                return true;
            }
            baseKeyframeAnimation6.setValueCallback(lottieValueCallback);
            return true;
        } else if (t == LottieProperty.TRANSFORM_OPACITY) {
            BaseKeyframeAnimation<Integer, Integer> baseKeyframeAnimation7 = this.opacity;
            if (baseKeyframeAnimation7 == null) {
                this.opacity = new ValueCallbackKeyframeAnimation(lottieValueCallback, 100);
                return true;
            }
            baseKeyframeAnimation7.setValueCallback(lottieValueCallback);
            return true;
        } else if (t != LottieProperty.TRANSFORM_START_OPACITY || (baseKeyframeAnimation2 = this.startOpacity) == null) {
            if (t != LottieProperty.TRANSFORM_END_OPACITY || (baseKeyframeAnimation = this.endOpacity) == null) {
                if (t == LottieProperty.TRANSFORM_SKEW && (floatKeyframeAnimation2 = this.skew) != null) {
                    if (floatKeyframeAnimation2 == null) {
                        this.skew = new FloatKeyframeAnimation(Collections.singletonList(new Keyframe(Float.valueOf(0.0f))));
                    }
                    this.skew.setValueCallback(lottieValueCallback);
                    return true;
                } else if (t != LottieProperty.TRANSFORM_SKEW_ANGLE || (floatKeyframeAnimation = this.skewAngle) == null) {
                    return false;
                } else {
                    if (floatKeyframeAnimation == null) {
                        this.skewAngle = new FloatKeyframeAnimation(Collections.singletonList(new Keyframe(Float.valueOf(0.0f))));
                    }
                    this.skewAngle.setValueCallback(lottieValueCallback);
                    return true;
                }
            } else if (baseKeyframeAnimation == null) {
                this.endOpacity = new ValueCallbackKeyframeAnimation(lottieValueCallback, 100);
                return true;
            } else {
                baseKeyframeAnimation.setValueCallback(lottieValueCallback);
                return true;
            }
        } else if (baseKeyframeAnimation2 == null) {
            this.startOpacity = new ValueCallbackKeyframeAnimation(lottieValueCallback, 100);
            return true;
        } else {
            baseKeyframeAnimation2.setValueCallback(lottieValueCallback);
            return true;
        }
    }

    public final Matrix getMatrix() {
        FloatKeyframeAnimation floatKeyframeAnimation;
        float f;
        FloatKeyframeAnimation floatKeyframeAnimation2;
        float f2;
        float f3;
        this.matrix.reset();
        BaseKeyframeAnimation<?, PointF> baseKeyframeAnimation = this.position;
        if (baseKeyframeAnimation != null) {
            PointF value = baseKeyframeAnimation.getValue();
            float f4 = value.x;
            if (!(f4 == 0.0f && value.y == 0.0f)) {
                this.matrix.preTranslate(f4, value.y);
            }
        }
        BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation2 = this.rotation;
        if (baseKeyframeAnimation2 != null) {
            if (baseKeyframeAnimation2 instanceof ValueCallbackKeyframeAnimation) {
                f3 = baseKeyframeAnimation2.getValue().floatValue();
            } else {
                f3 = ((FloatKeyframeAnimation) baseKeyframeAnimation2).getFloatValue();
            }
            if (f3 != 0.0f) {
                this.matrix.preRotate(f3);
            }
        }
        if (this.skew != null) {
            if (this.skewAngle == null) {
                f = 0.0f;
            } else {
                f = (float) Math.cos(Math.toRadians((-floatKeyframeAnimation.getFloatValue()) + 90.0f));
            }
            if (this.skewAngle == null) {
                f2 = 1.0f;
            } else {
                f2 = (float) Math.sin(Math.toRadians((-floatKeyframeAnimation2.getFloatValue()) + 90.0f));
            }
            float tan = (float) Math.tan(Math.toRadians(this.skew.getFloatValue()));
            for (int i = 0; i < 9; i++) {
                this.skewValues[i] = 0.0f;
            }
            float[] fArr = this.skewValues;
            fArr[0] = f;
            fArr[1] = f2;
            float f5 = -f2;
            fArr[3] = f5;
            fArr[4] = f;
            fArr[8] = 1.0f;
            this.skewMatrix1.setValues(fArr);
            for (int i2 = 0; i2 < 9; i2++) {
                this.skewValues[i2] = 0.0f;
            }
            float[] fArr2 = this.skewValues;
            fArr2[0] = 1.0f;
            fArr2[3] = tan;
            fArr2[4] = 1.0f;
            fArr2[8] = 1.0f;
            this.skewMatrix2.setValues(fArr2);
            for (int i3 = 0; i3 < 9; i3++) {
                this.skewValues[i3] = 0.0f;
            }
            float[] fArr3 = this.skewValues;
            fArr3[0] = f;
            fArr3[1] = f5;
            fArr3[3] = f2;
            fArr3[4] = f;
            fArr3[8] = 1.0f;
            this.skewMatrix3.setValues(fArr3);
            this.skewMatrix2.preConcat(this.skewMatrix1);
            this.skewMatrix3.preConcat(this.skewMatrix2);
            this.matrix.preConcat(this.skewMatrix3);
        }
        BaseKeyframeAnimation<ScaleXY, ScaleXY> baseKeyframeAnimation3 = this.scale;
        if (baseKeyframeAnimation3 != null) {
            ScaleXY value2 = baseKeyframeAnimation3.getValue();
            Objects.requireNonNull(value2);
            float f6 = value2.scaleX;
            if (!(f6 == 1.0f && value2.scaleY == 1.0f)) {
                this.matrix.preScale(f6, value2.scaleY);
            }
        }
        BaseKeyframeAnimation<PointF, PointF> baseKeyframeAnimation4 = this.anchorPoint;
        if (baseKeyframeAnimation4 != null) {
            PointF value3 = baseKeyframeAnimation4.getValue();
            float f7 = value3.x;
            if (!(f7 == 0.0f && value3.y == 0.0f)) {
                this.matrix.preTranslate(-f7, -value3.y);
            }
        }
        return this.matrix;
    }

    public final Matrix getMatrixForRepeater(float f) {
        PointF pointF;
        ScaleXY scaleXY;
        float f2;
        BaseKeyframeAnimation<?, PointF> baseKeyframeAnimation = this.position;
        PointF pointF2 = null;
        if (baseKeyframeAnimation == null) {
            pointF = null;
        } else {
            pointF = baseKeyframeAnimation.getValue();
        }
        BaseKeyframeAnimation<ScaleXY, ScaleXY> baseKeyframeAnimation2 = this.scale;
        if (baseKeyframeAnimation2 == null) {
            scaleXY = null;
        } else {
            scaleXY = baseKeyframeAnimation2.getValue();
        }
        this.matrix.reset();
        if (pointF != null) {
            this.matrix.preTranslate(pointF.x * f, pointF.y * f);
        }
        if (scaleXY != null) {
            double d = f;
            this.matrix.preScale((float) Math.pow(scaleXY.scaleX, d), (float) Math.pow(scaleXY.scaleY, d));
        }
        BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation3 = this.rotation;
        if (baseKeyframeAnimation3 != null) {
            float floatValue = baseKeyframeAnimation3.getValue().floatValue();
            BaseKeyframeAnimation<PointF, PointF> baseKeyframeAnimation4 = this.anchorPoint;
            if (baseKeyframeAnimation4 != null) {
                pointF2 = baseKeyframeAnimation4.getValue();
            }
            Matrix matrix = this.matrix;
            float f3 = floatValue * f;
            float f4 = 0.0f;
            if (pointF2 == null) {
                f2 = 0.0f;
            } else {
                f2 = pointF2.x;
            }
            if (pointF2 != null) {
                f4 = pointF2.y;
            }
            matrix.preRotate(f3, f2, f4);
        }
        return this.matrix;
    }

    public TransformKeyframeAnimation(AnimatableTransform animatableTransform) {
        BaseKeyframeAnimation<PointF, PointF> baseKeyframeAnimation;
        BaseKeyframeAnimation<PointF, PointF> baseKeyframeAnimation2;
        BaseKeyframeAnimation<ScaleXY, ScaleXY> baseKeyframeAnimation3;
        BaseKeyframeAnimation<Float, Float> baseKeyframeAnimation4;
        FloatKeyframeAnimation floatKeyframeAnimation;
        FloatKeyframeAnimation floatKeyframeAnimation2;
        Objects.requireNonNull(animatableTransform);
        QSTileServiceWrapper qSTileServiceWrapper = animatableTransform.anchorPoint;
        if (qSTileServiceWrapper == null) {
            baseKeyframeAnimation = null;
        } else {
            baseKeyframeAnimation = qSTileServiceWrapper.createAnimation();
        }
        this.anchorPoint = baseKeyframeAnimation;
        AnimatableValue<PointF, PointF> animatableValue = animatableTransform.position;
        if (animatableValue == null) {
            baseKeyframeAnimation2 = null;
        } else {
            baseKeyframeAnimation2 = animatableValue.createAnimation();
        }
        this.position = baseKeyframeAnimation2;
        AnimatableScaleValue animatableScaleValue = animatableTransform.scale;
        if (animatableScaleValue == null) {
            baseKeyframeAnimation3 = null;
        } else {
            baseKeyframeAnimation3 = animatableScaleValue.createAnimation();
        }
        this.scale = baseKeyframeAnimation3;
        AnimatableFloatValue animatableFloatValue = animatableTransform.rotation;
        if (animatableFloatValue == null) {
            baseKeyframeAnimation4 = null;
        } else {
            baseKeyframeAnimation4 = animatableFloatValue.createAnimation();
        }
        this.rotation = baseKeyframeAnimation4;
        AnimatableFloatValue animatableFloatValue2 = animatableTransform.skew;
        if (animatableFloatValue2 == null) {
            floatKeyframeAnimation = null;
        } else {
            floatKeyframeAnimation = (FloatKeyframeAnimation) animatableFloatValue2.createAnimation();
        }
        this.skew = floatKeyframeAnimation;
        if (floatKeyframeAnimation != null) {
            this.skewMatrix1 = new Matrix();
            this.skewMatrix2 = new Matrix();
            this.skewMatrix3 = new Matrix();
            this.skewValues = new float[9];
        } else {
            this.skewMatrix1 = null;
            this.skewMatrix2 = null;
            this.skewMatrix3 = null;
            this.skewValues = null;
        }
        AnimatableFloatValue animatableFloatValue3 = animatableTransform.skewAngle;
        if (animatableFloatValue3 == null) {
            floatKeyframeAnimation2 = null;
        } else {
            floatKeyframeAnimation2 = (FloatKeyframeAnimation) animatableFloatValue3.createAnimation();
        }
        this.skewAngle = floatKeyframeAnimation2;
        AnimatableIntegerValue animatableIntegerValue = animatableTransform.opacity;
        if (animatableIntegerValue != null) {
            this.opacity = animatableIntegerValue.createAnimation();
        }
        AnimatableFloatValue animatableFloatValue4 = animatableTransform.startOpacity;
        if (animatableFloatValue4 != null) {
            this.startOpacity = animatableFloatValue4.createAnimation();
        } else {
            this.startOpacity = null;
        }
        AnimatableFloatValue animatableFloatValue5 = animatableTransform.endOpacity;
        if (animatableFloatValue5 != null) {
            this.endOpacity = animatableFloatValue5.createAnimation();
        } else {
            this.endOpacity = null;
        }
    }
}
