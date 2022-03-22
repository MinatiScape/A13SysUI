package com.android.systemui.statusbar.notification.row;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: NotifBindPipelineLogger.kt */
/* loaded from: classes.dex */
public final class NotifBindPipelineLogger$logFinishedPipeline$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotifBindPipelineLogger$logFinishedPipeline$2 INSTANCE = new NotifBindPipelineLogger$logFinishedPipeline$2();

    public NotifBindPipelineLogger$logFinishedPipeline$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Finished pipeline for notif ");
        m.append((Object) logMessage2.getStr1());
        m.append(" with ");
        m.append(logMessage2.getInt1());
        m.append(" callbacks");
        return m.toString();
    }
}
