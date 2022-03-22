package com.android.systemui.statusbar.policy;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: HeadsUpManagerLogger.kt */
/* loaded from: classes.dex */
final class HeadsUpManagerLogger$logPackageUnsnoozed$2 extends Lambda implements Function1<LogMessage, String> {
    public static final HeadsUpManagerLogger$logPackageUnsnoozed$2 INSTANCE = new HeadsUpManagerLogger$logPackageUnsnoozed$2();

    public HeadsUpManagerLogger$logPackageUnsnoozed$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        return Intrinsics.stringPlus("package unsnoozed ", logMessage.getStr1());
    }
}
