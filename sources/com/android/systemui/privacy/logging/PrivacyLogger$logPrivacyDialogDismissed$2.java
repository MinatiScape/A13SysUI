package com.android.systemui.privacy.logging;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: PrivacyLogger.kt */
/* loaded from: classes.dex */
public final class PrivacyLogger$logPrivacyDialogDismissed$2 extends Lambda implements Function1<LogMessage, String> {
    public static final PrivacyLogger$logPrivacyDialogDismissed$2 INSTANCE = new PrivacyLogger$logPrivacyDialogDismissed$2();

    public PrivacyLogger$logPrivacyDialogDismissed$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final /* bridge */ /* synthetic */ String invoke(LogMessage logMessage) {
        return "Privacy dialog dismissed";
    }
}
