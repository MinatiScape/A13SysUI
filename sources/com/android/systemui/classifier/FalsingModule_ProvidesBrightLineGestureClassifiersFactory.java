package com.android.systemui.classifier;

import com.android.keyguard.KeyguardUnfoldTransition_Factory;
import dagger.internal.Factory;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FalsingModule_ProvidesBrightLineGestureClassifiersFactory implements Factory<Set<FalsingClassifier>> {
    public final Provider<DiagonalClassifier> diagonalClassifierProvider;
    public final Provider<DistanceClassifier> distanceClassifierProvider;
    public final Provider<PointerCountClassifier> pointerCountClassifierProvider;
    public final Provider<ProximityClassifier> proximityClassifierProvider;
    public final Provider<TypeClassifier> typeClassifierProvider;
    public final Provider<ZigZagClassifier> zigZagClassifierProvider;

    public static FalsingModule_ProvidesBrightLineGestureClassifiersFactory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, KeyguardUnfoldTransition_Factory keyguardUnfoldTransition_Factory) {
        return new FalsingModule_ProvidesBrightLineGestureClassifiersFactory(provider, provider2, provider3, provider4, provider5, keyguardUnfoldTransition_Factory);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new HashSet(Arrays.asList(this.pointerCountClassifierProvider.mo144get(), this.typeClassifierProvider.mo144get(), this.diagonalClassifierProvider.mo144get(), this.distanceClassifierProvider.mo144get(), this.proximityClassifierProvider.mo144get(), this.zigZagClassifierProvider.mo144get()));
    }

    public FalsingModule_ProvidesBrightLineGestureClassifiersFactory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, KeyguardUnfoldTransition_Factory keyguardUnfoldTransition_Factory) {
        this.distanceClassifierProvider = provider;
        this.proximityClassifierProvider = provider2;
        this.pointerCountClassifierProvider = provider3;
        this.typeClassifierProvider = provider4;
        this.diagonalClassifierProvider = provider5;
        this.zigZagClassifierProvider = keyguardUnfoldTransition_Factory;
    }
}
