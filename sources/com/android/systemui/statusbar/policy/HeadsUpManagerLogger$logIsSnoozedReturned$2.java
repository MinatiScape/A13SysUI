package com.android.systemui.statusbar.policy;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: HeadsUpManagerLogger.kt */
/* loaded from: classes.dex */
final class HeadsUpManagerLogger$logIsSnoozedReturned$2 extends Lambda implements Function1<LogMessage, String> {
    public static final HeadsUpManagerLogger$logIsSnoozedReturned$2 INSTANCE = new HeadsUpManagerLogger$logIsSnoozedReturned$2();

    public HeadsUpManagerLogger$logIsSnoozedReturned$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        return Intrinsics.stringPlus("package snoozed when queried ", logMessage.getStr1());
    }
}
