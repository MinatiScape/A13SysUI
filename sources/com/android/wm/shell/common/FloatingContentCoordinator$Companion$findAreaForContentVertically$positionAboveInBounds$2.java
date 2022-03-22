package com.android.wm.shell.common;

import android.graphics.Rect;
import kotlin.Lazy;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: FloatingContentCoordinator.kt */
/* loaded from: classes.dex */
final class FloatingContentCoordinator$Companion$findAreaForContentVertically$positionAboveInBounds$2 extends Lambda implements Function0<Boolean> {
    public final /* synthetic */ Rect $allowedBounds;
    public final /* synthetic */ Lazy<Rect> $newContentBoundsAbove$delegate;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FloatingContentCoordinator$Companion$findAreaForContentVertically$positionAboveInBounds$2(Rect rect, Lazy<Rect> lazy) {
        super(0);
        this.$allowedBounds = rect;
        this.$newContentBoundsAbove$delegate = lazy;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Boolean invoke() {
        return Boolean.valueOf(this.$allowedBounds.contains(this.$newContentBoundsAbove$delegate.getValue()));
    }
}
