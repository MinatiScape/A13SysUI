package com.google.android.systemui.assist.uihints.edgelights;

import android.animation.ValueAnimator;
import android.util.MathUtils;
import com.android.systemui.assist.ui.EdgeLight;
import java.util.Objects;
/* loaded from: classes.dex */
public final class EdgeLightUpdateListener implements ValueAnimator.AnimatorUpdateListener {
    public EdgeLight[] mFinalLights;
    public EdgeLight[] mInitialLights;
    public EdgeLight[] mLights;
    public EdgeLightsView mView;

    public EdgeLightUpdateListener(EdgeLight[] edgeLightArr, EdgeLight[] edgeLightArr2, EdgeLight[] edgeLightArr3, EdgeLightsView edgeLightsView) {
        if (edgeLightArr.length == edgeLightArr2.length && edgeLightArr3.length == edgeLightArr2.length) {
            this.mFinalLights = edgeLightArr2;
            this.mInitialLights = edgeLightArr;
            this.mLights = edgeLightArr3;
            this.mView = edgeLightsView;
            return;
        }
        throw new IllegalArgumentException("Lights arrays must be the same length");
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        int i = 0;
        while (true) {
            EdgeLight[] edgeLightArr = this.mLights;
            if (i < edgeLightArr.length) {
                EdgeLight edgeLight = this.mInitialLights[i];
                Objects.requireNonNull(edgeLight);
                float f = edgeLight.mLength;
                EdgeLight edgeLight2 = this.mFinalLights[i];
                Objects.requireNonNull(edgeLight2);
                float f2 = edgeLight2.mLength;
                EdgeLight edgeLight3 = this.mLights[i];
                float lerp = MathUtils.lerp(f, f2, animatedFraction);
                Objects.requireNonNull(edgeLight3);
                edgeLight3.mLength = lerp;
                EdgeLight edgeLight4 = this.mInitialLights[i];
                Objects.requireNonNull(edgeLight4);
                float f3 = edgeLight4.mStart;
                EdgeLight edgeLight5 = this.mFinalLights[i];
                Objects.requireNonNull(edgeLight5);
                float f4 = edgeLight5.mStart;
                EdgeLight edgeLight6 = this.mLights[i];
                float lerp2 = MathUtils.lerp(f3, f4, animatedFraction);
                Objects.requireNonNull(edgeLight6);
                edgeLight6.mStart = lerp2;
                i++;
            } else {
                this.mView.setAssistLights(edgeLightArr);
                return;
            }
        }
    }
}
