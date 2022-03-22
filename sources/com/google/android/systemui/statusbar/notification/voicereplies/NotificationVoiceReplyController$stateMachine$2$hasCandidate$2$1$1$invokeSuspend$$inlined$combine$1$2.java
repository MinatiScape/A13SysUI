package com.google.android.systemui.statusbar.notification.voicereplies;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import kotlinx.coroutines.flow.Flow;
/* compiled from: Zip.kt */
/* loaded from: classes.dex */
public final class NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$1$1$invokeSuspend$$inlined$combine$1$2 extends Lambda implements Function0<CtaState[]> {
    public final /* synthetic */ Flow[] $flowArray;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyController$stateMachine$2$hasCandidate$2$1$1$invokeSuspend$$inlined$combine$1$2(Flow[] flowArr) {
        super(0);
        this.$flowArray = flowArr;
    }

    @Override // kotlin.jvm.functions.Function0
    public final CtaState[] invoke() {
        return new CtaState[this.$flowArray.length];
    }
}
