package com.android.systemui.privacy.logging;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: PrivacyLogger.kt */
/* loaded from: classes.dex */
public final class PrivacyLogger$logStartSettingsActivityFromDialog$2 extends Lambda implements Function1<LogMessage, String> {
    public static final PrivacyLogger$logStartSettingsActivityFromDialog$2 INSTANCE = new PrivacyLogger$logStartSettingsActivityFromDialog$2();

    public PrivacyLogger$logStartSettingsActivityFromDialog$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Start settings activity from dialog for packageName=");
        m.append((Object) logMessage2.getStr1());
        m.append(", userId=");
        m.append(logMessage2.getInt1());
        m.append(' ');
        return m.toString();
    }
}
