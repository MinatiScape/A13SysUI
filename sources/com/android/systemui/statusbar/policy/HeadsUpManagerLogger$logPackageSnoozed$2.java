package com.android.systemui.statusbar.policy;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: HeadsUpManagerLogger.kt */
/* loaded from: classes.dex */
final class HeadsUpManagerLogger$logPackageSnoozed$2 extends Lambda implements Function1<LogMessage, String> {
    public static final HeadsUpManagerLogger$logPackageSnoozed$2 INSTANCE = new HeadsUpManagerLogger$logPackageSnoozed$2();

    public HeadsUpManagerLogger$logPackageSnoozed$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        return Intrinsics.stringPlus("package snoozed ", logMessage.getStr1());
    }
}
