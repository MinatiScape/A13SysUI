package com.google.android.systemui.assist.uihints;

import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsController;
import com.google.android.systemui.assist.uihints.input.NgaInputHandler;
import dagger.internal.Factory;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AssistantUIHintsModule_BindEdgeLightsInfoListenersFactory implements Factory<Set<NgaMessageHandler.EdgeLightsInfoListener>> {
    public final Provider<EdgeLightsController> edgeLightsControllerProvider;
    public final Provider<NgaInputHandler> ngaInputHandlerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new HashSet(Arrays.asList(this.edgeLightsControllerProvider.mo144get(), this.ngaInputHandlerProvider.mo144get()));
    }

    public AssistantUIHintsModule_BindEdgeLightsInfoListenersFactory(Provider<EdgeLightsController> provider, Provider<NgaInputHandler> provider2) {
        this.edgeLightsControllerProvider = provider;
        this.ngaInputHandlerProvider = provider2;
    }
}
