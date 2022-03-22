package com.android.systemui.statusbar.policy;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: HeadsUpManagerLogger.kt */
/* loaded from: classes.dex */
final class HeadsUpManagerLogger$logSetEntryPinned$2 extends Lambda implements Function1<LogMessage, String> {
    public static final HeadsUpManagerLogger$logSetEntryPinned$2 INSTANCE = new HeadsUpManagerLogger$logSetEntryPinned$2();

    public HeadsUpManagerLogger$logSetEntryPinned$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("set entry pinned ");
        m.append((Object) logMessage2.getStr1());
        m.append(" pinned: ");
        m.append(logMessage2.getBool1());
        return m.toString();
    }
}
