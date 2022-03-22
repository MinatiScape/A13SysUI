package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.ColorKeyframeAnimation;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final class AnimatableColorValue extends BaseAnimatableValue<Integer, Integer> {
    @Override // com.airbnb.lottie.model.animatable.AnimatableValue
    public final BaseKeyframeAnimation<Integer, Integer> createAnimation() {
        return new ColorKeyframeAnimation(this.keyframes);
    }

    public AnimatableColorValue(ArrayList arrayList) {
        super(arrayList);
    }
}
