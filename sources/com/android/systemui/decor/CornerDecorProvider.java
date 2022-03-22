package com.android.systemui.decor;

import java.util.List;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
/* compiled from: DecorProvider.kt */
/* loaded from: classes.dex */
public abstract class CornerDecorProvider extends DecorProvider {
    public final Lazy alignedBounds$delegate = LazyKt__LazyJVMKt.lazy(new CornerDecorProvider$alignedBounds$2(this));

    @Override // com.android.systemui.decor.DecorProvider
    public final List<Integer> getAlignedBounds() {
        return (List) this.alignedBounds$delegate.getValue();
    }
}
