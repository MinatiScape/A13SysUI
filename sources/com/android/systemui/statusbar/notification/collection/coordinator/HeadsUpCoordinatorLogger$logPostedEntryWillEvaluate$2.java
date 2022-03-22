package com.android.systemui.statusbar.notification.collection.coordinator;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: HeadsUpCoordinatorLogger.kt */
/* loaded from: classes.dex */
final class HeadsUpCoordinatorLogger$logPostedEntryWillEvaluate$2 extends Lambda implements Function1<LogMessage, String> {
    public static final HeadsUpCoordinatorLogger$logPostedEntryWillEvaluate$2 INSTANCE = new HeadsUpCoordinatorLogger$logPostedEntryWillEvaluate$2();

    public HeadsUpCoordinatorLogger$logPostedEntryWillEvaluate$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("will evaluate posted entry ");
        m.append((Object) logMessage2.getStr1());
        m.append(": reason=");
        m.append((Object) logMessage2.getStr2());
        m.append(" shouldHeadsUpEver=");
        m.append(logMessage2.getBool1());
        m.append(" shouldHeadsUpAgain=");
        m.append(logMessage2.getBool2());
        return m.toString();
    }
}
