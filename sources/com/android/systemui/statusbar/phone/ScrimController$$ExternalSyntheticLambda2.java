package com.android.systemui.statusbar.phone;

import com.android.internal.colorextraction.ColorExtractor;
import com.android.internal.util.function.TriConsumer;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ScrimController$$ExternalSyntheticLambda2 implements TriConsumer {
    public final /* synthetic */ LightBarController f$0;

    public /* synthetic */ ScrimController$$ExternalSyntheticLambda2(LightBarController lightBarController) {
        this.f$0 = lightBarController;
    }

    public final void accept(Object obj, Object obj2, Object obj3) {
        boolean z;
        LightBarController lightBarController = this.f$0;
        ScrimState scrimState = (ScrimState) obj;
        float floatValue = ((Float) obj2).floatValue();
        ColorExtractor.GradientColors gradientColors = (ColorExtractor.GradientColors) obj3;
        Objects.requireNonNull(lightBarController);
        boolean z2 = lightBarController.mForceDarkForScrim;
        if (scrimState == ScrimState.BOUNCER || scrimState == ScrimState.BOUNCER_SCRIMMED || floatValue < 0.1f || gradientColors.supportsDarkText()) {
            z = false;
        } else {
            z = true;
        }
        lightBarController.mForceDarkForScrim = z;
        if (lightBarController.mHasLightNavigationBar && z != z2) {
            lightBarController.reevaluate();
        }
    }
}
