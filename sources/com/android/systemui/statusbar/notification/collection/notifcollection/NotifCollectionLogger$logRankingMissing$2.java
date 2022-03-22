package com.android.systemui.statusbar.notification.collection.notifcollection;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotifCollectionLogger.kt */
/* loaded from: classes.dex */
public final class NotifCollectionLogger$logRankingMissing$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotifCollectionLogger$logRankingMissing$2 INSTANCE = new NotifCollectionLogger$logRankingMissing$2();

    public NotifCollectionLogger$logRankingMissing$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        return Intrinsics.stringPlus("Ranking update is missing ranking for ", logMessage.getStr1());
    }
}
