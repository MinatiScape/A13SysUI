package com.airbnb.lottie.animation.content;

import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.FloatKeyframeAnimation;
import com.airbnb.lottie.model.content.ShapeTrimPath;
import com.airbnb.lottie.model.layer.BaseLayer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class TrimPathContent implements Content, BaseKeyframeAnimation.AnimationListener {
    public final FloatKeyframeAnimation endAnimation;
    public final boolean hidden;
    public final ArrayList listeners = new ArrayList();
    public final FloatKeyframeAnimation offsetAnimation;
    public final FloatKeyframeAnimation startAnimation;
    public final ShapeTrimPath.Type type;

    @Override // com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation.AnimationListener
    public final void onValueChanged() {
        for (int i = 0; i < this.listeners.size(); i++) {
            ((BaseKeyframeAnimation.AnimationListener) this.listeners.get(i)).onValueChanged();
        }
    }

    @Override // com.airbnb.lottie.animation.content.Content
    public final void setContents(List<Content> list, List<Content> list2) {
    }

    public final void addListener(BaseKeyframeAnimation.AnimationListener animationListener) {
        this.listeners.add(animationListener);
    }

    public TrimPathContent(BaseLayer baseLayer, ShapeTrimPath shapeTrimPath) {
        Objects.requireNonNull(shapeTrimPath);
        this.hidden = shapeTrimPath.hidden;
        this.type = shapeTrimPath.type;
        BaseKeyframeAnimation<Float, Float> createAnimation = shapeTrimPath.start.createAnimation();
        this.startAnimation = (FloatKeyframeAnimation) createAnimation;
        BaseKeyframeAnimation<Float, Float> createAnimation2 = shapeTrimPath.end.createAnimation();
        this.endAnimation = (FloatKeyframeAnimation) createAnimation2;
        BaseKeyframeAnimation<Float, Float> createAnimation3 = shapeTrimPath.offset.createAnimation();
        this.offsetAnimation = (FloatKeyframeAnimation) createAnimation3;
        baseLayer.addAnimation(createAnimation);
        baseLayer.addAnimation(createAnimation2);
        baseLayer.addAnimation(createAnimation3);
        createAnimation.addUpdateListener(this);
        createAnimation2.addUpdateListener(this);
        createAnimation3.addUpdateListener(this);
    }
}
