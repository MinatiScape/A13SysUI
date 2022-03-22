package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotifCollectionLogger.kt */
/* loaded from: classes.dex */
public final class NotifCollectionLogger$logDismissOnAlreadyCanceledEntry$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotifCollectionLogger$logDismissOnAlreadyCanceledEntry$2 INSTANCE = new NotifCollectionLogger$logDismissOnAlreadyCanceledEntry$2();

    public NotifCollectionLogger$logDismissOnAlreadyCanceledEntry$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Dismiss on ");
        m.append((Object) logMessage.getStr1());
        m.append(", which was already canceled. Trying to remove...");
        return m.toString();
    }
}
