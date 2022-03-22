package com.android.systemui.statusbar.notification.row;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: RowContentBindStageLogger.kt */
/* loaded from: classes.dex */
final class RowContentBindStageLogger$logStageParams$2 extends Lambda implements Function1<LogMessage, String> {
    public static final RowContentBindStageLogger$logStageParams$2 INSTANCE = new RowContentBindStageLogger$logStageParams$2();

    public RowContentBindStageLogger$logStageParams$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Invalidated notif ");
        m.append((Object) logMessage2.getStr1());
        m.append(" with params: \n");
        m.append((Object) logMessage2.getStr2());
        return m.toString();
    }
}
