package com.android.systemui.statusbar.phone;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: LSShadeTransitionLogger.kt */
/* loaded from: classes.dex */
public final class LSShadeTransitionLogger$logTryGoToLockedShade$2 extends Lambda implements Function1<LogMessage, String> {
    public static final LSShadeTransitionLogger$logTryGoToLockedShade$2 INSTANCE = new LSShadeTransitionLogger$logTryGoToLockedShade$2();

    public LSShadeTransitionLogger$logTryGoToLockedShade$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        String str;
        if (logMessage.getBool1()) {
            str = "from keyguard";
        } else {
            str = "not from keyguard";
        }
        return Intrinsics.stringPlus("Trying to go to locked shade ", str);
    }
}
