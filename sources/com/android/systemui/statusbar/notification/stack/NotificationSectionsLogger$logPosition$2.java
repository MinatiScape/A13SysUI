package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationSectionsLogger.kt */
/* loaded from: classes.dex */
public final class NotificationSectionsLogger$logPosition$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationSectionsLogger$logPosition$2 INSTANCE = new NotificationSectionsLogger$logPosition$2();

    public NotificationSectionsLogger$logPosition$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        LogMessage logMessage2 = logMessage;
        return logMessage2.getInt1() + ": " + ((Object) logMessage2.getStr1()) + ((Object) logMessage2.getStr2());
    }
}
