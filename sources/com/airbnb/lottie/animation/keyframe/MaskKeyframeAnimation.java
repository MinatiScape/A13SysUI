package com.airbnb.lottie.animation.keyframe;

import com.airbnb.lottie.model.content.Mask;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MaskKeyframeAnimation {
    public final ArrayList maskAnimations;
    public final List<Mask> masks;
    public final ArrayList opacityAnimations;

    public MaskKeyframeAnimation(List<Mask> list) {
        this.masks = list;
        this.maskAnimations = new ArrayList(list.size());
        this.opacityAnimations = new ArrayList(list.size());
        for (int i = 0; i < list.size(); i++) {
            ArrayList arrayList = this.maskAnimations;
            Mask mask = list.get(i);
            Objects.requireNonNull(mask);
            arrayList.add(mask.maskPath.createAnimation());
            Mask mask2 = list.get(i);
            Objects.requireNonNull(mask2);
            this.opacityAnimations.add(mask2.opacity.createAnimation());
        }
    }
}
