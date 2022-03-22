package com.google.android.systemui.assist.uihints;

import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsController;
import dagger.internal.Factory;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AssistantUIHintsModule_ProvideAudioInfoListenersFactory implements Factory<Set<NgaMessageHandler.AudioInfoListener>> {
    public final Provider<EdgeLightsController> edgeLightsControllerProvider;
    public final Provider<GlowController> glowControllerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new HashSet(Arrays.asList(this.edgeLightsControllerProvider.mo144get(), this.glowControllerProvider.mo144get()));
    }

    public AssistantUIHintsModule_ProvideAudioInfoListenersFactory(Provider<EdgeLightsController> provider, Provider<GlowController> provider2) {
        this.edgeLightsControllerProvider = provider;
        this.glowControllerProvider = provider2;
    }
}
