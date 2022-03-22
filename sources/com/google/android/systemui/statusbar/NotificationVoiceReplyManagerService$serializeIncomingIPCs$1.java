package com.google.android.systemui.statusbar;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: NotificationVoiceReplyManagerService.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService", f = "NotificationVoiceReplyManagerService.kt", l = {186, 187}, m = "serializeIncomingIPCs")
/* loaded from: classes.dex */
public final class NotificationVoiceReplyManagerService$serializeIncomingIPCs$1 extends ContinuationImpl {
    public Object L$0;
    public int label;
    public /* synthetic */ Object result;
    public final /* synthetic */ NotificationVoiceReplyManagerService this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationVoiceReplyManagerService$serializeIncomingIPCs$1(NotificationVoiceReplyManagerService notificationVoiceReplyManagerService, Continuation<? super NotificationVoiceReplyManagerService$serializeIncomingIPCs$1> continuation) {
        super(continuation);
        this.this$0 = notificationVoiceReplyManagerService;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return NotificationVoiceReplyManagerService.access$serializeIncomingIPCs(this.this$0, this);
    }
}
