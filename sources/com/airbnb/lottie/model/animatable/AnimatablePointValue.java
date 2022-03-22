package com.airbnb.lottie.model.animatable;

import android.graphics.PointF;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.PointKeyframeAnimation;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final class AnimatablePointValue extends BaseAnimatableValue<PointF, PointF> {
    @Override // com.airbnb.lottie.model.animatable.AnimatableValue
    public final BaseKeyframeAnimation<PointF, PointF> createAnimation() {
        return new PointKeyframeAnimation(this.keyframes);
    }

    public AnimatablePointValue(ArrayList arrayList) {
        super(arrayList);
    }
}
