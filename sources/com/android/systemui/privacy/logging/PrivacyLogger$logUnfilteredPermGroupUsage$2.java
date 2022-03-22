package com.android.systemui.privacy.logging;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: PrivacyLogger.kt */
/* loaded from: classes.dex */
public final class PrivacyLogger$logUnfilteredPermGroupUsage$2 extends Lambda implements Function1<LogMessage, String> {
    public static final PrivacyLogger$logUnfilteredPermGroupUsage$2 INSTANCE = new PrivacyLogger$logUnfilteredPermGroupUsage$2();

    public PrivacyLogger$logUnfilteredPermGroupUsage$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        return Intrinsics.stringPlus("Perm group usage: ", logMessage.getStr1());
    }
}
