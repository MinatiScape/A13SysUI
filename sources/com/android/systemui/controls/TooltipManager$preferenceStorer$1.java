package com.android.systemui.controls;

import android.content.Context;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: TooltipManager.kt */
/* loaded from: classes.dex */
public final class TooltipManager$preferenceStorer$1 extends Lambda implements Function1<Integer, Unit> {
    public final /* synthetic */ Context $context;
    public final /* synthetic */ TooltipManager this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public TooltipManager$preferenceStorer$1(Context context, TooltipManager tooltipManager) {
        super(1);
        this.$context = context;
        this.this$0 = tooltipManager;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Unit invoke(Integer num) {
        int intValue = num.intValue();
        Context context = this.$context;
        Objects.requireNonNull(this.this$0);
        context.getSharedPreferences(context.getPackageName(), 0).edit().putInt("ControlsStructureSwipeTooltipCount", intValue).apply();
        return Unit.INSTANCE;
    }
}
