package com.airbnb.lottie.animation.keyframe;

import android.graphics.PointF;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.List;
/* loaded from: classes.dex */
public final class IntegerKeyframeAnimation extends KeyframeAnimation<Integer> {
    public final int getIntValue(Keyframe<Integer> keyframe, float f) {
        if (keyframe.startValue == null || keyframe.endValue == null) {
            throw new IllegalStateException("Missing values for keyframe.");
        }
        LottieValueCallback<A> lottieValueCallback = this.valueCallback;
        if (lottieValueCallback != 0) {
            keyframe.endFrame.floatValue();
            Integer num = keyframe.startValue;
            Integer num2 = keyframe.endValue;
            getLinearCurrentKeyframeProgress();
            Integer num3 = (Integer) lottieValueCallback.getValueInternal(num, num2);
            if (num3 != null) {
                return num3.intValue();
            }
        }
        if (keyframe.startValueInt == 784923401) {
            keyframe.startValueInt = keyframe.startValue.intValue();
        }
        int i = keyframe.startValueInt;
        if (keyframe.endValueInt == 784923401) {
            keyframe.endValueInt = keyframe.endValue.intValue();
        }
        int i2 = keyframe.endValueInt;
        PointF pointF = MiscUtils.pathFromDataCurrentPoint;
        return (int) ((f * (i2 - i)) + i);
    }

    @Override // com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation
    public final Object getValue(Keyframe keyframe, float f) {
        return Integer.valueOf(getIntValue(keyframe, f));
    }

    public IntegerKeyframeAnimation(List<Keyframe<Integer>> list) {
        super(list);
    }
}
