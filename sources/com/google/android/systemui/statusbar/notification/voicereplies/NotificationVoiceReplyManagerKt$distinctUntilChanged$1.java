package com.google.android.systemui.statusbar.notification.voicereplies;

import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes.dex */
final class NotificationVoiceReplyManagerKt$distinctUntilChanged$1 extends Lambda implements Function2 {
    public static final NotificationVoiceReplyManagerKt$distinctUntilChanged$1 INSTANCE = new NotificationVoiceReplyManagerKt$distinctUntilChanged$1();

    public NotificationVoiceReplyManagerKt$distinctUntilChanged$1() {
        super(2);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Object obj2) {
        return Boolean.valueOf(Intrinsics.areEqual(obj, obj2));
    }
}
