package com.android.systemui.privacy.logging;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: PrivacyLogger.kt */
/* loaded from: classes.dex */
public final class PrivacyLogger$logStatusBarIconsVisible$2 extends Lambda implements Function1<LogMessage, String> {
    public static final PrivacyLogger$logStatusBarIconsVisible$2 INSTANCE = new PrivacyLogger$logStatusBarIconsVisible$2();

    public PrivacyLogger$logStatusBarIconsVisible$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Status bar icons visible: camera=");
        m.append(logMessage2.getBool1());
        m.append(", microphone=");
        m.append(logMessage2.getBool2());
        m.append(", location=");
        m.append(logMessage2.getBool3());
        return m.toString();
    }
}
