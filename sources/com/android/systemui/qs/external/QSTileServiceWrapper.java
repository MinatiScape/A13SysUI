package com.android.systemui.qs.external;

import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.PathKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.PointKeyframeAnimation;
import com.airbnb.lottie.model.animatable.AnimatableValue;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;
/* loaded from: classes.dex */
public final class QSTileServiceWrapper implements AnimatableValue {
    public final Object mService;

    public /* synthetic */ QSTileServiceWrapper(Object obj) {
        this.mService = obj;
    }

    @Override // com.airbnb.lottie.model.animatable.AnimatableValue
    public final BaseKeyframeAnimation createAnimation() {
        if (((Keyframe) ((List) this.mService).get(0)).isStatic()) {
            return new PointKeyframeAnimation((List) this.mService);
        }
        return new PathKeyframeAnimation((List) this.mService);
    }

    @Override // com.airbnb.lottie.model.animatable.AnimatableValue
    public final List getKeyframes() {
        return (List) this.mService;
    }

    @Override // com.airbnb.lottie.model.animatable.AnimatableValue
    public final boolean isStatic() {
        if (((List) this.mService).size() != 1 || !((Keyframe) ((List) this.mService).get(0)).isStatic()) {
            return false;
        }
        return true;
    }
}
