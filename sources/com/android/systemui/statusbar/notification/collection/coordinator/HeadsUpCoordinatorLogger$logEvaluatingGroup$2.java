package com.android.systemui.statusbar.notification.collection.coordinator;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: HeadsUpCoordinatorLogger.kt */
/* loaded from: classes.dex */
final class HeadsUpCoordinatorLogger$logEvaluatingGroup$2 extends Lambda implements Function1<LogMessage, String> {
    public static final HeadsUpCoordinatorLogger$logEvaluatingGroup$2 INSTANCE = new HeadsUpCoordinatorLogger$logEvaluatingGroup$2();

    public HeadsUpCoordinatorLogger$logEvaluatingGroup$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("evaluating group for alert transfer: ");
        m.append((Object) logMessage2.getStr1());
        m.append(" numPostedEntries=");
        m.append(logMessage2.getInt1());
        m.append(" logicalGroupSize=");
        m.append(logMessage2.getInt2());
        return m.toString();
    }
}
