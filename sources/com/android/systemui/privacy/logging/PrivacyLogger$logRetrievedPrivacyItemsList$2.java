package com.android.systemui.privacy.logging;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: PrivacyLogger.kt */
/* loaded from: classes.dex */
public final class PrivacyLogger$logRetrievedPrivacyItemsList$2 extends Lambda implements Function1<LogMessage, String> {
    public static final PrivacyLogger$logRetrievedPrivacyItemsList$2 INSTANCE = new PrivacyLogger$logRetrievedPrivacyItemsList$2();

    public PrivacyLogger$logRetrievedPrivacyItemsList$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        return Intrinsics.stringPlus("Retrieved list to process: ", logMessage.getStr1());
    }
}
