package com.google.android.systemui.statusbar.notification.voicereplies;

import android.view.View;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes.dex */
final class NotificationVoiceReplyManagerKt$getReplyButton$1 extends Lambda implements Function1<View, Boolean> {
    public static final NotificationVoiceReplyManagerKt$getReplyButton$1 INSTANCE = new NotificationVoiceReplyManagerKt$getReplyButton$1();

    public NotificationVoiceReplyManagerKt$getReplyButton$1() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Boolean invoke(View view) {
        boolean z;
        if (view.getId() == 16908716) {
            z = true;
        } else {
            z = false;
        }
        return Boolean.valueOf(z);
    }
}
