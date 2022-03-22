package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotifCollectionLogger.kt */
/* loaded from: classes.dex */
public final class NotifCollectionLogger$logLifetimeExtensionEnded$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotifCollectionLogger$logLifetimeExtensionEnded$2 INSTANCE = new NotifCollectionLogger$logLifetimeExtensionEnded$2();

    public NotifCollectionLogger$logLifetimeExtensionEnded$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("LIFETIME EXTENSION ENDED for ");
        m.append((Object) logMessage2.getStr1());
        m.append(" by '");
        m.append((Object) logMessage2.getStr2());
        m.append("'; ");
        m.append(logMessage2.getInt1());
        m.append(" remaining extensions");
        return m.toString();
    }
}
