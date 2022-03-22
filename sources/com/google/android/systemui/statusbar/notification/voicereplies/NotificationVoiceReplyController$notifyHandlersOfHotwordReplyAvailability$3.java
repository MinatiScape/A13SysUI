package com.google.android.systemui.statusbar.notification.voicereplies;

import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes.dex */
final class NotificationVoiceReplyController$notifyHandlersOfHotwordReplyAvailability$3 extends Lambda implements Function2<VoiceReplyTarget, VoiceReplyTarget, Boolean> {
    public static final NotificationVoiceReplyController$notifyHandlersOfHotwordReplyAvailability$3 INSTANCE = new NotificationVoiceReplyController$notifyHandlersOfHotwordReplyAvailability$3();

    public NotificationVoiceReplyController$notifyHandlersOfHotwordReplyAvailability$3() {
        super(2);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Boolean invoke(VoiceReplyTarget voiceReplyTarget, VoiceReplyTarget voiceReplyTarget2) {
        Integer num;
        VoiceReplyTarget voiceReplyTarget3 = voiceReplyTarget;
        VoiceReplyTarget voiceReplyTarget4 = voiceReplyTarget2;
        Integer num2 = null;
        if (voiceReplyTarget3 == null) {
            num = null;
        } else {
            num = Integer.valueOf(voiceReplyTarget3.userId);
        }
        if (voiceReplyTarget4 != null) {
            num2 = Integer.valueOf(voiceReplyTarget4.userId);
        }
        return Boolean.valueOf(Intrinsics.areEqual(num, num2));
    }
}
