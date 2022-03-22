package com.google.android.systemui.statusbar;

import kotlin.Pair;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationVoiceReplyManagerService.kt */
/* loaded from: classes.dex */
final class NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$3 extends Lambda implements Function2<Pair<? extends Integer, ? extends Integer>, Pair<? extends Integer, ? extends Integer>, Boolean> {
    public static final NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$3 INSTANCE = new NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$3();

    public NotificationVoiceReplyManagerService$binder$1$registerCallbacksWhenEnabled$3() {
        super(2);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Boolean invoke(Pair<? extends Integer, ? extends Integer> pair, Pair<? extends Integer, ? extends Integer> pair2) {
        boolean z;
        boolean z2;
        int intValue = ((Number) pair.component2()).intValue();
        int intValue2 = ((Number) pair2.component2()).intValue();
        boolean z3 = true;
        if (intValue != 0) {
            z = true;
        } else {
            z = false;
        }
        if (intValue2 != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (z != z2) {
            z3 = false;
        }
        return Boolean.valueOf(z3);
    }
}
