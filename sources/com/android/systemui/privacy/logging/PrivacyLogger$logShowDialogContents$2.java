package com.android.systemui.privacy.logging;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: PrivacyLogger.kt */
/* loaded from: classes.dex */
final class PrivacyLogger$logShowDialogContents$2 extends Lambda implements Function1<LogMessage, String> {
    public static final PrivacyLogger$logShowDialogContents$2 INSTANCE = new PrivacyLogger$logShowDialogContents$2();

    public PrivacyLogger$logShowDialogContents$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        return Intrinsics.stringPlus("Privacy dialog shown. Contents: ", logMessage.getStr1());
    }
}
