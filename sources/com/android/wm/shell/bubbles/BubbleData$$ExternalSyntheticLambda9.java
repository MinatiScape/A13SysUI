package com.android.wm.shell.bubbles;

import java.util.Objects;
import java.util.function.ToLongFunction;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BubbleData$$ExternalSyntheticLambda9 implements ToLongFunction {
    public static final /* synthetic */ BubbleData$$ExternalSyntheticLambda9 INSTANCE = new BubbleData$$ExternalSyntheticLambda9();

    @Override // java.util.function.ToLongFunction
    public final long applyAsLong(Object obj) {
        Bubble bubble = (Bubble) obj;
        Objects.requireNonNull(bubble);
        return Math.max(bubble.mLastUpdated, bubble.mLastAccessed);
    }
}
