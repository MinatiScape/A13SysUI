package com.android.systemui.privacy.logging;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: PrivacyLogger.kt */
/* loaded from: classes.dex */
public final class PrivacyLogger$logUpdatedItemFromAppOps$2 extends Lambda implements Function1<LogMessage, String> {
    public static final PrivacyLogger$logUpdatedItemFromAppOps$2 INSTANCE = new PrivacyLogger$logUpdatedItemFromAppOps$2();

    public PrivacyLogger$logUpdatedItemFromAppOps$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("App Op: ");
        m.append(logMessage2.getInt1());
        m.append(" for ");
        m.append((Object) logMessage2.getStr1());
        m.append('(');
        m.append(logMessage2.getInt2());
        m.append("), active=");
        m.append(logMessage2.getBool1());
        return m.toString();
    }
}
