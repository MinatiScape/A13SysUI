package com.android.systemui.unfold.util;

import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import com.android.systemui.unfold.util.ScaleAwareTransitionProgressProvider;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ScaleAwareTransitionProgressProvider_Factory_Impl implements ScaleAwareTransitionProgressProvider.Factory {
    public final C0015ScaleAwareTransitionProgressProvider_Factory delegateFactory;

    @Override // com.android.systemui.unfold.util.ScaleAwareTransitionProgressProvider.Factory
    public final ScaleAwareTransitionProgressProvider wrap(UnfoldTransitionProgressProvider unfoldTransitionProgressProvider) {
        C0015ScaleAwareTransitionProgressProvider_Factory scaleAwareTransitionProgressProvider_Factory = this.delegateFactory;
        Objects.requireNonNull(scaleAwareTransitionProgressProvider_Factory);
        return new ScaleAwareTransitionProgressProvider(unfoldTransitionProgressProvider, scaleAwareTransitionProgressProvider_Factory.contentResolverProvider.mo144get());
    }

    public ScaleAwareTransitionProgressProvider_Factory_Impl(C0015ScaleAwareTransitionProgressProvider_Factory scaleAwareTransitionProgressProvider_Factory) {
        this.delegateFactory = scaleAwareTransitionProgressProvider_Factory;
    }
}
