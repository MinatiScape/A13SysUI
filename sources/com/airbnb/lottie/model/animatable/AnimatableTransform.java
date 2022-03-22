package com.airbnb.lottie.model.animatable;

import android.graphics.PointF;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.model.content.ContentModel;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.android.systemui.qs.external.QSTileServiceWrapper;
/* loaded from: classes.dex */
public final class AnimatableTransform implements ContentModel {
    public final QSTileServiceWrapper anchorPoint;
    public final AnimatableFloatValue endOpacity;
    public final AnimatableIntegerValue opacity;
    public final AnimatableValue<PointF, PointF> position;
    public final AnimatableFloatValue rotation;
    public final AnimatableScaleValue scale;
    public final AnimatableFloatValue skew;
    public final AnimatableFloatValue skewAngle;
    public final AnimatableFloatValue startOpacity;

    public AnimatableTransform() {
        this(null, null, null, null, null, null, null, null, null);
    }

    @Override // com.airbnb.lottie.model.content.ContentModel
    public final Content toContent(LottieDrawable lottieDrawable, BaseLayer baseLayer) {
        return null;
    }

    public AnimatableTransform(QSTileServiceWrapper qSTileServiceWrapper, AnimatableValue<PointF, PointF> animatableValue, AnimatableScaleValue animatableScaleValue, AnimatableFloatValue animatableFloatValue, AnimatableIntegerValue animatableIntegerValue, AnimatableFloatValue animatableFloatValue2, AnimatableFloatValue animatableFloatValue3, AnimatableFloatValue animatableFloatValue4, AnimatableFloatValue animatableFloatValue5) {
        this.anchorPoint = qSTileServiceWrapper;
        this.position = animatableValue;
        this.scale = animatableScaleValue;
        this.rotation = animatableFloatValue;
        this.opacity = animatableIntegerValue;
        this.startOpacity = animatableFloatValue2;
        this.endOpacity = animatableFloatValue3;
        this.skew = animatableFloatValue4;
        this.skewAngle = animatableFloatValue5;
    }
}
