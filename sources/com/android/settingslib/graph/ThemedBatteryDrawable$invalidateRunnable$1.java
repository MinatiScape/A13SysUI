package com.android.settingslib.graph;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: ThemedBatteryDrawable.kt */
/* loaded from: classes.dex */
public final class ThemedBatteryDrawable$invalidateRunnable$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ ThemedBatteryDrawable this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ThemedBatteryDrawable$invalidateRunnable$1(ThemedBatteryDrawable themedBatteryDrawable) {
        super(0);
        this.this$0 = themedBatteryDrawable;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Unit invoke() {
        this.this$0.invalidateSelf();
        return Unit.INSTANCE;
    }
}
