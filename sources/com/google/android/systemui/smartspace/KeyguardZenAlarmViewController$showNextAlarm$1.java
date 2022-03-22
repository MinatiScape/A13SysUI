package com.google.android.systemui.smartspace;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;
/* compiled from: KeyguardZenAlarmViewController.kt */
/* loaded from: classes.dex */
public /* synthetic */ class KeyguardZenAlarmViewController$showNextAlarm$1 extends FunctionReferenceImpl implements Function0<Unit> {
    public KeyguardZenAlarmViewController$showNextAlarm$1(Object obj) {
        super(0, obj, KeyguardZenAlarmViewController.class, "showAlarm", "showAlarm()V", 0);
    }

    @Override // kotlin.jvm.functions.Function0
    public final Unit invoke() {
        ((KeyguardZenAlarmViewController) this.receiver).showAlarm();
        return Unit.INSTANCE;
    }
}
