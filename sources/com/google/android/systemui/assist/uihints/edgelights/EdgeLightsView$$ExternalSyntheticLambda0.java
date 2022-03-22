package com.google.android.systemui.assist.uihints.edgelights;

import com.android.systemui.assist.ui.EdgeLight;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class EdgeLightsView$$ExternalSyntheticLambda0 implements Consumer {
    public static final /* synthetic */ EdgeLightsView$$ExternalSyntheticLambda0 INSTANCE = new EdgeLightsView$$ExternalSyntheticLambda0();

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        EdgeLight edgeLight = (EdgeLight) obj;
        int i = EdgeLightsView.$r8$clinit;
        Objects.requireNonNull(edgeLight);
        edgeLight.mLength = 0.0f;
    }
}
