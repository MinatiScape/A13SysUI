package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationSectionsLogger.kt */
/* loaded from: classes.dex */
final class NotificationSectionsLogger$logStartSectionUpdate$2 extends Lambda implements Function1<LogMessage, String> {
    public final /* synthetic */ String $reason;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationSectionsLogger$logStartSectionUpdate$2(String str) {
        super(1);
        this.$reason = str;
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(LogMessage logMessage) {
        return Intrinsics.stringPlus("Updating section boundaries: ", this.$reason);
    }
}
