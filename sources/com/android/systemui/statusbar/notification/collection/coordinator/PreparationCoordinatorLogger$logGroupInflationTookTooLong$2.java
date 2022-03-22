package com.android.systemui.statusbar.notification.collection.coordinator;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: PreparationCoordinatorLogger.kt */
/* loaded from: classes.dex */
final class PreparationCoordinatorLogger$logGroupInflationTookTooLong$2 extends Lambda implements Function1<LogMessage, String> {
    public static final PreparationCoordinatorLogger$logGroupInflationTookTooLong$2 INSTANCE = new PreparationCoordinatorLogger$logGroupInflationTookTooLong$2();

    public PreparationCoordinatorLogger$logGroupInflationTookTooLong$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Group inflation took too long for ");
        m.append((Object) logMessage.getStr1());
        m.append(", releasing children early");
        return m.toString();
    }
}
