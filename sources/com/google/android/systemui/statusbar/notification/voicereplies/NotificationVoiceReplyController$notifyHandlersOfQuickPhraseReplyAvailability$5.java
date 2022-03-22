package com.google.android.systemui.statusbar.notification.voicereplies;

import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes.dex */
final class NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$5 extends Lambda implements Function2<VoiceReplyTarget, VoiceReplyTarget, Boolean> {
    public static final NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$5 INSTANCE = new NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$5();

    public NotificationVoiceReplyController$notifyHandlersOfQuickPhraseReplyAvailability$5() {
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
