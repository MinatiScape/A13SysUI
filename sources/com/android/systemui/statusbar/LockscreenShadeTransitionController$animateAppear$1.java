package com.android.systemui.statusbar;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: LockscreenShadeTransitionController.kt */
/* loaded from: classes.dex */
public final class LockscreenShadeTransitionController$animateAppear$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ LockscreenShadeTransitionController this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public LockscreenShadeTransitionController$animateAppear$1(LockscreenShadeTransitionController lockscreenShadeTransitionController) {
        super(0);
        this.this$0 = lockscreenShadeTransitionController;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Unit invoke() {
        this.this$0.setDragDownAmount$frameworks__base__packages__SystemUI__android_common__SystemUI_core(0.0f);
        this.this$0.forceApplyAmount = false;
        return Unit.INSTANCE;
    }
}
