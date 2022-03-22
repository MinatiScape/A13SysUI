package com.airbnb.lottie.animation.keyframe;

import android.graphics.PointF;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
import com.airbnb.lottie.value.Keyframe;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.List;
/* loaded from: classes.dex */
public final class PointKeyframeAnimation extends KeyframeAnimation<PointF> {
    public final PointF point = new PointF();

    @Override // com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation
    public final Object getValue(Keyframe keyframe, float f) {
        T t;
        T t2 = keyframe.startValue;
        if (t2 == 0 || (t = keyframe.endValue) == 0) {
            throw new IllegalStateException("Missing values for keyframe.");
        }
        PointF pointF = (PointF) t2;
        PointF pointF2 = (PointF) t;
        LottieValueCallback<A> lottieValueCallback = this.valueCallback;
        if (lottieValueCallback != 0) {
            keyframe.endFrame.floatValue();
            getLinearCurrentKeyframeProgress();
            PointF pointF3 = (PointF) lottieValueCallback.getValueInternal(pointF, pointF2);
            if (pointF3 != null) {
                return pointF3;
            }
        }
        PointF pointF4 = this.point;
        float f2 = pointF.x;
        float m = MotionController$$ExternalSyntheticOutline0.m(pointF2.x, f2, f, f2);
        float f3 = pointF.y;
        pointF4.set(m, ((pointF2.y - f3) * f) + f3);
        return this.point;
    }

    public PointKeyframeAnimation(List<Keyframe<PointF>> list) {
        super(list);
    }
}
