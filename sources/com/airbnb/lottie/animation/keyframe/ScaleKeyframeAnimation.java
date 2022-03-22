package com.airbnb.lottie.animation.keyframe;

import android.graphics.PointF;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.value.ScaleXY;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ScaleKeyframeAnimation extends KeyframeAnimation<ScaleXY> {
    public final ScaleXY scaleXY = new ScaleXY();

    @Override // com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation
    public final Object getValue(Keyframe keyframe, float f) {
        T t;
        T t2 = keyframe.startValue;
        if (t2 == 0 || (t = keyframe.endValue) == 0) {
            throw new IllegalStateException("Missing values for keyframe.");
        }
        ScaleXY scaleXY = (ScaleXY) t2;
        ScaleXY scaleXY2 = (ScaleXY) t;
        LottieValueCallback<A> lottieValueCallback = this.valueCallback;
        if (lottieValueCallback != 0) {
            keyframe.endFrame.floatValue();
            getLinearCurrentKeyframeProgress();
            ScaleXY scaleXY3 = (ScaleXY) lottieValueCallback.getValueInternal(scaleXY, scaleXY2);
            if (scaleXY3 != null) {
                return scaleXY3;
            }
        }
        ScaleXY scaleXY4 = this.scaleXY;
        float f2 = scaleXY.scaleX;
        float f3 = scaleXY2.scaleX;
        PointF pointF = MiscUtils.pathFromDataCurrentPoint;
        float m = MotionController$$ExternalSyntheticOutline0.m(f3, f2, f, f2);
        float f4 = scaleXY.scaleY;
        Objects.requireNonNull(scaleXY4);
        scaleXY4.scaleX = m;
        scaleXY4.scaleY = ((scaleXY2.scaleY - f4) * f) + f4;
        return this.scaleXY;
    }

    public ScaleKeyframeAnimation(List<Keyframe<ScaleXY>> list) {
        super(list);
    }
}
