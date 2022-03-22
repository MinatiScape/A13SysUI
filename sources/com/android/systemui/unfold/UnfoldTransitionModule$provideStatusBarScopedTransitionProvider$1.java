package com.android.systemui.unfold;

import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import com.android.systemui.unfold.util.ScopedUnfoldTransitionProgressProvider;
import java.util.function.Function;
/* compiled from: UnfoldTransitionModule.kt */
/* loaded from: classes.dex */
public final class UnfoldTransitionModule$provideStatusBarScopedTransitionProvider$1<T, R> implements Function {
    public static final UnfoldTransitionModule$provideStatusBarScopedTransitionProvider$1<T, R> INSTANCE = new UnfoldTransitionModule$provideStatusBarScopedTransitionProvider$1<>();

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return new ScopedUnfoldTransitionProgressProvider((NaturalRotationUnfoldProgressProvider) obj);
    }
}
