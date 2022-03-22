package com.android.systemui.statusbar.notification.collection.coordinator;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: GutsCoordinatorLogger.kt */
/* loaded from: classes.dex */
final class GutsCoordinatorLogger$logGutsOpened$2 extends Lambda implements Function1<LogMessage, String> {
    public static final GutsCoordinatorLogger$logGutsOpened$2 INSTANCE = new GutsCoordinatorLogger$logGutsOpened$2();

    public GutsCoordinatorLogger$logGutsOpened$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Guts of type ");
        m.append((Object) logMessage2.getStr2());
        m.append(" (leave behind: ");
        m.append(logMessage2.getBool1());
        m.append(") opened for class ");
        m.append((Object) logMessage2.getStr1());
        return m.toString();
    }
}
