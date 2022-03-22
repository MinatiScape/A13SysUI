package com.android.systemui.toast;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: ToastLogger.kt */
/* loaded from: classes.dex */
final class ToastLogger$logOrientationChange$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ToastLogger$logOrientationChange$2 INSTANCE = new ToastLogger$logOrientationChange$2();

    public ToastLogger$logOrientationChange$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Orientation change for toast. msg='");
        m.append((Object) logMessage2.getStr1());
        m.append("' isPortrait=");
        m.append(logMessage2.getBool1());
        return m.toString();
    }
}
