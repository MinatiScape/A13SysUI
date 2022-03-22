package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationSectionsLogger.kt */
/* loaded from: classes.dex */
public final class NotificationSectionsLogger$logStr$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationSectionsLogger$logStr$2 INSTANCE = new NotificationSectionsLogger$logStr$2();

    public NotificationSectionsLogger$logStr$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        return String.valueOf(logMessage.getStr1());
    }
}
