package com.android.wm.shell.common;

import android.graphics.Rect;
import java.util.List;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.Ref$ObjectRef;
/* compiled from: FloatingContentCoordinator.kt */
/* loaded from: classes.dex */
final class FloatingContentCoordinator$Companion$findAreaForContentVertically$newContentBoundsAbove$2 extends Lambda implements Function0<Rect> {
    public final /* synthetic */ Rect $contentRect;
    public final /* synthetic */ Rect $newlyOverlappingRect;
    public final /* synthetic */ Ref$ObjectRef<List<Rect>> $rectsToAvoidAbove;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FloatingContentCoordinator$Companion$findAreaForContentVertically$newContentBoundsAbove$2(Rect rect, Ref$ObjectRef<List<Rect>> ref$ObjectRef, Rect rect2) {
        super(0);
        this.$contentRect = rect;
        this.$rectsToAvoidAbove = ref$ObjectRef;
        this.$newlyOverlappingRect = rect2;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Rect invoke() {
        Rect rect = this.$contentRect;
        List<Rect> sortedWith = CollectionsKt___CollectionsKt.sortedWith(CollectionsKt___CollectionsKt.plus(this.$rectsToAvoidAbove.element, this.$newlyOverlappingRect), new FloatingContentCoordinator$Companion$findAreaForContentAboveOrBelow$$inlined$sortedBy$1(true));
        Rect rect2 = new Rect(rect);
        for (Rect rect3 : sortedWith) {
            if (!Rect.intersects(rect2, rect3)) {
                break;
            }
            rect2.offsetTo(rect2.left, rect3.top + (-rect.height()));
        }
        return rect2;
    }
}
