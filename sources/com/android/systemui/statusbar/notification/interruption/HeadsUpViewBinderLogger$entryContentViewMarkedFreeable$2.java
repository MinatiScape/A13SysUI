package com.android.systemui.statusbar.notification.interruption;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: HeadsUpViewBinderLogger.kt */
/* loaded from: classes.dex */
final class HeadsUpViewBinderLogger$entryContentViewMarkedFreeable$2 extends Lambda implements Function1<LogMessage, String> {
    public static final HeadsUpViewBinderLogger$entryContentViewMarkedFreeable$2 INSTANCE = new HeadsUpViewBinderLogger$entryContentViewMarkedFreeable$2();

    public HeadsUpViewBinderLogger$entryContentViewMarkedFreeable$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("start unbinding heads up entry ");
        m.append((Object) logMessage.getStr1());
        m.append(' ');
        return m.toString();
    }
}
