package com.android.systemui.doze;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import com.android.systemui.statusbar.policy.DevicePostureController;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: DozeLogger.kt */
/* loaded from: classes.dex */
final class DozeLogger$logPostureChanged$2 extends Lambda implements Function1<LogMessage, String> {
    public static final DozeLogger$logPostureChanged$2 INSTANCE = new DozeLogger$logPostureChanged$2();

    public DozeLogger$logPostureChanged$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Posture changed, posture=");
        m.append((Object) DevicePostureController.devicePostureToString(logMessage2.getInt1()));
        m.append(" partUpdated=");
        m.append((Object) logMessage2.getStr1());
        return m.toString();
    }
}
