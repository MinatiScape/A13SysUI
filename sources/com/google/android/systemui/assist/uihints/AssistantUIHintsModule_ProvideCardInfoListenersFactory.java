package com.google.android.systemui.assist.uihints;

import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import dagger.internal.Factory;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class AssistantUIHintsModule_ProvideCardInfoListenersFactory implements Factory<Set<NgaMessageHandler.CardInfoListener>> {
    public final Provider<GlowController> glowControllerProvider;
    public final Provider<LightnessProvider> lightnessProvider;
    public final Provider<ScrimController> scrimControllerProvider;
    public final Provider<TranscriptionController> transcriptionControllerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new HashSet(Arrays.asList(this.glowControllerProvider.mo144get(), this.scrimControllerProvider.mo144get(), this.transcriptionControllerProvider.mo144get(), this.lightnessProvider.mo144get()));
    }

    public AssistantUIHintsModule_ProvideCardInfoListenersFactory(Provider<GlowController> provider, Provider<ScrimController> provider2, Provider<TranscriptionController> provider3, Provider<LightnessProvider> provider4) {
        this.glowControllerProvider = provider;
        this.scrimControllerProvider = provider2;
        this.transcriptionControllerProvider = provider3;
        this.lightnessProvider = provider4;
    }
}
