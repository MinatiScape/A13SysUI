package com.airbnb.lottie.model.animatable;

import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.TextKeyframeAnimation;
import com.airbnb.lottie.model.DocumentData;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final class AnimatableTextFrame extends BaseAnimatableValue<DocumentData, DocumentData> {
    @Override // com.airbnb.lottie.model.animatable.AnimatableValue
    public final BaseKeyframeAnimation createAnimation() {
        return new TextKeyframeAnimation(this.keyframes);
    }

    public AnimatableTextFrame(ArrayList arrayList) {
        super(arrayList);
    }
}
