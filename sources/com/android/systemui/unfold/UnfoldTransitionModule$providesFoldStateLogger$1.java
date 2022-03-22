package com.android.systemui.unfold;

import java.util.function.Function;
/* compiled from: UnfoldTransitionModule.kt */
/* loaded from: classes.dex */
public final class UnfoldTransitionModule$providesFoldStateLogger$1<T, R> implements Function {
    public static final UnfoldTransitionModule$providesFoldStateLogger$1<T, R> INSTANCE = new UnfoldTransitionModule$providesFoldStateLogger$1<>();

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return new FoldStateLogger((FoldStateLoggingProvider) obj);
    }
}
