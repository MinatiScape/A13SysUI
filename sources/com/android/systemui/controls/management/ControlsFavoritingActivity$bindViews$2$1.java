package com.android.systemui.controls.management;

import com.android.systemui.controls.TooltipManager;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: ControlsFavoritingActivity.kt */
/* loaded from: classes.dex */
final class ControlsFavoritingActivity$bindViews$2$1 extends Lambda implements Function1<Integer, Unit> {
    public final /* synthetic */ ControlsFavoritingActivity this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ControlsFavoritingActivity$bindViews$2$1(ControlsFavoritingActivity controlsFavoritingActivity) {
        super(1);
        this.this$0 = controlsFavoritingActivity;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Unit invoke(Integer num) {
        TooltipManager tooltipManager;
        if (!(num.intValue() == 0 || (tooltipManager = this.this$0.mTooltipManager) == null)) {
            tooltipManager.hide(true);
        }
        return Unit.INSTANCE;
    }
}
