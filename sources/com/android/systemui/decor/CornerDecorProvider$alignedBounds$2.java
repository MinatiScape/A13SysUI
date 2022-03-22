package com.android.systemui.decor;

import java.util.List;
import java.util.Objects;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: DecorProvider.kt */
/* loaded from: classes.dex */
public final class CornerDecorProvider$alignedBounds$2 extends Lambda implements Function0<List<? extends Integer>> {
    public final /* synthetic */ CornerDecorProvider this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CornerDecorProvider$alignedBounds$2(CornerDecorProvider cornerDecorProvider) {
        super(0);
        this.this$0 = cornerDecorProvider;
    }

    @Override // kotlin.jvm.functions.Function0
    public final List<? extends Integer> invoke() {
        PrivacyDotCornerDecorProviderImpl privacyDotCornerDecorProviderImpl = (PrivacyDotCornerDecorProviderImpl) this.this$0;
        Objects.requireNonNull(privacyDotCornerDecorProviderImpl);
        PrivacyDotCornerDecorProviderImpl privacyDotCornerDecorProviderImpl2 = (PrivacyDotCornerDecorProviderImpl) this.this$0;
        Objects.requireNonNull(privacyDotCornerDecorProviderImpl2);
        return SetsKt__SetsKt.listOf(Integer.valueOf(privacyDotCornerDecorProviderImpl.alignedBound1), Integer.valueOf(privacyDotCornerDecorProviderImpl2.alignedBound2));
    }
}
