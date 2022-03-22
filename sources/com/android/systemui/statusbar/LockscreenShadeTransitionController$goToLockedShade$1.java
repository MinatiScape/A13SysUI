package com.android.systemui.statusbar;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: LockscreenShadeTransitionController.kt */
/* loaded from: classes.dex */
public final class LockscreenShadeTransitionController$goToLockedShade$1 extends Lambda implements Function1<Long, Unit> {
    public final /* synthetic */ LockscreenShadeTransitionController this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public LockscreenShadeTransitionController$goToLockedShade$1(LockscreenShadeTransitionController lockscreenShadeTransitionController) {
        super(1);
        this.this$0 = lockscreenShadeTransitionController;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Unit invoke(Long l) {
        this.this$0.getNotificationPanelController().animateToFullShade(l.longValue());
        return Unit.INSTANCE;
    }
}
