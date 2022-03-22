package com.google.android.systemui.statusbar.notification.voicereplies;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyController", f = "NotificationVoiceReplyManager.kt", l = {1172}, m = "resetStateOnUserChange")
/* loaded from: classes.dex */
public final class NotificationVoiceReplyController$resetStateOnUserChange$1 extends ContinuationImpl {
    public Object L$0;
    public Object L$1;
    public int label;
    public /* synthetic */ Object result;
    public final /* synthetic */ NotificationVoiceReplyController this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyController$resetStateOnUserChange$1(NotificationVoiceReplyController notificationVoiceReplyController, Continuation<? super NotificationVoiceReplyController$resetStateOnUserChange$1> continuation) {
        super(continuation);
        this.this$0 = notificationVoiceReplyController;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        NotificationVoiceReplyController.access$resetStateOnUserChange(this.this$0, null, this);
        return CoroutineSingletons.COROUTINE_SUSPENDED;
    }
}
