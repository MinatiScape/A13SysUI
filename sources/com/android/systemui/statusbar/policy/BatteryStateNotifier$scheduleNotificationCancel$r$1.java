package com.android.systemui.statusbar.policy;

import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: BatteryStateNotifier.kt */
/* loaded from: classes.dex */
final class BatteryStateNotifier$scheduleNotificationCancel$r$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ BatteryStateNotifier this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public BatteryStateNotifier$scheduleNotificationCancel$r$1(BatteryStateNotifier batteryStateNotifier) {
        super(0);
        this.this$0 = batteryStateNotifier;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Unit invoke() {
        BatteryStateNotifier batteryStateNotifier = this.this$0;
        Objects.requireNonNull(batteryStateNotifier);
        if (!batteryStateNotifier.stateUnknown) {
            BatteryStateNotifier batteryStateNotifier2 = this.this$0;
            Objects.requireNonNull(batteryStateNotifier2);
            batteryStateNotifier2.noMan.cancel(666);
        }
        return Unit.INSTANCE;
    }
}
