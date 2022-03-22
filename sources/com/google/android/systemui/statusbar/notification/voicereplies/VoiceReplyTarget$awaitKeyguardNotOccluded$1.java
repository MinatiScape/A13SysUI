package com.google.android.systemui.statusbar.notification.voicereplies;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget", f = "NotificationVoiceReplyManager.kt", l = {847, 853}, m = "awaitKeyguardNotOccluded")
/* loaded from: classes.dex */
public final class VoiceReplyTarget$awaitKeyguardNotOccluded$1 extends ContinuationImpl {
    public Object L$0;
    public int label;
    public /* synthetic */ Object result;
    public final /* synthetic */ VoiceReplyTarget this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public VoiceReplyTarget$awaitKeyguardNotOccluded$1(VoiceReplyTarget voiceReplyTarget, Continuation<? super VoiceReplyTarget$awaitKeyguardNotOccluded$1> continuation) {
        super(continuation);
        this.this$0 = voiceReplyTarget;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return VoiceReplyTarget.access$awaitKeyguardNotOccluded(this.this$0, this);
    }
}
