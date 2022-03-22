package com.airbnb.lottie.animation.keyframe;

import android.graphics.PointF;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.List;
/* loaded from: classes.dex */
public final class FloatKeyframeAnimation extends KeyframeAnimation<Float> {
    public final float getFloatValue(Keyframe<Float> keyframe, float f) {
        if (keyframe.startValue == null || keyframe.endValue == null) {
            throw new IllegalStateException("Missing values for keyframe.");
        }
        LottieValueCallback<A> lottieValueCallback = this.valueCallback;
        if (lottieValueCallback != 0) {
            keyframe.endFrame.floatValue();
            Float f2 = keyframe.startValue;
            Float f3 = keyframe.endValue;
            getLinearCurrentKeyframeProgress();
            Float f4 = (Float) lottieValueCallback.getValueInternal(f2, f3);
            if (f4 != null) {
                return f4.floatValue();
            }
        }
        if (keyframe.startValueFloat == -3987645.8f) {
            keyframe.startValueFloat = keyframe.startValue.floatValue();
        }
        float f5 = keyframe.startValueFloat;
        if (keyframe.endValueFloat == -3987645.8f) {
            keyframe.endValueFloat = keyframe.endValue.floatValue();
        }
        float f6 = keyframe.endValueFloat;
        PointF pointF = MiscUtils.pathFromDataCurrentPoint;
        return MotionController$$ExternalSyntheticOutline0.m(f6, f5, f, f5);
    }

    @Override // com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation
    public final Object getValue(Keyframe keyframe, float f) {
        return Float.valueOf(getFloatValue(keyframe, f));
    }

    public final float getFloatValue() {
        return getFloatValue(getCurrentKeyframe(), getInterpolatedCurrentKeyframeProgress());
    }

    public FloatKeyframeAnimation(List<Keyframe<Float>> list) {
        super(list);
    }
}
