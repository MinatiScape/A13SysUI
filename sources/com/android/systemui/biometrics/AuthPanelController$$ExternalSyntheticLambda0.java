package com.android.systemui.biometrics;

import android.animation.ValueAnimator;
import com.android.systemui.assist.ui.EdgeLight;
import com.google.android.systemui.assist.uihints.GlowController;
import com.google.android.systemui.assist.uihints.GlowView;
import com.google.android.systemui.assist.uihints.edgelights.mode.FullListening;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class AuthPanelController$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ AuthPanelController$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        EdgeLight[] edgeLightArr;
        switch (this.$r8$classId) {
            case 0:
                AuthPanelController authPanelController = (AuthPanelController) this.f$0;
                Objects.requireNonNull(authPanelController);
                authPanelController.mContentHeight = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                authPanelController.mPanelView.invalidateOutline();
                return;
            default:
                GlowController glowController = (GlowController) this.f$0;
                Objects.requireNonNull(glowController);
                int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                glowController.mGlowsY = intValue;
                GlowView glowView = glowController.mGlowView;
                int minTranslationY = glowController.getMinTranslationY();
                if (glowController.mEdgeLightsMode instanceof FullListening) {
                    edgeLightArr = glowController.mEdgeLights;
                } else {
                    edgeLightArr = null;
                }
                glowView.setGlowsY(intValue, minTranslationY, edgeLightArr);
                return;
        }
    }
}
