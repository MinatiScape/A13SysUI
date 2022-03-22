package com.google.android.systemui.statusbar.notification.voicereplies;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt", f = "NotificationVoiceReplyManager.kt", l = {1172}, m = "sendHunStateChanges")
/* loaded from: classes.dex */
public final class NotificationVoiceReplyManagerKt$sendHunStateChanges$1 extends ContinuationImpl {
    public Object L$0;
    public Object L$1;
    public int label;
    public /* synthetic */ Object result;

    public NotificationVoiceReplyManagerKt$sendHunStateChanges$1(Continuation<? super NotificationVoiceReplyManagerKt$sendHunStateChanges$1> continuation) {
        super(continuation);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        NotificationVoiceReplyManagerKt.access$sendHunStateChanges(null, null, this);
        return CoroutineSingletons.COROUTINE_SUSPENDED;
    }
}
