package com.airbnb.lottie.animation.keyframe;

import androidx.leanback.R$style;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.List;
/* loaded from: classes.dex */
public final class ColorKeyframeAnimation extends KeyframeAnimation<Integer> {
    public final int getIntValue(Keyframe<Integer> keyframe, float f) {
        Integer num = keyframe.startValue;
        if (num == null || keyframe.endValue == null) {
            throw new IllegalStateException("Missing values for keyframe.");
        }
        int intValue = num.intValue();
        int intValue2 = keyframe.endValue.intValue();
        LottieValueCallback<A> lottieValueCallback = this.valueCallback;
        if (lottieValueCallback != 0) {
            keyframe.endFrame.floatValue();
            Integer valueOf = Integer.valueOf(intValue);
            Integer valueOf2 = Integer.valueOf(intValue2);
            getLinearCurrentKeyframeProgress();
            Integer num2 = (Integer) lottieValueCallback.getValueInternal(valueOf, valueOf2);
            if (num2 != null) {
                return num2.intValue();
            }
        }
        return R$style.evaluate(MiscUtils.clamp(f, 0.0f, 1.0f), intValue, intValue2);
    }

    @Override // com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation
    public final Object getValue(Keyframe keyframe, float f) {
        return Integer.valueOf(getIntValue(keyframe, f));
    }

    public ColorKeyframeAnimation(List<Keyframe<Integer>> list) {
        super(list);
    }
}
